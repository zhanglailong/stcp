package org.jeecg.modules.running.monitortools.controller;

import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.running.monitortools.entity.RunningToolsAndVm;
import org.jeecg.modules.running.monitortools.service.IRunningToolsAndVmService;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.testtooldistribute.fegin.SocketServerFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试工具运行监控
 * @Author: jeecg-boot
 * @Date:   2021-07-15
 * @Version: V1.0
 */
@Api(tags="测试工具运行监控")
@RestController
@RequestMapping("/monitortools/runningToolsAndVm")
@Slf4j
public class RunningToolsAndVmController extends JeecgController<RunningToolsAndVm, IRunningToolsAndVmService> {
	@Autowired
	private IRunningToolsAndVmService runningToolsAndVmService;
	@Resource
	private SocketServerFegin socketServerFegin;
	@Value(value = "${testtool.monitorTestToolsIp}")
	public String testTools;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningToolsAndVm
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-分页列表查询")
	@ApiOperation(value="测试工具运行监控-分页列表查询", notes="测试工具运行监控-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningToolsAndVm runningToolsAndVm,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningToolsAndVm> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsAndVm, req.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
		Page<RunningToolsAndVm> page = new Page<RunningToolsAndVm>(pageNo, pageSize);
		IPage<RunningToolsAndVm> pageList = runningToolsAndVmService.page(page, queryWrapper);
		List<RunningToolsAndVm> runningToolsAndVms = pageList.getRecords();
		runningToolsAndVms.forEach(t -> {
			Result<?> result = socketServerFegin.sendMsg(testTools, t.getId(), CommonConstant.SOCKET_REGISTER_CODE_200, null, null, t.getToolsCode(), CommonConstant.DATA_INT_4, t.getToolProcessName(), t.getId(), t.getToolsPort(), t.getToolLinuxProcessName());
			log.info("查询进程开始 >>>>>>>" + result);
		});
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param runningToolsAndVm
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-添加")
	@ApiOperation(value="测试工具运行监控-添加", notes="测试工具运行监控-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningToolsAndVm runningToolsAndVm) {
		runningToolsAndVmService.save(runningToolsAndVm);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningToolsAndVm
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-编辑")
	@ApiOperation(value="测试工具运行监控-编辑", notes="测试工具运行监控-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningToolsAndVm runningToolsAndVm) {
		runningToolsAndVmService.updateById(runningToolsAndVm);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-通过id删除")
	@ApiOperation(value="测试工具运行监控-通过id删除", notes="测试工具运行监控-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningToolsAndVmService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-批量删除")
	@ApiOperation(value="测试工具运行监控-批量删除", notes="测试工具运行监控-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningToolsAndVmService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具运行监控-通过id查询")
	@ApiOperation(value="测试工具运行监控-通过id查询", notes="测试工具运行监控-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningToolsAndVm runningToolsAndVm = runningToolsAndVmService.getById(id);
		if(runningToolsAndVm==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(runningToolsAndVm);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningToolsAndVm
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningToolsAndVm runningToolsAndVm) {
        return super.exportXls(request, runningToolsAndVm, RunningToolsAndVm.class, "测试工具运行监控");
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
        return super.importExcel(request, response, RunningToolsAndVm.class);
    }

}
