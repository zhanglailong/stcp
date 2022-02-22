package org.jeecg.modules.collectionresult.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.collectionresult.entity.SjcjCollectionResult;
import org.jeecg.modules.collectionresult.service.ISjcjCollectionResultService;
import org.jeecg.modules.sjcj.collectiondatamanagement.entity.CollectionDataManagement;
import org.jeecg.modules.sjcj.collectiondatamanagement.service.ICollectionDataManagementService;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;
import org.jeecg.modules.sjcj.resultdataanalysis.service.IResultDataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.xmlpull.v1.XmlPullParserException;

@Api(tags = "测试数据采集结果表")
@RestController
@RequestMapping("/collectionresult/sjcjCollectionResult")
@Slf4j
public class SjcjCollectionResultController
		extends JeecgController<SjcjCollectionResult, ISjcjCollectionResultService> {
	@Autowired
	private ISjcjCollectionResultService sjcjCollectionResultService;

	@Autowired
	private IResultDataAnalysisService resultDataAnalysisService;

	@Autowired
	private ICollectionDataManagementService collectionDataManagementService;

	private ArrayList<String> idArray = new ArrayList<>(Arrays.asList("1341647180284678146","1342658577659195394",
			"1342658900054372353","1342659460316917762","1342659955639054337","1342660167866642433","1342660509505286145"));

	private static final String JMETERID = "1342659707554361346";

	private static final String LRID = "1342659301919027201";

	public static final String SEPARATOR = "/";

	@Value(value = "${jeecg.minio.minio-url}")
	private String minioUrl;

	@Value(value = "${jeecg.minio.bucket-sjcj}")
	private String bucketName;

	private MinioClient minioClient = null;

	@Value(value = "${jeecg.minio.minio-name}")
	private String minioName;

	@Value(value = "${jeecg.minio.minio-pwd}")
	private String minioPass;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjCollectionResult
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-分页列表查询")
	@ApiOperation(value = "测试数据采集结果表-分页列表查询", notes = "测试数据采集结果表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjCollectionResult sjcjCollectionResult,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjCollectionResult> queryWrapper = QueryGenerator.initQueryWrapper(sjcjCollectionResult,
				req.getParameterMap());
		Page<SjcjCollectionResult> page = new Page<SjcjCollectionResult>(pageNo, pageSize);
		IPage<SjcjCollectionResult> pageList = sjcjCollectionResultService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjCollectionResult
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-添加")
	@ApiOperation(value = "测试数据采集结果表-添加", notes = "测试数据采集结果表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjCollectionResult sjcjCollectionResult) {
		sjcjCollectionResultService.save(sjcjCollectionResult);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjCollectionResult
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-编辑")
	@ApiOperation(value = "测试数据采集结果表-编辑", notes = "测试数据采集结果表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjCollectionResult sjcjCollectionResult) {
		sjcjCollectionResultService.updateById(sjcjCollectionResult);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-通过id删除")
	@ApiOperation(value = "测试数据采集结果表-通过id删除", notes = "测试数据采集结果表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjCollectionResultService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-批量删除")
	@ApiOperation(value = "测试数据采集结果表-批量删除", notes = "测试数据采集结果表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjCollectionResultService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试数据采集结果表-通过id查询")
	@ApiOperation(value = "测试数据采集结果表-通过id查询", notes = "测试数据采集结果表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjCollectionResult sjcjCollectionResult = sjcjCollectionResultService.getById(id);
		if (sjcjCollectionResult == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjCollectionResult);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjCollectionResult
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjCollectionResult sjcjCollectionResult) {
		return super.exportXls(request, sjcjCollectionResult, SjcjCollectionResult.class, "测试数据采集结果表");
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
		return super.importExcel(request, response, SjcjCollectionResult.class);
	}

	/**
	 * 根据`sjcj_agent_bind_case`表ID获取文件采集历史列表
	 */
	@GetMapping("/getResultHistory")
	public Result<?> getResultHistory(
			@RequestParam(name = "sjcjAgentBindCaseId", required = false) String sjcjAgentBindCaseId,
			@RequestParam(name = "pageNo", defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		Page<SjcjCollectionResult> pageList = new Page<SjcjCollectionResult>(pageNo, pageSize);
		pageList = sjcjCollectionResultService.queryPageList(pageList, sjcjAgentBindCaseId);
		return Result.ok(pageList);
	}

	/**
	 * 根据采集时间与`sjcj_agent_bind_case`表ID获取文件采集结果列表
	 */
	@GetMapping("/getResultDetail")
	public Result<?> getResultDetail(@RequestParam(name = "collectionTime", required = false) String collectionTime,
			@RequestParam(name = "sjcjAgentBindCaseId", required = false) String sjcjAgentBindCaseId) {
		List<Map<String, String>> list = sjcjCollectionResultService.getResultDetail(collectionTime,
				sjcjAgentBindCaseId);
		return Result.ok(list);
	}

	/**
	 * 根据`sjcj_agent_bind_case`表ID获取截屏历史列表
	 */
	@GetMapping("/getScreenshotHistory")
	public Result<?> getScreenshotHistory(
			@RequestParam(name = "sjcjAgentBindCaseId", required = false) String sjcjAgentBindCaseId,
			@RequestParam(name = "createTimeBegin", required = false) String createTimeBegin,
			@RequestParam(name = "createTimeEnd", required = false) String createTimeEnd,
			@RequestParam(name = "collectionDescription", required = false) String collectionDescription,
			@RequestParam(name = "pageNo", defaultValue = "1", required = false) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
		Page<SjcjCollectionResult> pageList = new Page<SjcjCollectionResult>(pageNo, pageSize);
		pageList = sjcjCollectionResultService.queryScreenshotPageList(pageList, sjcjAgentBindCaseId, createTimeBegin, createTimeEnd, collectionDescription);
		return Result.ok(pageList);
	}

	@GetMapping(value = "/analysisUrl")
	@ResponseBody
	public Result<?> analysisUrl(@RequestParam(name = "path") String path, @RequestParam(name = "toolId") String toolId,
								 @RequestParam(name = "agentIp") String agentIp, @RequestParam(name = "projectId") String projectId,
								 @RequestParam(name = "collectionDataManagementId") String collectionDataManagementId) throws Exception {
		Result<String> result = null;
		ResultDataAnalysis resultDataAnalysis;
		CollectionDataManagement entity = new CollectionDataManagement();
		entity.setId(collectionDataManagementId);
		// 更具工具处理路径
		if (idArray.contains(toolId)) {
			// LDRA TESTBED
			result=new Result<>(true,"分析数据成功",path);
		}else if (JMETERID.equals(toolId)) {
			// JMeter
			// result的目录
			String fileUrl = path.replace("/minio", "").concat("result").concat(SEPARATOR);
			// 调用接口将路径和工具ID传入service方法
			resultDataAnalysis=new ResultDataAnalysis(fileUrl,agentIp,projectId,toolId);
			try {
				resultDataAnalysisService.add(resultDataAnalysis);
				entity.setAbnormalInformation("1");
				collectionDataManagementService.updateById(entity);
				result=new Result<>(true,"分析数据成功",resultDataAnalysis.getId());
			} catch (IOException e) {
				entity.setAbnormalInformation("2");
				collectionDataManagementService.updateById(entity);
				return result.error500("分析数据时失败");
			}
		} else if (LRID.equals(toolId)) {
			// LoadRunner
			// 找result下一级目录名
			String objectName = getObjName(path);
			objectName = objectName.concat("result").concat(SEPARATOR);
			List<String> fileUrls = new ArrayList<String>();
			MinioClient minioClient = getMinioClient();
			Iterable<io.minio.Result<Item>> listObjects;
			try {
				listObjects = minioClient.listObjects(bucketName, objectName);
				Iterator<io.minio.Result<Item>> iterator = listObjects.iterator();
				while (iterator.hasNext()) {
					Item item = iterator.next().get();
					String fileName = item.objectName().replace(objectName, "");
					if (fileName.indexOf(SEPARATOR) >= 0) {
						String[] filenames = fileName.split(SEPARATOR);
						if (filenames.length >= 1) {
							boolean isCunZai = false;
							for (String fileUrl : fileUrls) {
								if (fileUrl.indexOf(filenames[0]) >= 0) { isCunZai = true; }
							}
							if (!isCunZai) { fileUrls.add(path.replace("/minio", "") + "result" + SEPARATOR + filenames[0] + SEPARATOR); }
						}
					}
				}
			} catch (XmlPullParserException | InvalidKeyException | InvalidBucketNameException
					| NoSuchAlgorithmException | InsufficientDataException | NoResponseException
					| ErrorResponseException | InternalException | IOException e) {
				log.info(e.getMessage());
				return result.error500("分析失败");
			}
			if (fileUrls.size() <= 0) {
				return result.error500("未找到指定采集结果文件");
			}
			// 调用接口将路径和工具ID传入service方法
			resultDataAnalysis=new ResultDataAnalysis(fileUrls.get(0),agentIp,projectId,toolId);
			try {
				resultDataAnalysisService.add(resultDataAnalysis);
				entity.setAbnormalInformation("1");
				collectionDataManagementService.updateById(entity);
				result=new Result<>(true,"分析数据成功",resultDataAnalysis.getId());
			} catch (IOException e) {
				entity.setAbnormalInformation("2");
				collectionDataManagementService.updateById(entity);
				return result.error500("分析数据时失败");
			}
		} else {
			return result.error500("未找到解析工具");
		}
		return result;
	}

	/**
	 * 获取文件夹路径
	 *
	 * @param url
	 * @return
	 */
	private String getObjName(String url) {
		String objName = "";
		String beforUrl = minioUrl + SEPARATOR + "minio" + SEPARATOR + bucketName + SEPARATOR;
		if (url.indexOf(beforUrl) >= 0) {
			objName = url.replace(beforUrl, "");
		}
		return objName;
	}

	/**
	 * 获取minioClient
	 *
	 * @return
	 */
	private MinioClient getMinioClient() {
		if (minioClient == null) {
			try {
				minioClient = new MinioClient(minioUrl, minioName, minioPass);
			} catch (InvalidEndpointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return minioClient;
	}
}