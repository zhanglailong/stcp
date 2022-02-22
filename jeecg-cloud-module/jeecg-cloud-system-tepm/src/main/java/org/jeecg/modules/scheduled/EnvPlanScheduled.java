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
     * 定时处理队列消息
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
               //处理环境队列
               if (stacks.size() > 0) {
                   searchStack(stacks);
               }
               //处理虚拟机队列
               if (servers.size() > 0) {
                   getStackList(servers);
               }
           }
            handleVm();
           running = false;
            lock.unlock();
           log.info(CommonConstant.SCHEDULED_QUEUE_LOCK+"解锁成功");
       }else{
          log.info(CommonConstant.SCHEDULED_QUEUE_LOCK+"获取锁失败");
       }
    }


    /**
     * 定时查询环境资源
     */
    private void searchStack(List<StackQueue> stacks) {
        log.info("定时查询环境资源");
        List<EnvCustomized> updateStacks = new ArrayList<>();
        List<StackQueue> addQueues = new ArrayList<>();
        stacks.forEach(n -> {
            try {
                //环境信息查询
                JSONObject resultJsonObj = openStackServiceExt.searchStackById(n.getOpenStackId());
                if (resultJsonObj != null && StringUtils.isNotEmpty(resultJsonObj.get(CommonConstant.DATA_STRING_STACK).toString())) {
                    EnvStackStatusEnum statusEnum = EnvStackStatusEnum.toEnum(resultJsonObj.get(CommonConstant.DATA_STRING_STACK).toString());
                    if (statusEnum != null && StringUtils.isNotEmpty(statusEnum.getState())) {
                        //判断是什么状态,状态相同不删除该队列
                        if (!statusEnum.getState().equals(n.getStatus())) {
                            EnvCustomized customized = customizedService.getById(n.getHandleId());
                            //环境创建成功-需要查看是否有虚拟机
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
                            //环境 备份中、快照中
                            if (n.getStatus().equals(EnvStackStatusEnum.IN_PROGRESS.toString())){
                                //查询环境信息 中环境的状态 如果为 完成 调用 获取环境事件接口
                                //查询 备份、快照表
                                StackRecovery stackRecovery = stackRecoveryService.getById(n.getHandleId());
                                if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.SNAPSHOT_COMPLETE.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_COMPLETE.getState())){
                                    EventsList eventsList = openStackServiceExt.getStacksEvents(n.getOpenStackId(),CommonConstant.SNAPSHOT,CommonConstant.COMPLETE);
                                    if (eventsList != null && eventsList.getDatas().getEvents().size() >0 ){
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        //开始时间
                                        Date BackupsBeginTime = null;
                                        //结束时间
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
                                        //开始时间先减8小时
                                        Date date = FormatTimeEightUtils.reduceTime(df.format(BackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                                        //结束时间大于开始时间
                                        if (date.before(newBackupsEndTime)){
                                            //修改 备份、快照的 状态 以及 结束时间
                                            //结束时间
                                            Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                            stackRecovery.setBackupsEndTime(endDate);
                                            //状态
                                            stackRecovery.setStatus(EnvStackStatusEnum.CREATE_COMPLETE.toString());
                                            stackRecoveryService.updateById(stackRecovery);

                                            //vmid不为空 环境监视下的虚拟机 快照/备份   环境+虚拟机 状态都要改变
                                            if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                                //虚拟机信息
                                                VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                                //环境信息
                                                EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                                //快照
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
                                                //只需要修改环境的状态
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
                                    // 备份/ 快照 失败
                                    //vmid不为空 环境监视下的虚拟机 快照/备份   环境+虚拟机 状态都要改变
                                    if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                        //虚拟机信息
                                        VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                        //环境信息
                                        EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                        //快照
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
                                        //只需要修改环境的状态
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
                            // 快照 / 备份 恢复 回执时间
                            if (n.getStatus().equals(EnvStackStatusEnum.RESTORE_IN_PROGRESS) || n.getStatus().equals(EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS)){
                                //查询 备份、快照表
                                StackRecovery stackRecovery = stackRecoveryService.getById(n.getHandleId());
                                if (resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE.getState())|| resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS).toString().equals(EnvStackStatusEnum.RESTORE_COMPLETE.getState())){
                                    EventsList eventsList = openStackServiceExt.getStacksEvents(n.getOpenStackId(),CommonConstant.RESTORE,CommonConstant.COMPLETE);
                                    //获取虚拟机 和 环境信息
                                    if (eventsList != null && eventsList.getDatas().getEvents().size() >0 ){
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        //开始时间
                                        Date BackupsBeginTime = null;
                                        //结束时间
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
                                        //开始时间先减8小时
                                        Date beginDate = FormatTimeEightUtils.reduceTime(df.format(BackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                                        //结束时间大于开始时间
                                        if (beginDate.before(newBackupsEndTime)){
                                            //修改 备份、快照 恢复的 状态 以及 结束时间
                                            //结束时间
                                            Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                            stackRecovery.setRestoreEndTime(endDate);
                                            //状态 0快照
                                            if (stackRecovery.getType().equals(CommonConstant.DATA_INT_0)){
                                                //从快照恢复完成
                                                stackRecovery.setStatus(EnvStackStatusEnum.RESTORE_COMPLETE.toString());
                                            }else {
                                                //从备份恢复完成
                                                stackRecovery.setStatus(EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE.toString());
                                            }
                                            stackRecoveryService.updateById(stackRecovery);

                                            //vmid不为空 环境监视下的虚拟机 快照/备份 恢复  环境+虚拟机 状态都要改变
                                            if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                                //虚拟机信息
                                                VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                                //环境信息
                                                EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                                //快照
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
                                                //只需要修改环境的状态
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
                                    // 备份/ 快照  恢复 失败
                                    //vmid不为空 环境监视下的虚拟机 快照/备份   环境+虚拟机 状态都要改变
                                    if (StringUtils.isNotBlank(stackRecovery.getVmId())){
                                        //虚拟机信息
                                        VmDesign vmDesign = vmDesignService.getById(stackRecovery.getVmId());
                                        //环境信息
                                        EnvCustomized envCustomized = customizedService.getById(vmDesign.getPlanId());
                                        //快照
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
                                        //只需要修改环境的状态
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
                            //环境挂起成功-修改虚拟机状态-SUSPENDED
                            if (statusEnum.getState().equals(EnvStackStatusEnum.SUSPEND_COMPLETE.getState())) {
                                vmDesignService.update(new UpdateWrapper<VmDesign>()
                                        .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                                        .eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, n.getHandleId())
                                        .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.SUSPENDED));
                                n.setMsg(resultJsonObj.get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString());
                                n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                            }
                            //环境恢复挂起成功 修改虚拟机状态 ACTIVE
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
                log.error("定时任务-查询环境:" + n.getHandleId() + "信息异常,原因:" + e.getMessage());
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
            log.error("定时任务-查询环境信息更新异常,原因:" + e.getMessage());
        }
    }

    /**
     * 查看栈资源获取虚拟机id
     */
    public void getStackList(List<StackQueue> servers) {
        log.info("查看栈资源获取虚拟机");
        List<StackQueue> addQueues = new ArrayList<>();
        servers.forEach(n -> {
            try {
                //环境创建成功
                if (n.getStatus().equals(EnvStackStatusEnum.CREATE_COMPLETE.getState())) {
                    //找环境资源栈
                    StackList stackList = openStackServiceExt.getStackList(n.getOpenStackId());
                    if (stackList != null && stackList.getDatas() != null
                            && stackList.getDatas().getResources() != null
                            && stackList.getDatas().getResources().size() > 0) {
                        List<StackList.Resources> resources = stackList.getDatas().getResources();
                        //判断HandleId是否有值 HandleId环境id  planId 规划id
                        EnvPlan envPlan = null;
                        if (StringUtils.isNotEmpty(n.getHandleId())) {
                            envPlan = envPlanService.getById(n.getPlanId());
                        }
                        if (resources != null && resources.size() > 0 && envPlan != null) {
                            if (vmDesignService.getSaveOrUpByJsonAndPid(n.getCreateBy(), envPlan.getPlanJson(), n.getHandleId(), n.getName(), resources)) {
                                //虚拟机创建成功
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
                //创建 获取ip
                if (n.getStatus().equals(ServerStatusEnum.IN_PROGRESS.getStatus())
                        && StringUtils.isNotEmpty(n.getHandleId())) {
                    //找环境资源栈
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
                                //创建串口与外部工装
                                EnvPlan envPlan = envPlanService.getById(n.getPlanId());
                                iVmSerportExdevRelationService.createPortAndUsb(n.getCreateBy(),envPlan, n.getHandleId(), n.getName());
                            }
                        }
                    }
                }
                //虚拟机挂起成功-修改虚拟机状态-SUSPENDED
                if (n.getStatus().equals(ServerStatusEnum.SUSPEND_IN_PROGRESS.toString())) {
                    vmDesignService.update(new UpdateWrapper<VmDesign>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_ID, n.getHandleId())
                            .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.SUSPENDED));
                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                }
                //虚拟机恢复-修改虚拟机的状态
                if (n.getStatus().equals(ServerStatusEnum.RESUME_IN_PROGRESS.toString())) {
                    vmDesignService.update(new UpdateWrapper<VmDesign>()
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                            .eq(CommonConstant.DATA_STRING_ID, n.getHandleId())
                            .set(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.ACTIVE));
                    n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                }
                //虚拟机迁移 回执时间
                if (n.getStatus().equals(ServerStatusEnum.MIGRATING.toString())){
                    //查询虚拟机的信息
                    VmInformation vmInformation = openStackServiceExt.getVmInformation(n.getOpenStackId());
                    //查询迁移记录表 获取迁移开始时间
                    VmRemoval vmRemoval = vmRemovalService.getById(n.getHandleId());
                    //查询虚拟机信息
                    VmDesign vmDesign = vmDesignService.getOne(new QueryWrapper<VmDesign>().eq(CommonConstant.SERVER_ID,vmRemoval.getVmId())
                            .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0));
                    //虚拟机的状态不为 错误
                    if (!vmInformation.getStatus().equals(ServerStatusEnum.ERROR.toString())){
                        //查询 虚拟机操作记录列表
                        VmAction networksList = openStackServiceExt.getVmActions(n.getOpenStackId());
                        if (networksList != null && networksList.getCode() == CommonConstant.DATA_INT_0 && networksList.getDatas() != null){
                            List<VmAction.Actions> actionsList = networksList.getDatas().getActions();
                            //热迁移
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (vmRemoval.getManner().equals(CommonConstant.DATA_INT_0)){
                                if (actionsList.get(0).getAction().equals(CommonConstant.LIVE_MIGRATION)){
                                    //结束时间
                                    Date newBackupsEndTime = null ;
                                    //开始时间
                                    //开始日期减8小时
                                    Date beginDate = FormatTimeEightUtils.reduceTime(df.format(vmRemoval.getCreateTime()),CommonConstant.TIME_STEP_SIZE);
                                    try {
                                        String endTime = FormatTimeEightUtils.formattingTime(actionsList.get(0).getUpdatedAt());
                                        newBackupsEndTime = df.parse(endTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (beginDate.before(newBackupsEndTime)){
                                        //日期增加 8小时
                                        Date endDate = FormatTimeEightUtils.timeReversal(df.format(newBackupsEndTime),CommonConstant.TIME_STEP_SIZE);
                                        vmRemoval.setEndTime(endDate);
                                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING_COMPLETE);
                                        vmDesign.setStatus(ServerStatusEnum.toEnum(vmInformation.getStatus()));
                                        n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                                    }
                                }
                            }else {
                                if (actionsList.get(0).getAction().equals(CommonConstant.CONFIRM_RESIZE) && actionsList.get(1).getAction().equals(CommonConstant.MIGRATE)){
                                    //结束时间
                                    Date newBackupsEndTime = null ;
                                    Date beginDate = FormatTimeEightUtils.reduceTime(df.format(vmRemoval.getCreateTime()),CommonConstant.TIME_STEP_SIZE);
                                    try {
                                        String endTime = FormatTimeEightUtils.formattingTime(actionsList.get(1).getUpdatedAt());
                                        newBackupsEndTime = df.parse(endTime);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (beginDate.before(newBackupsEndTime)){
                                        //日期增加 8小时
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
                        //迁移失败
                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING_FAILED);
                        vmRemovalService.updateById(vmRemoval);
                        //修改虚拟机的状态
                        vmDesign.setStatus(ServerStatusEnum.ERROR);
                        vmDesignService.updateById(vmDesign);
                        n.setIdel(CommonConstant.DATA_INT_IDEL_1);
                    }
                }
                //TODO 快照回滚  备份回滚接口没提供暂时不做
                n.setCount(n.getCount() + 1);
            } catch (Exception e) {
                log.error("定时任务-查看栈资源:" + n.getOpenStackId() + "获取虚拟机异常,原因:" + e.getMessage());
            }

        });
        try {
            stackQueueService.saveOrUpdateBatch(servers);
            if (addQueues.size() > 0) {
                stackQueueService.saveBatch(addQueues);
            }
        } catch (Exception e) {
            log.error("定时任务-查看栈资源获取虚拟机更新异常,原因:" + e.getMessage());
        }
    }

    /**
     * 查询虚拟机信息-获取ip
     *
     * @param envId 环境id
     * @return 是否获取到IP
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
                    log.error("定时任务-stacks:" + m.getOpenVmId() + "-查询虚拟机信息-获取ip异常,原因:" + e.getMessage());
                }
            });
        } else {
            //通过虚拟机获取单个虚拟机ip
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
                log.error("定时任务-stacks:" + vm.getOpenVmId() + "-查询虚拟机信息-获取ip异常,原因:" + e.getMessage());
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
     * 处理虚拟机物理机名称
     */
    public void handleVm(){
        try {
            List<VmDesign> vmDesignList = vmDesignService.getHandleVm();
            List<VmDesign> vmDesigns = new ArrayList<>();
            if (vmDesignList != null && vmDesignList.size() > 0){
                vmDesignList.forEach(vmDesign -> {
                    //调用统一接口查询虚拟机的信息，取出物理机名称 存入虚拟机表
                    JSONObject resultJson = openStackServiceExt.getVirIpAndStatus(vmDesign.getOpenVmId());
                    if (resultJson != null) {
                        //获取物理机名称
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
                    log.error("虚拟机物理机名称保存异常:" + e.getMessage());
                }
            }
        }catch (Exception e){
            log.error("虚拟机物理机名称处理异常:" + e.getMessage());
        }
    }
}
