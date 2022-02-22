package org.jeecg.modules.cloudtools.runex.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.modules.cloudtools.CaseType;
import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;
import org.jeecg.modules.cloudtools.runex.service.IRunCaseService;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;
import org.jeecg.modules.cloudtools.runex.service.IRunQueService;
import org.jeecg.modules.cloud.entity.ToolsControlList;
import org.jeecg.modules.cloud.service.IToolsControlListService;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.SocketHandler;
import org.jeecg.modules.project.service.IRunningProjectService;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: 记录xrun的用例
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Api(tags="记录xrun的用例")
@RestController
@RequestMapping("/xrunex/xrunCase")
@Slf4j
public class RunCaseController extends JeecgController<RunCase, IRunCaseService> {
	@Autowired
	private IRunCaseService runCaseService;
	@Autowired
	private IToolsControlListService toolControlService;
	@Autowired
	private IRunParamsSetService paramService;
	@Autowired
	private TaskDispatcher taskDispatcher;
	@Autowired
	private IRunQueService runQueService;
	@Value("${jeecg.minio.minio-url}")
	private String minioUrl;
	@Value("${jeecg.minio.bucket-system}")
	private String bucket;

	@Autowired
	private IRunningProjectService runningProjectService;
	@Autowired
	private IRunningTaskService runningTaskService;
	@Autowired
	private IRunningCaseService runningCaseService;

	 /**
	  *
	  * @param projectId 项目ID
	  * @return json
	  */
		@GetMapping("/getCaseTree")
	public JSONObject caseTree(String projectId){
		//项目
		JSONObject tree = new JSONObject();
		if(StringUtils.isEmpty(projectId)){
			return tree;
		}
		String projName = runningProjectService.getById(projectId).getProjectName();
		tree.put("title",projName);
		tree.put("key",projectId);
		//tree.put("disableCheckbox",true);
		//任务list
		QueryWrapper<RunningTask> taskWrapper = new QueryWrapper<>();
		taskWrapper.eq("del_flag","0").eq("project_id",projectId).eq("task_status",0);
		List<RunningTask> taskList = runningTaskService.list(taskWrapper);

		if(taskList!=null){

			List<JSONObject> children = taskList.stream().map((task)->{

				JSONObject json = new JSONObject();
				json.put("title",task.getTaskName());
				json.put("key",projectId.concat("-").concat(task.getId()));
				//json.put("disableCheckbox",true);
				//用例
				QueryWrapper<RunningCase> caseWrapper = new QueryWrapper<>();
				caseWrapper.eq("del_flag",0).eq("test_task_id",task.getId());
				List<RunningCase> caseList = runningCaseService.list(caseWrapper);
				List<JSONObject> caseJsonList = caseList.stream().map(cs->{
					JSONObject caseJson = new JSONObject();
					caseJson.put("title",cs.getTestName());
					caseJson.put("titleText",projName.concat("-").concat(task.getTaskName()).concat("-").concat(cs.getTestName()));
					caseJson.put("key",projectId.concat("-").concat(task.getId()).concat("-").concat(cs.getId()));
					JSONObject slot = new JSONObject();
					slot.put("customRender","key");
					slot.put("title","key");
					caseJson.put("scopedSlots",slot);
					return caseJson;
				}).collect(Collectors.toList());

				json.put("children",caseJsonList);

				return json;
			}).collect(Collectors.toList());

			tree.put("children",children);
		}

		return tree;
	}

	
	/**
	 * 分页列表查询
	 *
	 * @param runCase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-分页列表查询")
	@ApiOperation(value="记录xrun的用例-分页列表查询", notes="记录xrun的用例-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunCase runCase,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunCase> queryWrapper = QueryGenerator.initQueryWrapper(runCase, req.getParameterMap());
		queryWrapper.orderByDesc("update_time");
		Page<RunCase> page = new Page<RunCase>(pageNo, pageSize);
		IPage<RunCase> pageList = runCaseService.page(page, queryWrapper);
		
		//处理下
		List<RunCase> list = pageList.getRecords();
		pageList.setRecords(list.stream().map(run->run.stparamsDetail(minioUrl.concat("/").concat(bucket))).collect(Collectors.toList()));
		
		return Result.ok(pageList);
	}
	
	@GetMapping("count")
	public Integer count(String row,String val,String id) {
		QueryWrapper<RunCase> wrapper = new QueryWrapper<>();
		wrapper.eq(row, val);
		if(!StringUtils.isEmpty(id)) {
			wrapper.ne("id", id);
		}
		return runCaseService.count(wrapper);
	}
	
	@GetMapping(value = "/getHtml")
	public Result<?> getHtml(String toolName) {
		Result res= Result.ok();
		QueryWrapper<ToolsControlList> wrapper = new QueryWrapper<>();
		wrapper.eq("name",toolName);
		ToolsControlList tool = toolControlService.getOne(wrapper, false);
		//获取列表
		QueryWrapper<RunParamsSet> runParamsSetWrapper = new QueryWrapper<>();
		runParamsSetWrapper.eq("control_set_id", tool.getId()).orderByAsc("`order`");
		List<RunParamsSet> list =paramService.getParamsList(runParamsSetWrapper);
		res.setResult(list);
		return res;
	}
	
	/**
	 *   添加
	 *
	 * @param runCase
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-添加")
	@ApiOperation(value="记录xrun的用例-添加", notes="记录xrun的用例-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunCase runCase) {
		runCaseService.save(runCase);
		
		
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runCase
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-编辑")
	@ApiOperation(value="记录xrun的用例-编辑", notes="记录xrun的用例-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunCase runCase) {
		runCaseService.updateById(runCase);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-通过id删除")
	@ApiOperation(value="记录xrun的用例-通过id删除", notes="记录xrun的用例-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runCaseService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-批量删除")
	@ApiOperation(value="记录xrun的用例-批量删除", notes="记录xrun的用例-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runCaseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "记录xrun的用例-通过id查询")
	@ApiOperation(value="记录xrun的用例-通过id查询", notes="记录xrun的用例-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunCase runCase = runCaseService.getById(id);
		if(runCase==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runCase);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runCase
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunCase runCase) {
        return super.exportXls(request, runCase, RunCase.class, "记录xrun的用例");
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
        return super.importExcel(request, response, RunCase.class);
    }
    
    @PostMapping("/startTask")
    public Result<?> startTask(@RequestBody JSONObject param) {
    	String id = param.getString("id");
    	//获取用例
    	RunCase runCase = runCaseService.getById(id);
    	//处理用例参数
    	JSONObject json = JSONObject.parseObject(runCase.getParams());
    			 
		 //若队列中已存在不重复添加
		 QueryWrapper<RunQue> wrapper = new QueryWrapper<>();
		 wrapper.eq("case_id", id);
		 int count = runQueService.count(wrapper);
		 if(count == 0) {
			 //加入队列
			 RunQue que = new RunQue();
			 que.setCaseId(id);
			 que.setCaseName(runCase.getCaseName());
			 que.setClientIp(runCase.getClientIp());
			 que.setPriorityLevel(runCase.getPriorityLevel());
			 que.setProjectId(runCase.getProjectId()); // 项目ID
			 que.setTaskId(runCase.getTaskId()); // 任务ID
			 que.setCaseType(runCase.getCaseType());
			 runQueService.save(que);
			 runCase.setStatus(CommonConstant.QUEUING);
			 runCase.setStartTime(null);
			 runCase.setEndTime(null);
			 runCaseService.updateById(runCase);
			 
			 if(!SocketHandler.getSocketLock().containsKey(que.getClientIp())){
				 return Result.error("客户端不在线");
			 }else if(SocketHandler.getSocketLock().get(que.getClientIp()).get(CaseType.valueOf(runCase.getCaseType())) == 0) {
				 taskDispatcher.goNext(runCase.getClientIp(), CaseType.valueOf(runCase.getCaseType()));
			 }
		 }
    	
    	
    	return Result.ok();
    }

}
