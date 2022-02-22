package org.jeecg.modules.tools.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tools.entity.RunningToolsHistory;
import org.jeecg.modules.tools.service.IRunningToolsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 测试工具操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Api(tags="测试工具操作历史表")
@RestController
@RequestMapping("/tools/runningToolsHistory")
@Slf4j
public class RunningToolsHistoryController extends JeecgController<RunningToolsHistory, IRunningToolsHistoryService> {
	@Autowired
	private IRunningToolsHistoryService runningToolsHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-分页列表查询")
	@ApiOperation(value="测试工具操作历史表-分页列表查询", notes="测试工具操作历史表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(	RunningToolsHistory runningToolsHistory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
//		String runningToolsId = req.getParameter("runningToolsId");
		QueryWrapper<RunningToolsHistory> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsHistory, req.getParameterMap());
//		if(!StringUtils.isEmpty(runningToolsId)){
//			queryWrapper.eq("running_tools_id",runningToolsId);
//		}
//		queryWrapper.orderByDesc("create_time");
		Page<RunningToolsHistory> page = new Page<>(pageNo, pageSize);
		//IPage<RunningToolsHistory> pageList1 = runningToolsHistoryService.page(page, queryWrapper);
		//IPage<RunningToolsHistory> pageList = runningToolsHistoryService.getRunningToolsOperationList(page,queryWrapper);
		return Result.ok(runningToolsHistoryService.getRunningToolsOperationList(page,runningToolsHistory));
	}
	
	/**
	 *   添加
	 *
	 * @param runningToolsHistory
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-添加")
	@ApiOperation(value="测试工具操作历史表-添加", notes="测试工具操作历史表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningToolsHistory runningToolsHistory) {
		runningToolsHistoryService.save(runningToolsHistory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningToolsHistory
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-编辑")
	@ApiOperation(value="测试工具操作历史表-编辑", notes="测试工具操作历史表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningToolsHistory runningToolsHistory) {
		runningToolsHistoryService.updateById(runningToolsHistory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-通过id删除")
	@ApiOperation(value="测试工具操作历史表-通过id删除", notes="测试工具操作历史表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningToolsHistoryService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-批量删除")
	@ApiOperation(value="测试工具操作历史表-批量删除", notes="测试工具操作历史表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningToolsHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具操作历史表-通过id查询")
	@ApiOperation(value="测试工具操作历史表-通过id查询", notes="测试工具操作历史表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningToolsHistory runningToolsHistory = runningToolsHistoryService.getById(id);
		if(runningToolsHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningToolsHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningToolsHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningToolsHistory runningToolsHistory) {
        return super.exportXls(request, runningToolsHistory, RunningToolsHistory.class, "测试工具操作历史表");
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
        return super.importExcel(request, response, RunningToolsHistory.class);
    }

}
