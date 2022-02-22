package org.jeecg.modules.sjcj.screencapturemanagement.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.sjcj.agentmanager.entity.AgentManager;
import org.jeecg.modules.sjcj.agentmanager.service.IAgentManagerService;
import org.jeecg.modules.sjcj.dbdatamanagement.util.MinioUtil;
import org.jeecg.modules.sjcj.screencapturemanagement.entity.ScreenCaptureManagement;
import org.jeecg.modules.sjcj.screencapturemanagement.service.IScreenCaptureManagementService;
import org.jeecg.modules.util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
 * @Description: 截屏管理表
 * @Author: jeecg-boot
 * @Date: 2021-05-20
 * @Version: V1.0
 */
@Api(tags = "截屏管理表")
@RestController
@RequestMapping("/sjcj.screencapturemanagement/screenCaptureManagement")
@Slf4j
public class ScreenCaptureManagementController
		extends JeecgController<ScreenCaptureManagement, IScreenCaptureManagementService> {
	@Autowired
	private IScreenCaptureManagementService screenCaptureManagementService;
	@Autowired
	private MinioUtil minioUtil;
	@Autowired
	private IAgentManagerService agentManagerService;

	/**
	 * 分页列表查询
	 *
	 * @param screenCaptureManagement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "截屏管理表-分页列表查询")
	@ApiOperation(value = "截屏管理表-分页列表查询", notes = "截屏管理表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ScreenCaptureManagement screenCaptureManagement,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<ScreenCaptureManagement> queryWrapper = QueryGenerator.initQueryWrapper(screenCaptureManagement,
				req.getParameterMap());
		Page<ScreenCaptureManagement> page = new Page<ScreenCaptureManagement>(pageNo, pageSize);
		IPage<ScreenCaptureManagement> pageList = screenCaptureManagementService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param screenCaptureManagement
	 * @return
	 */
	@AutoLog(value = "截屏管理表-添加")
	@ApiOperation(value = "截屏管理表-添加", notes = "截屏管理表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ScreenCaptureManagement screenCaptureManagement) {
		screenCaptureManagementService.save(screenCaptureManagement);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param screenCaptureManagement
	 * @return
	 */
	@AutoLog(value = "截屏管理表-编辑")
	@ApiOperation(value = "截屏管理表-编辑", notes = "截屏管理表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ScreenCaptureManagement screenCaptureManagement) {
		screenCaptureManagementService.updateById(screenCaptureManagement);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "截屏管理表-通过id删除")
	@ApiOperation(value = "截屏管理表-通过id删除", notes = "截屏管理表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		screenCaptureManagementService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "截屏管理表-批量删除")
	@ApiOperation(value = "截屏管理表-批量删除", notes = "截屏管理表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.screenCaptureManagementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "截屏管理表-通过id查询")
	@ApiOperation(value = "截屏管理表-通过id查询", notes = "截屏管理表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		ScreenCaptureManagement screenCaptureManagement = screenCaptureManagementService.getById(id);
		if (screenCaptureManagement == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(screenCaptureManagement);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param screenCaptureManagement
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, ScreenCaptureManagement screenCaptureManagement) {
		return super.exportXls(request, screenCaptureManagement, ScreenCaptureManagement.class, "截屏管理表");
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
		return super.importExcel(request, response, ScreenCaptureManagement.class);
	}

	/**
	 * 截屏
	 */
	@RequestMapping(value = "/screenCapture", method = RequestMethod.GET)
	public Result<?> screenCapture(@RequestParam(name = "id", required = true) String id) {
		// 判断请求入参不能为空
		if (null == id || "".equals(id)) {
			return Result.error("请求参数不能为空");
		}
		// 通过id获取ScreenCaptureManagement
		AgentManager agentManager = agentManagerService.getById(id);
		// 校验是否有效
		if (null == agentManager) {
			return Result.error("未找到对应数据");
		}
		String agentIp = agentManager.getAgentIp();
		String agentPort = agentManager.getAgentPort();
		if (!StringUtils.isEmpty(agentIp) && !StringUtils.isEmpty(agentPort)) {
			Map<String, Object> params = new HashMap<>(2000);
			String agentManagerFormat = JSONObject.toJSONString(agentManager);
			params.put("agentManager", agentManagerFormat);
			params.put("minioUrl",
					minioUtil.getMinio().get("minio_url") + "/minio/" + minioUtil.getMinio().get("bucketName"));
			HttpUtil.doPost("http://" + agentIp + ":" + agentPort + "/stcp-sjcj-agent/screen/capture", params, null);
			return Result.ok("截屏成功");
		} else {
			return Result.error("截屏失败");
		}
	}

}
