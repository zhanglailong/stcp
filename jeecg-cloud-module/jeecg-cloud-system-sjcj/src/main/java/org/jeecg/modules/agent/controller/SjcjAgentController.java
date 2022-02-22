package org.jeecg.modules.agent.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.agent.entity.SjcjAgent;
import org.jeecg.modules.agent.service.ISjcjAgentService;
import org.jeecg.modules.running.entity.RunningProject;
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

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "采集客户端表")
@RestController
@RequestMapping("/agent/sjcjAgent")
@Slf4j
public class SjcjAgentController extends JeecgController<SjcjAgent, ISjcjAgentService> {
	@Autowired
	private ISjcjAgentService sjcjAgentService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjAgent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "采集客户端表-分页列表查询")
	@ApiOperation(value = "采集客户端表-分页列表查询", notes = "采集客户端表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjAgent sjcjAgent,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req,
			@RequestParam(name = "projectIdCookie", required = false) String projectIdCookie) {
		Page<SjcjAgent> pageList = new Page<SjcjAgent>(pageNo, pageSize);
		if (!StringUtils.isEmpty(projectIdCookie)) {
			pageList = sjcjAgentService.queryPageList(pageList, projectIdCookie, sjcjAgent);
		}
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjAgent
	 * @return
	 */
	@AutoLog(value = "采集客户端表-添加")
	@ApiOperation(value = "采集客户端表-添加", notes = "采集客户端表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjAgent sjcjAgent) {
		sjcjAgentService.save(sjcjAgent);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjAgent
	 * @return
	 */
	@AutoLog(value = "采集客户端表-编辑")
	@ApiOperation(value = "采集客户端表-编辑", notes = "采集客户端表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjAgent sjcjAgent) {
		sjcjAgentService.updateById(sjcjAgent);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集客户端表-通过id删除")
	@ApiOperation(value = "采集客户端表-通过id删除", notes = "采集客户端表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjAgentService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采集客户端表-批量删除")
	@ApiOperation(value = "采集客户端表-批量删除", notes = "采集客户端表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjAgentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采集客户端表-通过id查询")
	@ApiOperation(value = "采集客户端表-通过id查询", notes = "采集客户端表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjAgent sjcjAgent = sjcjAgentService.getById(id);
		if (sjcjAgent == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjAgent);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjAgent
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjAgent sjcjAgent) {
		return super.exportXls(request, sjcjAgent, SjcjAgent.class, "采集客户端表");
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
		return super.importExcel(request, response, SjcjAgent.class);
	}

	/**
	 * 树形数据
	 */
	@GetMapping("/getTree")
	public Result<?> getTree(@RequestParam(name = "projectIdCookie", required = false) String projectIdCookie) {
		List<RunningProject> proTaskList = new ArrayList<>();
		if (!StringUtils.isEmpty(projectIdCookie)) {
			proTaskList = sjcjAgentService.getTreeByProjectId(projectIdCookie);
		}
		return Result.ok(proTaskList);
	}

	/**
	 * 更改客户端状态
	 */
	@PostMapping(value = "/changeAgentStatus")
	public JSONObject changeAgentStatus(@RequestBody Map<String, Object> params) {
		JSONObject jsonObject = new JSONObject();
		List<String> environmentIdList = (List<String>)params.get("environmentIdList");
		List<String> agentIpList = (List<String>)params.get("agentIpList");
		String agentStatus = (String)params.get("agentStatus");
		try {
			// 若环境ID列表和客户端IP均为空
			if (environmentIdList.size() <= 0 && agentIpList.size() <= 0) {
				jsonObject.put("code", 400);
				jsonObject.put("success", false);
				jsonObject.put("message", "环境ID列表与客户端IP列表不能均为空，至少有一项非空！");
				return jsonObject;
			}
			// 若客户端状态为空
			if (StringUtils.isEmpty(agentStatus)) {
				jsonObject.put("code", 400);
				jsonObject.put("success", false);
				jsonObject.put("message", "客户端状态不能为空！");
				return jsonObject;
			}
			// 按环境ID更改客户端状态
			if (environmentIdList.size() > 0) {
				for (String environmentId : environmentIdList) {
					sjcjAgentService.changeAgentStatusByEnvironmentId(environmentId, agentStatus);
				}
			}
			// 按客户端IP更改客户端状态
			if (agentIpList.size() > 0) {
				for (String agentIp : agentIpList) {
					sjcjAgentService.changeAgentStatusByAgentIp(agentIp, agentStatus);
				}
			}
			jsonObject.put("code", 200);
			jsonObject.put("success", true);
			jsonObject.put("message", "已成功修改客户端状态！");
			return jsonObject;
		} catch (Exception e) {
			jsonObject.put("code", 500);
			jsonObject.put("success", false);
			jsonObject.put("message", e.getMessage());
			return jsonObject;
		}
	}

	/**
	 * 下拉选层级列表
	 */
	@GetMapping("/getOptionByCondition")
	public Result<List<DictModel>> getOptionByCondition(
			@RequestParam(name = "projectId", required = false) String projectId,
			@RequestParam(name = "turnId", required = false) String turnId,
			@RequestParam(name = "testTypeId", required = false) String testTypeId,
			@RequestParam(name = "taskId", required = false) String taskId) {
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			ls = sjcjAgentService.getOptionByCondition(projectId, turnId, testTypeId, taskId);
			result.setSuccess(true);
			result.setResult(ls);
		} catch (Exception e) {
			result.error500("操作失败");
			return result;
		}
		return result;
	}

}
