package org.jeecg.modules.eval.controller;

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
import org.jeecg.modules.eval.entity.EvalFormula;
import org.jeecg.modules.eval.service.IEvalFormulaService;

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
 * @Description: 评价体系公式信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Api(tags="评价体系公式信息表")
@RestController
@RequestMapping("/eval/evalFormula")
@Slf4j
public class EvalFormulaController extends JeecgController<EvalFormula, IEvalFormulaService> {
	@Autowired
	private IEvalFormulaService evalFormulaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param evalFormula
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-分页列表查询")
	@ApiOperation(value="评价体系公式信息表-分页列表查询", notes="评价体系公式信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(EvalFormula evalFormula,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EvalFormula> queryWrapper = QueryGenerator.initQueryWrapper(evalFormula, req.getParameterMap());
		Page<EvalFormula> page = new Page<EvalFormula>(pageNo, pageSize);
		IPage<EvalFormula> pageList = evalFormulaService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param evalFormula
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-添加")
	@ApiOperation(value="评价体系公式信息表-添加", notes="评价体系公式信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EvalFormula evalFormula) {
		evalFormulaService.save(evalFormula);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param evalFormula
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-编辑")
	@ApiOperation(value="评价体系公式信息表-编辑", notes="评价体系公式信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody EvalFormula evalFormula) {
		evalFormulaService.updateById(evalFormula);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-通过id删除")
	@ApiOperation(value="评价体系公式信息表-通过id删除", notes="评价体系公式信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		evalFormulaService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-批量删除")
	@ApiOperation(value="评价体系公式信息表-批量删除", notes="评价体系公式信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.evalFormulaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评价体系公式信息表-通过id查询")
	@ApiOperation(value="评价体系公式信息表-通过id查询", notes="评价体系公式信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		EvalFormula evalFormula = evalFormulaService.getById(id);
		if(evalFormula==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(evalFormula);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param evalFormula
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EvalFormula evalFormula) {
        return super.exportXls(request, evalFormula, EvalFormula.class, "评价体系公式信息表");
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
        return super.importExcel(request, response, EvalFormula.class);
    }

}
