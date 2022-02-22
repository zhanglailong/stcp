package org.jeecg.modules.queue.controller;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.queue.entity.StcpTestQueue;
import org.jeecg.modules.queue.service.IStcpTestQueueService;
import org.jeecg.modules.tools.mapper.RunningToolsLicensemonitorMapper;
import org.jeecg.modules.tools.vo.RunningToolsMonitorListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 云化工具分时使用列表
 * @Author: jeecg-boot
 * @Date: 2020-12-25
 * @Version: V1.0
 */
@Api(tags = "云化工具分时使用列表")
@RestController
@RequestMapping("/queue/stcpTestQueue")
@Slf4j
public class StcpTestQueueController extends JeecgController<StcpTestQueue, IStcpTestQueueService> {
    @Autowired
    private IStcpTestQueueService stcpTestQueueService;

    @Resource
    private RunningToolsLicensemonitorMapper runningToolsLicensemonitorMapper;

    /**
     * 分页列表查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-分页列表查询")
    @ApiOperation(value = "云化工具分时使用列表-分页列表查询", notes = "云化工具分时使用列表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        String toolsName = req.getParameter("toolName");
        Page<RunningToolsMonitorListVO> page = new Page<>(pageNo, pageSize);
        IPage<RunningToolsMonitorListVO> pageList = runningToolsLicensemonitorMapper.selectMonitorList(page, toolsName);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param stcpTestQueue
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-添加")
    @ApiOperation(value = "云化工具分时使用列表-添加", notes = "云化工具分时使用列表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody StcpTestQueue stcpTestQueue) {
        stcpTestQueueService.save(stcpTestQueue);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param stcpTestQueue
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-编辑")
    @ApiOperation(value = "云化工具分时使用列表-编辑", notes = "云化工具分时使用列表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody StcpTestQueue stcpTestQueue) {
        stcpTestQueueService.updateById(stcpTestQueue);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-通过id删除")
    @ApiOperation(value = "云化工具分时使用列表-通过id删除", notes = "云化工具分时使用列表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        stcpTestQueueService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-批量删除")
    @ApiOperation(value = "云化工具分时使用列表-批量删除", notes = "云化工具分时使用列表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.stcpTestQueueService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "云化工具分时使用列表-通过id查询")
    @ApiOperation(value = "云化工具分时使用列表-通过id查询", notes = "云化工具分时使用列表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        StcpTestQueue stcpTestQueue = stcpTestQueueService.getById(id);
        if (stcpTestQueue == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(stcpTestQueue);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param stcpTestQueue
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StcpTestQueue stcpTestQueue) {
        return super.exportXls(request, stcpTestQueue, StcpTestQueue.class, "云化工具分时使用列表");
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
        return super.importExcel(request, response, StcpTestQueue.class);
    }

}
