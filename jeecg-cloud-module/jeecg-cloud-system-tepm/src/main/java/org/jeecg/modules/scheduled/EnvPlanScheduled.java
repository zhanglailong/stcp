package org.jeecg.modules.scheduled;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.boot.starter.lock.client.RedissonLockClient;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FormatTimeEightUtils;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.jeecg.modules.openstack.entity.*;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.IStackRecoveryService;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.entity.EnvPlan;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.entity.VmRemoval;
import org.jeecg.modules.plan.service.IEnvPlanService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.plan.service.IVmRemovalService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.vmserportexdevrelation.service.IVmSerportExdevRelationService;
import org.redisson.api.RLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/5/7
 */
@Slf4j
@Component
public class EnvPlanScheduled {

    @Resource
    IEnvCustomizedService customizedService;
    @Resource
    IEnvPlanService envPlanService;
    @Resource
    IVmDesignService vmDesignService;
    @Resource
    OpenStackServiceExt openStackServiceExt;
    @Resource
    IStackQueueService stackQueueService;
    @Resource
    IVmSerportExdevRelationService iVmSerportExdevRelationService;

    @Resource
    RedissonLockClient redissonLockClient;
    @Resource
    private IStackRecoveryService stackRecoveryService;
    @Resource
    private IVmRemovalService vmRemovalService;

    private boolean running;


    /**
     * ????????????????????????
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void handleStackQueue() {
        RLock lock = redissonLockClient.getLock(CommonConstant.SCHEDULED_QUEUE_LOCK);
        if(lock.tryLock()) {
           log.info("Timed processing of queue messages");
           if (running) {
               return;
           }
           running = true;
           List<StackQueue> queues = stackQueueService.getHandleQueue();
           List<StackQueue> stacks = new ArrayList<>();
           List<StackQueue> servers = new ArrayList<>();

           if (queues != null && queues.size() > 0) {
               queues.forEach(n -> {
                   if (CommonConstant.DATA_INT_0.equals(n.getType())) {
                       stacks.add(n);
                   }
                   if (CommonConstant.DATA_INT_1.equals(n.getType())) {
                       servers.add(n);
                   }
               });
               log.info("stacks:" + stacks.size());
               log.info("servers:" + servers.size());
               //??????????????????
               if (stacks.size() > 0) {
                   searchStack(stacks);
               }
               //?????????????????????
               if (servers.size() > 0) {
                   getStackList(servers);
               }
           }
            handleVm();
           running = false;
            lock.unlock();
           log.info(CommonConstant.SCHEDULED_QUEUE_LOCK+"????????????");
       }else{
          log.info(CommonConstant.SCHEDULED_QUEUE_LOCK+"???????????????");
       }
    }


    /**
     * ????????????????????????
     */
    private void searchStack(List<StackQueue> stacks) {
        log.info("????????????????????????");
        List<EnvCustomized> updateStacks = new ArrayList<>();
        List<StackQueue> addQueues = new ArrayList<>();
        stacks.forEach(n -> {
            try {
                //??????????????????
                JSONObject resultJsonObj = openStackServiceExt.searchStackById(n.getOpenStackId());
                if (resultJsonObj != null && StringUtils.isNotEmpty(resultJsonObj.get(CommonConstant.DATA_STRING_STACK).toString())) {
                    EnvStackStatusEnum statusEnum = EnvStackStatusEnum.toEnum(resultJsonObj.get(CommonConstant.DATA_STRING_STACK).toString());
                    if (statusEnum != null && StringUtils.isNotEmpty(statusEnum.getState())) {
                        //?????????????????????,??????????????????????????????
                        if (!statusEnum.getState().equals(n.getStatus())) {
                            EnvCustomized customized = customizedService.getById(n.getHandleId());
                            //??????????????????-??????????????????????????????
                            if (statusEnum.getState().equals(EnvStackStatusEnum.CREATE_COMPLETE.getState())) {
                                StackQueue queue = new StackQueue();
                                queue.setName(n.getName());
                                queue.setIdel(CommonConstant.DATA_INT_IDEL_0);
                                queue.setHandleId(n.getHandleId());
                                queue.setOpenStackId(n.getOpenStackId());
                                queue.setEnvId(n.getHandleId());
                                queue.setPlanId(customized!=null?customized.getPlanId():"");
                                queue.setStatus(EnvStackStatusEnum.CREATE_COMPLETE.getState());
                                queue.setType(CommonConstant.DATA_INT_1);
                                queue.setCount(0);
                                queue.setCreateBy(n.getCreateBy());
                                addQueues.add(queue);
                            }
                            //?????? ?????????????????????
                            if (n.getStatus().equals(EnvStackStatusEnum.IN_PROGRESS.toString())){
                                //?????????????????? ?????????????????? ????????? ?????? ?????? ????????????????????????
                                //?????? ??????????????????
                                StackRecovery stackRecovery = stackRecoveryService.getById(n.getHandleId());
                                if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.SNAPSHOT_COMPLETE.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_COMPLETE.getState())){
                                    EventsList eventsList = openStackServiceExt.getStacksEvents(n.getOpenStackId(),CommonConstant.SNAPSHOT,CommonConstant.COMPLETE);
                                    if (eventsList != null && eventsList.getDatas().getEvents().size() >0 ){
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        //????????????
                                        Date BackupsBeginTime = null;
                                        //????????????
                                        Date backupsEndTime = null;
                                        Date newBackupsEndTime = null ;
                                        try {
                                            BackupsBeginTime = df.parse(df.format(stackRecovery.getBackupsBeginTime()));
                                            backupsEndTime = sdf.parse(eventsList.getDatas().getEvents().get(0).getEventTime());

                                            String str = df.format(backupsEndTime);
                                            newBackupsEndTime = df.parse(str);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //??????????????????8??????
                                        Date date = FormatTimeEightUtils.reduceTime(df.format(BackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                                        //??????????????????????????????
                                        if (date.before(newBackupsEndTime)){
                                            //?????? ?????????????????? ?????? ?????? ????????????
                                            //????????????
                                            Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                            stackRecovery.setBackupsEndTime(endDate);
                                            //??????
                                            stackRecovery.setStatus(EnvStackStatusEnum.CREATE_COMPLETE.toString());
                                            stackRecoveryService.updateById(stackRecovery);

                                            //vmid????????? ??????????????????????????? ??????/??????   ??????+????????? ??????????????????
                                            if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                                //???????????????
                                                VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                                //????????????
                                                EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                                //??????
                                                if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                    vmDesign.setStatus(ServerStatusEnum.SNAPSHOT_COMPLETE);
                                                    envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_COMPLETE);
                                                }else {
                                                    vmDesign.setStatus(ServerStatusEnum.BACKUP_COMPLETE);
                                                    envCustomized.setState(EnvStackStatusEnum.BACKUP_COMPLETE);
                                                }
                                                vmDesignService.updateById(vmDesign);
                                                customizedService.updateById(envCustomized);

                                            }else {
                                                //??????????????????????????????
                                                EnvCustomized envCustomized = customizedService.getById(stackRecovery.getEnvId());
                                                if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                    envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_COMPLETE);
                                                }else {
                                                    envCustomized.setState(EnvStackStatusEnum.BACKUP_COMPLETE);
                                                }
                                                customizedService.updateById(envCustomized);
                                            }
                                            n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                            n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                        }
                                    }
                                }else if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.SNAPSHOT_FAILED.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_FAILED.getState())){
                                    // ??????/ ?????? ??????
                                    //vmid????????? ??????????????????????????? ??????/??????   ??????+????????? ??????????????????
                                    if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                        //???????????????
                                        VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                        //????????????
                                        EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                        //??????
                                        if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                            vmDesign.setStatus(ServerStatusEnum.SNAPSHOT_FAILED);
                                            envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_FAILED);
                                        }else {
                                            vmDesign.setStatus(ServerStatusEnum.BACKUP_FAILED);
                                            envCustomized.setState(EnvStackStatusEnum.BACKUP_FAILED);
                                        }
                                        vmDesignService.updateById(vmDesign);
                                        customizedService.updateById(envCustomized);

                                    }else {
                                        //??????????????????????????????
                                        EnvCustomized envCustomized = customizedService.getById(stackRecovery.getEnvId());
                                        if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                            envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_FAILED);
                                        }else {
                                            envCustomized.setState(EnvStackStatusEnum.BACKUP_FAILED);
                                        }
                                        customizedService.updateById(envCustomized);
                                    }
                                    n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                }
                            }
                            // ?????? / ?????? ?????? ????????????
                            if (n.getStatus().equals(EnvStackStatusEnum.RESTORE_IN_PROGRESS) || n.getStatus().equals(EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS)){
                                //?????? ??????????????????
                                StackRecovery stackRecovery = stackRecoveryService.getById(n.getHandleId());
                                if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.RESTORE_COMPLETE.getState())){
                                    EventsList eventsList = openStackServiceExt.getStacksEvents(n.getOpenStackId(),CommonConstant.RESTORE,CommonConstant.COMPLETE);
                                    //??????????????? ??? ????????????
                                    if (eventsList != null && eventsList.getDatas().getEvents().size() >0 ){
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        //????????????
                                        Date BackupsBeginTime = null;
                                        //????????????
                                        Date backupsEndTime = null;
                                        Date newBackupsEndTime = null ;
                                        try {
                                            BackupsBeginTime = df.parse(df.format(stackRecovery.getRestoreBeginTime()));
                                            backupsEndTime = sdf.parse(eventsList.getDatas().getEvents().get(0).getEventTime());

                                            String str = df.format(backupsEndTime);
                                            newBackupsEndTime = df.parse(str);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        //??????????????????8??????
                                        Date beginDate = FormatTimeEightUtils.reduceTime(df.format(BackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                                        //??????????????????????????????
                                        if (beginDate.before(newBackupsEndTime)){
                                            //?????? ??????????????? ????????? ?????? ?????? ????????????
                                            //????????????
                                            Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                            stackRecovery.setRestoreEndTime(endDate);
                                            //?????? 0??????
                                            if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                //?????????????????????
                                                stackRecovery.setStatus(EnvStackStatusEnum.RESTORE_COMPLETE.toString());
                                            }else {
                                                //?????????????????????
                                                stackRecovery.setStatus(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE.toString());
                                            }
                                            stackRecoveryService.updateById(stackRecovery);

                                            //vmid????????? ??????????????????????????? ??????/?????? ??????  ??????+????????? ??????????????????
                                            if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                                //???????????????
                                                VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                                //????????????
                                                EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                                //??????
                                                if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                    vmDesign.setStatus(ServerStatusEnum.RESTORE_COMPLETE);
                                                    envCustomized.setState(EnvStackStatusEnum.RESTORE_COMPLETE);
                                                }else {
                                                    vmDesign.setStatus(ServerStatusEnum.BACKUP_RESTORE_COMPLETE);
                                                    envCustomized.setState(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE);
                                                }
                                                vmDesignService.updateById(vmDesign);
                                                customizedService.updateById(envCustomized);

                                            }else {
                                                //??????????????????????????????
                                                EnvCustomized envCustomized = customizedService.getById(stackRecovery.getEnvId());
                                                if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                    envCustomized.setState(EnvStackStatusEnum.RESTORE_COMPLETE);
                                                }else {
                                                    envCustomized.setState(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE);
                                                }
                                                customizedService.updateById(envCustomized);
                                            }
                                            n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                            n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                        }
                                    }
                                }else if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.RESTORE_FAILED.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_RESTORE_FAILED.getState())){
                                    // ??????/ ??????  ?????? ??????
                                    //vmid????????? ??????????????????????????? ??????/??????   ??????+????????? ??????????????????
                                    if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                        //???????????????
                                        VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                        //????????????
                                        EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                        //??????
                                        if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){

                                            vmDesign.setStatus(ServerStatusEnum.RESTORE_FAILED);
                                            envCustomized.setState(EnvStackStatusEnum.RESTORE_FAILED);
                                        }else {
                                            vmDesign.setStatus(ServerStatusEnum.BACKUP_RESTORE_FAILED);
                                            envCustomized.setState(EnvStackStatusEnum.BACKUP_RESTORE_FAILED);
                                        }
                                        vmDesignService.updateById(vmDesign);
                                        customizedService.updateById(envCustomized);

                                    }else {
                                        //??????????????????????????????
                                        EnvCustomized envCustomized = customizedService.getById(stackRecovery.getEnvId());
                                        if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                            envCustomized.setState(EnvStackStatusEnum.RESTORE_FAILED);
                                        }else {
                                            envCustomized.setState(EnvStackStatusEnum.BACKUP_RESTORE_FAILED);
                                        }
                                        customizedService.updateById(envCustomized);
                                    }
                                    n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                }
                            }
                            //??????????????????-?????????????????????-SUSPENDED
                            if (statusEnum.getState().equals(EnvStackStatusEnum.SUSPEND_COMPLETE.getState())) {
                                vmDesignService.update(new UpdateWrapper<VmDesign>()
                                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                                        .eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, n.getHandleId())
                                        .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.SUSPENDED));
                                n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            }
                            //???????????????????????? ????????????????????? ACTIVE
                            if (statusEnum.getState().equals(EnvStackStatusEnum.RESUME_COMPLETE.getState())) {
                                vmDesignService.update(new UpdateWrapper<VmDesign>()
                                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                                        .eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, n.getHandleId())
                                        .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.ACTIVE));
                                n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            }
                            if (customized != null) {
                                customized.setState(statusEnum);
                                customized.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                updateStacks.add(customized);
                                n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            }

                        }
                    }
                }
                n.setCount(n.getCount() + 1);
            } catch (Exception e) {
                log.error("????????????-????????????:" + n.getHandleId() + "????????????,??????:" + e.getMessage());
            }
        });
        try {
            stackQueueService.updateBatchById(stacks);
            if (updateStacks.size() > 0) {
                customizedService.updateBatchById(updateStacks);
            }
            if (addQueues.size() > 0) {
                stackQueueService.saveBatch(addQueues);
            }
        } catch (Exception e) {
            log.error("????????????-??????????????????????????????,??????:" + e.getMessage());
        }
    }

    /**
     * ??????????????????????????????id
     */
    public void getStackList(List<StackQueue> servers) {
        log.info("??????????????????????????????");
        List<StackQueue> addQueues = new ArrayList<>();
        servers.forEach(n -> {
            try {
                //??????????????????
                if (n.getStatus().equals(EnvStackStatusEnum.CREATE_COMPLETE.getState())) {
                    //??????????????????
                    StackList stackList = openStackServiceExt.getStackList(n.getOpenStackId());
                    if (stackList != null && stackList.getDatas() != null
                            && stackList.getDatas().getResources() != null
                            && stackList.getDatas().getResources().size() > 0) {
                        List<StackList.Resources> resources = stackList.getDatas().getResources();
                        //??????HandleId???????????? HandleId??????id  planId ??????id
                        EnvPlan envPlan = null;
                        if (StringUtils.isNotEmpty(n.getHandleId())) {
                            envPlan = envPlanService.getById(n.getPlanId());
                        }
                        if (resources != null && resources.size() > 0 && envPlan != null) {
                            if (vmDesignService.getSaveOrUpByJsonAndPid(n.getCreateBy(), envPlan.getPlanJson(), n.getHandleId(), n.getName(), resources)) {
                                //?????????????????????
                                StackQueue queue = new StackQueue();
                                queue.setName(n.getName());
                                queue.setIdel(CommonConstant.DATA_INT_IDEL_0);
                                queue.setHandleId(n.getHandleId());
                                queue.setPlanId(n.getPlanId());
                                queue.setOpenStackId(n.getOpenStackId());
                                queue.setStatus(ServerStatusEnum.IN_PROGRESS.getStatus());
                                queue.setType(CommonConstant.DATA_INT_1);
                                queue.setCount(CommonConstant.DATA_INT_0);
                                queue.setCreateBy(n.getCreateBy());
                                addQueues.add(queue);
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            }
                        }
                    }
                }
                //?????? ??????ip
                if (n.getStatus().equals(ServerStatusEnum.IN_PROGRESS.getStatus())
                        && StringUtils.isNotEmpty(n.getHandleId())) {
                    //??????????????????
                    StackList stackList = openStackServiceExt.getStackList(n.getOpenStackId());
                    if (stackList != null){
                        String vmid = stackList.getDatas().getResources().get(0).getPhysicalResourceId();
                        if (StringUtils.isNotBlank(vmid)){
                            VmDesign vmDesign = new VmDesign();
                            vmDesign.setId(n.getHandleId());
                            vmDesign.setServerId(vmid);
                            vmDesign.setOpenVmId(vmid);
                            vmDesign.setStatus(ServerStatusEnum.ACTIVE);
                            vmDesignService.updateById(vmDesign);
                            n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            if (getVirService(n.getHandleId())) {
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                //???????????????????????????
                                EnvPlan envPlan = envPlanService.getById(n.getPlanId());
                                iVmSerportExdevRelationService.createPortAndUsb(n.getCreateBy(),envPlan, n.getHandleId(), n.getName());
                            }
                        }
                    }
                }
                //?????????????????????-?????????????????????-SUSPENDED
                if (n.getStatus().equals(ServerStatusEnum.SUSPEND_IN_PROGRESS.toString())) {
                    vmDesignService.update(new UpdateWrapper<VmDesign>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_ID, n.getHandleId())
                            .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.SUSPENDED));
                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                }
                //???????????????-????????????????????????
                if (n.getStatus().equals(ServerStatusEnum.RESUME_IN_PROGRESS.toString())) {
                    vmDesignService.update(new UpdateWrapper<VmDesign>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_ID, n.getHandleId())
                            .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.ACTIVE));
                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                }
                //??????????????? ????????????
                if (n.getStatus().equals(ServerStatusEnum.MIGRATING.toString())){
                    //????????????????????????
                    VmInformation vmInformation = openStackServiceExt.getVmInformation(n.getOpenStackId());
                    //????????????????????? ????????????????????????
                    VmRemoval vmRemoval = vmRemovalService.getById(n.getHandleId());
                    //?????????????????????
                    VmDesign vmDesign = vmDesignService.getOne(new QueryWrapper<VmDesign>().eq(CommonConstant.SERVER_ID,vmRemoval.getVmId())
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                    //???????????????????????? ??????
                    if (!vmInformation.getStatus().equals(ServerStatusEnum.ERROR.toString())){
                        //?????? ???????????????????????????
                        VmAction networksList = openStackServiceExt.getVmActions(n.getOpenStackId());
                        if (networksList != null && networksList.getCode() == CommonConstant.DATA_INT_0 && networksList.getDatas() != null){
                            List<VmAction.Actions> actionsList = networksList.getDatas().getActions();
                            //?????????
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (vmRemoval.getManner().equals(CommonConstant.DATA_INT_0)){
                                if (actionsList.get(0).getAction().equals(CommonConstant.LIVE_MIGRATION)){
                                    //????????????
                                    Date newBackupsEndTime = null ;
                                    //????????????
                                    //???????????????8??????
                                    Date beginDate = FormatTimeEightUtils.reduceTime(df.format(vmRemoval.getCreateTime()),CommonConstant.TIME_STEP_SIZE);
                                    try {
                                        String endTime = FormatTimeEightUtils.formattingTime(actionsList.get(0).getUpdatedAt());
                                        newBackupsEndTime = df.parse(endTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (beginDate.before(newBackupsEndTime)){
                                        //???????????? 8??????
                                        Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                        vmRemoval.setEndTime(endDate);
                                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING_COMPLETE);
                                        vmDesign.setStatus(ServerStatusEnum.toEnum(vmInformation.getStatus()));
                                        n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                    }
                                }
                            }else {
                                if (actionsList.get(0).getAction().equals(CommonConstant.CONFIRM_RESIZE) && actionsList.get(1).getAction().equals(CommonConstant.MIGRATE)){
                                    //????????????
                                    Date newBackupsEndTime = null ;
                                    Date beginDate = FormatTimeEightUtils.reduceTime(df.format(vmRemoval.getCreateTime()),CommonConstant.TIME_STEP_SIZE);
                                    try {
                                        String endTime = FormatTimeEightUtils.formattingTime(actionsList.get(1).getUpdatedAt());
                                        newBackupsEndTime = df.parse(endTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (beginDate.before(newBackupsEndTime)){
                                        //???????????? 8??????
                                        Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                        vmRemoval.setEndTime(endDate);
                                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING_COMPLETE);
                                        vmDesign.setStatus(ServerStatusEnum.toEnum(vmInformation.getStatus()));
                                        n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                    }
                                }
                            }
                            vmRemovalService.updateById(vmRemoval);
                            vmDesignService.updateById(vmDesign);
                        }
                    }else {
                        //????????????
                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING_FAILED);
                        vmRemovalService.updateById(vmRemoval);
                        //????????????????????????
                        vmDesign.setStatus(ServerStatusEnum.ERROR);
                        vmDesignService.updateById(vmDesign);
                        n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                    }
                }
                //TODO ????????????  ???????????????????????????????????????
                n.setCount(n.getCount() + 1);
            } catch (Exception e) {
                log.error("????????????-???????????????:" + n.getOpenStackId() + "?????????????????????,??????:" + e.getMessage());
            }

        });
        try {
            stackQueueService.saveOrUpdateBatch(servers);
            if (addQueues.size() > 0) {
                stackQueueService.saveBatch(addQueues);
            }
        } catch (Exception e) {
            log.error("????????????-??????????????????????????????????????????,??????:" + e.getMessage());
        }
    }

    /**
     * ?????????????????????-??????ip
     *
     * @param envId ??????id
     * @return ???????????????IP
     */
    public boolean getVirService(String envId) {
        AtomicBoolean resultBoolean = new AtomicBoolean(false);
        List<VmDesign> vmDesigns = vmDesignService.getVmListByEnvId(envId);
        VmDesign vm = null;
        if (vmDesigns != null && vmDesigns.size() > 0) {
            vmDesigns.forEach(m -> {
                try {
                    JSONObject resultJson = openStackServiceExt.getVirIpAndStatus(m.getOpenVmId());
                    if (resultJson != null) {
                        String status = resultJson.containsKey(CommonConstant.DATA_STRING_SERVER_STATUS) ?
                                resultJson.get(CommonConstant.DATA_STRING_SERVER_STATUS).toString() : null;
                        String addresses = resultJson.get(CommonConstant.DATA_STRING_ADDRESS).toString();
                        if (StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(addresses)) {
                            //network
                            JSONObject ipsJsonObject = JSONObject.parseObject(addresses);
                            if (ipsJsonObject != null){
                                Map<String, List<ServerInfo.Networks>> ips = JSONObject.parseObject(ipsJsonObject.toJSONString(), new TypeReference<Map<String, List<ServerInfo.Networks>>>(){});
                                log.info("ips:"+ JSON.toJSONString(ips));
                                if (ips != null){
                                    ips.forEach((k,v)->{
                                        if (v!=null && v.size() > 0){
                                            v.forEach(n->{
                                                if (n.getType().equals(CommonConstant.DATA_STRING_SERVER_FIXED)){
                                                    m.setNetworkAddress(n.getAddr());
                                                }
                                                if (n.getType().equals(CommonConstant.DATA_STRING_SERVER_FLOATING)){
                                                    m.setFloatIp(n.getAddr());
                                                }
                                            });
                                            m.setStatus(ServerStatusEnum.toEnum(status));
                                            resultBoolean.set(true);
                                        }
                                    });

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error("????????????-stacks:" + m.getOpenVmId() + "-?????????????????????-??????ip??????,??????:" + e.getMessage());
                }
            });
        } else {
            //????????????????????????????????????ip
            vm = vmDesignService.getById(envId);
            try {
                if (vm != null) {
                    JSONObject resultJson = openStackServiceExt.getVirIpAndStatus(vm.getServerId());
                    if (resultJson != null) {
                        String status = resultJson.containsKey(CommonConstant.DATA_STRING_SERVER_STATUS) ?
                                resultJson.get(CommonConstant.DATA_STRING_SERVER_STATUS).toString() : null;
                        String addresses = resultJson.get(CommonConstant.DATA_STRING_ADDRESS).toString();
                        if (StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(addresses)) {
                            //network
                            JSONObject ipsJsonObject = JSONObject.parseObject(addresses);
                            if (ipsJsonObject != null){
                                Map<String, List<ServerInfo.Networks>> ips = JSONObject.parseObject(ipsJsonObject.toJSONString(), new TypeReference<Map<String, List<ServerInfo.Networks>>>(){});
                                if (ips != null){
                                    AtomicReference<String> fixedIp = new AtomicReference<>();
                                    AtomicReference<String> floatIp = new AtomicReference<>();
                                    ips.forEach((k,v)->{
                                        if (v!=null && v.size() > 0){
                                            v.forEach(n->{
                                                if (n.getType().equals(CommonConstant.DATA_STRING_SERVER_FIXED)){
                                                    fixedIp.set(n.getAddr());
                                                }
                                                if (n.getType().equals(CommonConstant.DATA_STRING_SERVER_FLOATING)){
                                                    floatIp.set(n.getAddr());
                                                }
                                            });
                                            resultBoolean.set(true);
                                        }
                                    });
                                    if (resultBoolean.get()){
                                        vm.setNetworkAddress(fixedIp.get());
                                        vm.setFloatIp(floatIp.get());
                                        vm.setStatus(ServerStatusEnum.toEnum(status));
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("????????????-stacks:" + vm.getOpenVmId() + "-?????????????????????-??????ip??????,??????:" + e.getMessage());
                return false;
            }
        }
        if (resultBoolean.get() && vmDesigns != null && vmDesigns.size() > 0) {
            return vmDesignService.updateBatchById(vmDesigns);
        }
        if (resultBoolean.get() && vm != null) {
            return vmDesignService.updateById(vm);
        }
        return false;
    }

    /**
     * ??????????????????????????????
     */
    public void handleVm(){
        try {
            List<VmDesign> vmDesignList = vmDesignService.getHandleVm();
            List<VmDesign> vmDesigns = new ArrayList<>();
            if (vmDesignList != null && vmDesignList.size() > 0){
                vmDesignList.forEach(vmDesign -> {
                    //?????????????????????????????????????????????????????????????????? ??????????????????
                    JSONObject resultJson = openStackServiceExt.getVirIpAndStatus(vmDesign.getOpenVmId());
                    if (resultJson != null) {
                        //?????????????????????
                        String hostName = resultJson.get(CommonConstant.OS_EXT_SRV_ATTR_HOST).toString();
                        if (StringUtils.isNotBlank(hostName)){
                            vmDesign.setHostName(hostName);
                        }
                    }
                    vmDesigns.add(vmDesign);
                });
                try {
                    if (vmDesigns.size()!=0) {
                        vmDesignService.updateBatchById(vmDesigns);
                    }
                } catch (Exception e) {
                    log.error("????????????????????????????????????:" + e.getMessage());
                }
            }
        }catch (Exception e){
            log.error("????????????????????????????????????:" + e.getMessage());
        }
    }
}
