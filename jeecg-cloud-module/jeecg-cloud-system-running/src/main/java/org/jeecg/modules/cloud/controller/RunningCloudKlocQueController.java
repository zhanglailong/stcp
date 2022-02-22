package org.jeecg.modules.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: kloc队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Api(tags="kloc队列表")
@RestController
@RequestMapping("/running/runningCloudKlocQue")
@Slf4j
public class RunningCloudKlocQueController extends JeecgController<RunningCloudKlocQue, IRunningCloudKlocQueService> {
	@Autowired
	private IRunningCloudKlocQueService runningCloudKlocQueService;
	@Autowired
	private IRunningCloudKlocCaseService caseService;
	@Autowired
	private TaskDispatcher taskDispatcher;
	/**
	 * 分页列表查询
	 *
	 * @param runningCloudKlocQue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "kloc队列表-分页列表查询")
	@ApiOperation(value="kloc队列表-分页列表查询", notes="kloc队列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningCloudKlocQue runningCloudKlocQue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningCloudKlocQue> queryWrapper =QueryGenerator.initQueryWrapper(runningCloudKlocQue, req.getParameterMap());
		queryWrapper.orderByDesc("exec_step","priority_level","create_time");
		Page<RunningCloudKlocQue> page = new Page<RunningCloudKlocQue>(pageNo, pageSize);
		IPage<RunningCloudKlocQue> pageList = runningCloudKlocQueService.page(page, queryWrapper);
		if(SocketHandler.getSocketMap()!=null) {
			List<RunningCloudKlocQue> res= pageList.getRecords().stream().map(item->
				item.setStateByClientStatus(SocketHandler.getSocketMap().keySet())).collect(Collectors.toList());
			pageList.setRecords(res);
		}
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningCloudKlocQue
	 * @return
	 */
	@AutoLog(value = "kloc队列表-添加")
	@ApiOperation(value="kloc队列表-添加", notes="kloc队列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningCloudKlocQue runningCloudKlocQue) {
		runningCloudKlocQueService.save(runningCloudKlocQue);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 停止
	 */
	@AutoLog(value = "kloc队列表-停止")
	@ApiOperation(value="kloc队列表-停止", notes="kloc队列表-停止")
	@GetMapping(value = "/stop")
	public Result<?> stop(String id) {
		RunningCloudKlocQue que = runningCloudKlocQueService.getById(id);
		taskDispatcher.stop(que.getClientIp(), CaseType.Kloc);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 停止
	 */
	@AutoLog(value = "kloc队列表-强制停止")
	@ApiOperation(value="kloc队列表-强制停止", notes="kloc队列表-强制停止")
	@GetMapping(value = "/forceStop")
	public Result<?> forceStop(String id) {
		RunningCloudKlocQue que = runningCloudKlocQueService.getById(id);
		taskDispatcher.forceStop(que.getClientIp(), CaseType.Kloc);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningCloudKlocQue
	 * @return
	 */
	@AutoLog(value = "kloc队列表-编辑")
	@ApiOperation(value="kloc队列表-编辑", notes="kloc队列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningCloudKlocQue runningCloudKlocQue) {
		runningCloudKlocQueService.updateById(runningCloudKlocQue);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kloc队列表-通过id删除")
	@ApiOperation(value="kloc队列表-通过id删除", notes="kloc队列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunningCloudKlocQue que = runningCloudKlocQueService.getById(id);
		RunningCloudKlocCase kloccase = caseService.getById(que.getKlocCaseId());
		kloccase.setStatus(CommonConstant.KLOCNORUNING);
		caseService.updateById(kloccase);
		runningCloudKlocQueService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "kloc队列表-批量删除")
	@ApiOperation(value="kloc队列表-批量删除", notes="kloc队列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningCloudKlocQueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "kloc队列表-通过id查询")
	@ApiOperation(value="kloc队列表-通过id查询", notes="kloc队列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningCloudKlocQue runningCloudKlocQue = runningCloudKlocQueService.getById(id);
		if(runningCloudKlocQue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningCloudKlocQue);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningCloudKlocQue
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningCloudKlocQue runningCloudKlocQue) {
        return super.exportXls(request, runningCloudKlocQue, RunningCloudKlocQue.class, "kloc队列表");
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
        return super.importExcel(request, response, RunningCloudKlocQue.class);
    }

}
