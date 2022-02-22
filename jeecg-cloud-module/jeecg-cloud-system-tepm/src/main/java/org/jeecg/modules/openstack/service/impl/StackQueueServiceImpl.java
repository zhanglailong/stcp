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
 * 堆栈序列-处理openstack相关接口
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
     * 镜像删除
     *
     * @param imageId 镜像id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteImages(String id, String imageId) {
        try {
            Boolean code = basedInterfaceServiceExt.deleteImages(imageId, null, null, null, CommonConstant.DATA_STR_0);
            if (code) {
                //删除镜像
                iImageManageService.removeById(id);
                //删除镜像版本软件测试工具关联表
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
            log.error("镜像删除异常,原因:" + e.getMessage());
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

    /**
     * 环境销毁 删除整个环境
     *
     * @param ids 平台环境id
     * @return boolean
     */
    @Override
    public List<String> deleteEnv(String ids) {
        //平台虚拟机id 集合
        List<String> messageList = new ArrayList<>();
        List<String> idList = Arrays.asList(ids.split(","));
        if (idList.size() > 0) {
            try {
                for (int i = 0; i < idList.size(); i++) {
                    //根据主键id查询环境信息
                    QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq(CommonConstant.DATA_STRING_ID, idList.get(i));
                    queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                    EnvCustomized envCustomized = envCustomizedService.getOne(queryWrapper);
                    //openstack栈环境id
                    String openStackId = envCustomized.getStackId();
                    //调用统一接口删除环境
                    Boolean code = null;
                    if (StringUtils.isNotBlank(openStackId)) {
                        code = basedInterfaceServiceExt.deleteImages(null, openStackId, null, null, CommonConstant.DATA_STR_1);
                    } else {
                        messageList.add(envCustomized.getId());
                    }
                    if (code) {
                        //根据环境id查询虚拟机信息  删除环境下面的所有虚拟机
                        QueryWrapper<VmDesign> queryWrapperVm = new QueryWrapper<>();
                        queryWrapperVm.eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, envCustomized.getId());
                        queryWrapperVm.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<VmDesign> vmDesignList = vmDesignService.list(queryWrapperVm);
                        List<String> vmIds = vmDesignList.stream().map(VmDesign::getId).collect(Collectors.toList());
                        vmDesignService.removeByIds(vmIds);

                        //根据环境id查询快照id 删除快照
                        QueryWrapper<StackRecovery> stackRecoveryQueryWrapper = new QueryWrapper<>();
                        stackRecoveryQueryWrapper.eq(CommonConstant.EVN_ID, envCustomized.getId());
                        stackRecoveryQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<StackRecovery> stackRecoveryList = stackRecoveryService.list(stackRecoveryQueryWrapper);
                        List<String> stackRecoveryIds = stackRecoveryList.stream().map(StackRecovery::getId).collect(Collectors.toList());
                        stackRecoveryService.removeByIds(stackRecoveryIds);

                        //根据环境id 查询串口表  删除串口
                        QueryWrapper<VmSerportExdevRelation> vmSerportExdevRelationQueryWrapper = new QueryWrapper<>();
                        vmSerportExdevRelationQueryWrapper.eq(CommonConstant.EVN_ID, envCustomized.getId());
                        vmSerportExdevRelationQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                        List<VmSerportExdevRelation> vmSerportExdevRelationList = iVmSerportExdevRelationService.list(vmSerportExdevRelationQueryWrapper);
                        List<String> vmSerportExdevRelationIds = vmSerportExdevRelationList.stream().map(VmSerportExdevRelation::getId).collect(Collectors.toList());
                        iVmSerportExdevRelationService.removeByIds(vmSerportExdevRelationIds);

                        //删除定制环境
                        envCustomizedService.removeById(envCustomized.getId());
                    } else {
                        messageList.add(envCustomized.getId());
                    }
                }
                return messageList;
            } catch (Exception e) {
                log.error("环境销毁异常,原因:" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * 删除虚拟机
     *
     * @param serverId 虚拟机id
     * @param id       平台虚拟机id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteVm(String id, String serverId) {
        try {
            Boolean code = basedInterfaceServiceExt.deleteImages(null, null, null, serverId, CommonConstant.DATA_STR_3);
            if (code) {
                //删除镜像
                vmDesignService.removeById(id);
                return true;
            }
        } catch (Exception e) {
            log.error("虚拟机删除异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 虚拟机挂起
     *
     * @param id       虚拟机表主键id
     * @param serverId 虚拟机id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean suspendVm(String id, String serverId) {
        try {
            //1.查询虚拟机信息获取当前虚拟机的状态
            VmDesign vmDesign = vmDesignService.getById(id);
            if (vmDesign != null) {
                //虚拟机状态
                String status = vmDesign.getStatus().toString();
                //2.虚拟机的状态为 运行 的时候进行挂起
                if (ServerStatusEnum.ACTIVE.toString().equals(status)) {
                    //3.请求挂起接口开始对虚拟机挂起
                    Boolean code = basedInterfaceServiceExt.getSuspendVm(serverId, CommonConstant.DATA_STR_0);
                    if (code) {
                        //虚拟机挂起直接改为挂起中
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
            log.error("测试环境定制-挂起异常:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 虚拟机恢复
     *
     * @param id       虚拟机表主键id
     * @param serverId 虚拟机id
     * @return boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean resumeVm(String id, String serverId) {
        try {
            //1.查询虚拟机信息获取当前虚拟机的状态
            VmDesign vmDesign = vmDesignService.getById(id);
            if (vmDesign != null) {
                //虚拟机状态
                String status = vmDesign.getStatus().toString();
                //2.虚拟机的状态为 挂起的时候进行恢复
                if (ServerStatusEnum.SUSPENDED.toString().equals(status)) {
                    //3.请求挂起恢复接口开始对虚拟机恢复
                    Boolean code = basedInterfaceServiceExt.getSuspendVm(serverId, CommonConstant.DATA_STR_1);
                    if (code) {
                        //虚拟机恢复中
                        vmDesign.setStatus(ServerStatusEnum.RESUME_IN_PROGRESS);
                        vmDesignService.updateById(vmDesign);
                        //加入队列
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
            log.error("测试环境定制-挂起异常:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * jmeter使用
     *
     * @param planName    测试名称
     * @param numThreads  线程数
     * @param loops       循环次数
     * @param url         域名或者ip
     * @param port        端口号
     * @param httpRequest 请求方式
     * @param duration    并发时间
     * @param path        测试用例路径
     * @param request     请求参数
     * @param jmeterPath  测试用例地址
     * @param id          测试工具表主键id
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
                //测试名称
                monitorTools.setTestName(planName);
                //线程数
                monitorTools.setNumThreads(String.valueOf(numThreads));
                //并发时间
                monitorTools.setRampUp(String.valueOf(duration));
                //循环次数
                monitorTools.setLoops(loops);
                //请求地址
                monitorTools.setDomain(url);
                //端口号
                monitorTools.setPort(port);
                //请求方式
                monitorTools.setMethod(httpRequest);
                //报告地址
                monitorTools.setPath(htmlPath);
                //测试用例地址
                monitorTools.setJmeterPath(jmeterPath);
                monitorToolsService.updateById(monitorTools);
                return true;
            }
        } catch (IOException e) {
            log.error("jmeter测试-异常:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 调整虚拟机配置
     *
     * @param id     虚拟机表主键
     * @param flavor 调整参数
     * @return true
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getVmAlter(String id, String flavor, String size, String rollback) {
        try {
            //根据 id 查询当前虚拟机状态
            VmDesign vmDesign = vmDesignService.getById(id);
            //只有当虚拟机的状态为运行的时候才可以进行配置修改
            if ((ServerStatusEnum.ACTIVE.getStatus().equals(vmDesign.getStatus().toString())||ServerStatusEnum.BACKUP_COMPLETE.getStatus().equals(vmDesign.getStatus().toString()) || ServerStatusEnum.SNAPSHOT_COMPLETE.getStatus().equals(vmDesign.getStatus().toString()))&& CommonConstant.DATA_INT_0.equals(vmDesign.getIdel())) {
                //查询虚拟机对应的环境信息 获取环境 id
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
                        //内存
                        vmDesign.setVirInner(originalNames[1].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                        //磁盘大小
                        vmDesign.setVirDisk(size);
                    }
                    vmDesign.setId(id);
                    if (vmDesignService.updateById(vmDesign)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("调整虚拟机配置-异常:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 创建单独虚拟机
     *
     * @param name   虚拟机名称
     * @param image  镜像名或者id
     * @param flavor 虚拟机规格
     * @return 创建单独虚拟机
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getServerCompute(String name, String image, String flavor, String rollback, String size, String envName, String envId, String projectId) {
        try {
            //如果size 为 0 就去统一接口查询数据
            if (size.equals(CommonConstant.DATA_STR_0)) {
                JSONObject jsonObject = openStackServiceExt.getImage(image);
                JSONArray objects = JSON.parseArray(jsonObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_IMAGE).get(CommonConstant.BLOCK_DEVICE_MAPPING).toString());
                JSONObject parseObject = JSONObject.parseObject(objects.get(0).toString());
                size = parseObject.get(CommonConstant.VOLUME_SIZE).toString();
            } else {
                // 不为 0 就 默认 100
                size = CommonConstant.DATA_STR_100;
            }
            //虚拟机名称
            String vmName = RandomUtil.randomString(CommonConstant.RANDOM,6);
            //获取数据
            ServerCompute serverCompute = openStackServiceExt.getServerCompute(vmName, image, flavor, rollback,size);
            if (CommonConstant.DATA_INT_0 == serverCompute.getCode()) {
                //虚拟机设计表添加数据
                VmDesign vmDesign = new VmDesign();
                //名称
                vmDesign.setVmName(name);
                //环境名称
                vmDesign.setEnvName(envName);
                //环境id
                vmDesign.setPlanId(envId);
                //镜像id
                vmDesign.setOpenstackImageId(image);
                //状态
                vmDesign.setStatus(ServerStatusEnum.toEnum(serverCompute.getStatus()));
                vmDesign.setIdel(CommonConstant.DATA_INT_IDEL_0);
                vmDesign.setPlanName(vmName);
                vmDesign.setProjectId(projectId);
                //CPU+内存+磁盘
                String[] originalNames = flavor.split(CommonConstant.SEPARATOR);
                if (originalNames.length >= CommonConstant.DATA_INT_3) {
                    //CPU
                    vmDesign.setVirCpu(originalNames[0].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                    //内存
                    vmDesign.setVirInner(originalNames[1].replaceAll(CommonConstant.REGULAR_EXPRESSION, ""));
                    //磁盘大小
                    vmDesign.setVirDisk(size);
                }
                EnvCustomized envCustomized = new EnvCustomized();
                envCustomized.setId(envId);
                envCustomized.setStackId(serverCompute.getId());
                envCustomizedService.updateById(envCustomized);
                if (vmDesignService.save(vmDesign)) {
                    //获取虚拟机内的测试工具
                    if (StringUtils.isEmpty(vmDesign.getOpenstackImageId())) {
                        log.info(vmDesign.getVmName() + ">>>>>>虚拟机是裸机，没有镜像");
                    } else {
                        ImageManage imageManage = iImageManageService.getImageByOpenstackId(vmDesign.getOpenstackImageId());
                        if (Objects.nonNull(imageManage) && StringUtils.isNotBlank(imageManage.getTestTools())) {
                            List<String> toolsList = Arrays.asList(imageManage.getTestTools().split(","));
                            //获取所有的测试工具并添加
                            if (iRunningToolsListService.setRunningToolListBytools(toolsList, vmDesign)) {
                                log.info("测试工具与虚拟机对应表添加成功");
                            }
                        }
                    }
                    //加入队列
                    if (iStackQueueService.saveOne(vmDesign.getVmName(), CommonConstant.DATA_INT_1, vmDesign.getId(), serverCompute.getId(), serverCompute.getStatus())) {
                      return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error("创建单独虚拟机异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * 从虚拟机创建镜像
     *
     * @param id            平台虚拟机id
     * @param serverId      openstack虚拟机id
     * @param stackName     虚拟机名称
     * @param stackStatus   虚拟机状态
     * @param remark        镜像备注
     * @param name          镜像名称
     * @param sysType       操作系统
     * @param otherSoftware 其他软件
     * @param testTools     测试工具
     * @param systemVersion 系统版本
     * @return 状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getServerAction(String id, String serverId, String stackName, String stackStatus, String
            remark, String name, String sysType, String otherSoftware, String testTools, String systemVersion) {
        try {
            //镜像名称
            String imageName = RandomUtil.randomStringUpper(6) + name;
            //获取返回数据
            FoundImage foundImage = openStackServiceExt.getServerAction(null, serverId, imageName);
            if (CommonConstant.DATA_INT_0.equals(foundImage.getCode())) {
                //维护镜像表
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
                //测试工具，其他软件 系统版本
                List<RunningToolsList> runningToolsLists = new ArrayList<>();
                runningToolsLists = iImageManageService.getRunningToolsLists(imageManage, runningToolsLists);
                if (StringUtils.isNotBlank(systemVersion)) {
                    imageManage.setSystemVersion(systemVersion);
                }
                if (StringUtils.isNotBlank(otherSoftware)) {
                    //处理逗号
                    String otherSoftwareString = iImageManageService.getDealCommaString(otherSoftware);
                    if (StringUtils.isNotBlank(otherSoftwareString)) {
                        imageManage.setOtherSoftware(otherSoftwareString);
                    }
                }
                iImageManageService.save(imageManage);
                //tepm_image_other_info 批量新增关联表
                iImageOtherInfoService.saveInfos(imageManage, runningToolsLists);
                return true;
            }
        } catch (Exception e) {
            log.error("从虚拟机创建镜像异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }


    /**
     * 环境的恢复-快照/备份恢复
     *
     * @param envId      定制环境id
     * @param stackId    openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @return 状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean restore(String envId, String stackId, String snapshotId) {
        try {
            //获取快照恢复状态code值 0 为成功 其他为失败
            String code = basedInterfaceServiceExt.getRestore(stackId, snapshotId);
            if (CommonConstant.DATA_STR_0.equals(code)) {
                //根据envId和snapshotId查询出一条信息
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
                // 恢复类型-0快照1备份'
                Integer type = stackRecovery.getType();
                EnvStackStatusEnum state = EnvStackStatusEnum.RESTORE_IN_PROGRESS;
                if (CommonConstant.DATA_INT_1.equals(type)) {
                    state = EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS;
                }
                if (envCustomized != null) {
                    //加入对列
                    boolean saveOne = iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), stackId, (state).toString());
                    if (saveOne) {
                        //修改定制环境的状态 :从快照恢复中 恢复类型-0快照1备份',
                        envCustomized.setState(state);
                        stackRecovery.setRestoreBeginTime(new Date());
                        //备份 / 快照 恢复 开始时间
                        stackRecoveryService.updateById(stackRecovery);
                        envCustomizedService.updateById(envCustomized);
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
     * 创建环境快照
     *
     * @param stackId openstack栈环境id
     * @param envId   定制环境id
     * @param name    快照/备份名称
     * @param type    0快照/1备份
     * @return 状态
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean snapshot(String stackId, String envId, String name, Integer type, String typeString) {
        try {
            //True表示快照，false表示备份
            boolean incremental = false;
            if (CommonConstant.DATA_INT_0.equals(type)) {
                incremental = true;
            }
            //创建环境快照
            String snapshotName = RandomUtil.randomStringUpper(6) + name;
            Snapshots snapshots = basedInterfaceServiceExt.getSnapshots(stackId, snapshotName, incremental);
            //创建快照中 或者 备份中
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
                stackRecoveryService.save(stackRecovery);
                //加入队列 查询回执时间
                if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, stackRecovery.getId(), stackId, snapshots.getStatus())) {
                    //查询环境信息
                    EnvCustomized envCustomized = envCustomizedService.getById(envId);
                    //快照
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
            log.error("创建环境" + typeString + "异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    /**
     * appScan测试工具
     *
     * @param path 测试地址
     * @param id   monitorTools主键
     * @return 报告地址
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getAppScan(String id, String path) {
        try {
            String appScanPath = appScanServiceExt.getAppScan(path);
            MonitorTools monitorTools = new MonitorTools();
            monitorTools.setId(id);
            //请求地址
            monitorTools.setDomain(path);
            monitorTools.setPath(appScanPath);
            monitorToolsService.updateById(monitorTools);
            return true;
        } catch (IOException e) {
            log.error("appScan工具测试异常,原因:" + e.getMessage());
            return false;
        }
    }

    /**
     * UnderStand测试工具
     *
     * @param id   monitorTools主键
     * @param path 测试文件地址
     * @return 报告地址
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean getUnderStand(String id, String path) {
        try {
            String underStandPath = underStandServiceExt.getUnderStand(path);
            MonitorTools monitorTools = new MonitorTools();
            monitorTools.setId(id);
            //请求地址
            monitorTools.setDomain(path);
            monitorTools.setPath(underStandPath);
            monitorToolsService.updateById(monitorTools);
            return true;
        } catch (IOException e) {
            log.error("UnderStand工具测试异常,原因:" + e.getMessage());
            return false;
        }
    }
}
