package org.jeecg.modules.Imageotherinfo.controller;

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
import org.jeecg.modules.Imageotherinfo.entity.ImageOtherInfo;
import org.jeecg.modules.Imageotherinfo.service.IImageOtherInfoService;

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
 * @Description: 镜像其他软件，版本信息，测试工具关联表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Api(tags="镜像其他软件，版本信息，测试工具关联表")
@RestController
@RequestMapping("/Imageotherinfo/imageOtherInfo")
@Slf4j
public class ImageOtherInfoController extends JeecgController<ImageOtherInfo, IImageOtherInfoService> {
	@Autowired
	private IImageOtherInfoService imageOtherInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param imageOtherInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-分页列表查询")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-分页列表查询", notes="镜像其他软件，版本信息，测试工具关联表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ImageOtherInfo imageOtherInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ImageOtherInfo> queryWrapper = QueryGenerator.initQueryWrapper(imageOtherInfo, req.getParameterMap());
		Page<ImageOtherInfo> page = new Page<ImageOtherInfo>(pageNo, pageSize);
		IPage<ImageOtherInfo> pageList = imageOtherInfoService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param imageOtherInfo
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-添加")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-添加", notes="镜像其他软件，版本信息，测试工具关联表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ImageOtherInfo imageOtherInfo) {
		imageOtherInfoService.save(imageOtherInfo);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param imageOtherInfo
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-编辑")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-编辑", notes="镜像其他软件，版本信息，测试工具关联表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ImageOtherInfo imageOtherInfo) {
		imageOtherInfoService.updateById(imageOtherInfo);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-通过id删除")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-通过id删除", notes="镜像其他软件，版本信息，测试工具关联表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		imageOtherInfoService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-批量删除")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-批量删除", notes="镜像其他软件，版本信息，测试工具关联表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.imageOtherInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "镜像其他软件，版本信息，测试工具关联表-通过id查询")
	@ApiOperation(value="镜像其他软件，版本信息，测试工具关联表-通过id查询", notes="镜像其他软件，版本信息，测试工具关联表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ImageOtherInfo imageOtherInfo = imageOtherInfoService.getById(id);
		if(imageOtherInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(imageOtherInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param imageOtherInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ImageOtherInfo imageOtherInfo) {
        return super.exportXls(request, imageOtherInfo, ImageOtherInfo.class, "镜像其他软件，版本信息，测试工具关联表");
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
        return super.importExcel(request, response, ImageOtherInfo.class);
    }

}
