package org.jeecg.modules.sjcj.dbdatamanagement.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.sjcj.dbdatamanagement.entity.DbDataManagement;
import org.jeecg.modules.sjcj.dbdatamanagement.service.IDbDataManagementService;
import org.jeecg.modules.sjcj.dbdatamanagement.util.DatabaseUtil;
import org.jeecg.modules.sjcj.dbdatamanagement.util.MinioUtil;
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
 * @Description: 数据库数据管理表
 * @Author: jeecg-boot
 * @Date: 2021-04-15
 * @Version: V1.0
 */
@Api(tags = "数据库数据管理表")
@RestController
@RequestMapping("/sjcj.dbdatamanagement/dbDataManagement")
@Slf4j
public class DbDataManagementController extends JeecgController<DbDataManagement, IDbDataManagementService> {
	@Autowired
	private IDbDataManagementService dbDataManagementService;

	private DatabaseUtil dbConnectionUtil = new DatabaseUtil();

	@Autowired
	private MinioUtil minioUtil;

	/**
	 * 分页列表查询
	 *
	 * @param dbDataManagement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-分页列表查询")
	@ApiOperation(value = "数据库数据管理表-分页列表查询", notes = "数据库数据管理表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(DbDataManagement dbDataManagement,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<DbDataManagement> queryWrapper = QueryGenerator.initQueryWrapper(dbDataManagement,
				req.getParameterMap());
		Page<DbDataManagement> page = new Page<DbDataManagement>(pageNo, pageSize);
		IPage<DbDataManagement> pageList = dbDataManagementService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param dbDataManagement
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-添加")
	@ApiOperation(value = "数据库数据管理表-添加", notes = "数据库数据管理表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody DbDataManagement dbDataManagement) {
		dbDataManagementService.save(dbDataManagement);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param dbDataManagement
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-编辑")
	@ApiOperation(value = "数据库数据管理表-编辑", notes = "数据库数据管理表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody DbDataManagement dbDataManagement) {
		dbDataManagementService.updateById(dbDataManagement);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-通过id删除")
	@ApiOperation(value = "数据库数据管理表-通过id删除", notes = "数据库数据管理表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		dbDataManagementService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-批量删除")
	@ApiOperation(value = "数据库数据管理表-批量删除", notes = "数据库数据管理表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.dbDataManagementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据库数据管理表-通过id查询")
	@ApiOperation(value = "数据库数据管理表-通过id查询", notes = "数据库数据管理表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		DbDataManagement dbDataManagement = dbDataManagementService.getById(id);
		if (dbDataManagement == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(dbDataManagement);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param dbDataManagement
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, DbDataManagement dbDataManagement) {
		return super.exportXls(request, dbDataManagement, DbDataManagement.class, "数据库数据管理表");
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
		return super.importExcel(request, response, DbDataManagement.class);
	}

	/**
	 * 数据库测试连接
	 */
	@RequestMapping(value = "/testConnectDb", method = RequestMethod.GET)
	public Result<?> testConnectDb(@RequestParam(name = "id", required = true) String id) {
		// 判断请求入参不能为空
		if (null == id || "".equals(id)) {
			return Result.error("请求参数不能为空");
		}
		// 通过id获取DbDataManagement
		DbDataManagement dbDataManagement = dbDataManagementService.getById(id);
		// 校验是否有效
		if (null == dbDataManagement) {
			return Result.error("未找到对应数据");
		}
		// 测试连接是否成功
		String result = dbConnectionUtil.isConnect(dbDataManagement.getDbType(), dbDataManagement.getDbIp(),
				dbDataManagement.getPort(), dbDataManagement.getDbName(), dbDataManagement.getUserName(),
				dbDataManagement.getPassword());
		String info="测试连接数据库成功";
		if (info.equals(result)) {
			return Result.ok(result);
		} else {
			return Result.error(result);
		}
	}

	/**
	 * 开始采集数据库数据
	 */
	@RequestMapping(value = "/startCollecting", method = RequestMethod.GET)
	public Result<?> startCollecting(@RequestParam(name = "id", required = true) String id) {
		// 判断请求入参不能为空
		if (null == id || "".equals(id)) {
			return Result.error("请求参数不能为空");
		}
		// 通过id获取DbDataManagement
		DbDataManagement dbDataManagement = dbDataManagementService.getById(id);
		// 校验是否有效
		if (null == dbDataManagement) {
			return Result.error("未找到对应数据");
		}
		// 开始采集数据库数据
		Map<String, Object> map = dbConnectionUtil.collectData(dbDataManagement.getDbType(), dbDataManagement.getDbIp(),
				dbDataManagement.getPort(), dbDataManagement.getDbName(), dbDataManagement.getUserName(),
				dbDataManagement.getPassword(), dbDataManagement.getSqlStatement(), minioUtil);
		String collectingResult = (String) map.get("collectingResult");
		Date collectingTime = (Date) map.get("collectingTime");
		String fileName = (String) map.get("fileName");
		String info1="数据库数据采集成功";
		if (info1.equals(collectingResult)) {
			// 已采集
			dbDataManagement.setGatherStatus("2");
			// 采集时间
			dbDataManagement.setGatherTime(collectingTime);
			// 数据文件名称
			dbDataManagement.setFileName(fileName);
			dbDataManagement.setFilePath(
					// 数据文件路径
					minioUtil.getMinio().get("minio_url") + "/minio/" + minioUtil.getMinio().get("bucketName")
							+ "/databaseData/" + dbDataManagement.getDbType() + "/" + map.get("timeStr") + "/");
			dbDataManagementService.updateById(dbDataManagement);
			return Result.ok(collectingResult);
		} else {
			return Result.error(collectingResult);
		}

	}

}
