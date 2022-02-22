package org.jeecg.modules.test.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.openstack.entity.HostRoute;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.ext.BasedInterfaceServiceExt;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.entity.EnvPlanJson;
import org.jeecg.modules.plan.entity.EnvPlanNodes;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.entity.EnvCustomizedLog;
import org.jeecg.modules.test.entity.EvnRunningVo;
import org.jeecg.modules.test.mapper.EnvCustomizedMapper;
import org.jeecg.modules.test.service.IEnvCustomizedLogService;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * @author zlf
 * 测试环境定制与管理
 * 2020-12-23
 * V1.0
 */
@Service
@Slf4j
public class EnvCustomizedServiceImpl extends ServiceImpl<EnvCustomizedMapper, EnvCustomized> implements IEnvCustomizedService {

    @Resource
    private OpenStackServiceExt openStackServiceExt;
    @Resource
    private IEnvCustomizedLogService customizedLogService;
    @Resource
    private IStackQueueService stackQueueService;
    @Resource
    private IStackQueueService iStackQueueService;
    @Resource
    private BasedInterfaceServiceExt basedInterfaceServiceExt;
    @Resource
    private IEnvCustomizedService envCustomizedService;
    @Autowired
    private EnvCustomizedMapper envCustomizedMapper;

    @Override
    public boolean sendPlanToStack(String openstackJson, String planName, EnvCustomized envCustomized,String planJson) {
        boolean result = false;
        EnvCustomizedLog cusLog = new EnvCustomizedLog();
        try {
            if (StringUtils.isEmpty(openstackJson)) {
                return false;
            }
            //查询子网信息
            EnvPlanJson envPlanJson = JSON.parseObject(planJson, EnvPlanJson.class);
            Map<String, List<EnvPlanNodes>> nodes = new HashMap<>(16);
            //获取type不同分类集合
            envPlanJson.getNodeList().forEach(n -> {
                List<EnvPlanNodes> plans = nodes.get(n.getType());
                if (plans == null || plans.isEmpty()) {
                    plans = new ArrayList<>();
                }
                plans.add(n);
                nodes.put(n.getType(), plans);
            });

            //获取子网信息
            List<EnvPlanNodes> childNetList = nodes.get(CommonConstant.PLAN_TYPE_CHILDNET);
            String msg = "";
            StringBuffer stringBuffer = new StringBuffer();
            if (childNetList != null && childNetList.size()>0){
                childNetList.forEach(c->{
                    String hostRoutesNew = StringEscapeUtils.unescapeJava(c.getHostRoutesNew());
                    List<HostRoute> hostRouteList = JSONObject.parseObject(hostRoutesNew, new TypeReference<List<HostRoute>>(){});
                    if (hostRouteList.size()>0){
                        if (!openStackServiceExt.updateSubnets(c.getChildnetId(),hostRouteList)){
                            stringBuffer.append("编辑子网路由表失败");
                        }
                    }
                });
            }
            if (stringBuffer != null){
                msg = stringBuffer.toString();
            }
            String stackId = openStackServiceExt.createPlan(openstackJson, planName);
            //获取token失败
            if (stackId.equals(CommonConstant.DATA_STR_0)) {
                msg = "获取token失败";
            }
            if (stackId.equals(CommonConstant.DATA_STR_2)) {
                msg = "createPlan 异常";
            }
            if (stackId.equals(CommonConstant.DATA_STR_1)) {
                msg = "发送失败";
            }
            BeanUtils.copyProperties(envCustomized, cusLog);
            //发送成功
            if (!stackId.equals(CommonConstant.DATA_STR_0) &&
                    !stackId.equals(CommonConstant.DATA_STR_2) &&
                    !stackId.equals(CommonConstant.DATA_STR_1)) {
                envCustomized.setStackId(stackId);
                envCustomized.setState(EnvStackStatusEnum.CREATE_IN_PROGRESS);
                envCustomized.setMsg(null);
                if (baseMapper.insert(envCustomized) >= 1) {
                    if (stackQueueService.saveOne(envCustomized.getEnvName(), CommonConstant.DATA_INT_0, envCustomized.getId(), stackId, EnvStackStatusEnum.CREATE_IN_PROGRESS.toString())) {
                        result = true;
                    }
                }
            } else {
                cusLog.setState(CommonConstant.DATA_STR_2);
                cusLog.setMsg(msg);
            }
            customizedLogService.save(cusLog);
        } catch (Exception e) {
            cusLog.setState(CommonConstant.DATA_STR_2);
            cusLog.setMsg("向open stack发送创建测试环境异常,原因:" + e.getMessage());
            customizedLogService.save(cusLog);
            log.error("向open stack发送创建测试环境异常,原因:" + e.getMessage());
        }
        return result;
    }

    @Override
    public List<EnvCustomized> getCreateStacksByState() {
        QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_STATE, EnvStackStatusEnum.CREATE_IN_PROGRESS);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<EnvCustomized> getCompleteStacksByState() {
        QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_STATE, EnvStackStatusEnum.CREATE_COMPLETE);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<EnvCustomized> getVirProgressStacksByState() {
        QueryWrapper<EnvCustomized> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_STATE, EnvStackStatusEnum.CREATE_COMPLETE);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 环境挂起
     *
     * @param id      环境id
     * @param stackId openStack栈环境id
     * @return 环境挂起
     */
    @Override
    public boolean suspend(String id, String stackId) {
        try {
            //1.查询环境信息获取当前环境的状态
            EnvCustomized envCustomized = envCustomizedService.getById(id);
            if (envCustomized != null) {
                //环境状态
                String status = envCustomized.getState().toString();
                //2.只有当前环境的状态为：创建完成 || 更新完成 || 恢复挂起完成 || 创建快照完成 || 从快照恢复完成 才可以对环境进行挂起操作
                if (EnvStackStatusEnum.CREATE_COMPLETE.toString().equals(status) || EnvStackStatusEnum.UPDATE_COMPLETE.toString().equals(status) ||
                        EnvStackStatusEnum.RESUME_COMPLETE.toString().equals(status) || EnvStackStatusEnum.SNAPSHOT_COMPLETE.toString().equals(status) ||
                        EnvStackStatusEnum.RESTORE_COMPLETE.toString().equals(status) || EnvStackStatusEnum.BACKUP_RESTORE_COMPLETE.toString().equals(status) ||
                        EnvStackStatusEnum.BACKUP_COMPLETE.toString().equals(status)) {
                    //5.请求挂起接口开始对环境进项挂起
                    Boolean code = basedInterfaceServiceExt.getSuspend(stackId, CommonConstant.DATA_STR_0);
                    if (code) {
                        //环境挂起直接改为挂起中
                        envCustomized.setState(EnvStackStatusEnum.SUSPEND_IN_PROGRESS);
                        envCustomizedService.updateById(envCustomized);
                        //加入队列
                        if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, id, stackId, EnvStackStatusEnum.SUSPEND_IN_PROGRESS.toString())) {
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
     * 挂起恢复
     *
     * @param id      环境id
     * @param stackId openStack栈环境id
     * @return 挂起恢复
     */
    @Override
    public boolean resume(String id, String stackId) {
        //1.查询环境信息获取当前环境的状态
        EnvCustomized envCustomized = envCustomizedService.getById(id);
        if (envCustomized != null) {
            //环境状态
            String status = envCustomized.getState().toString();
            //只有挂起成功的才能进行恢复
            if (EnvStackStatusEnum.SUSPEND_COMPLETE.toString().equals(status)) {
                //请求挂起恢复接口
                Boolean code = basedInterfaceServiceExt.getSuspend(stackId, CommonConstant.DATA_STR_1);
                if (code) {
                    //环境恢复中
                    EnvCustomized envCustomizeds = envCustomizedService.getById(id);
                    envCustomizeds.setState(EnvStackStatusEnum.RESUME_IN_PROGRESS);
                    // 环境恢复中
                    envCustomizedService.updateById(envCustomizeds);
                    //加入对列
                    boolean saveOne = iStackQueueService.saveOne(null, CommonConstant.DATA_INT_0, id, stackId, (EnvStackStatusEnum.RESUME_IN_PROGRESS).toString());
                    if (saveOne) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            return false;
        }
        return false;
    }

    /**
     * 项目+环境 关联查询
     *
     * @param envCustomized
     * @return list
     */
    @Override
    public List<EvnRunningVo> getEnvList(EvnRunningVo envCustomized) {
        List<EvnRunningVo> list = envCustomizedMapper.findByPage(envCustomized);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean manualEnv(String name,String image,String flavor,String rollback,String size,String envName,String environmentUse,String remarks,
                             String projectName,String projectId) {
        try {
            // 手动
            if (StringUtils.isNotEmpty(envName)) {
                EnvCustomized envCustomized = new EnvCustomized();
                envCustomized.setPlans("-1");
                envCustomized.setEnvName(envName);
                envCustomized.setEnvironmentUse(environmentUse);
                envCustomized.setRemarks(remarks);
                envCustomized.setIdel(CommonConstant.DATA_INT_IDEL_0);
                envCustomized.setState(EnvStackStatusEnum.CREATE_COMPLETE);
                envCustomized.setType(CommonConstant.DATA_INT_1);
                envCustomized.setProjectNames(projectName);
                envCustomized.setProjectId(projectId);

                if (envCustomizedService.save(envCustomized)) {
                    //环境id
                    String envId = envCustomized.getId();
                    //环境名称
                    String manEnvName = envCustomized.getEnvName();
                    //创建虚拟机
                    try {
                        if (iStackQueueService.getServerCompute(name,image,flavor,rollback,size ,envName,envId,projectId)) {
                            log.info("创建单独虚拟机");
                            return true;
                        }
                    } catch (Exception e) {
                        log.error("创建单独虚拟机异常,原因:" + e.getMessage());
                        return false;
                    }
                }

            }
        } catch (Exception e) {
            log.error("测试环境定制手动添加异常:" + e);
            return false;
        }
        return false;
    }
}
