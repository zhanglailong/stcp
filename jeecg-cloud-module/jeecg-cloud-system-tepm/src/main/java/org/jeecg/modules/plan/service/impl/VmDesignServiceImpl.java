package org.jeecg.modules.plan.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FormatTimeEightUtils;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.jeecg.modules.openstack.entity.Snapshots;
import org.jeecg.modules.openstack.entity.StackList;
import org.jeecg.modules.openstack.entity.StackRecovery;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.IStackRecoveryService;
import org.jeecg.modules.openstack.service.ext.BasedInterfaceServiceExt;
import org.jeecg.modules.plan.entity.*;
import org.jeecg.modules.plan.mapper.VmDesignMapper;
import org.jeecg.modules.plan.service.IImageManageService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.running.monitortools.entity.RunningToolsAndVm;
import org.jeecg.modules.running.monitortools.mapper.RunningToolsAndVmMapper;
import org.jeecg.modules.running.tools.mapper.RunningToolsListMapper;
import org.jeecg.modules.running.tools.service.IRunningToolsListService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.testtooldistribute.fegin.SocketServerFegin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zlf
 * ???????????????
 * 2021-01-06
 * V1.0
 */
@Slf4j
@Service
public class VmDesignServiceImpl extends ServiceImpl<VmDesignMapper, VmDesign> implements IVmDesignService {

    @Resource
    private VmDesignMapper vmDesignMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private RunningToolsListMapper runningToolsListMapper;
    @Resource
    private SocketServerFegin socketServerFegin;
    @Resource
    private RunningToolsAndVmMapper runningToolsAndVmMapper;
    @Resource
    IImageManageService iImageManageService;
    @Resource
    private IRunningToolsListService iRunningToolsListService;
    @Resource
    private BasedInterfaceServiceExt basedInterfaceServiceExt;
    @Resource
    private IStackRecoveryService stackRecoveryService;
    @Resource
    private IVmDesignService iVmDesignService;
    @Resource
    private IStackQueueService iStackQueueService;
    @Resource
    private IEnvCustomizedService envCustomizedService;


    @Value(value = "${socket.server.collect.startUrl}")
    public String startUrl;
    @Value(value = "${socket.server.collect.stopUrl}")
    public String stopUrl;

    @Resource
    IEnvCustomizedService iEnvCustomizedService;


    @Override
    public List<VmDesign> getVmListByIds(List<String> ids) {
        return vmDesignMapper.getVmListByIds(ids);
    }

    @Override
    public List<VmDesign> getVmListByEnvId(String envId) {
        QueryWrapper<VmDesign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", envId);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return vmDesignMapper.selectList(queryWrapper);
    }

    @Override
    public VmDesign getVmByVmId(String vmId) {
        QueryWrapper<VmDesign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vm_id", vmId);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return vmDesignMapper.selectOne(queryWrapper);
    }

    @Override
    public void sendVmOrder(String ids, Integer type) {
        try {
            List<VmDesign> vmDesigns = vmDesignMapper.getVmListByIds(Arrays.asList(ids.split(",")));
            if (vmDesigns != null) {
                System.out.print("vmDesigns:" + vmDesigns.size());
                vmDesigns.forEach(n -> {
                    System.out.println(",key:" + n.getNetworkId());
                    MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
                    map.add("key", n.getNetworkId());
                    if (type.equals(CommonConstant.DATA_INT_1)) {
                        // ??????
                        restTemplate.postForObject(startUrl, map, Boolean.class);
                    }
                    if (type.equals(CommonConstant.DATA_INT_2)) {
                        // ??????
                        restTemplate.postForObject(stopUrl, map, Boolean.class);
                    }
                });
            }
        } catch (Exception e) {
            log.error("????????????????????????:" + e);
        }
    }

    @Override
    public boolean getSaveOrUpByJsonAndPid(String createBy, String planJson, String envId, String envName, List<StackList.Resources> resources) {
        Map<String, String> collect = resources.stream().filter(x -> x.getResourceType().equals(CommonConstant.DATA_STRING_OS_NOVA_SERVER))
                .collect(Collectors.toMap(StackList.Resources::getResourceName, StackList.Resources::getPhysicalResourceId));
        //????????????????????????????????????
        EnvPlanJson envPlanJson = JSON.parseObject(planJson, EnvPlanJson.class);
        Map<String, List<EnvPlanNodes>> nodes = new HashMap<>(6);
        if (envPlanJson != null) {
            //??????type??????????????????
            envPlanJson.getNodeList().forEach(n -> {
                if (n.getType().equals(CommonConstant.PLAN_TYPE_VIRTUAL)) {
                    List<EnvPlanNodes> plans = nodes.get(n.getType());
                    if (plans == null || plans.isEmpty()) {
                        plans = new ArrayList<>();
                    }
                    plans.add(n);
                    nodes.put(n.getType(), plans);
                }
            });
        }
        List<EnvPlanNodes> virtualList = nodes.get(CommonConstant.PLAN_TYPE_VIRTUAL);
        List<VmDesign> vmDesigns = new ArrayList<>();
        virtualList.forEach(n -> {
            //?????????????????????
            int vnNum = StringUtils.isNotEmpty(n.getVmNum()) ? Integer.parseInt(n.getVmNum()) : CommonConstant.DATA_INT_1;
            for (int i = 0; i < vnNum; i++) {
                VmDesign vmDesign = new VmDesign();
                vmDesign.setVmName(StringUtils.isNotEmpty(n.getVmName()) ? n.getVmName() : n.getName());
                vmDesign.setVirCpu(n.getVirCpu());
                vmDesign.setVirInner(n.getVirInner());
                vmDesign.setVirDisk(n.getVirDisk());
                vmDesign.setCreateBy(createBy);
                vmDesign.setIdel(CommonConstant.DATA_INT_IDEL_0);
                vmDesign.setStatus(ServerStatusEnum.BUILD);
                vmDesign.setPlanId(envId);
                vmDesign.setPlanName(envName);
                vmDesign.setOpenstackImageId(n.getMirror());
                EnvCustomized env = iEnvCustomizedService.getById(envId);
                vmDesign.setEnvName(env.getEnvName());
                vmDesign.setProjectId(env.getProjectId());
                StringBuffer serverId = new StringBuffer();
                serverId.append(CommonConstant.DATA_STRING_VIR_SERVER).append(n.getName()).append(n.getId()).append(i + 1);
                String virId = collect.get(serverId.toString());
                //???????????????
                vmDesign.setVmCode(n.getId() + (i + 1));
                if (StringUtils.isNotEmpty(virId)) {
                    vmDesign.setOpenVmId(virId);
                    vmDesign.setServerId(virId);
                    vmDesigns.add(vmDesign);
                }
            }
        });
        if (vmDesigns.size() > 0) {
            if (this.saveBatch(vmDesigns)) {
                if (addToolsVm(vmDesigns)) {
                    log.info("????????????????????????>>>>>??????????????????????????????????????????");
                } else {
                    log.error("????????????????????????>>>>>??????????????????????????????????????????");
                }
            }
            return true;
        }
        return false;
    }

    public boolean addToolsVm(List<VmDesign> vmDesigns) {
        try {
            vmDesigns.forEach(v -> {
                //??????id
                String openstackImageId = v.getOpenstackImageId();
                //???????????????
                if(StringUtils.isEmpty(openstackImageId)){
                    log.info("????????????????????????");
                }else{
                    ImageManage imageManager = iImageManageService.getByOpenstackId(openstackImageId);
                    //????????????????????????
                    if (Objects.nonNull(imageManager) && StringUtils.isNotBlank(imageManager.getTestTools())) {
                        List<String> toolsList = Arrays.asList(imageManager.getTestTools().split(","));
                        //????????????????????????????????????
                        if (iRunningToolsListService.setRunningToolListBytools(toolsList, v)) {
                            log.info("?????????????????????????????????????????????");
                        }
                    }
                }
            });
            return true;
        } catch (Exception e) {
            log.error("?????????????????????????????????????????????" + e.getMessage());
            return false;
        }
    }

    @Override
    public List<VmDesign> getStateBuildServer() {
        QueryWrapper<VmDesign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_SERVER_STATUS, ServerStatusEnum.BUILD);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<RunningToolsAndVm> toolList(String vmId) {
        VmDesign vmDesign = vmDesignMapper.selectById(vmId);
        QueryWrapper<RunningToolsAndVm> queryWrapper = new QueryWrapper<RunningToolsAndVm>();
        queryWrapper.eq("vm_id", vmId);
        List<RunningToolsAndVm> runningToolsAndVms = runningToolsAndVmMapper.selectList(queryWrapper);
        runningToolsAndVms.forEach(t -> {
            Result<?> result = socketServerFegin.sendMsg(t.getVmAddress(), t.getId(), CommonConstant.SOCKET_REGISTER_CODE_200, null, null, t.getToolsCode(), CommonConstant.DATA_INT_4, t.getToolProcessName(), t.getId(), t.getToolsPort(), t.getToolLinuxProcessName());
            log.info("?????????????????? >>>>>>>" + result);
        });
        return runningToolsAndVms;
    }


    /**
     * ??????????????????
     *
     * @param vmDesign ???????????????
     * @param childNet ??????
     * @return ??????????????????
     */
    public VmDesign getChildNet(VmDesign vmDesign, VmDesign childNet) {
        vmDesign.setNetworkId(childNet.getNetworkId());
        vmDesign.setChildNetId(childNet.getChildNetId());
        vmDesign.setCidr(childNet.getCidr());
        vmDesign.setSubnetName(childNet.getSubnetName());
        vmDesign.setNetworkAddress(childNet.getNetworkAddress());
        vmDesign.setIpVersion(childNet.getIpVersion());
        vmDesign.setGatewayIp(childNet.getGatewayIp());
        vmDesign.setDisableGateway(childNet.getDisableGateway());
        vmDesign.setDhcpActivation(childNet.getDhcpActivation());
        vmDesign.setAddressPool(childNet.getAddressPool());
        vmDesign.setDnsServer(childNet.getDnsServer());
        return vmDesign;
    }

    /**
     * ?????????????????????
     *
     * @param vmDesign ?????????
     * @param vm       ?????????
     * @return ?????????????????????
     */
    public VmDesign getVm(VmDesign vmDesign, VmDesign vm) {
        vmDesign.setVmId(vm.getId());
        vmDesign.setVmName(vm.getVmName());
        vmDesign.setVirCpu(vm.getVirCpu());
        vmDesign.setVirInner(vm.getVirInner());
        vmDesign.setVirDisk(vm.getVirDisk());
        vmDesign.setSysType(vm.getSysType());
        vmDesign.setMeasurand(vm.getMeasurand());
        vmDesign.setTestTools(vm.getTestTools());
        vmDesign.setSupportSoftware(vm.getSupportSoftware());
        vmDesign.setAssetPool(vm.getAssetPool());
        vmDesign.setOpenVmId(vm.getOpenVmId());
        vmDesign.setOsFlavorAccess(vm.getOsFlavorAccess());
        vmDesign.setLinks(vm.getLinks());
        vmDesign.setRxtxFactor(vm.getRxtxFactor());
        return vmDesign;
    }

    /**
     * ??????????????????
     *
     * @param vmDesign ?????????
     * @param route    ??????
     * @return ??????????????????
     */
    public VmDesign getRoute(VmDesign vmDesign, VmDesign route) {
        vmDesign.setRouteFromId(route.getId());
        vmDesign.setRouteName(route.getRouteName());
        vmDesign.setRouteState(route.getRouteState());
        vmDesign.setExtranet(route.getExtranet());
        return vmDesign;
    }

    @Override
    public List<VmDesign> getHandleVm() {
        QueryWrapper<VmDesign> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(CommonConstant.DATA_STRING_ID);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.isNull(CommonConstant.HOST_NAME);
        queryWrapper.isNotNull(CommonConstant.OPEN_VM_ID);
        Page<VmDesign> page = new Page<>(1, 10);
        IPage<VmDesign> pageList = this.page(page, queryWrapper);
        if (pageList != null && pageList.getRecords() != null) {
            return pageList.getRecords();
        }
        return null;
    }

    /**
     * ?????????????????????
     *
     * @param vmId       ??????????????????id
     * @param name       ??????/????????????
     * @param type       0??????/1??????
     * @param typeString ??????/??????
     * @return ??????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean vmSnapshot(String vmId, String name, Integer type, String typeString) {
        try {
            //True???????????????false????????????
            boolean incremental = false;
            if (CommonConstant.DATA_INT_0.equals(type)) {
                incremental = true;
            }
            //??????????????? ?????? stackId
            VmDesign vmDesign = iVmDesignService.getById(vmId);
            EnvCustomized envCustomized = null;
            if (vmDesign != null && StringUtils.isNotBlank(vmDesign.getPlanId())) {
                envCustomized = iEnvCustomizedService.getById(vmDesign.getPlanId());
            }
            //?????????????????????
            String snapshotName = RandomUtil.randomStringUpper(6) + name;
            Snapshots snapshots = basedInterfaceServiceExt.getSnapshots(envCustomized.getStackId(), snapshotName, incremental);
            if (snapshots != null && CommonConstant.DATA_STR_0.equals(snapshots.getCode())) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                StackRecovery stackRecovery = new StackRecovery();
                stackRecovery.setIdel(CommonConstant.DATA_INT_IDEL_0);
                stackRecovery.setName(name);
                stackRecovery.setType(type);
                stackRecovery.setVmId(vmId);
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
                stackRecovery.setStackId(envCustomized.getStackId());
                stackRecoveryService.save(stackRecovery);
                //???????????? ??????????????????
                if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), envCustomized.getStackId(), snapshots.getStatus())) {
                    //??????
                    if (CommonConstant.DATA_INT_0.equals(type)) {
                        vmDesign.setStatus(ServerStatusEnum.SNAPSHOT_IN_PROGRESS);
                        envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_IN_PROGRESS);
                    } else {
                        vmDesign.setStatus(ServerStatusEnum.BACKUP_IN_PROGRESS);
                        envCustomized.setState(EnvStackStatusEnum.BACKUP_IN_PROGRESS);
                    }
                    //????????????????????????????????? ??? ?????? / ?????? ???
                    iVmDesignService.updateById(vmDesign);
                    iEnvCustomizedService.updateById(envCustomized);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("?????????" + typeString + "??????,??????:" + e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public List<VmProject> getEnvList(VmProjectVo vmProjectVo) {
        List<VmProject> list = vmDesignMapper.findByPage(vmProjectVo);
        return list;
    }

    /**
     * ?????????-??????/????????????
     *
     * @param vmId       ??????????????????id
     * @param stackId    openstack?????????id
     * @param snapshotId ????????????/??????id
     * @return ??????
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean restore(String envId, String vmId, String stackId, String snapshotId) {
        try {
            //????????????????????????code??? 0 ????????? ???????????????
            String code = basedInterfaceServiceExt.getRestore(stackId, snapshotId);
            if (CommonConstant.DATA_STR_0.equals(code)) {
                VmDesign vmDesign = iVmDesignService.getById(vmId);
                //??????envId???snapshotId?????????????????????
                QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(CommonConstant.DATA_STRING_ID, envId);
                queryWrapper.eq(CommonConstant.STRING_STACK_ID, stackId);
                queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                EnvCustomized envCustomized = envCustomizedService.getOne(queryWrapper);

                QueryWrapper<StackRecovery> stackWrapper = new QueryWrapper<>();
                stackWrapper.eq(CommonConstant.VM_ID, vmId);
                stackWrapper.eq(CommonConstant.STRING_STACK_ID, stackId);
                stackWrapper.eq(CommonConstant.SNAPSHOT_ID, snapshotId);
                stackWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                StackRecovery stackRecovery = stackRecoveryService.getOne(stackWrapper);
                // ????????????-0??????1??????'
                Integer type = stackRecovery.getType();
                String status = null;
                EnvStackStatusEnum state = EnvStackStatusEnum.RESTORE_IN_PROGRESS;
                ServerStatusEnum statusEnum = ServerStatusEnum.RESUME_IN_PROGRESS;
                if (CommonConstant.DATA_INT_1.equals(type)) {
                    state = EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                    statusEnum = ServerStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                }
                if (envCustomized != null) {
                    //????????????
                    boolean saveOne = iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, envId, stackId, (state).toString());
                    if (saveOne) {
                        //????????????????????????????????? ?????? :?????????????????? ????????????-0??????1??????',
                        envCustomized.setState(state);
                        vmDesign.setStatus(statusEnum);
                        stackRecovery.setRestoreBeginTime(new Date());
                        //?????? / ?????? ?????? ????????????
                        stackRecoveryService.updateById(stackRecovery);
                        envCustomizedService.updateById(envCustomized);
                        iVmDesignService.updateById(vmDesign);
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

    @Override
    public AgentInfo queryByAgentIp(String agentIp) {
        QueryWrapper<VmDesign> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.eq(CommonConstant.DATA_STRING_NETWORK_ADDRESS, agentIp);
        VmDesign vm = this.getOne(queryWrapper);
        if (vm != null) {
            AgentInfo agentInfo = new AgentInfo();
            agentInfo.setAgentIp(agentIp);
            agentInfo.setEnvirId(vm.getPlanId());
            agentInfo.setProjectId(vm.getProjectId());
            agentInfo.setStatus(vm.getStatus().getStatus());
            agentInfo.setVmName(vm.getVmName());
            QueryWrapper<ImageManage> imaQuery = new QueryWrapper<>();
            imaQuery.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            imaQuery.eq(CommonConstant.DATA_STRING_VM_ID, vm.getId());
            List<ImageManage> list = iImageManageService.list(imaQuery);
            List<String> testToolIds = new ArrayList<>();
            list.forEach(imageManage -> {
                List<String> testTools = Arrays.asList(imageManage.getTestTools().split(","));
                if (testTools.size() != 0) {
                    testToolIds.addAll(testTools);
                }
            });
            agentInfo.setTestToolId(String.join(",", testToolIds));
            return agentInfo;
        }
        return null;
    }
}
