package org.jeecg.modules.cloudtools.runex.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.cloudtools.CaseType;
import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;
import org.jeecg.modules.cloudtools.runex.service.IRunCaseService;
import org.jeecg.modules.cloudtools.runex.service.IRunQueService;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.SocketHandler;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Api(tags="队列表")
@RestController
@RequestMapping("/xrunex/runQue")
@Slf4j
public class RunQueController extends JeecgController<RunQue, IRunQueService> {
	@Autowired
	private IRunQueService runQueService;
	@Autowired
	private IRunCaseService caseService;
	@Autowired
	private TaskDispatcher taskDispatcher;

	public final static String NORUNING = "-1";
    public final static String QUE = "0";
    public final static String RUNING = "1";
    public final static String COMPLETE = "2";
    public final static String STOP = "3";
    public final static String ERRR = "401";
	
	
	/**
	 * 分页列表查询
	 *
	 * @param RunQue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "队列表-分页列表查询")
	@ApiOperation(value="队列表-分页列表查询", notes="队列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunQue RunQue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunQue> queryWrapper =QueryGenerator.initQueryWrapper(RunQue, req.getParameterMap());
		queryWrapper.orderByDesc("exec_step","priority_level","create_time");
		Page<RunQue> page = new Page<RunQue>(pageNo, pageSize);
		IPage<RunQue> pageList = runQueService.page(page, queryWrapper);
		if(SocketHandler.getSocketMap()!=null) {
			List<RunQue> res= pageList.getRecords().stream().map(item->
				item.setStateByClientStatus(SocketHandler.getSocketMap().keySet())).collect(Collectors.toList());
			pageList.setRecords(res);
		}
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param RunQue
	 * @return
	 */
	@AutoLog(value = "队列表-添加")
	@ApiOperation(value="队列表-添加", notes="队列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunQue RunQue) {
		runQueService.save(RunQue);
		return Result.ok("添加成功！");
	}
	
	/**
	 * 停止
	 */
	@AutoLog(value = "队列表-停止")
	@ApiOperation(value="队列表-停止", notes="队列表-停止")
	@GetMapping(value = "/stop")
	public Result<?> stop(String id) {
		RunQue que = runQueService.getById(id);
		RunCase runcase = caseService.getById(que.getCaseId());
		taskDispatcher.stop(que.getClientIp(), CaseType.valueOf(runcase.getCaseType()));
		return Result.ok("添加成功！");
	}
	
	/**
	 * 停止
	 */
	@AutoLog(value = "队列表-强制停止")
	@ApiOperation(value="队列表-强制停止", notes="队列表-强制停止")
	@GetMapping(value = "/forceStop")
	public Result<?> forceStop(String id,String caseType) {
		RunQue que = runQueService.getById(id);
		taskDispatcher.forceStop(que.getClientIp(), CaseType.valueOf(caseType));
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param RunQue
	 * @return
	 */
	@AutoLog(value = "队列表-编辑")
	@ApiOperation(value="队列表-编辑", notes="队列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunQue RunQue) {
		runQueService.updateById(RunQue);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "队列表-通过id删除")
	@ApiOperation(value="队列表-通过id删除", notes="队列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		RunQue que = runQueService.getById(id);
		RunCase cases = caseService.getById(que.getCaseId());
		cases.setStatus(NORUNING);
		caseService.updateById(cases);
		runQueService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "队列表-批量删除")
	@ApiOperation(value="队列表-批量删除", notes="队列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runQueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "队列表-通过id查询")
	@ApiOperation(value="队列表-通过id查询", notes="队列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunQue RunQue = runQueService.getById(id);
		if(RunQue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(RunQue);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param RunQue
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunQue RunQue) {
        return super.exportXls(request, RunQue, RunQue.class, "队列表");
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
        return super.importExcel(request, response, RunQue.class);
    }

}
