package org.jeecg.modules.sjcj.agentmanager.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.sjcj.agentmanager.entity.AgentManager;
import org.jeecg.modules.sjcj.agentmanager.service.IAgentManagerService;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SjcjWzjcjzl;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.service.ISjcjWzjcjzlService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 记录当前已开启的代理
 * @Author: jeecg-boot
 * @Date: 2021-01-06
 * @Version: V1.0
 */
@Api(tags = "记录当前已开启的代理")
@RestController
@RequestMapping("/agentmanager/agentManager")
@Slf4j
public class AgentManagerController extends JeecgController<AgentManager, IAgentManagerService> {
	@Autowired
	private IAgentManagerService agentManagerService;
	@Autowired
	private ISjcjWzjcjzlService sjcjWzjcjzlService;

	/**
	 * 分页列表查询
	 *
	 * @param agentManager
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-分页列表查询")
	@ApiOperation(value = "记录当前已开启的代理-分页列表查询", notes = "记录当前已开启的代理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(AgentManager agentManager,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<AgentManager> queryWrapper = QueryGenerator.initQueryWrapper(agentManager, req.getParameterMap());
		Page<AgentManager> page = new Page<AgentManager>(pageNo, pageSize);
		IPage<AgentManager> pageList = agentManagerService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param agentManager
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-添加")
	@ApiOperation(value = "记录当前已开启的代理-添加", notes = "记录当前已开启的代理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody AgentManager agentManager) {
		agentManagerService.save(agentManager);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param agentManager
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-编辑")
	@ApiOperation(value = "记录当前已开启的代理-编辑", notes = "记录当前已开启的代理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody AgentManager agentManager) {
		agentManagerService.updateById(agentManager);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-通过id删除")
	@ApiOperation(value = "记录当前已开启的代理-通过id删除", notes = "记录当前已开启的代理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		agentManagerService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-批量删除")
	@ApiOperation(value = "记录当前已开启的代理-批量删除", notes = "记录当前已开启的代理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.agentManagerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "记录当前已开启的代理-通过id查询")
	@ApiOperation(value = "记录当前已开启的代理-通过id查询", notes = "记录当前已开启的代理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		AgentManager agentManager = agentManagerService.getById(id);
		if (agentManager == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(agentManager);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param agentManager
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, AgentManager agentManager) {
		return super.exportXls(request, agentManager, AgentManager.class, "记录当前已开启的代理");
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
		return super.importExcel(request, response, AgentManager.class);
	}

	/**
	 * 下发任务
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/sendTask", method = RequestMethod.GET)
	public Result<?> sendTask(@RequestParam(name = "id", required = true) String id) {

		// 判断请求入参不能为空
		if (id == null || "".equals(id)) {
			return Result.error("请求参数不能为空");
		}
		try {
			// 通过id获取
			AgentManager agentManager = agentManagerService.getById(id);
			// 校验是否有效
			if (agentManager == null) {
				return Result.error("未找到对应数据");
			}

			// 通过配置id获取配置信息
			String configId = agentManager.getConfigId();
			SjcjWzjcjzl sjcjWzjcjzl = sjcjWzjcjzlService.getById(configId);
			agentManager.setToolId(sjcjWzjcjzl.getToolsId()); // 工具ID
			agentManager.setPzurl(sjcjWzjcjzl.getPzurl()); // 配置采集路径/自定义采集路径
			agentManager.setPzcjwjzl(sjcjWzjcjzl.getPzcjwjzl()); // 配置采集文件种类/自定义采集文件种类
			agentManager.setGcurl(sjcjWzjcjzl.getGcurl()); // 过程采集路径
			agentManager.setGccjwjzl(sjcjWzjcjzl.getGccjwjzl()); // 过程采集文件种类
			agentManager.setJgurl(sjcjWzjcjzl.getJgurl()); // 结果采集路径
			agentManager.setJgcjwjzl(sjcjWzjcjzl.getJgcjwjzl()); // 结果采集文件种类

			// 调用agent
			String am = JSONObject.toJSONString(agentManager);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("isStart", true);
			params.put("isConfig", true);
			params.put("agentManager", am);
			params.put("typeOfCollection", sjcjWzjcjzl.getTypeOfCollection()); // 采集类型
			String result = HttpUtil.doPost("http://" + agentManager.getAgentIp() + ":" + agentManager.getAgentPort()
					+ "/stcp-sjcj-agent/startTesting/startOrNot", params, null);
			if (result == null) {
				return Result.error("调用agent服务失败");
			}
			// 判断agent返回信息
			JSONObject json = JSONObject.parseObject(result);
			if (json.getString("code").equals("200")) {
				// 查询数据库
				agentManager = agentManagerService.getById(id);
				// 重新赋值
				agentManager.setPzurl(sjcjWzjcjzl.getPzurl());
				agentManager.setPzcjwjzl(sjcjWzjcjzl.getPzcjwjzl());
				agentManager.setGcurl(sjcjWzjcjzl.getGcurl());
				agentManager.setGccjwjzl(sjcjWzjcjzl.getGccjwjzl());
				agentManager.setJgurl(sjcjWzjcjzl.getJgurl());
				agentManager.setJgcjwjzl(sjcjWzjcjzl.getJgcjwjzl());

				// agent处理成功后更新状态
				if (agentManager.getRunningStatus().equals("1")) {
					agentManager.setRunningStatus("3");
					// 判断更新是否成功
					boolean updateFalg = agentManagerService.updateById(agentManager);
					// 成功返回数据
					if (updateFalg) {
						return Result.ok(agentManager);
					} else {
						// 失败返回响应
						return Result.error("更新任务失败");
					}
				} else if (agentManager.getRunningStatus().equals("2")) {
					return Result.ok(agentManager);
				} else {
					return Result.error("此任务已下发");
				}
			} else {
				return Result.error("调用agent服务，下发任务失败");
			}
		} catch (Exception e) {
			return Result.error("请求服务异常");
		}
	}

}
