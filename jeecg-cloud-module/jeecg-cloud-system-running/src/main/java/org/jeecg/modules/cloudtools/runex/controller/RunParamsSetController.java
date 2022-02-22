package org.jeecg.modules.cloudtools.runex.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;

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
 * @Description: 设置工具可用参数
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Api(tags="设置工具可用参数")
@RestController
@RequestMapping("/xrunex/xrunParamsSet")
@Slf4j
public class RunParamsSetController extends JeecgController<RunParamsSet, IRunParamsSetService> {
	@Autowired
	private IRunParamsSetService runParamsSetService;
	
	@AutoLog(value = "获取页面值")
	@ApiOperation(value="获取页面值",notes="获取页面值")
	@GetMapping(value="/getItems")
	public Result<?> getItems(String setid){
		QueryWrapper<RunParamsSet> wrapper = new QueryWrapper<>();
		wrapper.select("id","param","des").eq("control_set_id", setid);
		return Result.ok(runParamsSetService.list(wrapper));
	}
	
	/**
	 * 分页列表查询
	 *
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-分页列表查询")
	@ApiOperation(value="设置工具可用参数-分页列表查询", notes="设置工具可用参数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunParamsSet runParamsSet,HttpServletRequest req) {
		//获取所有项,倒序
		QueryWrapper<RunParamsSet> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("control_set_id",runParamsSet.getControlSetId()).orderByAsc("`order`");
		return Result.ok(runParamsSetService.getParamsList(queryWrapper));
	}


	/**
	 *   添加
	 *
	 * @param runParamsSet
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-添加")
	@ApiOperation(value="设置工具可用参数-添加", notes="设置工具可用参数-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunParamsSet runParamsSet) {
		runParamsSet.setCreateTime(new Date());
		runParamsSetService.save(runParamsSet);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runParamsSet
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-编辑")
	@ApiOperation(value="设置工具可用参数-编辑", notes="设置工具可用参数-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunParamsSet runParamsSet) {
		runParamsSetService.updateById(runParamsSet);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-通过id删除")
	@ApiOperation(value="设置工具可用参数-通过id删除", notes="设置工具可用参数-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runParamsSetService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-批量删除")
	@ApiOperation(value="设置工具可用参数-批量删除", notes="设置工具可用参数-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runParamsSetService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设置工具可用参数-通过id查询")
	@ApiOperation(value="设置工具可用参数-通过id查询", notes="设置工具可用参数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunParamsSet runParamsSet = runParamsSetService.getById(id);
		if(runParamsSet==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runParamsSet);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runParamsSet
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunParamsSet runParamsSet) {
        return super.exportXls(request, runParamsSet, RunParamsSet.class, "设置工具可用参数");
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
        return super.importExcel(request, response, RunParamsSet.class);
    }

}
