package org.jeecg.modules.sjcj.collectiondatamanagement.controller;

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
import org.jeecg.modules.sjcj.collectiondatamanagement.entity.CollectionDataManagement;
import org.jeecg.modules.sjcj.collectiondatamanagement.service.ICollectionDataManagementService;

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
 * @Description: 采集数据管理
 * @Author: jeecg-boot
 * @Date:   2021-01-11
 * @Version: V1.0
 */
@Api(tags="采集数据管理")
@RestController
@RequestMapping("/collectiondatamanagement/collectionDataManagement")
@Slf4j
public class CollectionDataManagementController extends JeecgController<CollectionDataManagement, ICollectionDataManagementService> {
	@Autowired
	private ICollectionDataManagementService collectionDataManagementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param collectionDataManagement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采集数据管理-分页列表查询")
	@ApiOperation(value="采集数据管理-分页列表查询", notes="采集数据管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CollectionDataManagement collectionDataManagement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		collectionDataManagement.setDeleteFalg("1");
		QueryWrapper<CollectionDataManagement> queryWrapper = QueryGenerator.initQueryWrapper(collectionDataManagement, req.getParameterMap());
		Page<CollectionDataManagement> page = new Page<CollectionDataManagement>(pageNo, pageSize);
		IPage<CollectionDataManagement> pageList = collectionDataManagementService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param collectionDataManagement
	 * @return
	 */
	@AutoLog(value = "采集数据管理-添加")
	@ApiOperation(value="采集数据管理-添加", notes="采集数据管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CollectionDataManagement collectionDataManagement) {
		collectionDataManagement.setDeleteFalg("1");
		collectionDataManagementService.save(collectionDataManagement);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param collectionDataManagement
	 * @return
	 */
	@AutoLog(value = "采集数据管理-编辑")
	@ApiOperation(value="采集数据管理-编辑", notes="采集数据管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CollectionDataManagement collectionDataManagement) {
		collectionDataManagementService.updateById(collectionDataManagement);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集数据管理-通过id删除")
	@ApiOperation(value="采集数据管理-通过id删除", notes="采集数据管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		CollectionDataManagement collectionDataManagement = collectionDataManagementService.getById(id);
		collectionDataManagement.setDeleteFalg("2");
		collectionDataManagementService.updateById(collectionDataManagement);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采集数据管理-批量删除")
	@ApiOperation(value="采集数据管理-批量删除", notes="采集数据管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		for (String id : idList) {
			CollectionDataManagement collectionDataManagement = collectionDataManagementService.getById(id);
			collectionDataManagement.setDeleteFalg("2");
			collectionDataManagementService.updateById(collectionDataManagement);
		}
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集数据管理-通过id查询")
	@ApiOperation(value="采集数据管理-通过id查询", notes="采集数据管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CollectionDataManagement collectionDataManagement = collectionDataManagementService.getById(id);
		if(collectionDataManagement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(collectionDataManagement);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param collectionDataManagement
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CollectionDataManagement collectionDataManagement) {
        return super.exportXls(request, collectionDataManagement, CollectionDataManagement.class, "采集数据管理");
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
        return super.importExcel(request, response, CollectionDataManagement.class);
    }

}
