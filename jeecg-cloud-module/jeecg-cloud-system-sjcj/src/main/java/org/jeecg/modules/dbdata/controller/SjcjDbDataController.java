package org.jeecg.modules.dbdata.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.dbdata.entity.SjcjDbData;
import org.jeecg.modules.dbdata.service.ISjcjDbDataService;
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

@Api(tags = "数据库数据采集配置表")
@RestController
@RequestMapping("/dbdata/sjcjDbData")
@Slf4j
public class SjcjDbDataController extends JeecgController<SjcjDbData, ISjcjDbDataService> {
	@Autowired
	private ISjcjDbDataService sjcjDbDataService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjDbData
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "数据库数据采集配置表-分页列表查询")
	@ApiOperation(value = "数据库数据采集配置表-分页列表查询", notes = "数据库数据采集配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjDbData sjcjDbData,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjDbData> queryWrapper = QueryGenerator.initQueryWrapper(sjcjDbData, req.getParameterMap());
		Page<SjcjDbData> page = new Page<SjcjDbData>(pageNo, pageSize);
		IPage<SjcjDbData> pageList = sjcjDbDataService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjDbData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "数据库数据采集配置表-添加")
	@ApiOperation(value = "数据库数据采集配置表-添加", notes = "数据库数据采集配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Map<String, Object> params) {
		Map<String, String> map = (Map<String, String>) params.get("sjcjDbData");
		SjcjDbData sjcjDbData = new SjcjDbData();
		sjcjDbData.setDbType(map.get("dbType"));
		sjcjDbData.setDbTypeName(sjcjDbDataService.getDbTypeName(map.get("dbType")));
		sjcjDbData.setDbIp(map.get("dbIp"));
		sjcjDbData.setDbPort(map.get("dbPort"));
		sjcjDbData.setDbName(map.get("dbName"));
		sjcjDbData.setUserName(map.get("userName"));
		sjcjDbData.setPassword(map.get("password"));
		sjcjDbData.setSqlStatement(map.get("sqlStatement"));
		sjcjDbData.setDbDataDescription(map.get("dbDataDescription"));
		sjcjDbData.setSjcjAgentBindCaseId((String) params.get("sjcjAgentBindCaseId"));
		sjcjDbData.setCreateTime(new Date());
		sjcjDbDataService.save(sjcjDbData);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjDbData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "数据库数据采集配置表-编辑")
	@ApiOperation(value = "数据库数据采集配置表-编辑", notes = "数据库数据采集配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody Map<String, Object> params) {
		Map<String, String> map = (Map<String, String>) params.get("sjcjDbData");
		SjcjDbData sjcjDbData = sjcjDbDataService.getById(map.get("id"));
		sjcjDbData.setDbDataDescription(map.get("dbDataDescription"));
		sjcjDbData.setDbType(map.get("dbType"));
		sjcjDbData.setDbTypeName(sjcjDbDataService.getDbTypeName(map.get("dbType")));
		sjcjDbData.setDbIp(map.get("dbIp"));
		sjcjDbData.setDbPort(map.get("dbPort"));
		sjcjDbData.setDbName(map.get("dbName"));
		sjcjDbData.setUserName(map.get("userName"));
		sjcjDbData.setPassword(map.get("password"));
		sjcjDbData.setSqlStatement(map.get("sqlStatement"));
		sjcjDbData.setSjcjAgentBindCaseId(map.get("sjcjAgentBindCaseId"));
		sjcjDbData.setUpdateTime(new Date());
		sjcjDbDataService.updateById(sjcjDbData);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据库数据采集配置表-通过id删除")
	@ApiOperation(value = "数据库数据采集配置表-通过id删除", notes = "数据库数据采集配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjDbDataService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据库数据采集配置表-批量删除")
	@ApiOperation(value = "数据库数据采集配置表-批量删除", notes = "数据库数据采集配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjDbDataService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据库数据采集配置表-通过id查询")
	@ApiOperation(value = "数据库数据采集配置表-通过id查询", notes = "数据库数据采集配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjDbData sjcjDbData = sjcjDbDataService.getById(id);
		if (sjcjDbData == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjDbData);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjDbData
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjDbData sjcjDbData) {
		return super.exportXls(request, sjcjDbData, SjcjDbData.class, "数据库数据采集配置表");
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
		return super.importExcel(request, response, SjcjDbData.class);
	}

	/**
	 * 根据sjcjAgentBindCaseId获取数据库数据采集配置列表
	 */
	@GetMapping("/getDbDataConfigList")
	public Result<?> getDbDataConfigList(
			@RequestParam(name = "sjcjAgentBindCaseId", required = false) String sjcjAgentBindCaseId) {
		QueryWrapper<SjcjDbData> queryWrapper = QueryGenerator.initQueryWrapper(new SjcjDbData(), null);
		queryWrapper.eq("sjcj_agent_bind_case_id", sjcjAgentBindCaseId);
		List<SjcjDbData> list = sjcjDbDataService.list(queryWrapper);
		return Result.ok(list);
	}

}
