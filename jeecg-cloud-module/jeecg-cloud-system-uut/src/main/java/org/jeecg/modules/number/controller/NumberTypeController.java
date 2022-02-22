package org.jeecg.modules.number.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.number.entity.NumberType;
import org.jeecg.modules.number.service.INumberTypeService;

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
 * @Description: 编号管理表
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Api(tags="编号管理表")
@RestController
@RequestMapping("/number/numberType")
@Slf4j
public class NumberTypeController extends JeecgController<NumberType, INumberTypeService> {
	@Autowired
	private INumberTypeService numberTypeService;
	
	/**
	 * 分页列表查询
	 *
	 * @param numberType
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "编号管理表-分页列表查询")
	@ApiOperation(value="编号管理表-分页列表查询", notes="编号管理表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NumberType numberType,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NumberType> queryWrapper = QueryGenerator.initQueryWrapper(numberType, req.getParameterMap());
		Page<NumberType> page = new Page<NumberType>(pageNo, pageSize);
		IPage<NumberType> pageList = numberTypeService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param numberType
	 * @return
	 */
	@AutoLog(value = "编号管理表-添加")
	@ApiOperation(value="编号管理表-添加", notes="编号管理表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NumberType numberType) {
		Result checkResult = numberTypeService.checkBeforeAddAndEdit(numberType);
		if(!checkResult.isSuccess())
		{
			return checkResult;
		}
		numberTypeService.save(numberType);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param numberType
	 * @return
	 */
	@AutoLog(value = "编号管理表-编辑")
	@ApiOperation(value="编号管理表-编辑", notes="编号管理表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NumberType numberType) {
		Result checkResult = numberTypeService.checkBeforeAddAndEdit(numberType);
		if(!checkResult.isSuccess())
		{
			return checkResult;
		}
		numberTypeService.updateById(numberType);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "编号管理表-通过id删除")
	@ApiOperation(value="编号管理表-通过id删除", notes="编号管理表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		numberTypeService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "编号管理表-批量删除")
	@ApiOperation(value="编号管理表-批量删除", notes="编号管理表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.numberTypeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "编号管理表-通过id查询")
	@ApiOperation(value="编号管理表-通过id查询", notes="编号管理表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NumberType numberType = numberTypeService.getById(id);
		if(numberType==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(numberType);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param numberType
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NumberType numberType) {
        return super.exportXls(request, numberType, NumberType.class, "编号管理表");
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
        return super.importExcel(request, response, NumberType.class);
    }

}
