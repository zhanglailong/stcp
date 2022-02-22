package org.jeecg.modules.running.tools.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.running.monitortools.entity.RunningToolsAndVm;
import org.jeecg.modules.running.monitortools.service.IRunningToolsAndVmService;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import org.jeecg.modules.running.tools.mapper.RunningToolsListMapper;
import org.jeecg.modules.running.tools.service.IRunningToolsListService;
import org.jeecg.modules.test.entity.RunningProject;
import org.jeecg.modules.test.service.IRunningProjectServer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 测试工具列表
 * @Author: jeecg-boot
 * @Date: 2020-12-18
 * @Version: V1.0
 */
@Service
public class RunningToolsListServiceImpl extends ServiceImpl<RunningToolsListMapper, RunningToolsList> implements IRunningToolsListService {


    @Resource
    RunningToolsListMapper runningToolsListMapper;
    @Resource
    private IRunningToolsAndVmService iRunningToolsAndVmService;
    @Resource
    private IRunningProjectServer iRunningProjectService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean setRunningToolListBytools(List<String> list, VmDesign vmDesign) {
        try {
            QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
            RunningProject project = iRunningProjectService.getOne(
                    queryWrapper.eq("id", vmDesign.getProjectId()));
            List<RunningToolsList> runningToolsLists = runningToolsListMapper.selectBatchIds(list);
            if (CollectionUtils.isNotEmpty(runningToolsLists)) {
                for (RunningToolsList rtl : runningToolsLists) {
                    //测试工具和虚拟机对应表
                    RunningToolsAndVm runningToolsAndVm = new RunningToolsAndVm();
                    BeanUtils.copyProperties(rtl, runningToolsAndVm);
                    runningToolsAndVm.setIdel(CommonConstant.DATA_INT_IDEL_0);
                    runningToolsAndVm.setVmId(vmDesign.getId());
                    runningToolsAndVm.setVmName(vmDesign.getVmName());
                    runningToolsAndVm.setProjectId(vmDesign.getProjectId());
                    runningToolsAndVm.setToolsRunStatus(CommonConstant.DATA_STR_0);
                    runningToolsAndVm.setId(null);
                    runningToolsAndVm.setProjectName(project.getProjectName());
                    runningToolsAndVm.setVmAddress(vmDesign.getNetworkAddress());
                    iRunningToolsAndVmService.save(runningToolsAndVm);
                }
                return true;
            }
            throw new RuntimeException("当前没有镜像没有测试工具，无需添加！");
        } catch (Exception e) {
            log.error("创建虚拟机和测试工具对应表,原因:" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean setRunningToolByTool() {
        return false;
    }


}
