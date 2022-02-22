package org.jeecg.modules.running.record.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.running.record.entity.RunningUutRecord;
import org.jeecg.modules.running.record.service.IRunningUutRecordService;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Api(tags="被测对象属性表")
@RestController
@RequestMapping("/record/runningUutRecord")
@Slf4j
public class RunningUutRecordController extends JeecgController<RunningUutRecord, IRunningUutRecordService> {
	@Autowired
	private IRunningUutRecordService runningUutRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningUutRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-分页列表查询")
	@ApiOperation(value="被测对象属性表-分页列表查询", notes="被测对象属性表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutRecord runningUutRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningUutRecord> queryWrapper = QueryGenerator.initQueryWrapper(runningUutRecord, req.getParameterMap());
		Page<RunningUutRecord> page = new Page<RunningUutRecord>(pageNo, pageSize);
		IPage<RunningUutRecord> pageList = runningUutRecordService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningUutRecord
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-添加")
	@ApiOperation(value="被测对象属性表-添加", notes="被测对象属性表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutRecord runningUutRecord) {
		runningUutRecordService.save(runningUutRecord);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningUutRecord
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-编辑")
	@ApiOperation(value="被测对象属性表-编辑", notes="被测对象属性表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutRecord runningUutRecord) {
		RunningUutRecord runningUutRecordEntity = runningUutRecordService.
				findUniqueBy("uut_list_id",runningUutRecord.getUutListId());
		if(runningUutRecordEntity==null) {
			//此时是新增操作
			runningUutRecordService.save(runningUutRecord);
		} else {
			runningUutRecordService.updateById(runningUutRecord);
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-通过id删除")
	@ApiOperation(value="被测对象属性表-通过id删除", notes="被测对象属性表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutRecordService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-批量删除")
	@ApiOperation(value="被测对象属性表-批量删除", notes="被测对象属性表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象属性表-通过id查询")
	@ApiOperation(value="被测对象属性表-通过id查询", notes="被测对象属性表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper(new RunningUutRecord(), null);
		queryWrapper.eq("uut_list_id", id);
		RunningUutRecord runningUutRecord = runningUutRecordService.getOne(queryWrapper);
		if(runningUutRecord==null) {
			runningUutRecord = new RunningUutRecord();
			runningUutRecord.setUutListId(id);
			return Result.ok(runningUutRecord);
		}
		return Result.ok(runningUutRecord);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "被测对象属性表-通过id查询")
	 @ApiOperation(value="被测对象属性表-通过id查询", notes="被测对象属性表-通过id查询")
	 @GetMapping(value = "/queryById/feign")
	 public Result<?> queryByIdFeign(@RequestParam(name="id",required=true) String id) {
	 	 String uutRecordId = runningUutRecordService.getUutRecordId(id);
		 RunningUutRecord runningUutRecord = runningUutRecordService.getById(uutRecordId);
		 if(runningUutRecord==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(runningUutRecord);
	 }

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutRecord
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutRecord runningUutRecord) {
        return super.exportXls(request, runningUutRecord, RunningUutRecord.class, "被测对象属性表");
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
        return super.importExcel(request, response, RunningUutRecord.class);
    }

}
