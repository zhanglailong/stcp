package org.jeecg.modules.sjcj.collectiondataanalyse.controller;


import java.util.Arrays;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import org.jeecg.modules.sjcj.collectiondataanalyse.entity.CollectionDataAnalyse;
import org.jeecg.modules.sjcj.collectiondataanalyse.service.ICollectionDataAnalyseService;

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
 * @Description: 采集数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
/*用在请求的类上,表示对类的说明。*/
@Api(tags="采集数据分析")
 /*@Controller + @ResponseBody组成*/
@RestController
/*请求的url 路径*/
@RequestMapping("/collectiondataanalyse/collectionDataAnalyse")
/**用于作日志输出的，一般会在项目每个类的开头加入该注解。*/
@Slf4j
/**
 * @Author: test
 * */
public class CollectionDataAnalyseController extends JeecgController<CollectionDataAnalyse, ICollectionDataAnalyseService> {


	@Autowired
	/**依赖注入*/
	private ICollectionDataAnalyseService collectionDataAnalyseService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectionDataAnalyse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	// 在需要记录日志信息的方法上添加@AutoLog注解，通过配置的切面类，即可插入数据库对应的日志信息。
	@AutoLog(value = "采集数据分析-分页列表查询")
	// 协议描述
	@ApiOperation(value="采集数据分析-分页列表查询", notes="采集数据分析-分页列表查询")
	/**get请求方式  进行交互*/
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectionDataAnalyse collectionDataAnalyse,
								   // pageNo参数
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   // pageSize参数
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		// 通过QueryGenerator的initQueryWrapper方法 获取 queryWrapper				// req.getParameterMap()方法
		QueryWrapper<CollectionDataAnalyse> queryWrapper = QueryGenerator.initQueryWrapper(collectionDataAnalyse, req.getParameterMap());
		// 通过 得到的 pageNo 和 pageSize  获取 page
		Page<CollectionDataAnalyse> page = new Page<CollectionDataAnalyse>(pageNo, pageSize);
		// 根据 获取的 page 和 queryWrapper 进行分页查询操作
		IPage<CollectionDataAnalyse> pageList = collectionDataAnalyseService.page(page, queryWrapper);
		// 返回值 Result.ok()
		return Result.ok(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param collectionDataAnalyse
	 * @return
	 */
	@AutoLog(value = "采集数据分析-添加")
	@ApiOperation(value="采集数据分析-添加", notes="采集数据分析-添加")
	/**post 请求方式 进行交互*/
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectionDataAnalyse collectionDataAnalyse) {
		// 通过collectionDataAnalyseService的 save 方法进行添加操作
		collectionDataAnalyseService.save(collectionDataAnalyse);
		// 返回值 Result.ok()
		return Result.ok("添加成功！");
	}

	 /**
	  *   添加
	  *
	  *
	  */
	 @AutoLog(value = "采集数据分析-插入")
	 @ApiOperation(value="采集数据分析-添加", notes="采集数据分析-添加")
	/**post 请求方式 进行交互*/
	 @PostMapping(value = "/inster")
	 public Result<?> inster() throws IOException {
		 return Result.ok("添加成功！");
	 }

	/**
	 *  编辑
	 *
	 * @param collectionDataAnalyse
	 * @return
	 */
	@AutoLog(value = "采集数据分析-编辑")
	@ApiOperation(value="采集数据分析-编辑", notes="采集数据分析-编辑")
	/**put 请求方式 进行交互*/
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectionDataAnalyse collectionDataAnalyse) {
		// 通过collectionDataAnalyseService的 updateById 方法进行编辑操作
		collectionDataAnalyseService.updateById(collectionDataAnalyse);
		// 返回值 Result.ok（）
		return Result.ok("编辑成功!");
	}
	
	/**
	 *  通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集数据分析-通过id删除")
	@ApiOperation(value="采集数据分析-通过id删除", notes="采集数据分析-通过id删除")
	/**使用delete 方式进行交互*/
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		// 通过collectionDataAnalyseService的 removeById方法  进行删除操作
		collectionDataAnalyseService.removeById(id);
		// 返回值 Result.ok()
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采集数据分析-批量删除")
	@ApiOperation(value="采集数据分析-批量删除", notes="采集数据分析-批量删除")
	/**使用delete 方式进行交互*/
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		// Arrays.asList()方法：此方法是将数组转化为list。
		// 通过collectionDataAnalyseService的removeByIds方法进行 批量删除操作
		this.collectionDataAnalyseService.removeByIds(Arrays.asList(ids.split(",")));
		// 返回值 Result.ok()
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集数据分析-通过id查询")
	@ApiOperation(value="采集数据分析-通过id查询", notes="采集数据分析-通过id查询")
	/**get 请求方式 进行交互*/
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		// 通过collectionDataAnalyseService的getById 进行获取 主键id
		CollectionDataAnalyse collectionDataAnalyse = collectionDataAnalyseService.getById(id);
		// 获取的主键id 进行判断是否为null 值
		if(collectionDataAnalyse==null) {
			return Result.error("未找到对应数据");
		}
		// 返回值 result
		return Result.ok(collectionDataAnalyse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectionDataAnalyse
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectionDataAnalyse collectionDataAnalyse) {
        return super.exportXls(request, collectionDataAnalyse, CollectionDataAnalyse.class, "采集数据分析");
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
        return super.importExcel(request, response, CollectionDataAnalyse.class);
    }


	 /***
	  *  添加 js Charts
	  */
	 @AutoLog(value = "采集数据分析-插入")
	 @ApiOperation(value="采集数据分析-添加", notes="采集数据分析-添加")
	 /**post 请求方式 进行交互*/
	 @PostMapping(value = "/addition")
	 public Result<?> addition () throws IOException, ScriptException {
		 return Result.ok("添加成功！");
	 }

	 /**
	  * 添加 html  Dashboard
	  */
	 @AutoLog(value = "采集数据分析-插入")
	 @ApiOperation(value="采集数据分析-添加", notes="采集数据分析-添加")
	/**post 请求方式 进行交互*/
	 @PostMapping(value = "/addDashboard")
	 public Result<?> addDashboard () throws Exception {

		 return Result.ok("添加成功！");
	 }
 }
