package org.jeecg.modules.running.uut.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.running.uut.entity.RunningUutKv;
import org.jeecg.modules.running.uut.service.IRunningUutKvService;

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
 * @Description: 审核记录键值对表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Api(tags="审核记录键值对表")
@RestController
@RequestMapping("/uut/runningUutKv")
@Slf4j
public class RunningUutKvController extends JeecgController<RunningUutKv, IRunningUutKvService> {
	@Autowired
	private IRunningUutKvService runningUutKvService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningUutKv
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-分页列表查询")
	@ApiOperation(value="审核记录键值对表-分页列表查询", notes="审核记录键值对表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutKv runningUutKv,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningUutKv> queryWrapper = QueryGenerator.initQueryWrapper(runningUutKv, req.getParameterMap());
		Page<RunningUutKv> page = new Page<RunningUutKv>(pageNo, pageSize);
		IPage<RunningUutKv> pageList = runningUutKvService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningUutKv
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-添加")
	@ApiOperation(value="审核记录键值对表-添加", notes="审核记录键值对表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutKv runningUutKv) {
		runningUutKvService.save(runningUutKv);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningUutKv
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-编辑")
	@ApiOperation(value="审核记录键值对表-编辑", notes="审核记录键值对表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutKv runningUutKv) {
		runningUutKvService.updateById(runningUutKv);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-通过id删除")
	@ApiOperation(value="审核记录键值对表-通过id删除", notes="审核记录键值对表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutKvService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-批量删除")
	@ApiOperation(value="审核记录键值对表-批量删除", notes="审核记录键值对表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutKvService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "审核记录键值对表-通过id查询")
	@ApiOperation(value="审核记录键值对表-通过id查询", notes="审核记录键值对表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutKv runningUutKv = runningUutKvService.getById(id);
		if(runningUutKv==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutKv);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutKv
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutKv runningUutKv) {
        return super.exportXls(request, runningUutKv, RunningUutKv.class, "审核记录键值对表");
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
        return super.importExcel(request, response, RunningUutKv.class);
    }

}
