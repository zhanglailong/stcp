package org.jeecg.modules.client.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.client.entity.RunningClient;
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
 * @Description: 云化工具客户端
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Api(tags="云化工具客户端")
@RestController
@RequestMapping("/client/runningClient")
@Slf4j
public class RunningClientController extends JeecgController<RunningClient, IRunningClientService> {
	@Autowired
	private IRunningClientService runningClientService;
	@Autowired
	private TaskDispatcher taskDispatcher;
	/**
	 * 分页列表查询
	 *
	 * @param runningClient
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-分页列表查询")
	@ApiOperation(value="云化工具客户端-分页列表查询", notes="云化工具客户端-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningClient runningClient,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningClient> queryWrapper = QueryGenerator.initQueryWrapper(runningClient, req.getParameterMap());
		Page<RunningClient> page = new Page<RunningClient>(pageNo, pageSize);
		IPage<RunningClient> pageList = runningClientService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	@AutoLog(value = "云化工具客户端-分页列表查询")
	@ApiOperation(value="云化工具客户端-分页列表查询", notes="云化工具客户端-分页列表查询")
	@GetMapping(value = "/select")
	public Result<?> list(HttpServletRequest req) {
		
		QueryWrapper<RunningClient> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("id","client_ip");
		java.util.List<RunningClient> list = runningClientService.list(queryWrapper);
		
		return Result.ok(list);
	}
	
	/**
	 *   添加
	 *
	 * @param runningClient
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-添加")
	@ApiOperation(value="云化工具客户端-添加", notes="云化工具客户端-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningClient runningClient) {
		runningClientService.save(runningClient);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningClient
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-编辑")
	@ApiOperation(value="云化工具客户端-编辑", notes="云化工具客户端-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningClient runningClient) {
		runningClientService.updateById(runningClient);
		String sign1="1";
		if(sign1.equals(runningClient.getClientState())&&
				"klocwork".equals(runningClient.getToolName())) {
			taskDispatcher.getKlocProjectNames(runningClient.getClientIp());
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-通过id删除")
	@ApiOperation(value="云化工具客户端-通过id删除", notes="云化工具客户端-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningClientService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-批量删除")
	@ApiOperation(value="云化工具客户端-批量删除", notes="云化工具客户端-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningClientService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "云化工具客户端-通过id查询")
	@ApiOperation(value="云化工具客户端-通过id查询", notes="云化工具客户端-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningClient runningClient = runningClientService.getById(id);
		if(runningClient==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningClient);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runningClient
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningClient runningClient) {
        return super.exportXls(request, runningClient, RunningClient.class, "云化工具客户端");
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
        return super.importExcel(request, response, RunningClient.class);
    }

}
