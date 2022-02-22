package org.jeecg.modules.running.uut.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.running.uut.entity.RunningUutListHistory;
import org.jeecg.modules.running.uut.service.IRunningUutListHistoryService;
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
 * @Description: 被测对象操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Api(tags="被测对象操作历史表")
@RestController
@RequestMapping("/record/runningUutListHistory")
@Slf4j
public class RunningUutListHistoryController extends JeecgController<RunningUutListHistory, IRunningUutListHistoryService> {
	@Autowired
	private IRunningUutListHistoryService runningUutListHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningUutListHistory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-分页列表查询")
	@ApiOperation(value="被测对象操作历史表-分页列表查询", notes="被测对象操作历史表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutListHistory runningUutListHistory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningUutListHistory> queryWrapper = QueryGenerator.initQueryWrapper(runningUutListHistory, req.getParameterMap());
		Page<RunningUutListHistory> page = new Page<RunningUutListHistory>(pageNo, pageSize);
		IPage<RunningUutListHistory> pageList = runningUutListHistoryService.page(page, queryWrapper);
		return Result.ok(runningUutListHistoryService.getRunningUutOperationList(page,runningUutListHistory));
	}
	
	/**
	 *   添加
	 *
	 * @param runningUutListHistory
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-添加")
	@ApiOperation(value="被测对象操作历史表-添加", notes="被测对象操作历史表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutListHistory runningUutListHistory) {
		runningUutListHistoryService.save(runningUutListHistory);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningUutListHistory
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-编辑")
	@ApiOperation(value="被测对象操作历史表-编辑", notes="被测对象操作历史表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutListHistory runningUutListHistory) {
		runningUutListHistoryService.updateById(runningUutListHistory);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-通过id删除")
	@ApiOperation(value="被测对象操作历史表-通过id删除", notes="被测对象操作历史表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutListHistoryService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-批量删除")
	@ApiOperation(value="被测对象操作历史表-批量删除", notes="被测对象操作历史表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutListHistoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象操作历史表-通过id查询")
	@ApiOperation(value="被测对象操作历史表-通过id查询", notes="被测对象操作历史表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutListHistory runningUutListHistory = runningUutListHistoryService.getById(id);
		if(runningUutListHistory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutListHistory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutListHistory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutListHistory runningUutListHistory) {
        return super.exportXls(request, runningUutListHistory, RunningUutListHistory.class, "被测对象操作历史表");
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
        return super.importExcel(request, response, RunningUutListHistory.class);
    }

}
