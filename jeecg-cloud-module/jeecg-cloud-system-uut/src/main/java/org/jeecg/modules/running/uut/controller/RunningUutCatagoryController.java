package org.jeecg.modules.running.uut.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.running.uut.entity.RunningUutCatagory;
import org.jeecg.modules.running.uut.entity.RunningUutNode;
import org.jeecg.modules.running.uut.service.IRunningUutCatagoryService;
import org.jeecg.modules.running.uut.service.IRunningUutNodeService;
import org.jeecg.modules.running.uut.vo.RunningUutCatagoryPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 被测对象流程分类表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Api(tags="被测对象流程分类表")
@RestController
@RequestMapping("/uut/runningUutCatagory")
@Slf4j
public class RunningUutCatagoryController {
	@Autowired
	private IRunningUutCatagoryService runningUutCatagoryService;
	@Autowired
	private IRunningUutNodeService runningUutNodeService;
	@Autowired
	private ISysDepartService departService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningUutCatagory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-分页列表查询")
	@ApiOperation(value="被测对象流程分类表-分页列表查询", notes="被测对象流程分类表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutCatagory runningUutCatagory,
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
		QueryWrapper<RunningUutCatagory> queryWrapper = QueryGenerator.initQueryWrapper(runningUutCatagory, req.getParameterMap());
		Page<RunningUutCatagory> page = new Page<RunningUutCatagory>(pageNo, pageSize);
		IPage<RunningUutCatagory> pageList = runningUutCatagoryService.page(page, queryWrapper);
		// 以下根据部门id拼接部门名称字段
		List<RunningUutCatagory> categoryList = pageList.getRecords();
		for(RunningUutCatagory category : categoryList) {
			String departmentIds = category.getDepartmentId();
			if(departmentIds != null) {
				String[] departArray = departmentIds.split(",");
				QueryWrapper<SysDepart> sysDepartQueryWrapper = new QueryWrapper<SysDepart>();
				sysDepartQueryWrapper.in("id",departArray);
				List<SysDepart> departList = departService.list(sysDepartQueryWrapper);
				List<String> nameList = departList.stream().map(SysDepart::getDepartName).collect(Collectors.toList());
				category.setDepartmentNames(StringUtils.join(nameList.toArray(),","));
			}
		}
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningUutCatagoryPage
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-添加")
	@ApiOperation(value="被测对象流程分类表-添加", notes="被测对象流程分类表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutCatagoryPage runningUutCatagoryPage) {
		RunningUutCatagory runningUutCatagory = new RunningUutCatagory();
		BeanUtils.copyProperties(runningUutCatagoryPage, runningUutCatagory);
		return runningUutCatagoryService.saveMain(runningUutCatagory, runningUutCatagoryPage.getRunningUutNodeList());
	}
	
	/**
	 *  编辑
	 *
	 * @param runningUutCatagoryPage
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-编辑")
	@ApiOperation(value="被测对象流程分类表-编辑", notes="被测对象流程分类表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutCatagoryPage runningUutCatagoryPage) {
		RunningUutCatagory runningUutCatagory = new RunningUutCatagory();
		BeanUtils.copyProperties(runningUutCatagoryPage, runningUutCatagory);
		RunningUutCatagory runningUutCatagoryEntity = runningUutCatagoryService.getById(runningUutCatagory.getId());
		if(runningUutCatagoryEntity==null) {
			return Result.error("未找到对应数据");
		}
		return runningUutCatagoryService.updateMain(runningUutCatagory, runningUutCatagoryPage.getRunningUutNodeList());
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-通过id删除")
	@ApiOperation(value="被测对象流程分类表-通过id删除", notes="被测对象流程分类表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutCatagoryService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-批量删除")
	@ApiOperation(value="被测对象流程分类表-批量删除", notes="被测对象流程分类表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutCatagoryService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象流程分类表-通过id查询")
	@ApiOperation(value="被测对象流程分类表-通过id查询", notes="被测对象流程分类表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutCatagory runningUutCatagory = runningUutCatagoryService.getById(id);
		if(runningUutCatagory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutCatagory);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象流程节点表通过主表ID查询")
	@ApiOperation(value="被测对象流程节点表主表ID查询", notes="被测对象流程节点表-通主表ID查询")
	@GetMapping(value = "/queryRunningUutNodeByMainId")
	public Result<?> queryRunningUutNodeListByMainId(@RequestParam(name="id",required=true) String id) {
		List<RunningUutNode> runningUutNodeList = runningUutNodeService.selectByMainId(id);
		return Result.ok(runningUutNodeList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutCatagory
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutCatagory runningUutCatagory) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<RunningUutCatagory> queryWrapper = QueryGenerator.initQueryWrapper(runningUutCatagory, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<RunningUutCatagory> queryList = runningUutCatagoryService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<RunningUutCatagory> runningUutCatagoryList = new ArrayList<RunningUutCatagory>();
      if(oConvertUtils.isEmpty(selections)) {
          runningUutCatagoryList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          runningUutCatagoryList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<RunningUutCatagoryPage> pageList = new ArrayList<RunningUutCatagoryPage>();
      for (RunningUutCatagory main : runningUutCatagoryList) {
          RunningUutCatagoryPage vo = new RunningUutCatagoryPage();
          BeanUtils.copyProperties(main, vo);
          List<RunningUutNode> runningUutNodeList = runningUutNodeService.selectByMainId(main.getId());
          vo.setRunningUutNodeList(runningUutNodeList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "被测对象流程分类表列表");
      mv.addObject(NormalExcelConstants.CLASS, RunningUutCatagoryPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("被测对象流程分类表数据", "导出人:"+sysUser.getRealname(), "被测对象流程分类表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
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
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
	      // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<RunningUutCatagoryPage> list = ExcelImportUtil.importExcel(file.getInputStream(), RunningUutCatagoryPage.class, params);
              for (RunningUutCatagoryPage page : list) {
                  RunningUutCatagory po = new RunningUutCatagory();
                  BeanUtils.copyProperties(page, po);
                  runningUutCatagoryService.saveMain(po, page.getRunningUutNodeList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
    }

	 /**
	  * 查询流程节点列表，添加编辑节点弹窗初始化需要，查询时如果id有值不能查询自己的节点,最后得到select下拉框需要的数据源
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "查询流程节点列表")
	 @ApiOperation(value="查询流程节点列表", notes="查询流程节点列表")
	 @PostMapping(value = "/selectNodeListByIdAndMainId")
	 public Result<?> selectNodeListByIdAndMainId(@RequestBody RunningUutNode params) {
		 return Result.ok(runningUutNodeService.selectNodeListByIdAndMainId(params));
	 }

	 /**
	  * 更新节点数据
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "更新节点数据")
	 @ApiOperation(value="更新节点数据", notes="更新节点数据")
	 @PostMapping(value = "/updateNodeData")
	 public Result<?> updateNodeData(@RequestBody RunningUutNode params) {
		 return runningUutNodeService.updateNodeData(params);
	 }

	 /**
	  * 新增节点数据
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "新增节点数据")
	 @ApiOperation(value="新增节点数据", notes="新增节点数据")
	 @PostMapping(value = "/insertNodeData")
	 public Result<?> insertNodeData(@RequestBody RunningUutNode params) {
		 return runningUutNodeService.insertNodeData(params);
	 }

	 /**
	  * 删除节点数据
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "删除节点数据")
	 @ApiOperation(value="删除节点数据", notes="删除节点数据")
	 @PostMapping(value = "/deleteNodeData")
	 public Result<?> deleteNodeData(@RequestBody RunningUutNode params) {
		 return runningUutNodeService.deleteNodeData(params);
	 }

	 /**
	  * 删除临时节点数据,当新增流程时,如果创建了节点,但是页面中点击了取消,这时就需要删除主表id为null的节点数据
	  *
	  * @param
	  * @return
	  */
	 @AutoLog(value = "删除临时节点数据")
	 @ApiOperation(value="删除临时节点数据", notes="删除临时节点数据")
	 @PostMapping(value = "/deleteTempNodeData")
	 public Result<?> deleteTempNodeData(@RequestBody RunningUutNode params) {
		 return runningUutNodeService.deleteTempNodeData(params);
	 }
}
