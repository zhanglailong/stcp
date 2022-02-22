package org.jeecg.modules.plan.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.plan.entity.TaskManage;
import org.jeecg.modules.plan.entity.TaskManageLog;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.service.ITaskManageLogService;
import org.jeecg.modules.plan.service.ITaskManageService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: 测试任务管理
 * @Author: jeecg-boot
 * @Date: 2020-12-25
 * @Version: V1.0
 */
@Api(tags = "测试环境管理")
@RestController
@RequestMapping("/plan/taskManage")
@Slf4j
public class TaskManageController extends JeecgController<TaskManage, ITaskManageService> {
    @Autowired
    private ITaskManageService taskManageService;
    @Autowired
    private ITaskManageLogService taskManageLogService;
    @Autowired
    private IVmDesignService vmDesignService;

    /**
     * 分页列表查询
     *
     * @param taskManage
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "测试环境管理-分页列表查询")
    @ApiOperation(value = "测试环境管理-分页列表查询", notes = "测试环境管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TaskManage taskManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TaskManage> queryWrapper = QueryGenerator.initQueryWrapper(taskManage, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq("create_by", sysUser.getUsername());
        Page<TaskManage> page = new Page<TaskManage>(pageNo, pageSize);
        IPage<TaskManage> pageList = taskManageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param taskManage
     * @return
     */
    @AutoLog(value = "测试环境管理-添加")
    @ApiOperation(value = "测试环境管理-添加", notes = "测试环境管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TaskManage taskManage) {
        taskManageService.save(taskManage);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param taskManage
     * @return
     */
    @AutoLog(value = "测试环境管理-编辑")
    @ApiOperation(value = "测试环境管理-编辑", notes = "测试环境管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TaskManage taskManage) {
        taskManageService.updateById(taskManage);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试环境管理-通过id删除")
    @ApiOperation(value = "测试环境管理-通过id删除", notes = "测试环境管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        taskManageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "测试环境管理-批量删除")
    @ApiOperation(value = "测试环境管理-批量删除", notes = "测试环境管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskManageService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    @AutoLog(value = "测试任务管理-批量启动")
    @ApiOperation(value = "测试任务管理-批量启动", notes = "测试任务管理-批量启动")
    @GetMapping(value = "/startBatch")
    public Result<?> startBatch(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "ids", required = true) String ids) {
        try {
            TaskManage taskManage = taskManageService.getById(id);
            if (taskManage == null) {
                return Result.error("未找到对应数据");
            }
            if (!taskManage.getHandle().equals("1")) {
                taskManage.setHandle("1");
            }
            if (taskManageService.updateById(taskManage)) {
                // 保存日志表
                taskManageLogService.saveLogAndSendSocketClent(taskManage);
                // 批量指令
                vmDesignService.sendVmOrder(ids, 1);
                return Result.OK("批量任务成功!");
            } else {
                return Result.error("批量任务失败!");
            }
        } catch (Exception e) {
            log.info("批量任务异常:{}", e);
            return Result.error("批量任务异常:" + e.getMessage());
        }
    }

    @AutoLog(value = "测试任务管理-批量停止")
    @ApiOperation(value = "测试任务管理-批量停止", notes = "测试任务管理-批量停止")
    @GetMapping(value = "/stopBatch")
    public Result<?> stopBatch(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "ids", required = true) String ids) {
        try {
            TaskManage taskManage = taskManageService.getById(id);
            if (taskManage == null) {
                return Result.error("未找到对应数据");
            }
            if (!taskManage.getHandle().equals("2")) {
                taskManage.setHandle("2");
            }
            if (taskManageService.updateById(taskManage)) {
                // 保存日志表
                taskManageLogService.saveLogAndSendSocketClent(taskManage);
                // 批量指令
                vmDesignService.sendVmOrder(ids, 2);
                return Result.OK("批量任务成功!");
            } else {
                return Result.error("批量任务失败!");
            }
        } catch (Exception e) {
            log.info("批量任务异常:{}", e);
            return Result.error("批量任务异常:" + e.getMessage());
        }
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试任务管理-通过id查询")
    @ApiOperation(value = "测试任务管理-通过id查询", notes = "测试任务管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        if (StringUtils.isEmpty(id)) {
            return Result.error("id不能为空");
        }
        TaskManage taskManage = taskManageService.getById(id);
        if (taskManage == null) {
            return Result.error("未找到对应数据");
        }
        List<TaskManageLog> tasks = taskManageLogService.getTasksByTaskId(taskManage.getId());
        // 通過规划id查找虚拟机信息
        Map<String, List> map = new HashMap<>();
        List<VmDesign> vmDesigns = vmDesignService.getVmListByEnvId(taskManage.getPlanSign());
        map.put("vmDesigns", vmDesigns);
        map.put("tasks", tasks);
        return Result.OK(map);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param taskManage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TaskManage taskManage) {
        return super.exportXls(request, taskManage, TaskManage.class, "测试环境管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TaskManage.class);
    }

}
