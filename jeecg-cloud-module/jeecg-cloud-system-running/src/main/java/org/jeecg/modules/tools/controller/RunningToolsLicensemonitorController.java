package org.jeecg.modules.tools.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import org.jeecg.modules.tools.service.IRunningToolsLicensemonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: License监控表
 * @Author: jeecg-boot
 * @Date: 2021-03-12
 * @Version: V1.0
 */
@Api(tags = "License监控表")
@RestController
@RequestMapping("/tools/runningToolsLicensemonitor")
@Slf4j
public class RunningToolsLicensemonitorController extends JeecgController<RunningToolsLicensemonitor, IRunningToolsLicensemonitorService> {
    @Autowired
    private IRunningToolsLicensemonitorService runningToolsLicensemonitorService;

//    /**
//     * 分页列表查询
//     *
//     * @param runningToolsLicensemonitor
//     * @param pageNo
//     * @param pageSize
//     * @param req
//     * @return
//     */
    @AutoLog(value = "License监控表-分页列表查询")
    @ApiOperation(value = "License监控表-分页列表查询", notes = "License监控表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(RunningToolsLicensemonitor runningToolsLicensemonitor,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<RunningToolsLicensemonitor> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsLicensemonitor, req.getParameterMap());
        Page<RunningToolsLicensemonitor> page = new Page<RunningToolsLicensemonitor>(pageNo, pageSize);
        IPage<RunningToolsLicensemonitor> pageList = runningToolsLicensemonitorService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加监控记录
     *
     * @param runningToolsLicensemonitorList
     * @return
     */
    @AutoLog(value = "License监控表-添加")
    @ApiOperation(value = "License监控表-添加", notes = "License监控表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody List<RunningToolsLicensemonitor> runningToolsLicensemonitorList) {
        runningToolsLicensemonitorList.forEach(
                runningToolsLicensemonitor -> runningToolsLicensemonitor.setStatus(CommonConstant.QUEUING)
        );
        runningToolsLicensemonitorService.saveBatch(runningToolsLicensemonitorList);
        return Result.ok("添加成功！");
    }

    /**
     * 修改监控状态为执行中
     *
     * @param taskId
     * @return
     */
    @AutoLog(value = "License监控表-编辑")
    @ApiOperation(value = "License监控表-编辑", notes = "License监控表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestParam(name = "taskId", required = true) String taskId) {
        RunningToolsLicensemonitor toolsLicensemonitor = runningToolsLicensemonitorService.lambdaQuery()
                .eq(RunningToolsLicensemonitor::getTaskId, taskId).one();
        toolsLicensemonitor.setStatus(CommonConstant.EXECUTING);
        runningToolsLicensemonitorService.updateById(toolsLicensemonitor);
        return Result.ok("编辑成功!");
    }

    /**
     * 任务执行完毕，删除监控记录
     *
     * @param taskId
     * @return
     */
    @AutoLog(value = "License监控表-通过id删除")
    @ApiOperation(value = "License监控表-通过id删除", notes = "License监控表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "taskId", required = true) String taskId) {
        LambdaQueryWrapper<RunningToolsLicensemonitor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RunningToolsLicensemonitor::getTaskId, taskId);
        runningToolsLicensemonitorService.remove(wrapper);
        return Result.ok("删除成功!");
    }

//    /**
//     * 批量删除
//     *
//     * @param ids
//     * @return
//     */
//    @AutoLog(value = "License监控表-批量删除")
//    @ApiOperation(value = "License监控表-批量删除", notes = "License监控表-批量删除")
//    @DeleteMapping(value = "/deleteBatch")
//    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
//        this.runningToolsLicensemonitorService.removeByIds(Arrays.asList(ids.split(",")));
//        return Result.ok("批量删除成功!");
//    }

    /**
     * 通过id查询
     *
     * @return
     */
    @AutoLog(value = "License监控表-通过id查询")
    @ApiOperation(value = "License监控表-通过id查询", notes = "License监控表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String toolsId = req.getParameter("toolsId");
        LambdaQueryWrapper<RunningToolsLicensemonitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RunningToolsLicensemonitor::getToolsId,toolsId);
        Page<RunningToolsLicensemonitor> page = new Page<>(pageNo, pageSize);
        IPage<RunningToolsLicensemonitor> pageList = runningToolsLicensemonitorService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

//    /**
//     * 导出excel
//     *
//     * @param request
//     * @param runningToolsLicensemonitor
//     */
//    @RequestMapping(value = "/exportXls")
//    public ModelAndView exportXls(HttpServletRequest request, RunningToolsLicensemonitor runningToolsLicensemonitor) {
//        return super.exportXls(request, runningToolsLicensemonitor, RunningToolsLicensemonitor.class, "License监控表");
//    }
//
//    /**
//     * 通过excel导入数据
//     *
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, RunningToolsLicensemonitor.class);
//    }

}
