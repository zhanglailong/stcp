package org.jeecg.modules.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.SnowflakeConfig;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.plan.entity.EnvPlan;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.service.IEnvPlanService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.entity.EnvCustomizedVo;
import org.jeecg.modules.test.entity.EvnRunningVo;
import org.jeecg.modules.test.entity.RunningProject;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.test.service.IRunningProjectServer;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author zlf
 * V1.0
 * 2021/1/16
 * 用一句话描述该文件做什么)
 */
@Api(tags = "测试环境定制")
@RestController
@RequestMapping("/env/customized")
@Slf4j
public class EnvCustomizedController extends JeecgController<EnvCustomized, IEnvCustomizedService> {

    @Resource
    private IEnvCustomizedService envCustomizedService;
    @Resource
    private IEnvPlanService envPlanService;
    @Resource
    private IVmDesignService vmDesignService;

    @Resource
    SnowflakeConfig snowflakeConfig;
    @Resource
    private IStackQueueService iStackQueueService;
    @Resource
    private IRunningProjectServer iRunningProjectServer;

    /**
     * 环境销毁
     *
     * @param id  环境id
     * @return 状态
     * 定制环境删除 环境快照删除 虚拟机删除
     */
    @AutoLog(value = "测试环境定制-销毁")
    @ApiOperation(value = "测试环境定制-销毁", notes = "测试环境定制-销毁")
    @PostMapping(value = "/destroy")
    public Result<?> destroy(@RequestParam(name = "id") String id) {
        try {
            List<String> idList = Arrays.asList(id.split(","));
            for (int i = 0; i < idList.size(); i++) {
                EnvCustomized envCustomized = envCustomizedService.getById(idList.get(i));
                EnvStackStatusEnum state = envCustomized.getState();
                if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state) ||
                        EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
                        || EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
                ) {
                    return Result.error("状态：" + state.getDesc() + "不允许进行操作");
                }
            }
            List messageList = iStackQueueService.deleteEnv(id);
            if (messageList.isEmpty()) {
                return Result.OK("环境销毁成功!");
            }else {
                return Result.error("环境销毁失败!" + messageList);
            }
        } catch (Exception e) {
            log.error("环境销毁异常,原因:" + e.getMessage());
            return Result.error("环境销毁异常,请联系管理员！");
        }
    }

    /**
     * 环境挂起
     *
     * @param id      环境id
     * @param stackId openstack栈环境id
     * @return 状态
     */
    @AutoLog(value = "测试环境定制-挂起")
    @ApiOperation(value = "测试环境定制-挂起", notes = "测试环境定制-挂起")
    @PostMapping(value = "/suspend")
    public Result<?> suspend(@RequestParam(name = "id") String id, @RequestParam(name = "stackId") String stackId) {
        try {
            EnvCustomized envCustomized = envCustomizedService.getById(id);
            EnvStackStatusEnum state = envCustomized.getState();
            if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state) ||
                    EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
                    || EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
            ) {
                return Result.error("状态：" + state.getDesc() + "不允许进行操作");
            }
            boolean suspend = envCustomizedService.suspend(id, stackId);
            if (suspend) {
                return Result.OK("环境挂起成功");
            } else {
                return Result.error("挂起失败");
            }
        } catch (Exception e) {
            log.error("测试环境定制-挂起异常:" + e.getMessage());
            return Result.error("测试环境定制-挂起异常:" + e.getMessage());
        }
    }

    /**
     * 环境挂起恢复
     *
     * @param id      环境id
     * @param stackId openstack栈环境id
     * @return 状态
     */
    @AutoLog(value = "测试环境定制-挂起恢复")
    @ApiOperation(value = "测试环境定制-挂起恢复", notes = "测试环境定制-挂起恢复")
    @PostMapping(value = "/resume")
    public Result<?> resume(@RequestParam(name = "id") String id, @RequestParam(name = "stackId") String stackId) {

        try {
            EnvCustomized envCustomized = envCustomizedService.getById(id);
            EnvStackStatusEnum state = envCustomized.getState();
            if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state) ||
                    EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
                    || EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
            ) {
                return Result.error("状态：" + state.getDesc() + "不允许进行操作");
            }
            boolean resume = envCustomizedService.resume(id, stackId);
            if (resume) {
                return Result.OK("环境挂起恢复成功");
            } else {
                return Result.error("环境挂起恢复失败");
            }
        } catch (Exception e) {
            log.error("测试环境定制-挂起恢复异常:" + e.getMessage());
            return Result.error("测试环境定制-挂起恢复异常:" + e.getMessage());
        }
    }

    /**
     * 分页列表查询
     *
     * @param envCustomized 环境定制
     * @param pageNo        页
     * @param pageSize      数量
     * @return 分页数据
     */
    @AutoLog(value = "测试环境定制-分页列表查询")
    @ApiOperation(value = "测试环境定制-分页列表查询", notes = "测试环境定制-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EvnRunningVo envCustomized,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        envCustomized.setCreateBy(sysUser.getUsername());
        List<EvnRunningVo> records = envCustomizedService.getEnvList(envCustomized);
        PageInfo pageInfo = new PageInfo(records);
        return Result.OK(pageInfo);
    }

    /**
     * 查询项目列表
     *
     * @return list
     */
    @AutoLog(value = "查询项目列表")
    @ApiOperation(value = "查询项目列表", notes = "查询项目列表")
    @GetMapping(value = "/projectList")
    public Result<?> getProjectList() {
        List<RunningProject> list = iRunningProjectServer.getProjectList();
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param name             虚拟机名称
     * @param image            镜像名或者id
     * @param flavor           虚拟机规格
     * @param envName          环境名称
     * @param environmentUse   用途
     * @param remarks          备注
     * @param projectName      项目名称
     * @param projectId        项目id
     * @return 添加状态
     */
    @AutoLog(value = "测试环境定制-添加")
    @ApiOperation(value = "测试环境定制-添加", notes = "测试环境定制-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestParam(name = "name") String name,
                         @RequestParam(name = "image") String image,
                         @RequestParam(name = "flavor") String flavor,
                         @RequestParam(name = "rollback")String rollback,
                         @RequestParam(name = "size") String size,
                         @RequestParam(name = "envName") String envName,
                         @RequestParam(name = "environmentUse") String environmentUse,
                         @RequestParam(name = "remarks") String remarks,
                         @RequestParam(name = "projectName") String projectName,
                         @RequestParam(name = "projectId") String projectId) {
        try {
            // 手动
            if (envCustomizedService.manualEnv(name, image, flavor,rollback,size,
                    envName, environmentUse, remarks, projectName, projectId)) {
                return Result.OK("测试环境定制手动添加成功！");
            }
        } catch (Exception e) {
            log.error("测试环境定制手动添加异常:" + e);
            return Result.error("测试环境定制手动添加异常：" + e.getMessage());
        }
        return Result.error("添加失败！");
    }

    /**
     * 添加
     *
     * @param ids            id
     * @param envName        环境名称
     * @param environmentUse 用途
     * @param remarks        备注
     * @param projectName    项目名称
     * @param projectId      项目id
     * @return 添加状态
     */
    @AutoLog(value = "测试环境定制-自动添加")
    @ApiOperation(value = "测试环境定制-自动添加", notes = "测试环境定制-自动添加")
    @PostMapping(value = "/autoAdd")
    public Result<?> autoAdd(@RequestParam String ids, @RequestParam String envName, @RequestParam String environmentUse, @RequestParam String remarks, @RequestParam String projectName, @RequestParam String projectId) {
        try {
            if (StringUtils.isNotEmpty(ids) && StringUtils.isNotEmpty(envName)) {
                EnvPlan envPlan = envPlanService.selectById(ids);
                if (envPlan == null || StringUtils.isEmpty(envPlan.getId())) {
                    return Result.error("添加失败！没找到对应的设计方案");
                }
                // 统一接口发送环境信息
                EnvCustomized envCustomized = new EnvCustomized();
                envCustomized.setEnvName(envName);
                envCustomized.setEnvironmentUse(environmentUse);
                envCustomized.setRemarks(remarks);
                envCustomized.setPlans(ids);
                envCustomized.setPlanId(ids);
                envCustomized.setIdel(CommonConstant.DATA_INT_IDEL_0);
                envCustomized.setState(EnvStackStatusEnum.CREATE_IN_PROGRESS);
                envCustomized.setPlanName(envPlan.getName());
                envCustomized.setStackName(CommonConstant.DATA_STRING_STACK_NAME_S + snowflakeConfig.snowflakeId());
                envCustomized.setType(CommonConstant.DATA_INT_2);
                envCustomized.setProjectNames(projectName);
                envCustomized.setProjectId(projectId);
                if (envCustomizedService.sendPlanToStack(envPlan.getOpenstackJson(), envCustomized.getStackName(), envCustomized,envPlan.getPlanJson())) {
                    return Result.OK("添加成功！");
                }
            }
        } catch (Exception e) {
            log.error("测试环境定制-自动添加异常，原因:" + e.getMessage());
            return Result.error("测试环境定制-自动添加异常，原因:" + e.getMessage());
        }
        return Result.error("添加失败！");
    }

    /**
     * 编辑
     *
     * @param envCustomized 环境
     * @return 编辑状态
     */
    @AutoLog(value = "测试环境定制-编辑")
    @ApiOperation(value = "测试环境定制-编辑", notes = "测试环境定制-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EnvCustomized envCustomized) {
        envCustomizedService.updateById(envCustomized);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return 删除状态
     */
    @AutoLog(value = "测试环境定制-通过id删除")
    @ApiOperation(value = "测试环境定制-通过id删除", notes = "测试环境定制-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        envCustomizedService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id集合
     * @return 删除状态
     */
    @AutoLog(value = "测试环境定制-批量删除")
    @ApiOperation(value = "测试环境定制-批量删除", notes = "测试环境定制-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.envCustomizedService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return 环境
     */
    @AutoLog(value = "测试环境定制-通过id查询")
    @ApiOperation(value = "测试环境定制-通过id查询", notes = "测试环境定制-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        EnvCustomized envCustomized = envCustomizedService.getById(id);
        if (envCustomized == null) {
            return Result.error("未找到对应数据");
        }
        if (envCustomized.getType().equals(CommonConstant.DATA_INT_1)) {
            //手动定制
            EnvCustomizedVo envCustomizedVo = new EnvCustomizedVo();
            VmDesign vmDesign = vmDesignService.getOne(new QueryWrapper<VmDesign>(
            ).eq(CommonConstant.DATA_STRING_STACK_PLAN_ID, envCustomized.getId()));
            if (vmDesign != null && StringUtils.isNotEmpty(vmDesign.getId())) {
                BeanUtils.copyProperties(vmDesign, envCustomizedVo);
                BeanUtils.copyProperties(envCustomized, envCustomizedVo);
                return Result.OK(envCustomizedVo);
            }
        } else {
            return Result.OK(envCustomized);
        }
        return Result.error("未找到对应数据");
    }
}
