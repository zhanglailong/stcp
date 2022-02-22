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
 * 虚拟机设计
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
                        // 启动
                        restTemplate.postForObject(startUrl, map, Boolean.class);
                    }
                    if (type.equals(CommonConstant.DATA_INT_2)) {
                        // 停止
                        restTemplate.postForObject(stopUrl, map, Boolean.class);
                    }
                });
            }
        } catch (Exception e) {
            log.error("发送批量指令异常:" + e);
        }
    }

    @Override
    public boolean getSaveOrUpByJsonAndPid(String createBy, String planJson, String envId, String envName, List<StackList.Resources> resources) {
        Map<String, String> collect = resources.stream().filter(x -> x.getResourceType().equals(CommonConstant.DATA_STRING_OS_NOVA_SERVER))
                .collect(Collectors.toMap(StackList.Resources::getResourceName, StackList.Resources::getPhysicalResourceId));
        //解析需要更新的虚拟机信息
        EnvPlanJson envPlanJson = JSON.parseObject(planJson, EnvPlanJson.class);
        Map<String, List<EnvPlanNodes>> nodes = new HashMap<>(6);
        if (envPlanJson != null) {
            //获取type不同分类集合
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
            //判断虚拟机数量
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
                //虚拟机标识
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
                    log.info("自动创建测试环境>>>>>测试工具虚拟机对应表添加完成");
                } else {
                    log.error("自动创建测试环境>>>>>测试工具虚拟机对应表添加失败");
                }
            }
            return true;
        }
        return false;
    }

    public boolean addToolsVm(List<VmDesign> vmDesigns) {
        try {
            vmDesigns.forEach(v -> {
                //镜像id
                String openstackImageId = v.getOpenstackImageId();
                //虚拟机镜像
                if(StringUtils.isEmpty(openstackImageId)){
                    log.info("当前虚拟机是裸机");
                }else{
                    ImageManage imageManager = iImageManageService.getByOpenstackId(openstackImageId);
                    //获取测试工具列表
                    if (Objects.nonNull(imageManager) && StringUtils.isNotBlank(imageManager.getTestTools())) {
                        List<String> toolsList = Arrays.asList(imageManager.getTestTools().split(","));
                        //获取所有的测试工具并添加
                        if (iRunningToolsListService.setRunningToolListBytools(toolsList, v)) {
                            log.info("测试工具与虚拟机对应表添加成功");
                        }
                    }
                }
            });
            return true;
        } catch (Exception e) {
            log.error("测试工具与虚拟机对应表添加失败" + e.getMessage());
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
            log.info("查询进程开始 >>>>>>>" + result);
        });
        return runningToolsAndVms;
    }


    /**
     * 获取子网信息
     *
     * @param vmDesign 虚拟机信息
     * @param childNet 子网
     * @return 获取子网信息
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
     * 获取虚拟机信息
     *
     * @param vmDesign 虚拟机
     * @param vm       虚拟机
     * @return 获取虚拟机信息
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
     * 获取路由信息
     *
     * @param vmDesign 虚拟机
     * @param route    路由
     * @return 获取路由信息
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
     * 创建虚拟机快照
     *
     * @param vmId       虚拟机表主键id
     * @param name       快照/备份名称
     * @param type       0快照/1备份
     * @param typeString 快照/备份
     * @return 状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean vmSnapshot(String vmId, String name, Integer type, String typeString) {
        try {
            //True表示快照，false表示备份
            boolean incremental = false;
            if (CommonConstant.DATA_INT_0.equals(type)) {
                incremental = true;
            }
            //查询环境表 获取 stackId
            VmDesign vmDesign = iVmDesignService.getById(vmId);
            EnvCustomized envCustomized = null;
            if (vmDesign != null && StringUtils.isNotBlank(vmDesign.getPlanId())) {
                envCustomized = iEnvCustomizedService.getById(vmDesign.getPlanId());
            }
            //创建虚拟机快照
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
                //快照/备份 开始时间
                Date backupsBeginTime = null;
                Date newBackupsBeginTime = null;
                try {
                    backupsBeginTime = sdf.parse(snapshots.getTime());
                    String str = df.format(backupsBeginTime);
                    newBackupsBeginTime = df.parse(str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //因为统一接口时区有问题 相差8小时 所以时间加8小时
                Date date = FormatTimeEightUtils.timeReversal(df.format(newBackupsBeginTime),CommonConstant.TIME_STEP_SIZE);
                stackRecovery.setBackupsBeginTime(date);
                stackRecovery.setStackId(envCustomized.getStackId());
                stackRecoveryService.save(stackRecovery);
                //加入队列 查询回执时间
                if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), envCustomized.getStackId(), snapshots.getStatus())) {
                    //快照
                    if (CommonConstant.DATA_INT_0.equals(type)) {
                        vmDesign.setStatus(ServerStatusEnum.SNAPSHOT_IN_PROGRESS);
                        envCustomized.setState(EnvStackStatusEnum.SNAPSHOT_IN_PROGRESS);
                    } else {
                        vmDesign.setStatus(ServerStatusEnum.BACKUP_IN_PROGRESS);
                        envCustomized.setState(EnvStackStatusEnum.BACKUP_IN_PROGRESS);
                    }
                    //修改虚拟机和环境的状态 为 快照 / 备份 中
                    iVmDesignService.updateById(vmDesign);
                    iEnvCustomizedService.updateById(envCustomized);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("虚拟机" + typeString + "异常,原因:" + e.getMessage());
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
     * 虚拟机-快照/备份恢复
     *
     * @param vmId       虚拟机表主键id
     * @param stackId    openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @return 状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean restore(String envId, String vmId, String stackId, String snapshotId) {
        try {
            //获取快照恢复状态code值 0 为成功 其他为失败
            String code = basedInterfaceServiceExt.getRestore(stackId, snapshotId);
            if (CommonConstant.DATA_STR_0.equals(code)) {
                VmDesign vmDesign = iVmDesignService.getById(vmId);
                //根据envId和snapshotId查询出一条信息
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
                // 恢复类型-0快照1备份'
                Integer type = stackRecovery.getType();
                String status = null;
                EnvStackStatusEnum state = EnvStackStatusEnum.RESTORE_IN_PROGRESS;
                ServerStatusEnum statusEnum = ServerStatusEnum.RESUME_IN_PROGRESS;
                if (CommonConstant.DATA_INT_1.equals(type)) {
                    state = EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                    statusEnum = ServerStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                }
                if (envCustomized != null) {
                    //加入对列
                    boolean saveOne = iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, envId, stackId, (state).toString());
                    if (saveOne) {
                        //修改定制环境和虚拟机的 状态 :从快照恢复中 恢复类型-0快照1备份',
                        envCustomized.setState(state);
                        vmDesign.setStatus(statusEnum);
                        stackRecovery.setRestoreBeginTime(new Date());
                        //备份 / 快照 恢复 开始时间
                        stackRecoveryService.updateById(stackRecovery);
                        envCustomizedService.updateById(envCustomized);
                        iVmDesignService.updateById(vmDesign);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("环境的恢复-快照/备份恢复异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 删除环境快照单个
     *
     * @param stackId    openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @param id         平台环境id
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
            log.error("删除环境快照异常,原因:" + e.getMessage());
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
