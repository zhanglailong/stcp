package org.jeecg.modules.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocCaseService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocQueService;
import org.jeecg.modules.cloudtools.CaseType;
import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;
import org.jeecg.modules.cloud.entity.RunningCloudKlocQue;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.SocketHandler;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Description: kloc用例表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Api(tags="kloc用例表")
@RestController
@RequestMapping("/running/runningCloudKlocCase")
@Slf4j
public class RunningCloudKlocCaseController extends JeecgController<RunningCloudKlocCase, IRunningCloudKlocCaseService> {
	@Autowired
	private IRunningCloudKlocCaseService runningCloudKlocCaseService;
	
	@Autowired
	private IRunningCloudKlocQueService runningCloudKlocQueService;
	
	@Autowired
	private IRunningClientService runningClientService;


	
	@Autowired
	private TaskDispatcher taskDispatcher;

	/**
	 * 分页列表查询
	 *
	 * @param runningCloudKlocCase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "kloc用例表-分页列表查询")
	@ApiOperation(value="kloc用例表-分页列表查询", notes="kloc用例表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningCloudKlocCase runningCloudKlocCase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningCloudKlocCase> queryWrapper = QueryGenerator.initQueryWrapper(runningCloudKlocCase, req.getParameterMap());
		queryWrapper.orderByDesc("update_time");
		if(!StringUtils.isEmpty(runningCloudKlocCase.getStatus())) {
			queryWrapper.eq("status", runningCloudKlocCase.getStatus());
		}
		Page<RunningCloudKlocCase> page = new Page<RunningCloudKlocCase>(pageNo, pageSize);
		IPage<RunningCloudKlocCase> pageList = runningCloudKlocCaseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	@AutoLog(value = "kloc用例表-查询同名")
	@ApiOperation(value="kloc用例表-查询同名", notes="kloc用例表-查询同名")
	@GetMapping(value = "/count")
	public Result<?> count(@RequestParam(name="key")String key,@RequestParam(name="val")String val,
			@RequestParam(name="ip")String ip) {
		
		QueryWrapper<RunningCloudKlocCase> wrapper = new QueryWrapper<>();
		wrapper.eq(key, val);
		String projectName="project_name";
		int count = runningCloudKlocCaseService.count(wrapper);
		//projectName需要额外判断
		if(count==0 && projectName.equals(key)) {
			if(!StringUtils.isEmpty(ip)&& SocketHandler.getKlocNameMap().containsKey(ip)&&
					SocketHandler.getKlocNameMap().get(ip).contains(val)) {
				count = 1;
			}
		}
		
		
		return Result.ok(count);
	}
	
	/**
	 *   添加
	 *
	 * @param runningCloudKlocCase
	 * @return
	 */
	@AutoLog(value = "kloc用例表-添加")
	@ApiOperation(value="kloc用例表-添加", notes="kloc用例表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningCloudKlocCase runningCloudKlocCase) {
		//设置测试状态
		runningCloudKlocCase.setUpdateTime(new Date());
		runningCloudKlocCase.setStatus(CommonConstant.KLOCNORUNING);
		runningCloudKlocCaseService.save(runningCloudKlocCase);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningCloudKlocCase
	 * @return
	 */
	@AutoLog(value = "kloc用例表-编辑")
	@ApiOperation(value="kloc用例表-编辑", notes="kloc用例表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningCloudKlocCase runningCloudKlocCase) {
		String newName = runningCloudKlocCase.getProjectName();
		String name = runningCloudKlocCaseService.getById(runningCloudKlocCase.getId()).getProjectName();
		if(!name.equals(newName)) {
			taskDispatcher.changeKlocProjectName(runningCloudKlocCase.getClientIp(), name, newName);
		}
		runningCloudKlocCaseService.updateById(runningCloudKlocCase);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kloc用例表-通过id删除")
	@ApiOperation(value="kloc用例表-通过id删除", notes="kloc用例表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunningCloudKlocCase cases = runningCloudKlocCaseService.getById(id);
		String sign1="1";
		String sign0="0";
		if(sign0.equals(cases.getStatus())|| sign1.equals(cases.getStatus())) {
			return Result.error("当前项目正在执行/队列中，无法删除！");	
		}
		
		
		if (taskDispatcher.delKlocProject(cases.getClientIp(), cases.getProjectName())) {
			runningCloudKlocCaseService.removeById(id);
		} else {
			return Result.error("删除kloc服务端项目失败。请自行登录kloc服务端删除项目！");
		}
		return Result.ok("删除成功!");
	}
	
	@GetMapping(value = "/getlog")
	public Result<?> getlog(@RequestParam(name="id",required=true) String id) {
		RunningCloudKlocCase cases = runningCloudKlocCaseService.getById(id);
		return Result.ok(taskDispatcher.getKlocLog(cases.getClientIp(), id));
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
//	@AutoLog(value = "kloc用例表-批量删除")
//	@ApiOperation(value="kloc用例表-批量删除", notes="kloc用例表-批量删除")
//	@DeleteMapping(value = "/deleteBatch")
//	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
//		this.runningCloudKlocCaseService.removeByIds(Arrays.asList(ids.split(",")));
//		return Result.ok("批量删除成功!");
//	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kloc用例表-通过id查询")
	@ApiOperation(value="kloc用例表-通过id查询", notes="kloc用例表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningCloudKlocCase runningCloudKlocCase = runningCloudKlocCaseService.getById(id);
		if(runningCloudKlocCase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningCloudKlocCase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningCloudKlocCase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningCloudKlocCase runningCloudKlocCase) {
        return super.exportXls(request, runningCloudKlocCase, RunningCloudKlocCase.class, "kloc用例表");
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
        return super.importExcel(request, response, RunningCloudKlocCase.class);
    }

	 /**
	  * 执行测试用例
	  *
	  * @param id
	  * @return
	  */
	 @GetMapping(value = "/execute")
	 public Result<?> execute(@RequestParam(name="id") String id) {
		 
		 RunningCloudKlocCase runningCloudKlocCase = runningCloudKlocCaseService.getById(id);
		 
		 //若队列中已存在不重复添加
		 QueryWrapper<RunningCloudKlocQue> wrapper = new QueryWrapper<>();
		 wrapper.eq("kloc_case_id", id);
		 int count = runningCloudKlocQueService.count(wrapper);
		 if(count == 0) {
			 //加入队列
			 RunningCloudKlocQue que = new RunningCloudKlocQue();
			 que.setKlocCaseId(id);
			 que.setKlocCaseName(runningCloudKlocCase.getKlocCaseName());
			 que.setClientIp(runningCloudKlocCase.getClientIp());
			 que.setPriorityLevel(runningCloudKlocCase.getPriorityLevel());
			 runningCloudKlocQueService.save(que);
			 runningCloudKlocCase.setStatus(CommonConstant.QUEUING);
			 runningCloudKlocCase.setStartTime(null);
			 runningCloudKlocCase.setEndTime(null);
			 runningCloudKlocCaseService.updateById(runningCloudKlocCase);
			 
			 if(!SocketHandler.getSocketLock().containsKey(que.getClientIp())){
				 return Result.error("客户端不在线");
			 }else if(SocketHandler.getSocketLock().get(que.getClientIp()).get(CaseType.Kloc) == 0) {
				 taskDispatcher.goNext(runningCloudKlocCase.getClientIp(), CaseType.Kloc);
			 }
		 }
		 return Result.ok();
	 }

}
