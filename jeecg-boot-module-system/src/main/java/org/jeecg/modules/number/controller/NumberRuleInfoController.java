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

import org.apache.bcel.generic.NEW;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.number.entity.NumberRuleInfo;
import org.jeecg.modules.number.entity.NumberType;
import org.jeecg.modules.number.service.INumberRuleInfoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.number.service.INumberTypeService;
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
@RequestMapping("/system/numberRuleInfo")
@Slf4j
public class NumberRuleInfoController extends JeecgController<NumberRuleInfo, INumberRuleInfoService> {
	@Autowired
	private INumberRuleInfoService numberRuleInfoService;

	/**
	 * 分页列表查询
	 *
	 * @param numberRuleInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "编号信息表-分页列表查询")
	@ApiOperation(value="编号信息表-分页列表查询", notes="编号信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(NumberRuleInfo numberRuleInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<NumberRuleInfo> queryWrapper = new QueryWrapper<NumberRuleInfo>();
		queryWrapper.eq("type_info_id",numberRuleInfo.getTypeInfoId());
		queryWrapper.orderByAsc("sort");
		Page<NumberRuleInfo> page = new Page<NumberRuleInfo>(pageNo, pageSize);
		IPage<NumberRuleInfo> pageList = numberRuleInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param numberRuleInfo
	 * @return
	 */
	@AutoLog(value = "编号信息表-添加")
	@ApiOperation(value="编号信息表-添加", notes="编号信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody NumberRuleInfo numberRuleInfo) {
		numberRuleInfoService.save(numberRuleInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param numberRuleInfo
	 * @return
	 */
	@AutoLog(value = "编号信息表-编辑")
	@ApiOperation(value="编号信息表-编辑", notes="编号信息表-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody NumberRuleInfo numberRuleInfo) {
		numberRuleInfoService.updateById(numberRuleInfo);
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
		numberRuleInfoService.removeById(id);
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
		this.numberRuleInfoService.removeByIds(Arrays.asList(ids.split(",")));
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
		NumberRuleInfo numberRuleInfo = numberRuleInfoService.getById(id);
		if(numberRuleInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(numberRuleInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param numberRuleInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, NumberRuleInfo numberRuleInfo) {
        return super.exportXls(request, numberRuleInfo, NumberRuleInfo.class, "编号信息表");
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
        return super.importExcel(request, response, NumberRuleInfo.class);
    }

	 /** dq add
	  *  规则归零
	  *
	  * @param
	  * 	NumberType
	  * @return
	  */
	 @AutoLog(value = "规则归零")
	 @ApiOperation(value="规则归零", notes="规则归零")
	 @PostMapping(value = "/toZero")
	 public Result<?> toZero(@RequestBody NumberType numberType) {
		 QueryWrapper<NumberRuleInfo> queryWrapper= new QueryWrapper<NumberRuleInfo>();
		 queryWrapper.eq("type_info_id",numberType.getId());
		 queryWrapper.eq("name","序列号");
		 // 这里总是假定一个规则中只有一个是序列号并且在最后，要不清零操作没法玩了
		 NumberRuleInfo ruleInfo = numberRuleInfoService.getOne(queryWrapper);
		 if(ruleInfo != null)
		 {
			 // 归零就是把当前值设为0
		 	ruleInfo.setNowNumber("0");
		 	numberRuleInfoService.updateById(ruleInfo);
		 }
		 return Result.ok("设置成功!");
	 }

}
