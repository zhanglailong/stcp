package org.jeecg.modules.collectionconfig.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.agent.service.ISjcjAgentService;
import org.jeecg.modules.collectionconfig.entity.SjcjCollectionConfig;
import org.jeecg.modules.collectionconfig.entity.SysDictItem;
import org.jeecg.modules.collectionconfig.service.ISjcjCollectionConfigService;
import org.jeecg.modules.dbdata.entity.SjcjDbData;
import org.jeecg.modules.dbdata.service.ISjcjDbDataService;
import org.jeecg.modules.networkmonitoring.entity.SjcjNetworkMonitoring;
import org.jeecg.modules.networkmonitoring.service.ISjcjNetworkMonitoringService;
import org.jeecg.modules.testtool.entity.SjcjTestTool;
import org.jeecg.modules.testtool.service.ISjcjTestToolService;
import org.jeecg.modules.util.HttpUtil;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 文件采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
@Api(tags = "文件采集配置表")
@RestController
@RequestMapping("/collectionconfig/sjcjCollectionConfig")
@Slf4j
public class SjcjCollectionConfigController
		extends JeecgController<SjcjCollectionConfig, ISjcjCollectionConfigService> {
	@Autowired
	private ISjcjCollectionConfigService sjcjCollectionConfigService;
	@Autowired
	private ISjcjAgentService sjcjAgentService;
	@Autowired
	private ISjcjTestToolService sjcjTestToolService;
	@Autowired
	private ISjcjNetworkMonitoringService sjcjNetworkMonitoringService;
	@Autowired
	private ISjcjDbDataService sjcjDbDataService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjCollectionConfig
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "文件采集配置表-分页列表查询")
	@ApiOperation(value = "文件采集配置表-分页列表查询", notes = "文件采集配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjCollectionConfig sjcjCollectionConfig,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjCollectionConfig> queryWrapper = QueryGenerator.initQueryWrapper(sjcjCollectionConfig,
				req.getParameterMap());
		Page<SjcjCollectionConfig> page = new Page<SjcjCollectionConfig>(pageNo, pageSize);
		IPage<SjcjCollectionConfig> pageList = sjcjCollectionConfigService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjCollectionConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "文件采集配置表-添加")
	@ApiOperation(value = "文件采集配置表-添加", notes = "文件采集配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody JSONObject json) {
		String sjcjAgentBindCaseId = json.getString("sjcjAgentBindCaseId"); // `sjcj_agent_bind_case`表ID
		String typeOfCollection = json.getString("typeOfCollection"); // 采集类型
		typeOfCollection = StringUtils.deleteWhitespace(typeOfCollection.substring(1, typeOfCollection.length() - 1));
		SjcjCollectionConfig sjcjCollectionConfig = new SjcjCollectionConfig();
		sjcjCollectionConfig.setCollectionDescription(json.getString("collectionDescription")); // 采集描述
		sjcjCollectionConfig.setTypeOfCollection(typeOfCollection); // 采集类型
		sjcjCollectionConfig.setSjcjAgentBindCaseId(sjcjAgentBindCaseId); // `sjcj_agent_bind_case`表ID
		if (typeOfCollection.contains("testTool")) { // 采集类型为测试工具
			String toolId = ""; // 测试工具ID
			String sjcjTestToolId = ""; // `sjcj_test_tool`表ID
			List<Map<String, String>> testToolList = (List<Map<String, String>>) json.get("testToolList");
			if (testToolList.size() > 0) {
				for (Map<String, String> map : testToolList) {
					SjcjTestTool sjcjTestTool = sjcjTestToolService.getTestTooolConfig(sjcjAgentBindCaseId,
							map.get("testToolId"));
					toolId += map.get("testToolId") + ",";
					if (sjcjTestTool != null) {
						sjcjTestToolId += sjcjTestTool.getId() + ",";
					}
				}
				if (StringUtils.isEmpty(toolId)) {
					sjcjCollectionConfig.setToolId(toolId); // 测试工具ID
				} else {
					sjcjCollectionConfig.setToolId(toolId.substring(0, toolId.length() - 1)); // 测试工具ID
				}
				if (StringUtil.isEmpty(sjcjTestToolId)) {
					sjcjCollectionConfig.setSjcjTestToolId(sjcjTestToolId); // `sjcj_test_tool`表ID
				} else {
					sjcjCollectionConfig.setSjcjTestToolId(sjcjTestToolId.substring(0, sjcjTestToolId.length() - 1)); // `sjcj_test_tool`表ID
				}
			}
		}
		if (typeOfCollection.contains("measuredPiece")) { // 采集类型为被测件
			sjcjCollectionConfig.setUutCollectionPath(json.getString("uutCollectionPath")); // 被测件_自定义采集路径
			sjcjCollectionConfig.setUutFileType(json.getString("uutFileType")); // 被测件_自定义采集文件种类
		}
		if (typeOfCollection.contains("other")) { // 采集类型为其他
			sjcjCollectionConfig.setOtherCollectionPath(json.getString("otherCollectionPath")); // 其他_自定义采集路径
			sjcjCollectionConfig.setOtherFileType(json.getString("otherFileType")); // 其他_自定义采集文件种类
		}
		if (typeOfCollection.contains("networkMonitoring")) { // 采集类型为网络监听
			String sjcjNetworkMonitoringId = ""; // `sjcj_network_monitoring`表ID
			// 向`sjcj_network_monitoring`表插入记录
			List<Map<String, String>> nmList = (List<Map<String, String>>) json.get("nmList");
			if (nmList.size() > 0) {
				for (Map<String, String> map : nmList) {
					SjcjNetworkMonitoring sjcjNetworkMonitoring = new SjcjNetworkMonitoring();
					String nmIp = map.get("nmIp");
					String nmPort = String.valueOf(map.get("nmPort"));
					nmPort = StringUtils.deleteWhitespace(nmPort.substring(1, nmPort.length() - 1));
					if ("".equals(nmPort)) {
						nmPort = null;
					}
					sjcjNetworkMonitoring.setNmIp(nmIp);
					sjcjNetworkMonitoring.setNmPort(nmPort);
					sjcjNetworkMonitoring.setSjcjAgentBindCaseId(sjcjAgentBindCaseId);
					sjcjNetworkMonitoringService.save(sjcjNetworkMonitoring);
					sjcjNetworkMonitoringId += sjcjNetworkMonitoring.getId() + ",";
				}
				sjcjCollectionConfig.setSjcjNetworkMonitoringId(
						sjcjNetworkMonitoringId.substring(0, sjcjNetworkMonitoringId.length() - 1));
				// 将网络监听配置发送至stcp-sjcj-agent, 根据配置开始进行网络监听
//				Map<String, Object> models = new HashMap<String, Object>();
//				models.put("nmList", JSON.toJSON(nmList));
//				SjcjAgent sjcjAgent = sjcjAgentService.getSjcjAgentByCondition(sjcjAgentBindCaseId);
//				if (null != sjcjAgent) {
//					String agentIp = sjcjAgent.getAgentIp();
//					String agentPort = sjcjAgent.getAgentPort();
//					if (!StringUtils.isEmpty(agentIp) && !StringUtils.isEmpty(agentPort)) {
//						HttpUtil.doPost("http://" + agentIp + ":" + agentPort
//								+ "/stcp-sjcj-agent/dataCollection/startListening", models, null);
//					}
//				}
			}
		}
		if (typeOfCollection.contains("dbData")) { // 采集类型为数据库数据
			String sjcjDbDataId = "";
			List<Map<String, String>> dbDataList = (List<Map<String, String>>) json.get("dbDataList");
			for (Map<String, String> map : dbDataList) {
				if (!StringUtils.isEmpty(map.get("id"))) {
					sjcjDbDataId += map.get("id") + ",";
				}
			}
			sjcjCollectionConfig.setSjcjDbDataId(sjcjDbDataId.substring(0, sjcjDbDataId.length() - 1)); // `sjcj_db_data`表ID
		}
		sjcjCollectionConfigService.save(sjcjCollectionConfig);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjCollectionConfig
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "文件采集配置表-编辑")
	@ApiOperation(value = "文件采集配置表-编辑", notes = "文件采集配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody JSONObject json) {
		Date date = new Date();
		String id = json.getString("id");
		String sjcjAgentBindCaseId = json.getString("sjcjAgentBindCaseId");
		// 新的采集类型列表
		String typeOfCollection = json.getString("typeOfCollection");
		typeOfCollection = StringUtils.deleteWhitespace(typeOfCollection.substring(1, typeOfCollection.length() - 1));

		// 新的采集配置对象
		SjcjCollectionConfig sjcjCollectionConfig = sjcjCollectionConfigService.getById(id);
		sjcjCollectionConfig.setCollectionDescription(json.getString("collectionDescription")); // 采集描述
		sjcjCollectionConfig.setTypeOfCollection(typeOfCollection); // 采集类型列表

		if (typeOfCollection.contains("testTool")) { // 若新的采集类型列表包含测试工具
			String toolId = ""; // 测试工具ID
			String sjcjTestToolId = ""; // `sjcj_test_tool`表ID
			List<Map<String, String>> testToolList = (List<Map<String, String>>) json.get("testToolList");
			if (testToolList.size() > 0) {
				for (Map<String, String> map : testToolList) {
					SjcjTestTool sjcjTestTool = sjcjTestToolService.getTestTooolConfig(sjcjAgentBindCaseId,
							map.get("testToolId"));
					toolId += map.get("testToolId") + ",";
					if (sjcjTestTool != null) {
						sjcjTestToolId += sjcjTestTool.getId() + ",";
					}
				}
				sjcjCollectionConfig.setToolId(toolId.substring(0, toolId.length() - 1)); // 测试工具ID
				if (!StringUtil.isEmpty(sjcjTestToolId)) {
					sjcjCollectionConfig.setSjcjTestToolId(sjcjTestToolId.substring(0, sjcjTestToolId.length() - 1)); // `sjcj_test_tool`表ID
				} else {
					sjcjCollectionConfig.setSjcjTestToolId(sjcjTestToolId); // `sjcj_test_tool`表ID
				}
			}
		} else { // 若新的采集类型列表不包含测试工具
//			String sjcjTestToolIdStr = json.getString("sjcjTestToolId");
//			if (!StringUtils.isEmpty(sjcjTestToolIdStr)) {
//				String[] sjcjTestToolIds = sjcjTestToolIdStr.split(",");
//				if (sjcjTestToolIds.length > 0) {
//					for (String temp : sjcjTestToolIds) {
//						sjcjTestToolService.removeById(temp);
//					}
//				}
//				sjcjCollectionConfig.setSjcjTestToolId("");
//			}
			// 删除其测试工具采集配置
			QueryWrapper<SjcjTestTool> queryWrapper = QueryGenerator.initQueryWrapper(new SjcjTestTool(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			sjcjTestToolService.remove(queryWrapper);
			sjcjCollectionConfig.setToolId("");
			sjcjCollectionConfig.setSjcjTestToolId("");
		}

		if (typeOfCollection.contains("measuredPiece")) { // 若新的采集类型列表包含被测件
			sjcjCollectionConfig.setUutCollectionPath(json.getString("uutCollectionPath")); // 被测件_自定义采集路径
			sjcjCollectionConfig.setUutFileType(json.getString("uutFileType")); // 被测件_自定义采集文件种类
		} else { // 若新的采集类型列表不包含被测件
			sjcjCollectionConfig.setUutCollectionPath(""); // 被测件_自定义采集路径
			sjcjCollectionConfig.setUutFileType(""); // 被测件_自定义采集文件种类
		}

		if (typeOfCollection.contains("other")) { // 若新的采集类型列表包含其他
			sjcjCollectionConfig.setOtherCollectionPath(json.getString("otherCollectionPath")); // 其他_自定义采集路径
			sjcjCollectionConfig.setOtherFileType(json.getString("otherFileType")); // 其他_自定义采集文件种类
		} else { // 若新的采集类型列表包含其他
			sjcjCollectionConfig.setOtherCollectionPath(""); // 其他_自定义采集路径
			sjcjCollectionConfig.setOtherFileType(""); // 其他_自定义采集文件种类
		}

		if (typeOfCollection.contains("networkMonitoring")) { // 若新的采集类型列表包含网络数据监听
			QueryWrapper<SjcjNetworkMonitoring> queryWrapper = QueryGenerator
					.initQueryWrapper(new SjcjNetworkMonitoring(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			sjcjNetworkMonitoringService.remove(queryWrapper);
			String sjcjNetworkMonitoringId = ""; // `sjcj_network_monitoring`表ID
			// 向`sjcj_network_monitoring`表插入记录
			List<Map<String, String>> nmList = (List<Map<String, String>>) json.get("nmList");
			if (nmList.size() > 0) {
				for (Map<String, String> map : nmList) {
					SjcjNetworkMonitoring sjcjNetworkMonitoring = new SjcjNetworkMonitoring();
					String nmIp = map.get("nmIp");
					String nmPort = String.valueOf(map.get("nmPort"));
					nmPort = StringUtils.deleteWhitespace(nmPort.substring(1, nmPort.length() - 1));
					if ("".equals(nmPort)) {
						nmPort = null;
					}
					sjcjNetworkMonitoring.setNmIp(nmIp);
					sjcjNetworkMonitoring.setNmPort(nmPort);
					sjcjNetworkMonitoring.setSjcjAgentBindCaseId(sjcjAgentBindCaseId);
					sjcjNetworkMonitoringService.save(sjcjNetworkMonitoring);
					sjcjNetworkMonitoringId += sjcjNetworkMonitoring.getId() + ",";
				}
				sjcjCollectionConfig.setSjcjNetworkMonitoringId(
						sjcjNetworkMonitoringId.substring(0, sjcjNetworkMonitoringId.length() - 1));
				// 将网络监听配置发送至stcp-sjcj-agent, 根据配置开始进行网络监听
//				Map<String, Object> models = new HashMap<String, Object>();
//				models.put("nmList", JSON.toJSON(nmList));
//				SjcjAgent sjcjAgent = sjcjAgentService.getSjcjAgentByCondition(sjcjAgentBindCaseId);
//				if (null != sjcjAgent) {
//					String agentIp = sjcjAgent.getAgentIp();
//					String agentPort = sjcjAgent.getAgentPort();
//					if (!StringUtils.isEmpty(agentIp) && !StringUtils.isEmpty(agentPort)) {
//						HttpUtil.doPost("http://" + agentIp + ":" + agentPort
//								+ "/stcp-sjcj-agent/dataCollection/startListening", models, null);
//					}
//				}
			}

		} else { // 若新的采集类型列表不包含网络数据监听
			// 删除所有网络监听配置
			QueryWrapper<SjcjNetworkMonitoring> queryWrapper = QueryGenerator
					.initQueryWrapper(new SjcjNetworkMonitoring(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			sjcjNetworkMonitoringService.remove(queryWrapper);
			sjcjCollectionConfig.setSjcjNetworkMonitoringId("");
		}
		if (typeOfCollection.contains("dbData")) { // 若新的采集类型列表包含数据库数据
			String sjcjDbDataId = "";
			QueryWrapper<SjcjDbData> queryWrapper = QueryGenerator.initQueryWrapper(new SjcjDbData(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			List<SjcjDbData> list = sjcjDbDataService.list(queryWrapper);
			if (list.size() > 0) {
				for (SjcjDbData temp : list) {
					sjcjDbDataId += temp.getId() + ",";
				}
			}
			if (StringUtils.isEmpty(sjcjDbDataId)) {
				sjcjCollectionConfig.setSjcjDbDataId(sjcjDbDataId);
			} else {
				sjcjCollectionConfig.setSjcjDbDataId(sjcjDbDataId.substring(0, sjcjDbDataId.length() - 1));
			}
		} else { // 若新的采集类型列表不包含数据库数据
			// 删除所有数据库数据采集配置
			QueryWrapper<SjcjDbData> queryWrapper = QueryGenerator.initQueryWrapper(new SjcjDbData(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			sjcjDbDataService.remove(queryWrapper);
			sjcjCollectionConfig.setSjcjDbDataId("");
		}
		sjcjCollectionConfig.setSjcjAgentBindCaseId(sjcjAgentBindCaseId); // `sjcj_agent_bind_case`表ID
		sjcjCollectionConfig.setUpdateTime(date); // 更新日期

		// 更改采集配置
		sjcjCollectionConfigService.updateById(sjcjCollectionConfig);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件采集配置表-通过id删除")
	@ApiOperation(value = "文件采集配置表-通过id删除", notes = "文件采集配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		SjcjCollectionConfig sjcjCollectionConfig = sjcjCollectionConfigService.getById(id);
		String sjcjAgentBindCaseId = sjcjCollectionConfig.getSjcjAgentBindCaseId();
		// 删除测试工具采集配置
		QueryWrapper<SjcjTestTool> sjcjTestToolQueryWrapper = QueryGenerator.initQueryWrapper(new SjcjTestTool(), null);
		sjcjTestToolQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjTestToolService.remove(sjcjTestToolQueryWrapper);
		// 删除网络监听采集配置
		QueryWrapper<SjcjNetworkMonitoring> sjcjNetworkMonitoringQueryWrapper = QueryGenerator
				.initQueryWrapper(new SjcjNetworkMonitoring(), null);
		sjcjNetworkMonitoringQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjNetworkMonitoringService.remove(sjcjNetworkMonitoringQueryWrapper);
		// 删除数据库数据采集配置
		QueryWrapper<SjcjDbData> sjcjDbDataQueryWrapper = QueryGenerator.initQueryWrapper(new SjcjDbData(), null);
		sjcjDbDataQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjDbDataService.remove(sjcjDbDataQueryWrapper);
		// 删除采集配置
		sjcjCollectionConfigService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "文件采集配置表-批量删除")
	@ApiOperation(value = "文件采集配置表-批量删除", notes = "文件采集配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjCollectionConfigService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "文件采集配置表-通过id查询")
	@ApiOperation(value = "文件采集配置表-通过id查询", notes = "文件采集配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjCollectionConfig sjcjCollectionConfig = sjcjCollectionConfigService.getById(id);
		if (sjcjCollectionConfig == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjCollectionConfig);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjCollectionConfig
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjCollectionConfig sjcjCollectionConfig) {
		return super.exportXls(request, sjcjCollectionConfig, SjcjCollectionConfig.class, "文件采集配置表");
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
		return super.importExcel(request, response, SjcjCollectionConfig.class);
	}

	/**
	 * 根据`sjcj_agent_bind_case`表ID获取`sjcj_collection_config`对象
	 */
	@GetMapping(value = "/getSjcjCollectionConfig")
	public Result<?> getSjcjCollectionConfig(String sjcjAgentBindCaseId) {
		SjcjCollectionConfig sjcjCollectionConfig = new SjcjCollectionConfig();
		if (sjcjAgentBindCaseId.contains(",") && sjcjAgentBindCaseId.split(",").length >= 1) {
			for (String temp : sjcjAgentBindCaseId.split(",")) {
				QueryWrapper<SjcjCollectionConfig> queryWrapper = QueryGenerator
						.initQueryWrapper(new SjcjCollectionConfig(), null);
				queryWrapper.eq("sjcj_agent_bind_case_id", temp);
				sjcjCollectionConfig = sjcjCollectionConfigService.getOne(queryWrapper);
				if (null == sjcjCollectionConfig) {
					break;
				}
			}
		} else {
			QueryWrapper<SjcjCollectionConfig> queryWrapper = QueryGenerator
					.initQueryWrapper(new SjcjCollectionConfig(), null);
			queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
			sjcjCollectionConfig = sjcjCollectionConfigService.getOne(queryWrapper);
		}
		return Result.ok(sjcjCollectionConfig);
	}

	/**
	 * 获取采集类型列表
	 */
	@GetMapping(value = "/getTypeOfCollection")
	public Result<?> getTypeOfCollection() {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<SysDictItem> list = sjcjCollectionConfigService.getTypeOfCollection();
		if (list != null) {
			for (SysDictItem temp : list) {
				Map<String, Object> map = new HashMap<>(2000);
				map.put("label", temp.getItemText());
				map.put("value", temp.getItemValue());
				resultList.add(map);
			}
		}
		return Result.ok(resultList);
	}

	/**
	 * 开始采集
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/startCollecting", method = RequestMethod.POST)
	public Result<?> startCollecting(@RequestBody Map<String, Object> params) {
		Map<String, Object> models = new HashMap<String, Object>();
		Map<String, Object> sjcjAgentBindCase = (Map<String, Object>) params.get("sjcjAgentBindCase");
		Map<String, Object> sjcjCollectionConfig = (Map<String, Object>) params.get("sjcjCollectionConfig");

		String typeOfCollection = (String) sjcjCollectionConfig.get("typeOfCollection");
		models.put("typeOfCollection", typeOfCollection); // 采集类型
		models.put("collectionDescription", sjcjCollectionConfig.get("collectionDescription")); // 采集描述
		models.put("sjcjAgentBindCaseId", sjcjCollectionConfig.get("sjcjAgentBindCaseId")); // `sjcj_agent_bind_case`表ID
		if (typeOfCollection.contains("testTool")) { // 采集类型为测试工具
			String testToolIdStr = (String) sjcjCollectionConfig.get("sjcjTestToolId");
			String[] testToolIdList = testToolIdStr.split(",");
			List<SjcjTestTool> testToolList = new ArrayList<>();
			for (String testToolId : testToolIdList) {
				testToolList.add(sjcjTestToolService.getById(testToolId));
			}
			models.put("testToolList", JSON.toJSON(testToolList)); // 测试工具数据采集配置列表
		}
		if (typeOfCollection.contains("measuredPiece")) { // 采集类型为被测件
			models.put("uutCollectionPath", sjcjCollectionConfig.get("uutCollectionPath")); // 被测件_自定义采集路径
			models.put("uutFileType", sjcjCollectionConfig.get("uutFileType")); // 被测件_自定义采集文件种类
		}
		if (typeOfCollection.contains("other")) { // 采集类型为其他
			models.put("otherCollectionPath", sjcjCollectionConfig.get("otherCollectionPath")); // 其他_自定义采集路径
			models.put("otherFileType", sjcjCollectionConfig.get("otherFileType")); // 其他_自定义采集文件种类
		}
		if (typeOfCollection.contains("networkMonitoring")) { // 采集类型为网络监听
			String nmIdStr = (String) sjcjCollectionConfig.get("sjcjNetworkMonitoringId");
			if (nmIdStr.contains(",")) {
				String[] nmIdList = nmIdStr.split(",");
				List<SjcjNetworkMonitoring> nmList = new ArrayList<>();
				for (String nmId : nmIdList) {
					nmList.add(sjcjNetworkMonitoringService.getById(nmId));
				}
				models.put("nmList", JSON.toJSON(nmList)); // 网络监听数据采集配置列表
			}
		}
		if (typeOfCollection.contains("dbData")) { // 采集类型为数据库数据
			String dbDataIdStr = (String) sjcjCollectionConfig.get("sjcjDbDataId");
			String[] dbDataIdList = dbDataIdStr.split(",");
			List<SjcjDbData> dbDataList = new ArrayList<>();
			for (String dbDataId : dbDataIdList) {
				dbDataList.add(sjcjDbDataService.getById(dbDataId));
			}
			models.put("dbDataList", JSON.toJSON(dbDataList));
		}

		models.put("projectId", sjcjAgentBindCase.get("projectId")); // 项目ID
		models.put("projectCode", sjcjAgentBindCase.get("projectCode")); // 项目标识
		models.put("projectName", sjcjAgentBindCase.get("projectName")); // 项目名称

		models.put("turnId", sjcjAgentBindCase.get("turnId")); // 轮次ID
		models.put("turnName", sjcjAgentBindCase.get("turnName")); // 轮次名称(标识)

		models.put("testTypeId", sjcjAgentBindCase.get("testTypeId")); // 测试类型ID
		models.put("testTypeCode", sjcjAgentBindCase.get("testTypeCode")); // 测试类型标识
		models.put("testTypeName", sjcjAgentBindCase.get("testTypeName")); // 测试类型名称

		models.put("taskId", sjcjAgentBindCase.get("taskId")); // 测试项ID
		models.put("taskCode", sjcjAgentBindCase.get("taskCode")); // 测试项标识
		models.put("taskName", sjcjAgentBindCase.get("taskName")); // 测试项名称

		models.put("caseId", sjcjAgentBindCase.get("caseId")); // 测试用例ID
		models.put("caseCode", sjcjAgentBindCase.get("caseCode")); // 测试用例标识
		models.put("caseName", sjcjAgentBindCase.get("caseName")); // 测试用例名称

		String agentIp = (String) sjcjAgentBindCase.get("agentIp");
		String agentPort = (String) sjcjAgentBindCase.get("agentPort");
		models.put("agentIp", agentIp); // 客户端IP
		models.put("agentName", sjcjAgentBindCase.get("agentName")); // 客户端名称
		models.put("agentPort", agentPort); // 客户端端口号

		String result = HttpUtil.doPost(
				"http://" + agentIp + ":" + agentPort + "/stcp-sjcj-agent/dataCollection/startCollecting", models,
				null);
		return Result.ok(result);
	}
}
