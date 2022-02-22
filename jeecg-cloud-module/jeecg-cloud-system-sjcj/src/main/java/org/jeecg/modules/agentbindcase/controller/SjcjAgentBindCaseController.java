package org.jeecg.modules.agentbindcase.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.agent.entity.SjcjAgent;
import org.jeecg.modules.agent.service.ISjcjAgentService;
import org.jeecg.modules.agentbindcase.entity.SjcjAgentBindCase;
import org.jeecg.modules.agentbindcase.service.ISjcjAgentBindCaseService;
import org.jeecg.modules.collectionconfig.entity.SjcjCollectionConfig;
import org.jeecg.modules.collectionconfig.service.ISjcjCollectionConfigService;
import org.jeecg.modules.collectionresult.entity.SjcjCollectionResult;
import org.jeecg.modules.collectionresult.service.ISjcjCollectionResultService;
import org.jeecg.modules.dbdata.entity.SjcjDbData;
import org.jeecg.modules.dbdata.service.ISjcjDbDataService;
import org.jeecg.modules.networkmonitoring.entity.SjcjNetworkMonitoring;
import org.jeecg.modules.networkmonitoring.service.ISjcjNetworkMonitoringService;
import org.jeecg.modules.projectassociatedagent.service.ISjcjProjectAssociatedAgentService;
import org.jeecg.modules.testtool.entity.SjcjTestTool;
import org.jeecg.modules.testtool.service.ISjcjTestToolService;
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
import org.xmlpull.v1.XmlPullParserException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "客户端绑定测试用例等信息")
@RestController
@RequestMapping("/agentbindcase/sjcjAgentBindCase")
@Slf4j
public class SjcjAgentBindCaseController extends JeecgController<SjcjAgentBindCase, ISjcjAgentBindCaseService> {
	@Autowired
	ISjcjAgentService sjcjAgentService;
	@Autowired
	private ISjcjAgentBindCaseService sjcjAgentBindCaseService;
	@Autowired
	private ISjcjProjectAssociatedAgentService sjcjProjectAssociatedAgentService;
	@Autowired
	private ISjcjCollectionConfigService sjcjCollectionConfigService;
	@Autowired
	private ISjcjDbDataService sjcjDbDataService;
	@Autowired
	private ISjcjNetworkMonitoringService sjcjNetworkMonitoringService;
	@Autowired
	private ISjcjTestToolService sjcjTestToolService;
	@Autowired
	private ISjcjCollectionResultService sjcjCollectionResultService;
	private MinioClient minioClient;
	@Value(value = "${jeecg.minio.minio-url}")
	private String minioUrl;
	@Value(value = "${jeecg.minio.minio-name}")
	private String minioName;
	@Value(value = "${jeecg.minio.minio-pwd}")
	private String minioPwd;

	/**
	 * minio: : minioadmin : sjcj
	 */

	/**
	 * 分页列表查询
	 *
	 * @param sjcjAgentBindCase
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-分页列表查询")
	@ApiOperation(value = "客户端绑定测试用例等信息-分页列表查询", notes = "客户端绑定测试用例等信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjAgentBindCase sjcjAgentBindCase,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjAgentBindCase> queryWrapper = QueryGenerator.initQueryWrapper(sjcjAgentBindCase,
				req.getParameterMap());
		Page<SjcjAgentBindCase> page = new Page<SjcjAgentBindCase>(pageNo, pageSize);
		IPage<SjcjAgentBindCase> pageList = sjcjAgentBindCaseService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjAgentBindCase
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-添加")
	@ApiOperation(value = "客户端绑定测试用例等信息-添加", notes = "客户端绑定测试用例等信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjAgentBindCase sjcjAgentBindCase) {
		sjcjAgentBindCaseService.save(sjcjAgentBindCase);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjAgentBindCase
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-编辑")
	@ApiOperation(value = "客户端绑定测试用例等信息-编辑", notes = "客户端绑定测试用例等信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjAgentBindCase sjcjAgentBindCase) {
		sjcjAgentBindCaseService.updateById(sjcjAgentBindCase);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-通过id删除")
	@ApiOperation(value = "客户端绑定测试用例等信息-通过id删除", notes = "客户端绑定测试用例等信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		String result = "";
		// 根据`sjcj_agent_bind_case`表ID删除其绑定的关联数据
		this.delBySjcjAgentBindCaseId(id, true);
		// 根据id删除表`sjcj_agent_bind_case`中的数据
		boolean isDel = sjcjAgentBindCaseService.removeById(id);
		if (isDel) {
			result = "200";
		}
		return Result.ok(result);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-批量删除")
	@ApiOperation(value = "客户端绑定测试用例等信息-批量删除", notes = "客户端绑定测试用例等信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		String result = "";
		List<String> list = Arrays.asList(ids.split(","));
		for (String id : list) {
			// 根据`sjcj_agent_bind_case`表ID删除其绑定的关联数据
			this.delBySjcjAgentBindCaseId(id, true);
		}
		boolean isDel = this.sjcjAgentBindCaseService.removeByIds(list);
		if (isDel) {
			result = "200";
		}
		return Result.ok(result);
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户端绑定测试用例等信息-通过id查询")
	@ApiOperation(value = "客户端绑定测试用例等信息-通过id查询", notes = "客户端绑定测试用例等信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjAgentBindCase sjcjAgentBindCase = sjcjAgentBindCaseService.getById(id);
		if (sjcjAgentBindCase == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjAgentBindCase);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjAgentBindCase
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjAgentBindCase sjcjAgentBindCase) {
		return super.exportXls(request, sjcjAgentBindCase, SjcjAgentBindCase.class, "客户端绑定测试用例等信息");
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
		return super.importExcel(request, response, SjcjAgentBindCase.class);
	}

	/**
	 * 客户端绑定测试用例等信息
	 */
	@RequestMapping(value = "/agentBindCase", method = RequestMethod.POST)
	public Result<?> agentBindCase(@RequestBody JSONObject json) {
		String result = "";
		Date createTime = new Date();
		// 根据项目ID及客户端ID获取其测试工具ID
		String toolId = sjcjProjectAssociatedAgentService.getToolIdByCondition(json.getString("projectId"),
				json.getString("sjcjAgentId"));
		// 被绑定的原测试用例ID列表
		String originalCaseIds = json.getString("originalCaseId");
		// 新的被选中的测试用例ID列表
		String caseIds = json.getString("caseId");
		try {
			if (StringUtils.isEmpty(caseIds)) { // 新测试用例项ID列表为空, 清除原绑定项
				if (!StringUtils.isEmpty(originalCaseIds)) {
					for (String temp : originalCaseIds.split(",")) {
						QueryWrapper<SjcjAgentBindCase> queryWrapper = QueryGenerator
								.initQueryWrapper(new SjcjAgentBindCase(), null);
						queryWrapper.eq("project_id", json.getString("projectId"))
								.eq("turn_id", json.getString("turnId"))
								.eq("test_type_id", json.getString("testTypeId"))
								.eq("task_id", json.getString("taskId")).eq("case_id", temp).eq("tool_id", toolId)
								.eq("sjcj_agent_id", json.getString("sjcjAgentId"));
						SjcjAgentBindCase sjcjAgentBindCase = sjcjAgentBindCaseService.getOne(queryWrapper);
						// 根据`sjcj_agent_bind_case`表ID删除其绑定的关联数据
						if (StringUtils.isEmpty(sjcjAgentBindCase.getId())) {
							this.delBySjcjAgentBindCaseId(sjcjAgentBindCase.getId(), true);
						}
						// 删除其在`sjcj_agent_bind_case`表中的数据
						sjcjAgentBindCaseService.remove(queryWrapper);
					}
				}
			} else { // 测试用例非空, 对比原绑定项进行增加或删除绑定项
				if (StringUtils.isEmpty(originalCaseIds)) { // 新增绑定关系
					String[] caseIdsArray = caseIds.split(",");
					for (String temp : caseIdsArray) {
						SjcjAgentBindCase sjcjAgentBindCase = new SjcjAgentBindCase()
								.setProjectId(json.getString("projectId")).setTurnId(json.getString("turnId"))
								.setTestTypeId(json.getString("testTypeId")).setTaskId(json.getString("taskId"))
								.setCaseId(temp).setToolId(toolId).setSjcjAgentId(json.getString("sjcjAgentId"))
								.setCreateTime(createTime);
						sjcjAgentBindCaseService.save(sjcjAgentBindCase);
					}
				} else { // 修改绑定关系(新增或解绑)
					// 绑定测试用例(将原来绑定的测试用例和这次做比较, 差异处理提高效率)
					List<String> caseIdsOfAdd = this.getDiff(originalCaseIds, caseIds); // 新增的绑定的测试用例列表
					List<String> caseIdsOfDel = this.getDiff(caseIds, originalCaseIds); // 待删的绑定的测试用例列表
					// 新增绑定关系
					for (String temp : caseIdsOfAdd) {
						SjcjAgentBindCase sjcjAgentBindCase = new SjcjAgentBindCase()
								.setProjectId(json.getString("projectId")).setTurnId(json.getString("turnId"))
								.setTestTypeId(json.getString("testTypeId")).setTaskId(json.getString("taskId"))
								.setCaseId(temp).setToolId(toolId).setSjcjAgentId(json.getString("sjcjAgentId"))
								.setCreateTime(createTime);
						sjcjAgentBindCaseService.save(sjcjAgentBindCase);
					}
					// 删除绑定关系
					for (String temp : caseIdsOfDel) {
						QueryWrapper<SjcjAgentBindCase> queryWrapper = QueryGenerator
								.initQueryWrapper(new SjcjAgentBindCase(), null);
						queryWrapper.eq("project_id", json.getString("projectId"))
								.eq("turn_id", json.getString("turnId"))
								.eq("test_type_id", json.getString("testTypeId"))
								.eq("task_id", json.getString("taskId")).eq("case_id", temp).eq("tool_id", toolId)
								.eq("sjcj_agent_id", json.getString("sjcjAgentId"));
						SjcjAgentBindCase sjcjAgentBindCase = sjcjAgentBindCaseService.getOne(queryWrapper);
						// 根据`sjcj_agent_bind_case`表ID删除其绑定的关联数据
						if (StringUtils.isEmpty(sjcjAgentBindCase.getId())) {
							this.delBySjcjAgentBindCaseId(sjcjAgentBindCase.getId(), true);
						}
						// 删除其在`sjcj_agent_bind_case`表中的数据
						sjcjAgentBindCaseService.remove(queryWrapper);
					}
				}
			}
			result = "200";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.ok(result);
	}

	/**
	 * 根据测试用例ID获取其关联的客户端列表
	 */
	@RequestMapping(value = "/getAgentByCheckedKeys", method = RequestMethod.POST)
	public Result<?> getAgentByCheckedKeys(@RequestBody Map<String, Object> params) {
		List<SjcjAgentBindCase> list = new ArrayList<>();
		List<String> idList = (List<String>) params.get("checkedKeys");
		if (idList.size() > 0) {
			for (String temp : idList) {
				// 根据测试用例ID获取客户端等信息
				list.addAll(sjcjAgentBindCaseService.getAgentInfoByCaseId(temp));
			}
		}
		return Result.ok(list);
	}

	/**
	 * 根据项目ID、轮次ID、测试类型ID、测试项ID及客户端ID获取该客户端绑定的测试用例列表
	 */
	@RequestMapping(value = "/getCaseWhichBindAgent", method = RequestMethod.GET)
	public Result<?> getCaseWhichBindAgent(@RequestParam(name = "projectId", required = false) String projectId,
			@RequestParam(name = "turnId", required = false) String turnId,
			@RequestParam(name = "testTypeId", required = false) String testTypeId,
			@RequestParam(name = "taskId", required = false) String taskId,
			@RequestParam(name = "sjcjAgentId", required = false) String sjcjAgentId) {
		QueryWrapper<SjcjAgentBindCase> queryWrapper = QueryGenerator.initQueryWrapper(new SjcjAgentBindCase(), null);
		queryWrapper.select("case_id").eq("project_id", projectId).eq("turn_id", turnId).eq("test_type_id", testTypeId)
				.eq("task_id", taskId).eq("sjcj_agent_id", sjcjAgentId);
		List<Object> list = sjcjAgentBindCaseService.listObjs(queryWrapper);
		return Result.ok(list);
	}

	/**
	 * 从参数diff中找出参数main中没有的元素
	 */
	public List<String> getDiff(String main, String diff) {
		if (StringUtils.isEmpty(diff)) {
			return null;
		}
		if (StringUtils.isEmpty(main)) {
			return Arrays.asList(diff.split(","));
		}
		String[] mainArr = main.split(",");
		String[] diffArr = diff.split(",");
		Map<String, Integer> map = new HashMap<>(2000);
		for (String string : mainArr) {
			map.put(string, 1);
		}
		List<String> res = new ArrayList<String>();
		for (String key : diffArr) {
			if (!StringUtils.isEmpty(key) && !map.containsKey(key)) {
				res.add(key);
			}
		}
		return res;
	}

	/**
	 * 根据`sjcj_agent_bind_case`表ID删除其绑定的关联数据
	 */
	public void delBySjcjAgentBindCaseId(String sjcjAgentBindCaseId, boolean toDel) {
		// 根据sjcjAgentBindCaseId到表`sjcj_test_tool`中获取测试工具采集配置, 若有数据, 则删除
		QueryWrapper<SjcjTestTool> testToolQueryWrapper = QueryGenerator.initQueryWrapper(new SjcjTestTool(), null);
		testToolQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjTestToolService.remove(testToolQueryWrapper);
		// 根据sjcjAgentBindCaseId到表`sjcj_network_monitoring`中获取网络监听采集配置, 若有数据, 则删除
		QueryWrapper<SjcjNetworkMonitoring> networkMonitoringQueryWrapper = QueryGenerator
				.initQueryWrapper(new SjcjNetworkMonitoring(), null);
		networkMonitoringQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjNetworkMonitoringService.remove(networkMonitoringQueryWrapper);
		// 根据sjcjAgentBindCaseId到表`sjcj_db_data`中获取数据库数据采集配置, 若有数据, 则删除
		QueryWrapper<SjcjDbData> dbDataQueryWrapper = QueryGenerator.initQueryWrapper(new SjcjDbData(), null);
		dbDataQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjDbDataService.remove(dbDataQueryWrapper);
		// 根据sjcjAgentBindCaseId到表`sjcj_collection_config`中获取文件数据采集配置, 若有数据, 则删除
		QueryWrapper<SjcjCollectionConfig> collectionConfigQueryWrapper = QueryGenerator
				.initQueryWrapper(new SjcjCollectionConfig(), null);
		collectionConfigQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjCollectionConfigService.remove(collectionConfigQueryWrapper);
		// 若toDel为true时, 则删除其位于MinIO上的采集文件
		if (toDel) {
			// 根据sjcjAgentBindCaseId在表`sjcj_collection_result`中获取采集文件存储路径及采集时间列表
			SjcjCollectionResult filePathEntity = sjcjCollectionResultService.getFilePath(sjcjAgentBindCaseId);
			if (null != filePathEntity) {
				// 删除MinIO上的文件
				String fileStoragePath = filePathEntity.getFileStoragePath();
				String bucketName = "";
				if (fileStoragePath.contains("/")) {
					bucketName = fileStoragePath.substring(fileStoragePath.indexOf("minio/") + "minio/".length());
					bucketName = bucketName.substring(0, bucketName.indexOf("/"));
				} else if (fileStoragePath.contains("\\")) {
					bucketName = fileStoragePath.substring(fileStoragePath.indexOf("minio\\") + "minio\\".length());
					bucketName = bucketName.substring(0, bucketName.indexOf("\\"));
				}
				// 根据`sjcj_agent_bind_case`表ID获取agentIp
				SjcjAgent sjcjAgent = sjcjAgentService.getSjcjAgentByCondition(sjcjAgentBindCaseId);
				if (null != sjcjAgent) {
					String agentIp = sjcjAgent.getAgentIp();
					fileStoragePath = fileStoragePath.substring(0,
							fileStoragePath.indexOf(agentIp) + agentIp.length() + 1);
				}
				this.deleteMinIOFile(bucketName, fileStoragePath);
			}
		}
		// 根据sjcjAgentBindCaseId到表`sjcj_collection_result`中获取文件数据采集结果, 若有数据, 则删除
		QueryWrapper<SjcjCollectionResult> collectionResultQueryWrapper = QueryGenerator
				.initQueryWrapper(new SjcjCollectionResult(), null);
		collectionResultQueryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		sjcjCollectionResultService.remove(collectionResultQueryWrapper);
	}

	/**
	 * 根据桶名及文件路径删除MinIO上的文件
	 * 
	 * @param bucketName 桶名
	 * @param folderUrl  文件夹路径
	 */
	public void deleteMinIOFile(String bucketName, String folderUrl) {
		this.initMinio(minioUrl, minioName, minioPwd);
		try {
			if (!StringUtils.isBlank(bucketName) && !StringUtils.isBlank(folderUrl)
					&& (folderUrl.endsWith("/") || folderUrl.endsWith("\\"))) {
				Iterable<io.minio.Result<Item>> list = minioClient.listObjects(bucketName);
				final String folderPath = folderUrl.substring(folderUrl.indexOf(bucketName) + bucketName.length() + 1);
				list.forEach(e -> {
					String fileName;
					try {
						fileName = URLDecoder.decode(e.get().objectName(), "utf-8"); // 文件名
						if (fileName.contains(folderPath)) {
							minioClient.removeObject(bucketName, fileName);
						}
					} catch (InvalidKeyException | InvalidBucketNameException | NoSuchAlgorithmException
							| InsufficientDataException | NoResponseException | ErrorResponseException
							| InternalException | IOException | XmlPullParserException | InvalidArgumentException e1) {
						e1.printStackTrace();
					}
				});

			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	private MinioClient initMinio(String minioUrl, String minioName, String minioPass) {
		if (null == minioClient) {
			try {
				minioClient = new MinioClient(minioUrl, minioName, minioPass);
			} catch (InvalidEndpointException | InvalidPortException e) {
				e.printStackTrace();
			}
		}
		return minioClient;
	}
}