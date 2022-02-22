package org.jeecg.modules.testtooldistribute.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.StackQueue;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.running.project.entity.ProjectUserAssociation;
import org.jeecg.modules.running.project.service.ProjectUserAssociationService;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import org.jeecg.modules.running.tools.service.IRunningToolsListService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.testtooldistribute.entity.*;
import org.jeecg.modules.testtooldistribute.fegin.SocketServerFegin;
import org.jeecg.modules.testtooldistribute.fegin.entity.ResultSocket;
import org.jeecg.modules.testtooldistribute.mapper.TestToolDistributeMapper;
import org.jeecg.modules.testtooldistribute.service.ITestToolDistributeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yeyl
 */
@Service
@AllArgsConstructor
@Slf4j
public class TestToolDistributeServiceImpl extends ServiceImpl<TestToolDistributeMapper, TestToolDistribute> implements ITestToolDistributeService {
    private  final IEnvCustomizedService iEnvCustomizedService;
    private  final IVmDesignService iVmDesignService;
    private final IRunningToolsListService iRunningToolsListService;
    private  final ProjectUserAssociationService projectUserAssociationService;
    private final IStackQueueService stackQueueService;

    private final SocketServerFegin socketServerFegin;
    @Override
    public List<EnvironmentOptions> envVmToolOptions() {
        //查用户下得项目
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String>  userProjectIds=new ArrayList<>();
        List<ProjectUserAssociation> list = projectUserAssociationService.list();
        list.forEach(projectUserAssociation -> {
            List<String> memberIds = Arrays.asList(projectUserAssociation.getProjectMemberIds().split(","));
            if (memberIds.contains(sysUser.getId())){
                userProjectIds.add(projectUserAssociation.getProjectId());
            }
        });
        //这些项目下得环境
        List<EnvironmentOptions> environmentOptions=new ArrayList<>();
        QueryWrapper<EnvCustomized> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        queryWrapper.in("project_id", userProjectIds);
        List<EnvCustomized> envCustomizedList = iEnvCustomizedService.list(queryWrapper);
        envCustomizedList.forEach(envCustomized -> {
            EnvironmentOptions environmentOption=new EnvironmentOptions();
            environmentOption.setId(envCustomized.getId());
            environmentOption.setName(envCustomized.getEnvName());
            QueryWrapper<VmDesign> vmWrapper=new QueryWrapper<>();
            vmWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            vmWrapper.eq("plan_id", envCustomized.getId());
            List<VmDesign> vmDesigns = iVmDesignService.list(vmWrapper);
            if (vmDesigns!=null&&vmDesigns.size()!=0) {
                List<VmOptions> vmOptions = vmDesigns.stream().map(e -> new VmOptions(e.getId(), e.getVmName())).collect(Collectors.toList());
//            vmOptions.forEach(vmOption -> {
//                VmDesign vmDesign = iVmDesignService.getById(vmOption.getId());
//                String testTools = vmDesign.getTestTools();
//                if (StringUtils.isNotBlank(testTools)) {
//                    List<String> toolIds = Arrays.asList(testTools.split(","));
//                    QueryWrapper<RunningToolsList> runWrapper = new QueryWrapper<>();
//                    runWrapper.eq("del_flag", CommonConstant.DATA_INT_IDEL_0);
//                    runWrapper.in("id", toolIds);
//                    List<RunningToolsList> runningToolsLists = iRunningToolsListService.list(runWrapper);
//                    List<TestToolOptions> testToolOptions = runningToolsLists.stream().map(e -> new TestToolOptions(e.getId(), e.getToolsName())).collect(Collectors.toList());
//                    vmOption.setTestToolOptions(testToolOptions);
//                }

//            });
                environmentOption.setVmOptionsList(vmOptions);
            }
            environmentOptions.add(environmentOption);
        });
        return environmentOptions;
    }

    @Override
    public void add(TestToolDistribute testToolDistribute) {
        addOrEdit( testToolDistribute);
    }

    @Override
    public void edit(TestToolDistribute testToolDistribute) {
        addOrEdit( testToolDistribute);
    }

    @Override
    public DistributeResult distributeById(TestToolDistribute testToolDistribute) {
        DistributeResult distributeResult=new DistributeResult();
        distributeResult.setVmId(testToolDistribute.getVmId());
        distributeResult.setVmName(testToolDistribute.getVmName());
        distributeResult.setDistributeState(testToolDistribute.getDistributeState());
        distributeResult.setSucceed(false);
        if (CommonConstant.DATA_INT_0.equals(testToolDistribute.getDistributeState())){
            //分发成功不需要分发
            distributeResult.setSucceed(false);
            distributeResult.setDistributeState(testToolDistribute.getDistributeState());
            distributeResult.setMessage(testToolDistribute.getVmName() + "分发已经分发成功，不需要重复分发");
            log.info(testToolDistribute.getVmName() + "分发已经分发成功，不需要重复分发");
            return distributeResult;
        }
        //加入队列
        StackQueue stackQueue = new StackQueue();
        stackQueue.setEnvId(testToolDistribute.getEnvirId());
        stackQueue.setIdel(CommonConstant.DATA_INT_IDEL_0);
        stackQueue.setType(CommonConstant.DATA_INT_3);
        stackQueue.setHandleId(testToolDistribute.getId());
        //状态分发中
        stackQueue.setStatus(CommonConstant.DATA_STR_2);
        stackQueue.setName(testToolDistribute.getVmName());
        stackQueue.setCount(CommonConstant.DATA_INT_IDEL_0);
        EnvCustomized envCustomized = iEnvCustomizedService.getById(testToolDistribute.getEnvirId());
        //规划id
        stackQueue.setPlanId(envCustomized.getPlanId());
        if(stackQueueService.save(stackQueue)){
            //测试工具id
            try {
                ResultSocket resultSocket=new ResultSocket();
                resultSocket.setCode(CommonConstant.SOCKET_REGISTER_CODE_200);
                resultSocket.setId(stackQueue.getId());
                RunningToolsList run = iRunningToolsListService.getById(testToolDistribute.getTestToolId());
                //测试工具标识
                resultSocket.setTool(run.getToolsCode());
                resultSocket.setType(CommonConstant.DATA_INT_3);
                Result<?> result = socketServerFegin.sendMsg(
                        testToolDistribute.getVmIp(),
                        resultSocket.getId(),
                        CommonConstant.SOCKET_REGISTER_CODE_200,
                        null, null,
                        resultSocket.getTool(),CommonConstant.DATA_INT_3,null,null,null,null);
                if(result.isSuccess()) {
                    // 成功改状态 删除队列
                    stackQueue.setIdel(CommonConstant.DATA_INT_IDEL_1);
                    stackQueueService.removeById(stackQueue);
                    testToolDistribute.setDistributeState(CommonConstant.DATA_INT_0);
                    this.updateById(testToolDistribute);
                    distributeResult.setSucceed(true);
                    distributeResult.setDistributeState(testToolDistribute.getDistributeState());
                    distributeResult.setMessage("成功");
                }else{
                    testToolDistribute.setDistributeState(CommonConstant.DATA_INT_1);
                    this.updateById(testToolDistribute);
                    log.error("发送失败:"+ result.getMessage());
                    distributeResult.setSucceed(false);
                    distributeResult.setDistributeState(testToolDistribute.getDistributeState());
                    distributeResult.setMessage(testToolDistribute.getVmName() +"发送失败:"+ result.getMessage());
                }
                return distributeResult;
            }catch (Exception e){
                testToolDistribute.setDistributeState(CommonConstant.DATA_INT_1);
                this.updateById(testToolDistribute);
                log.error("发送异常:"+e.getMessage());
                distributeResult.setSucceed(false);
                distributeResult.setDistributeState(testToolDistribute.getDistributeState());
                distributeResult.setMessage(testToolDistribute.getVmName() +"发送异常:"+e.getMessage());
                return distributeResult;
            }
        };
        distributeResult.setMessage(testToolDistribute.getVmName() +"分发失败，创建队列不成功");
        return distributeResult;
    }

    @Override
    public void distributeByIds(List<String> stringList) {
        //批量分发
        boolean flag=false;
       for (String s:stringList) {
           TestToolDistribute testToolDistribute = this.getById(s);
           DistributeResult distributeResult = this.distributeById(testToolDistribute);
           if (distributeResult.isSucceed()) {
               flag = true;
               log.info(testToolDistribute.getVmName() + "分发成功");
           } else {
               log.info(testToolDistribute.getVmName() + "分发失败"+distributeResult.getMessage());
           }
       }
       if (!flag){
           log.error("全部分发失败");
           throw new UnsupportedOperationException("全部分发失败！ "+" 提示:分发成功的不允许重复分发!或者调用接口不成功");
       }
    }

    @Override
    public List<TestToolOptions> testToolOptions() {
        QueryWrapper<RunningToolsList> runWrapper = new QueryWrapper<>();
        runWrapper.eq("del_flag", CommonConstant.DATA_INT_IDEL_0);
        List<RunningToolsList> runningToolsLists = iRunningToolsListService.list(runWrapper);
        return runningToolsLists.stream().map(e -> new TestToolOptions(e.getId(), e.getToolsName())).collect(Collectors.toList());
    }


    public  void addOrEdit(TestToolDistribute testToolDistribute) {
        if (StringUtils.isBlank(testToolDistribute.getEnvirId())
                || StringUtils.isBlank(testToolDistribute.getTestToolId())
                || StringUtils.isBlank(testToolDistribute.getVmId())
        ) {
            throw new UnsupportedOperationException("环境必填,虚拟机必填，测试工具必填");
        }
        //前端多选虚拟机和测试工具
        List<String> vmIds = Arrays.asList(testToolDistribute.getVmId().split(","));
        List<String> tools = Arrays.asList(testToolDistribute.getTestToolId().split(","));

        EnvCustomized envCustomized = iEnvCustomizedService.getById(testToolDistribute.getEnvirId());
        if (envCustomized == null || CommonConstant.DATA_INT_IDEL_1.equals(envCustomized.getIdel())) {
            throw new UnsupportedOperationException("该环境不存在或者被删除，请重试");
        }
        QueryWrapper<VmDesign> vmWrapper = new QueryWrapper<>();
        vmWrapper.in("id", vmIds);
        vmWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        List<VmDesign> vms = iVmDesignService.list(vmWrapper);
        if (vms.size()<vmIds.size()) {
            throw new UnsupportedOperationException("有虚拟机不存在或者被删除，请刷新重试！");
        }else {
            vms.forEach(vmDesign -> {
                if (!testToolDistribute.getEnvirId().equals(vmDesign.getPlanId())) {
                    throw new UnsupportedOperationException(vmDesign.getVmName()+"虚拟机不在这个环境下，请刷新重试");
                }
            });
        }

        QueryWrapper<RunningToolsList> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", tools);
        queryWrapper.eq("del_flag", CommonConstant.DATA_INT_IDEL_0);
        List<RunningToolsList> runningToolsLists = iRunningToolsListService.list(queryWrapper);
        if (runningToolsLists.size()<tools.size()) {
            throw new UnsupportedOperationException("有测试工具不存在或者被删除，请刷新重试");
        }

        if (testToolDistribute.getId() != null&&StringUtils.isNotBlank(testToolDistribute.getId())) {
            //更新
            RunningToolsList runningTool = iRunningToolsListService.getById(testToolDistribute.getTestToolId());
            QueryWrapper<TestToolDistribute> testToolDistributeQueryWrapper = new QueryWrapper<>();
            testToolDistributeQueryWrapper.eq("test_tool_id", runningTool.getId());
            testToolDistributeQueryWrapper.eq("vm_id",  vms.get(0).getId());
            testToolDistributeQueryWrapper.eq("envir_id", envCustomized.getId());
            testToolDistributeQueryWrapper.ne("id", testToolDistribute.getId());
            testToolDistributeQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            List<TestToolDistribute> toolDistributes = this.list(testToolDistributeQueryWrapper);
            if (toolDistributes != null && toolDistributes.size() != 0) {
                throw new UnsupportedOperationException("该测试工具分发已经存在");
            }
            TestToolDistribute o = getTestToolDistribute(envCustomized, vms.get(0), runningTool);
            o.setDistributeState(testToolDistribute.getDistributeState());
            o.setId(testToolDistribute.getId());
            if (!this.updateById(o)) {
                throw new UnsupportedOperationException("更新失败!");
            }
        } else {
            //新增
            List<TestToolDistribute> testToolDistributes = new ArrayList<>();
            //存在的测试工具
            StringBuilder isExistTools=new StringBuilder();
            for (VmDesign vm:vms) {
                for (RunningToolsList runningToolsList : runningToolsLists) {
                    QueryWrapper<TestToolDistribute> testToolDistributeQueryWrapper = new QueryWrapper<>();
                    testToolDistributeQueryWrapper.eq("test_tool_id", runningToolsList.getId());
                    testToolDistributeQueryWrapper.eq("vm_id", vm.getId());
                    testToolDistributeQueryWrapper.eq("envir_id", envCustomized.getId());
                    testToolDistributeQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                    List<TestToolDistribute> toolDistributes = this.list(testToolDistributeQueryWrapper);
                    if (toolDistributes != null && toolDistributes.size() != 0) {
                        isExistTools.append(runningToolsList.getToolsName()).append(",");
                        log.info(runningToolsList.getToolsName() + "该测试工具分发已经存在,继续循环");
                        continue;
                    }
                    TestToolDistribute o = getTestToolDistribute(envCustomized, vm, runningToolsList);
                    testToolDistributes.add(o);
                }
            }
            if (!this.saveBatch(testToolDistributes)) {
                throw new UnsupportedOperationException(isExistTools+"已经存在了,添加失败!");
            }
        }
    }

    private TestToolDistribute getTestToolDistribute(EnvCustomized envCustomized, VmDesign vmDesign, RunningToolsList runningTool) {
        TestToolDistribute o = new TestToolDistribute();
        o.setEnvirId(envCustomized.getId());
        o.setEnvirName(envCustomized.getEnvName());
        o.setVmId(vmDesign.getId());
        o.setVmName(vmDesign.getVmName());
        o.setVmIp(vmDesign.getNetworkAddress());
        o.setIdel(CommonConstant.DATA_INT_IDEL_0);
        o.setTestToolId(runningTool.getId());
        o.setTestToolName(runningTool.getToolsName());
        return o;
    }
}
