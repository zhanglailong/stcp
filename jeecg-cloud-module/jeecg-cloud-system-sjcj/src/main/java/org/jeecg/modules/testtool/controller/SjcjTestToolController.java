package org.jeecg.modules.testtool.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.testtool.entity.SjcjTestTool;
import org.jeecg.modules.testtool.service.ISjcjTestToolService;
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
 * @Description: 测试工具采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
@Api(tags = "测试工具采集配置表")
@RestController
@RequestMapping("/testtool/sjcjTestTool")
@Slf4j
public class SjcjTestToolController extends JeecgController<SjcjTestTool, ISjcjTestToolService> {
	@Autowired
	private ISjcjTestToolService sjcjTestToolService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjTestTool
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试工具采集配置表-分页列表查询")
	@ApiOperation(value = "测试工具采集配置表-分页列表查询", notes = "测试工具采集配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjTestTool sjcjTestTool,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjTestTool> queryWrapper = QueryGenerator.initQueryWrapper(sjcjTestTool, req.getParameterMap());
		Page<SjcjTestTool> page = new Page<SjcjTestTool>(pageNo, pageSize);
		IPage<SjcjTestTool> pageList = sjcjTestToolService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjTestTool
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "测试工具采集配置表-添加")
	@ApiOperation(value = "测试工具采集配置表-添加", notes = "测试工具采集配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Map<String, Object> params) {
		Map<String, String> map = (Map<String, String>) params.get("sjcjTestTool");
		// 根据测试工具ID获取其名称
		String testToolId = map.get("testToolId");
		SjcjTestTool sjcjTestTool = new SjcjTestTool();
		sjcjTestTool.setTestToolId(testToolId);
		sjcjTestTool.setTestToolName(sjcjTestToolService.getToolInfo(testToolId).get("tools_name"));
		sjcjTestTool.setTestToolCode(sjcjTestToolService.getToolInfo(testToolId).get("tools_code"));
		sjcjTestTool.setConfigCollectionPath(map.get("configCollectionPath"));
		sjcjTestTool.setConfigFileType(map.get("configFileType"));
		sjcjTestTool.setProcessCollectionPath(map.get("processCollectionPath"));
		sjcjTestTool.setProcessFileType(map.get("processFileType"));
		sjcjTestTool.setResultCollectionPath(map.get("resultCollectionPath"));
		sjcjTestTool.setResultFileType(map.get("resultFileType"));
		sjcjTestTool.setSjcjAgentBindCaseId((String) params.get("sjcjAgentBindCaseId"));
		sjcjTestTool.setCreateTime(new Date());
		sjcjTestToolService.save(sjcjTestTool);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjTestTool
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "测试工具采集配置表-编辑")
	@ApiOperation(value = "测试工具采集配置表-编辑", notes = "测试工具采集配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Map<String, Object> params) {
		Map<String, String> map = (Map<String, String>) params.get("sjcjTestTool");
		String testToolId = map.get("testToolId");
		SjcjTestTool sjcjTestTool = sjcjTestToolService.getById(map.get("id"));
		sjcjTestTool.setTestToolId(testToolId);
		sjcjTestTool.setTestToolName(sjcjTestToolService.getToolInfo(testToolId).get("tools_name"));
		sjcjTestTool.setTestToolCode(sjcjTestToolService.getToolInfo(testToolId).get("tools_code"));
		sjcjTestTool.setConfigCollectionPath(map.get("configCollectionPath"));
		sjcjTestTool.setConfigFileType(map.get("configFileType"));
		sjcjTestTool.setProcessCollectionPath(map.get("processCollectionPath"));
		sjcjTestTool.setProcessFileType(map.get("processFileType"));
		sjcjTestTool.setResultCollectionPath(map.get("resultCollectionPath"));
		sjcjTestTool.setResultFileType(map.get("resultFileType"));
		sjcjTestTool.setSjcjAgentBindCaseId(map.get("sjcjAgentBindCaseId"));
		sjcjTestTool.setUpdateTime(new Date());
		sjcjTestToolService.updateById(sjcjTestTool);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具采集配置表-通过id删除")
	@ApiOperation(value = "测试工具采集配置表-通过id删除", notes = "测试工具采集配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjTestToolService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试工具采集配置表-批量删除")
	@ApiOperation(value = "测试工具采集配置表-批量删除", notes = "测试工具采集配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjTestToolService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试工具采集配置表-通过id查询")
	@ApiOperation(value = "测试工具采集配置表-通过id查询", notes = "测试工具采集配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjTestTool sjcjTestTool = sjcjTestToolService.getById(id);
		if (sjcjTestTool == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjTestTool);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjTestTool
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjTestTool sjcjTestTool) {
		return super.exportXls(request, sjcjTestTool, SjcjTestTool.class, "测试工具采集配置表");
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
		return super.importExcel(request, response, SjcjTestTool.class);
	}

	/**
	 * 根据sjcjAgentBindCaseId及测试工具ID获取测试工具采集配置
	 */
	@RequestMapping(value = "/getTestTooolConfig", method = RequestMethod.GET)
	public Result<?> getTestTooolConfig(
			@RequestParam(name = "sjcjAgentBindCaseId", required = false) String sjcjAgentBindCaseId,
			@RequestParam(name = "toolId", required = false) String toolId) {
		return Result.ok(sjcjTestToolService.getTestTooolConfig(sjcjAgentBindCaseId, toolId));
	}

	/**
	 * 根据toolId获取其工具名称及其默认路径
	 */
	@GetMapping("/getToolInfo")
	public Result<?> getToolInfo(@RequestParam(name = "toolId", required = false) String toolId) {
		Map<String, String> map = sjcjTestToolService.getToolInfo(toolId);
		return Result.ok(map);
	}
}
