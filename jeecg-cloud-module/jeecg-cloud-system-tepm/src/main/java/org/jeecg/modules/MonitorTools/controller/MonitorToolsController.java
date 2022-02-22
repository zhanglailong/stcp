package org.jeecg.modules.MonitorTools.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.MonitorTools.entity.MonitorTools;
import org.jeecg.modules.MonitorTools.service.IMonitorToolsService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 测试工具主服务表
 * @Author: jeecg-boot
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Api(tags = "测试工具主服务表")
@RestController
@RequestMapping("/monitorTools")
@Slf4j
public class MonitorToolsController extends JeecgController<MonitorTools, IMonitorToolsService> {
    @Autowired
    private IMonitorToolsService monitorToolsService;

    /**
     * 分页列表查询
     *
     * @param monitorTools
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "测试工具主服务表-分页列表查询")
    @ApiOperation(value = "测试工具主服务表-分页列表查询", notes = "测试工具主服务表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MonitorTools monitorTools,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MonitorTools> queryWrapper = QueryGenerator.initQueryWrapper(monitorTools, req.getParameterMap());
        Page<MonitorTools> page = new Page<MonitorTools>(pageNo, pageSize);
        IPage<MonitorTools> pageList = monitorToolsService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    @AutoLog(value = "ToolsServer-修改测试工具的状态")
    @ApiOperation(value = "ToolsServer-修改测试工具的状态", notes = "ToolsServer-修改测试工具的状态")
    @PostMapping(value = "/updateToolsStatus")
    public Result<?> updateToolsStatus(@RequestParam("taskNum") String taskNum, @RequestParam("toolStatus") String toolStatus) {
        log.info("SocketServer-修改测试工具的状态:taskNum:" + taskNum + ",toolStatus:" + toolStatus);
        try {
            if (StringUtils.isNotEmpty(taskNum) && StringUtils.isNotEmpty(toolStatus)) {
                QueryWrapper<MonitorTools> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("task_num", taskNum);
                MonitorTools monitorTools = monitorToolsService.getOne(queryWrapper);
                if (monitorTools != null && StringUtils.isNotEmpty(monitorTools.getId())) {
                    //1 启动 2暂停 3恢复 4终止
                    switch (toolStatus) {
                        case "1":
                            monitorTools.setToolStatus("1");
                            monitorTools.setResultStatus("1");
                            monitorTools.setToolLog("");
                            monitorToolsService.startTools(monitorTools);
                            break;
                        case "2":
                            monitorTools.setToolStatus("2");
                            break;
                        case "3":
                            monitorTools.setToolStatus("3");
                            break;
                        case "4":
                            monitorTools.setToolStatus("4");
                            break;
                        default:
                            System.out.println("ERROR!");
                            break;
                    }
                    monitorToolsService.saveOrUpdate(monitorTools);
                    return Result.OK("修改成功");
                }
            }
        } catch (Exception e) {
            return Result.error("异常:" + e.getMessage());
        }
        return Result.error("失败");
    }


    /**
     * 添加
     *
     * @param monitorTools
     * @return
     */
    @AutoLog(value = "测试工具主服务表-添加")
    @ApiOperation(value = "测试工具主服务表-添加", notes = "测试工具主服务表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MonitorTools monitorTools) {
        monitorToolsService.save(monitorTools);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param monitorTools
     * @return
     */
    @AutoLog(value = "测试工具主服务表-编辑")
    @ApiOperation(value = "测试工具主服务表-编辑", notes = "测试工具主服务表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MonitorTools monitorTools) {
        System.out.println("monitorTools:" + JSON.toJSONString(monitorTools));
        monitorToolsService.updateById(monitorTools);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试工具主服务表-通过id删除")
    @ApiOperation(value = "测试工具主服务表-通过id删除", notes = "测试工具主服务表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        monitorToolsService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "测试工具主服务表-批量删除")
    @ApiOperation(value = "测试工具主服务表-批量删除", notes = "测试工具主服务表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.monitorToolsService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "测试工具主服务表-通过id查询")
    @ApiOperation(value = "测试工具主服务表-通过id查询", notes = "测试工具主服务表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MonitorTools monitorTools = monitorToolsService.getById(id);
        if (monitorTools == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(monitorTools);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param monitorTools
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MonitorTools monitorTools) {
        return super.exportXls(request, monitorTools, MonitorTools.class, "测试工具主服务表");
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
        return super.importExcel(request, response, MonitorTools.class);
    }

}
