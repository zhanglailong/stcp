package org.jeecg.modules.running.uut.controller;

import java.util.*;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import org.jeecg.modules.running.uut.entity.RunningUutFileListHistory;
import org.jeecg.modules.running.uut.entity.RunningUutListUser;
import org.jeecg.modules.running.uut.service.IRunningUutFileListHistoryService;
import org.jeecg.modules.running.uut.service.IRunningUutFileListService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.running.uut.service.IRunningUutListUserService;
import org.jeecg.modules.system.service.ISysUserService;
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
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 被测件附件更动表
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
@Api(tags="被测件附件更动表")
@RestController
@RequestMapping("/uut/runningUutFileList")
@Slf4j
public class RunningUutFileListController extends JeecgController<RunningUutFileList, IRunningUutFileListService> {
	@Autowired
	private IRunningUutFileListService runningUutFileListService;
	@Autowired
	private IRunningUutListUserService runningUutListUserService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IRunningUutFileListHistoryService runningUutFileListHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningUutFileList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-分页列表查询")
	@ApiOperation(value="被测件附件更动表-分页列表查询", notes="被测件附件更动表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutFileList runningUutFileList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   @RequestParam(name="uutListId", required = true) String uutListId,
								   HttpServletRequest req) {
		QueryWrapper<RunningUutFileList> queryWrapper = QueryGenerator.initQueryWrapper(runningUutFileList, req.getParameterMap());
		queryWrapper.eq("uut_list_id", uutListId);
		Page<RunningUutFileList> page = new Page<RunningUutFileList>(pageNo, pageSize);
		IPage<RunningUutFileList> pageList = runningUutFileListService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningUutFileList
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-添加")
	@ApiOperation(value="被测件附件更动表-添加", notes="被测件附件更动表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutFileList runningUutFileList) {
		runningUutFileListService.save(runningUutFileList);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningUutFileList
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-编辑")
	@ApiOperation(value="被测件附件更动表-编辑", notes="被测件附件更动表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutFileList runningUutFileList) {
		runningUutFileListService.updateById(runningUutFileList);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-通过id删除")
	@ApiOperation(value="被测件附件更动表-通过id删除", notes="被测件附件更动表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutFileListService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-批量删除")
	@ApiOperation(value="被测件附件更动表-批量删除", notes="被测件附件更动表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutFileListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测件附件更动表-通过id查询")
	@ApiOperation(value="被测件附件更动表-通过id查询", notes="被测件附件更动表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutFileList runningUutFileList = runningUutFileListService.getById(id);
		if(runningUutFileList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutFileList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutFileList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutFileList runningUutFileList) {
        return super.exportXls(request, runningUutFileList, RunningUutFileList.class, "被测件附件更动表");
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
        return super.importExcel(request, response, RunningUutFileList.class);
    }


	 /**
	  * 通过uutListId查询
	  *
	  * @param uutListId
	  * @return
	  */
	 @AutoLog(value = "被测件附件更动表-通过uutListId查询")
	 @ApiOperation(value="被测件附件更动表-通过uutListId查询", notes="被测件附件更动表-通过uutListId查询")
	 @GetMapping(value = "/queryByUutListId")
	 public Result<?> queryByUutListId(@RequestParam(name="uutListId",required=true) String uutListId) {
	 	List<RunningUutFileList> runningUutFileList = runningUutFileListService.getFileListByUutListId(uutListId);
		 if(runningUutFileList==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(runningUutFileList);
	 }

	 /**
	  * 判断当前登录人是否为人员范围内的成员
	  *
	  * @param id
	  * @return Boolean
	  */
	 @AutoLog(value = "人员范围表-通过uutId查询")
	 @ApiOperation(value="人员范围表-通过uutId查询", notes="人员范围表-通过uutId查询")
	 @GetMapping(value = "/checkIsUser")
	 public Boolean checkIsUser(@RequestParam(name="id",required=true) String id,
								@RequestParam(name = "userIdCookie",required = false) String userIdCookie) {
	 	 Boolean isUser = false;
	 	 int num = 0;
		 String loginUserId = sysUserService.getIdByUserName(userIdCookie);
		 List<RunningUutListUser> userList = runningUutListUserService.getUutUserById(id);
		 if( !userList.isEmpty() && userList.size() > 0 ){
			 for(RunningUutListUser runningUutFileList : userList){
				 if(runningUutFileList.getUserId().equals(loginUserId)){
				 	num+=1;
				 }
			 }
		 }
		 if(num != 0){
			 isUser = true;
		 }else {
			 isUser = false;
		 }
		 return isUser;
	 }

	 /**
	  * 通过uutListId获取uutVersion
	  *
	  * @param uutListId
	  * @return
	  */
	 @AutoLog(value = "通过uutListId获取uutVersion")
	 @ApiOperation(value = "通过uutListId获取uutVersion", notes = "通过uutListId获取uutVersion")
	 @GetMapping(value = "/getUutVersionByUutListId")
	 public Result<List<DictModel>> getUutVersionByUutListId(@RequestParam(name = "uutListId", required = false) String uutListId) {
		 Result<List<DictModel>> result = new Result<List<DictModel>>();
		 List<DictModel> ls = null;
		 try {
			 ls = runningUutFileListService.getUutVersionOptions(uutListId);
			 result.setSuccess(true);
			 result.setResult(ls);
		 } catch (Exception e) {
			 result.error500("操作失败");
			 return result;
		 }
		 return result;
	 }

	 /**
	  * 被测件附件更动履历表-通过uutListId和uutVersionId获取fileName
	  *
	  * @param uutListId
	  * @param uutVersionId
	  * @return
	  */
	 @AutoLog(value = "被测件附件更动履历表-通过uutListId和uutVersionId获取fileName")
	 @ApiOperation(value = "被测件附件更动履历表-通过uutListId和uutVersionId获取fileName", notes = "被测件附件更动履历表-通过uutListId和uutVersionId获取fileName")
	 @GetMapping(value = "/getFileByUutListIdAndUutVersionId")
	 public Result<?> getFileByUutListIdAndUutVersionId(@RequestParam(name = "uutListId", required = false) String uutListId,
														@RequestParam(name = "uutVersionId", required = false) String uutVersionId){
		List<String> uutFileList = new ArrayList<>();
		List<String> uutOtherFileList = new ArrayList<>();
	 	List<RunningUutFileListHistory> runningUutFileListHistoryLists = runningUutFileListHistoryService.getFileByUutListIdAndUutVersionId(uutListId, uutVersionId);
		 if(runningUutFileListHistoryLists.isEmpty()){
			 return Result.error("未找到对应数据");
		 }
	 	for(RunningUutFileListHistory runningUutFileListHistory : runningUutFileListHistoryLists){
	 		if(runningUutFileListHistory.getFileType().equals("被测件")){
				uutFileList.add(runningUutFileListHistory.getFileName());
			}else if(runningUutFileListHistory.getFileType().equals("其他")){
				uutOtherFileList.add(runningUutFileListHistory.getFileName());
			}
		}

	 	Map<String, Object> map = new HashMap<>();
	 	map.put("uutFile", String.join(",", uutFileList));
	 	map.put("uutOtherFile", String.join(",", uutOtherFileList));

	 	return Result.ok(map);
	 }





}
