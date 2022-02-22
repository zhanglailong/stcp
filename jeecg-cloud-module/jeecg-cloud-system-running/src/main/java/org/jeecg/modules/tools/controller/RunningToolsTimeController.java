package org.jeecg.modules.tools.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tools.entity.RunningToolsTime;
import org.jeecg.modules.tools.service.IRunningToolsTimeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试工具时段表
 * @Author: jeecg-boot
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Api(tags="测试工具时段表")
@RestController
@RequestMapping("/tools/runningToolsTime")
@Slf4j
public class RunningToolsTimeController extends JeecgController<RunningToolsTime, IRunningToolsTimeService> {
	@Autowired
	private IRunningToolsTimeService runningToolsTimeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningToolsTime
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-分页列表查询")
	@ApiOperation(value="测试工具时段表-分页列表查询", notes="测试工具时段表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningToolsTime runningToolsTime,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningToolsTime> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsTime, req.getParameterMap());
		Page<RunningToolsTime> page = new Page<RunningToolsTime>(pageNo, pageSize);
		IPage<RunningToolsTime> pageList = runningToolsTimeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningToolsTime
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-添加")
	@ApiOperation(value="测试工具时段表-添加", notes="测试工具时段表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningToolsTime runningToolsTime) {
		runningToolsTimeService.save(runningToolsTime);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningToolsTime
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-编辑")
	@ApiOperation(value="测试工具时段表-编辑", notes="测试工具时段表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningToolsTime runningToolsTime) {
		runningToolsTimeService.updateById(runningToolsTime);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-通过id删除")
	@ApiOperation(value="测试工具时段表-通过id删除", notes="测试工具时段表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningToolsTimeService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-批量删除")
	@ApiOperation(value="测试工具时段表-批量删除", notes="测试工具时段表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningToolsTimeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具时段表-通过id查询")
	@ApiOperation(value="测试工具时段表-通过id查询", notes="测试工具时段表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningToolsTime runningToolsTime = runningToolsTimeService.getById(id);
		if(runningToolsTime==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningToolsTime);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningToolsTime
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningToolsTime runningToolsTime) {
        return super.exportXls(request, runningToolsTime, RunningToolsTime.class, "测试工具时段表");
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
        return super.importExcel(request, response, RunningToolsTime.class);
    }

}
