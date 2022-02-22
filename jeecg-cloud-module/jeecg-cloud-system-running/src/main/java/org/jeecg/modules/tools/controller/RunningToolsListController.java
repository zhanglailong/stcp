package org.jeecg.modules.tools.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.io.IoUtil;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.client.entity.RunningClient;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.tools.entity.RunningToolsHistory;
import org.jeecg.modules.tools.entity.RunningToolsList;
import org.jeecg.modules.tools.service.IRunningToolsHistoryService;
import org.jeecg.modules.tools.service.IRunningToolsListService;
import org.jeecg.modules.util.FtpUtil;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试工具列表
 * @Author: jeecg-boot
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Api(tags="测试工具列表")
@RestController
@RequestMapping("/tools/runningToolsList")
@Slf4j
public class RunningToolsListController extends JeecgController<RunningToolsList, IRunningToolsListService> {
	@Autowired
	private IRunningToolsListService runningToolsListService;
	
	@Autowired
	private IRunningClientService runningClientService;

	@Autowired
	private FtpUtil ftpUtil;

	@Autowired
	private IRunningToolsHistoryService runningToolsHistoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningToolsList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试工具列表-分页列表查询")
	@ApiOperation(value="测试工具列表-分页列表查询", notes="测试工具列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningToolsList runningToolsList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningToolsList> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsList, req.getParameterMap());
		queryWrapper.eq("del_flag", CommonConstant.DEL_FLAG_0);
		Page<RunningToolsList> page = new Page<RunningToolsList>(pageNo, pageSize);
		IPage<RunningToolsList> pageList = runningToolsListService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	 @GetMapping(value = "/getOne")
	 public Result<?> queryPageList(RunningToolsList runningToolsList) {
		 return Result.ok(runningToolsListService.getById(runningToolsList.getId()));
	 }
	
	/**
	 *   添加
	 *
	 * @param runningToolsList
	 * @return
	 */
	@AutoLog(value = "测试工具列表-添加")
	@ApiOperation(value="测试工具列表-添加", notes="测试工具列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningToolsList runningToolsList) {
		runningToolsList.setDelFlag(CommonConstant.DEL_FLAG_0);
		runningToolsListService.save(runningToolsList);
		//保存操作历史
		saveRunningToolsHistory(runningToolsList,CommonConstant.OPERATE_TYPE_2);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningToolsList
	 * @return
	 */
	@AutoLog(value = "测试工具列表-编辑")
	@ApiOperation(value="测试工具列表-编辑", notes="测试工具列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningToolsList runningToolsList) {
		if(runningToolsList.getNeedMonitorOrNot() == null || runningToolsList.getNeedMonitorOrNot() == 0)
		{
			//如果是否需要监控设为否,文件地址无条件清空
			runningToolsList.setToolsFileFtpLocations("");
		}
		runningToolsListService.updateById(runningToolsList);
		
		//同时绑定到客户端表
		RunningClient rc = new RunningClient();
		rc.setToolId(runningToolsList.getId());
		rc.setToolName(runningToolsList.getToolsName());
		UpdateWrapper<RunningClient> uw = new UpdateWrapper<>();
		uw.eq("client_ip", runningToolsList.getToolsLocation());
		runningClientService.update(rc, uw);
		//保存操作历史
		saveRunningToolsHistory(runningToolsList,CommonConstant.OPERATE_TYPE_3);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具列表-通过id删除")
	@ApiOperation(value="测试工具列表-通过id删除", notes="测试工具列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningToolsListService.lambdaUpdate()
				.set(RunningToolsList::getDelFlag,CommonConstant.DEL_FLAG_1).eq(RunningToolsList::getId,id).update();
		//保存操作历史
		RunningToolsList runningToolsList = runningToolsListService.getById(id);
		saveRunningToolsHistory(runningToolsList,CommonConstant.OPERATE_TYPE_4);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试工具列表-批量删除")
	@ApiOperation(value="测试工具列表-批量删除", notes="测试工具列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		String info=",";
		for (String id : ids.split(info)) {
			runningToolsListService.lambdaUpdate()
					.set(RunningToolsList::getDelFlag,CommonConstant.DEL_FLAG_1).eq(RunningToolsList::getId,id).update();
			//保存操作历史
			RunningToolsList runningToolsList = runningToolsListService.getById(id);
			saveRunningToolsHistory(runningToolsList,CommonConstant.OPERATE_TYPE_4);
		}
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具列表-通过id查询")
	@ApiOperation(value="测试工具列表-通过id查询", notes="测试工具列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningToolsList runningToolsList = runningToolsListService.getById(id);
		if(runningToolsList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningToolsList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningToolsList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningToolsList runningToolsList) {
        return super.exportXls(request, runningToolsList, RunningToolsList.class, "测试工具列表");
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
        return super.importExcel(request, response, RunningToolsList.class);
    }

	 /**
	  * ftp文件下载
	  *
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value = "/ftpDownload", method = RequestMethod.GET)
	 public void ftpDownload(HttpServletRequest request, HttpServletResponse response,String fileName) {
		 try {
			 ftpUtil.downloadFileChop(fileName,response);
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }

	 /**
	  * 保存工具操作历史对象
	  */
	 private void saveRunningToolsHistory(RunningToolsList runningToolsList,Integer operation){
		 RunningToolsHistory runningToolsHistory = new RunningToolsHistory();
		 BeanUtils.copyProperties(runningToolsList,runningToolsHistory);
		 runningToolsHistory.setId(null);
		 runningToolsHistory.setOperationType(operation);
		 runningToolsHistory.setRunningToolsId(runningToolsList.getId());
		 runningToolsHistory.setCreateTime(new Date());
		 runningToolsHistory.setUpdateTime(new Date());

		 LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 runningToolsHistory.setUpdateBy(user.getId());
		 runningToolsHistoryService.save(runningToolsHistory);
	 }

 }
