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
import org.jeecg.modules.number.entity.NumberTypeInfo;
import org.jeecg.modules.number.service.INumberTypeInfoService;

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
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Api(tags="编号信息表")
@RestController
@RequestMapping("/system/numberTypeInfo")
@Slf4j
public class NumberTypeInfoController extends JeecgController<NumberTypeInfo, INumberTypeInfoService> {
	@Autowired
	private INumberTypeInfoService numberTypeInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param numberTypeInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "编号信息表-分页列表查询")
	@ApiOperation(value="编号信息表-分页列表查询", notes="编号信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NumberTypeInfo numberTypeInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NumberTypeInfo> queryWrapper = QueryGenerator.initQueryWrapper(numberTypeInfo, req.getParameterMap());
		Page<NumberTypeInfo> page = new Page<NumberTypeInfo>(pageNo, pageSize);
		IPage<NumberTypeInfo> pageList = numberTypeInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param numberTypeInfo
	 * @return
	 */
	@AutoLog(value = "编号信息表-添加")
	@ApiOperation(value="编号信息表-添加", notes="编号信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NumberTypeInfo numberTypeInfo) {
		numberTypeInfoService.save(numberTypeInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param numberTypeInfo
	 * @return
	 */
	@AutoLog(value = "编号信息表-编辑")
	@ApiOperation(value="编号信息表-编辑", notes="编号信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody NumberTypeInfo numberTypeInfo) {
		numberTypeInfoService.updateById(numberTypeInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "编号信息表-通过id删除")
	@ApiOperation(value="编号信息表-通过id删除", notes="编号信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		numberTypeInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "编号信息表-批量删除")
	@ApiOperation(value="编号信息表-批量删除", notes="编号信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.numberTypeInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "编号信息表-通过id查询")
	@ApiOperation(value="编号信息表-通过id查询", notes="编号信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		NumberTypeInfo numberTypeInfo = numberTypeInfoService.getById(id);
		if(numberTypeInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(numberTypeInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param numberTypeInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NumberTypeInfo numberTypeInfo) {
        return super.exportXls(request, numberTypeInfo, NumberTypeInfo.class, "编号信息表");
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
        return super.importExcel(request, response, NumberTypeInfo.class);
    }

}
