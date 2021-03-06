package org.jeecg.modules.openstack.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.shiro.util.CollectionUtils;
import org.jeecg.modules.Imageotherinfo.entity.ImageOtherInfo;
import org.jeecg.modules.Imageotherinfo.mapper.ImageOtherInfoMapper;
import org.jeecg.modules.Imageotherinfo.service.IImageOtherInfoService;
import org.jeecg.modules.MonitorTools.entity.MonitorTools;
import org.jeecg.modules.MonitorTools.service.IMonitorToolsService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FormatTimeEightUtils;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.common.enums.ImageTypeEnum;
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.jeecg.modules.openstack.entity.*;
import org.jeecg.modules.openstack.mapper.StackQueueMapper;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.IStackRecoveryService;
import org.jeecg.modules.openstack.service.ext.*;
import org.jeecg.modules.plan.entity.ImageManage;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.service.IImageManageService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.running.monitortools.entity.RunningToolsAndVm;
import org.jeecg.modules.running.monitortools.service.IRunningToolsAndVmService;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import org.jeecg.modules.running.tools.service.IRunningToolsListService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.vmserportexdevrelation.entity.VmSerportExdevRelation;
import org.jeecg.modules.vmserportexdevrelation.service.IVmSerportExdevRelationService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zlf
 * ????????????-??????openstack????????????
 * 2021-05-14
 * V1.0
 */
@Slf4j
@Service
public class StackQueueServiceImpl extends ServiceImpl<StackQueueMapper, StackQueue> implements IStackQueueService {
    @Resource
    private IVmDesignService vmDesignService;
    @Resource
    private IStackQueueService iStackQueueService;
    @Resource
    private BasedInterfaceServiceExt basedInterfaceServiceExt;
    @Resource
    private JmeterServiceExt jmeterServiceExt;
    @Resource
    private IMonitorToolsService monitorToolsService;
    @Resource
    OpenStackServiceExt openStackServiceExt;
    @Resource
    IImageManageService iImageManageService;
    @Resource
    private IStackRecoveryService stackRecoveryService;
    @Resource
    private IEnvCustomizedService envCustomizedService;
    @Resource
    private AppScanServiceExt appScanServiceExt;
    @Resource
    private UnderStandServiceExt underStandServiceExt;
    @Resource
    private IRunningToolsListService iRunningToolsListService;

    @Resource
    private IImageOtherInfoService iImageOtherInfoService;

    @Resource
    private ImageOtherInfoMapper imageOtherInfoMapper;
    @Resource
    private IRunningToolsAndVmService iRunningToolsAndVmService;
    @Resource
    private IVmSerportExdevRelationService iVmSerportExdevRelationService;

    @Override
    public boolean saveOne(String name, int type, String handleId, String openStackId, String status) {
        StackQueue stackQueue = new StackQueue();
        stackQueue.setIdel(CommonConstant.DATA_INT_IDEL_0);
        stackQueue.setType(type);
        stackQueue.setHandleId(handleId);
        stackQueue.setStatus(status);
        stackQueue.setOpenStackId(openStackId);
        stackQueue.setName(name);
        stackQueue.setCount(0);
        return baseMapper.insert(stackQueue) >= 1;
    }

    @Override
    public List<StackQueue> getHandleQueue() {
        QueryWrapper<StackQueue> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(CommonConstant.DATA_STRING_ID);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        Page<StackQueue> page = new Page<>(1, 10);
        IPage<StackQueue> pageList = this.page(page, queryWrapper);
        if (pageList != null && pageList.getRecords() != null) {
            return pageList.getRecords();
        }
        return null;
    }

    /**
     * ????????????
     *
     * @param imageId ??????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteImages(String id, String imageId) {
        try {
            Boolean code = basedInterfaceServiceExt.deleteImages(imageId, null, null, null, CommonConstant.DATA_STR_0);
            if (code) {
                //????????????
                iImageManageService.removeById(id);
                //?????????????????????????????????????????????
                QueryWrapper<ImageOtherInfo> queryImageOtherWrapper = new QueryWrapper<>();
                queryImageOtherWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                queryImageOtherWrapper.eq(CommonConstant.OPENSTACK_ID, imageId);
                List<ImageOtherInfo> imageOtherInfos = iImageOtherInfoService.list(queryImageOtherWrapper);
                if (imageOtherInfos.size() != 0) {
                    iImageOtherInfoService.removeByIds(imageOtherInfos.stream().map(ImageOtherInfo::getId).collect(Collectors.toList()));
                }
                return true;
            }
        } catch (Exception e) {
            log.error("??????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ????????????????????????
     *
     * @param stackId    openstack?????????id
     * @param snapshotId ????????????/??????id
     * @param id         ????????????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteDestroy(String id, String stackId, String snapshotId) {
        try {
            Boolean code = basedInterfaceServiceExt.deleteImages(null, stackId, snapshotId, null, CommonConstant.DATA_STR_2);
            if (code) {
                stackRecoveryService.removeById(id);
                return true;
            }
        } catch (Exception e) {
            log.error("????????????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ???????????? ??????????????????
     *
     * @param ids ????????????id
     * @return boolean
     */
    @Override
    public List<String> deleteEnv(String ids) {
        //???????????????id ??????
        List<String> messageList = new ArrayList<>();
        List<String> idList = Arrays.asList(ids.split(","));
        if (idList.size() > 0) {
            try {
                for (int i = 0; i < idList.size(); i++) {
                    //????????????id??????????????????
                    QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq(CommonConstant.DATA_STRING_ID, idList.get(i));
                    queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                    EnvCustomized envCustomized = envCustomizedService.getOne(queryWrapper);
                    //openstack?????????id
                    String openStackId = envCustomized.getStackId();
                    //??????????????????????????????
                    Boolean code = null;
                    if (StringUtils.isNotBlank(openStackId)) {
                        code = basedInterfaceServiceExt.deleteImages(null, openStackId, null, null, CommonConstant.DATA_STR_1);
                    } else {
                        messageList.add(envCustomized.getId());
                    }
                    if (code) {
                        //????????????id?????????????????????  ????????????????????????????????????
                        QueryWrapper<VmDesign> queryWrapperVm = new QueryWrapper<>();
                        queryWrapperVm.eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, envCustomized.getId());
                        queryWrapperVm.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<VmDesign> vmDesignList = vmDesignService.list(queryWrapperVm);
                        List<String> vmIds = vmDesignList.stream().map(VmDesign::getId).collect(Collectors.toList());
                        vmDesignService.removeByIds(vmIds);

                        //????????????id????????????id ????????????
                        QueryWrapper<StackRecovery> stackRecoveryQueryWrapper = new QueryWrapper<>();
                        stackRecoveryQueryWrapper.eq(CommonConstant.EVN_ID, envCustomized.getId());
                        stackRecoveryQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<StackRecovery> stackRecoveryList = stackRecoveryService.list(stackRecoveryQueryWrapper);
                        List<String> stackRecoveryIds = stackRecoveryList.stream().map(StackRecovery::getId).collect(Collectors.toList());
                        stackRecoveryService.removeByIds(stackRecoveryIds);

                        //????????????id ???????????????  ????????????
                        QueryWrapper<VmSerportExdevRelation> vmSerportExdevRelationQueryWrapper = new QueryWrapper<>();
                        vmSerportExdevRelationQueryWrapper.eq(CommonConstant.EVN_ID, envCustomized.getId());
                        vmSerportExdevRelationQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<VmSerportExdevRelation> vmSerportExdevRelationList = iVmSerportExdevRelationService.list(vmSerportExdevRelationQueryWrapper);
                        List<String> vmSerportExdevRelationIds = vmSerportExdevRelationList.stream().map(VmSerportExdevRelation::getId).collect(Collectors.toList());
                        iVmSerportExdevRelationService.removeByIds(vmSerportExdevRelationIds);

                        //??????????????????
                        envCustomizedService.removeById(envCustomized.getId());
                    } else {
                        messageList.add(envCustomized.getId());
                    }
                }
                return messageList;
            } catch (Exception e) {
                log.error("??????????????????,??????:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * ???????????????
     *
     * @param serverId ?????????id
     * @param id       ???????????????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteVm(String id, String serverId) {
        try {
            Boolean code = basedInterfaceServiceExt.deleteImages(null, null, null, serverId, CommonConstant.DATA_STR_3);
            if (code) {
                //????????????
                vmDesignService.removeById(id);
                return true;
            }
        } catch (Exception e) {
            log.error("?????????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ???????????????
     *
     * @param id       ??????????????????id
     * @param serverId ?????????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean suspendVm(String id, String serverId) {
        try {
            //1.???????????????????????????????????????????????????
            VmDesign vmDesign = vmDesignService.getById(id);
            if (vmDesign != null) {
                //???????????????
                String status = vmDesign.getStatus().toString();
                //2.????????????????????? ?????? ?????????????????????
                if (ServerStatusEnum.ACTIVE.toString().equals(status)) {
                    //3.??????????????????????????????????????????
                    Boolean code = basedInterfaceServiceExt.getSuspendVm(serverId, CommonConstant.DATA_STR_0);
                    if (code) {
                        //????????????????????????????????????
                        vmDesign.setStatus(ServerStatusEnum.SUSPEND_IN_PROGRESS);
                        vmDesignService.updateById(vmDesign);
                        if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_1, id, serverId, ServerStatusEnum.SUSPEND_IN_PROGRESS.toString())) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("??????????????????-????????????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ???????????????
     *
     * @param id       ??????????????????id
     * @param serverId ?????????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean resumeVm(String id, String serverId) {
        try {
            //1.???????????????????????????????????????????????????
            VmDesign vmDesign = vmDesignService.getById(id);
            if (vmDesign != null) {
                //???????????????
                String status = vmDesign.getStatus().toString();
                //2.????????????????????? ???????????????????????????
                if (ServerStatusEnum.SUSPENDED.toString().equals(status)) {
                    //3.????????????????????????????????????????????????
                    Boolean code = basedInterfaceServiceExt.getSuspendVm(serverId, CommonConstant.DATA_STR_1);
                    if (code) {
                        //??????????????????
                        vmDesign.setStatus(ServerStatusEnum.RESUME_IN_PROGRESS);
                        vmDesignService.updateById(vmDesign);
                        //????????????
                        if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_1, id, serverId, ServerStatusEnum.RESUME_IN_PROGRESS.toString())) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("??????????????????-????????????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * jmeter??????
     *
     * @param planName    ????????????
     * @param numThreads  ?????????
     * @param loops       ????????????
     * @param url         ????????????ip
     * @param port        ?????????
     * @param httpRequest ????????????
     * @param duration    ????????????
     * @param path        ??????????????????
     * @param request     ????????????
     * @param jmeterPath  ??????????????????
     * @param id          ?????????????????????id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getJmeter(String planName, int numThreads, String loops, String url, String port, String httpRequest, int duration, String path, String request, String jmeterPath, String id) {
        try {
            String htmlPath = jmeterServiceExt.getJmeter(planName, numThreads, loops, url, port, httpRequest, duration, path, request, jmeterPath);
            if (StringUtils.isNotEmpty(htmlPath)) {
                MonitorTools monitorTools = new MonitorTools();
                monitorTools.setId(id);
                //????????????
                monitorTools.setTestName(planName);
                //?????????
                monitorTools.setNumThreads(String.valueOf(numThreads));
                //????????????
                monitorTools.setRampUp(String.valueOf(duration));
                //????????????
                monitorTools.setLoops(loops);
                //????????????
                monitorTools.setDomain(url);
                //?????????
                monitorTools.setPort(port);
                //????????????
                monitorTools.setMethod(httpRequest);
                //????????????
                monitorTools.setPath(htmlPath);
                //??????????????????
                monitorTools.setJmeterPath(jmeterPath);
                monitorToolsService.updateById(monitorTools);
                return true;
            }
        } catch (IOException e) {
            log.error("jmeter??????-??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ?????????????????????
     *
     * @param id     ??????????????????
     * @param flavor ????????????
     * @return true
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getVmAlter(String id, String flavor, String size, String rollback) {
        try {
            //?????? id ???????????????????????????
            VmDesign vmDesign = vmDesignService.getById(id);
            //????????????????????????????????????????????????????????????????????????
            if ((ServerStatusEnum.ACTIVE.getStatus().equals(vmDesign.getStatus().toString())||ServerStatusEnum.BACKUP_COMPLETE.getStatus().equals(vmDesign.getStatus().toString()) || ServerStatusEnum.SNAPSHOT_COMPLETE.getStatus().equals(vmDesign.getStatus().toString()))&& CommonConstant.DATA_INT_0.equals(vmDesign.getIdel())) {
                //???????????????????????????????????? ???????????? id
                EnvCustomized envCustomized = envCustomizedService.getById(vmDesign.getPlanId());
                Boolean code = null;
                if (envCustomized != null && StringUtils.isNotBlank(envCustomized.getStackId())) {
                    code = basedInterfaceServiceExt.getVmAlter(envCustomized.getStackId(), flavor, vmDesign.getPlanName(), rollback, vmDesign.getOpenstackImageId(), size);
                }
                if (code) {
                    String[] originalNames = flavor.split(CommonConstant.SEPARATOR);
                    if (originalNames.length >= CommonConstant.DATA_INT_3) {
                        //CPU
                        vmDesign.setVirCpu(originalNames[0].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                        //??????
                        vmDesign.setVirInner(originalNames[1].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                        //????????????
                        vmDesign.setVirDisk(size);
                    }
                    vmDesign.setId(id);
                    if (vmDesignService.updateById(vmDesign)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("?????????????????????-??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ?????????????????????
     *
     * @param name   ???????????????
     * @param image  ???????????????id
     * @param flavor ???????????????
     * @return ?????????????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getServerCompute(String name, String image, String flavor, String rollback, String size, String envName, String envId, String projectId) {
        try {
            //??????size ??? 0 ??????????????????????????????
            if (size.equals(CommonConstant.DATA_STR_0)) {
                JSONObject jsonObject = openStackServiceExt.getImage(image);
                JSONArray objects = JSON.parseArray(jsonObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_IMAGE).get(CommonConstant.BLOCK_DEVICE_MAPPING).toString());
                JSONObject parseObject = JSONObject.parseObject(objects.get(0).toString());
                size = parseObject.get(CommonConstant.VOLUME_SIZE).toString();
            } else {
                // ?????? 0 ??? ?????? 100
                size = CommonConstant.DATA_STR_100;
            }
            //???????????????
            String vmName = RandomUtil.randomString(CommonConstant.RANDOM,6);
            //????????????
            ServerCompute serverCompute = openStackServiceExt.getServerCompute(vmName, image, flavor, rollback,size);
            if (CommonConstant.DATA_INT_0 == serverCompute.getCode()) {
                //??????????????????????????????
                VmDesign vmDesign = new VmDesign();
                //??????
                vmDesign.setVmName(name);
                //????????????
                vmDesign.setEnvName(envName);
                //??????id
                vmDesign.setPlanId(envId);
                //??????id
                vmDesign.setOpenstackImageId(image);
                //??????
                vmDesign.setStatus(ServerStatusEnum.toEnum(serverCompute.getStatus()));
                vmDesign.setIdel(CommonConstant.DATA_INT_IDEL_0);
                vmDesign.setPlanName(vmName);
                vmDesign.setProjectId(projectId);
                //CPU+??????+??????
                String[] originalNames = flavor.split(CommonConstant.SEPARATOR);
                if (originalNames.length >= CommonConstant.DATA_INT_3) {
                    //CPU
                    vmDesign.setVirCpu(originalNames[0].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                    //??????
                    vmDesign.setVirInner(originalNames[1].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                    //????????????
                    vmDesign.setVirDisk(size);
                }
                EnvCustomized envCustomized = new EnvCustomized();
                envCustomized.setId(envId);
                envCustomized.setStackId(serverCompute.getId());
                envCustomizedService.updateById(envCustomized);
                if (vmDesignService.save(vmDesign)) {
                    //?????????????????????????????????
                    if (StringUtils.isEmpty(vmDesign.getOpenstackImageId())) {
                        log.info(vmDesign.getVmName() + ">>>>>>?????????????????????????????????");
                    } else {
                        ImageManage imageManage = iImageManageService.getImageByOpenstackId(vmDesign.getOpenstackImageId());
                        if (Objects.nonNull(imageManage) && StringUtils.isNotBlank(imageManage.getTestTools())) {
                            List<String> toolsList = Arrays.asList(imageManage.getTestTools().split(","));
                            //????????????????????????????????????
                            if (iRunningToolsListService.setRunningToolListBytools(toolsList, vmDesign)) {
                                log.info("?????????????????????????????????????????????");
                            }
                        }
                    }
                    //????????????
                    if (iStackQueueService.saveOne(vmDesign.getVmName(), CommonConstant.DATA_INT_1, vmDesign.getId(), serverCompute.getId(), serverCompute.getStatus())) {
                      return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("???????????????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ????????????????????????
     *
     * @param id            ???????????????id
     * @param serverId      openstack?????????id
     * @param stackName     ???????????????
     * @param stackStatus   ???????????????
     * @param remark        ????????????
     * @param name          ????????????
     * @param sysType       ????????????
     * @param otherSoftware ????????????
     * @param testTools     ????????????
     * @param systemVersion ????????????
     * @return ??????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getServerAction(String id, String serverId, String stackName, String stackStatus, String
            remark, String name, String sysType, String otherSoftware, String testTools, String systemVersion) {
        try {
            //????????????
            String imageName = RandomUtil.randomStringUpper(6) + name;
            //??????????????????
            FoundImage foundImage = openStackServiceExt.getServerAction(null, serverId, imageName);
            if (CommonConstant.DATA_INT_0.equals(foundImage.getCode())) {
                //???????????????
                ImageManage imageManage = new ImageManage();
                imageManage.setName(name);
                imageManage.setImageName(foundImage.getName());
                imageManage.setVmId(id);
                imageManage.setStackName(stackName);
                imageManage.setStackStatus(stackStatus);
                imageManage.setRemark(remark);
                imageManage.setSysType(sysType);
                imageManage.setOpenstackId(foundImage.getId());
                imageManage.setImageStatus(ImageTypeEnum.toEnum(foundImage.getStatus().toUpperCase()));
                imageManage.setServerId(serverId);
                imageManage.setIdel(CommonConstant.DATA_INT_IDEL_0);
                imageManage.setTestTools(testTools);
                //??????????????????????????? ????????????
                List<RunningToolsList> runningToolsLists = new ArrayList<>();
                runningToolsLists = iImageManageService.getRunningToolsLists(imageManage, runningToolsLists);
                if (StringUtils.isNotBlank(systemVersion)) {
                    imageManage.setSystemVersion(systemVersion);
                }
                if (StringUtils.isNotBlank(otherSoftware)) {
                    //????????????
                    String otherSoftwareString = iImageManageService.getDealCommaString(otherSoftware);
                    if (StringUtils.isNotBlank(otherSoftwareString)) {
                        imageManage.setOtherSoftware(otherSoftwareString);
                    }
                }
                iImageManageService.save(imageManage);
                //tepm_image_other_info ?????????????????????
                iImageOtherInfoService.saveInfos(imageManage, runningToolsLists);
                return true;
            }
        } catch (Exception e) {
            log.error("??????????????????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * ???????????????-??????/????????????
     *
     * @param envId      ????????????id
     * @param stackId    openstack?????????id
     * @param snapshotId ????????????/??????id
     * @return ??????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean restore(String envId, String stackId, String snapshotId) {
        try {
            //????????????????????????code??? 0 ????????? ???????????????
            String code = basedInterfaceServiceExt.getRestore(stackId, snapshotId);
            if (CommonConstant.DATA_STR_0.equals(code)) {
                //??????envId???snapshotId?????????????????????
                QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(CommonConstant.DATA_STRING_ID, envId);
                queryWrapper.eq(CommonConstant.STRING_STACK_ID, stackId);
                queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                EnvCustomized envCustomized = envCustomizedService.getOne(queryWrapper);

                QueryWrapper<StackRecovery> stackWrapper = new QueryWrapper<>();
                stackWrapper.eq(CommonConstant.EVN_ID, envId);
                stackWrapper.eq(CommonConstant.STRING_STACK_ID, stackId);
                stackWrapper.eq(CommonConstant.SNAPSHOT_ID, snapshotId);
                stackWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                StackRecovery stackRecovery = stackRecoveryService.getOne(stackWrapper);
                // ????????????-0??????1??????'
                Integer type = stackRecovery.getType();
                EnvStackStatusEnum state = EnvStackStatusEnum.RESTORE_IN_PROGRESS;
                if (CommonConstant.DATA_INT_1.equals(type)) {
                    state = EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                }
                if (envCustomized != null) {
                    //????????????
                    boolean saveOne = iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), stackId, (state).toString());
                    if (saveOne) {
                        //??????????????????????????? :?????????????????? ????????????-0??????1??????',
                        envCustomized.setState(state);
                        stackRecovery.setRestoreBeginTime(new Date());
                        //?????? / ?????? ?????? ????????????
                        stackRecoveryService.updateById(stackRecovery);
                        envCustomizedService.updateById(envCustomized);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("???????????????-??????/??????????????????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * ??????????????????
     *
     * @param stackId openstack?????????id
     * @param envId   ????????????id
     * @param name    ??????/????????????
     * @param type    0??????/1??????
     * @return ??????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean snapshot(String stackId, String envId, String name, Integer type, String typeString) {
        try {
            //True???????????????false????????????
            boolean incremental = false;
            if (CommonConstant.DATA_INT_0.equals(type)) {
                incremental = true;
            }
            //??????????????????
            String snapshotName = RandomUtil.randomStringUpper(6) + name;
            Snapshots snapshots = basedInterfaceServiceExt.getSnapshots(stackId, snapshotName, incremental);
            //??????????????? ?????? ?????????
            if (snapshots != null && CommonConstant.DATA_STR_0.equals(snapshots.getCode()) || EnvStackStatusEnum.IN_PROGRESS.equals(snapshots.getStatus())) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                StackRecovery stackRecovery = new StackRecovery();
                stackRecovery.setIdel(CommonConstant.DATA_INT_IDEL_0);
                stackRecovery.setName(name);
                stackRecovery.setType(type);
                stackRecovery.setStackId(stackId);
                stackRecovery.setEnvId(envId);
                stackRecovery.setSnapshotId(snapshots.getId());
                stackRecovery.setCreateTime(new Date());
                stackRecovery.setStackName(snapshots.getName());
                stackRecovery.setStatus(snapshots.getStatus());
                stackRecovery.setStatusReason(snapshots.getStatusReason());
                //??????/?????? ????????????
                Date backupsBeginTime = null;
                Date newBackupsBeginTime = null;
                try {
                    backupsBeginTime = sdf.parse(snapshots.getTime());
                    String str = df.format(backupsBeginTime);
                    newBackupsBeginTime = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //????????????????????????????????? ??????8?????? ???????????????8??????
                Date date = FormatTimeEightUtils.timeReversal(df.format(newBackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                stackRecovery.setBackupsBeginTime(date);
                stackRecoveryService.save(stackRecovery);
                //???????????? ??????????????????
                if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), stackId, snapshots.getStatus())) {
                    //??????????????????
                    EnvCustomized envCustomized = envCustomizedService.getById(envId);
                    //??????
                    if (CommonConstant.DATA_INT_0.equals(type)) {
                        envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_IN_PROGRESS);
                    } else {
                        envCustomized.setState(EnvStackStatusEnum.BACKUP_IN_PROGRESS);
                    }
                    envCustomizedService.updateById(envCustomized);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("????????????" + typeString + "??????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * appScan????????????
     *
     * @param path ????????????
     * @param id   monitorTools??????
     * @return ????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getAppScan(String id, String path) {
        try {
            String appScanPath = appScanServiceExt.getAppScan(path);
            MonitorTools monitorTools = new MonitorTools();
            monitorTools.setId(id);
            //????????????
            monitorTools.setDomain(path);
            monitorTools.setPath(appScanPath);
            monitorToolsService.updateById(monitorTools);
            return true;
        } catch (IOException e) {
            log.error("appScan??????????????????,??????:" + e.getMessage());
            return false;
        }
    }

    /**
     * UnderStand????????????
     *
     * @param id   monitorTools??????
     * @param path ??????????????????
     * @return ????????????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getUnderStand(String id, String path) {
        try {
            String underStandPath = underStandServiceExt.getUnderStand(path);
            MonitorTools monitorTools = new MonitorTools();
            monitorTools.setId(id);
            //????????????
            monitorTools.setDomain(path);
            monitorTools.setPath(underStandPath);
            monitorToolsService.updateById(monitorTools);
            return true;
        } catch (IOException e) {
            log.error("UnderStand??????????????????,??????:" + e.getMessage());
            return false;
        }
    }
}
