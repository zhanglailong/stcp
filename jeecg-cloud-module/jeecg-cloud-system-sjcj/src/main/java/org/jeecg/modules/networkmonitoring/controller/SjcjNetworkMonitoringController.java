package org.jeecg.modules.networkmonitoring.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.networkmonitoring.entity.SjcjNetworkMonitoring;
import org.jeecg.modules.networkmonitoring.service.ISjcjNetworkMonitoringService;
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

import com.alibaba.nacos.api.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 网络监听配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
@Api(tags = "网络监听配置表")
@RestController
@RequestMapping("/networkmonitoring/sjcjNetworkMonitoring")
@Slf4j
public class SjcjNetworkMonitoringController
		extends JeecgController<SjcjNetworkMonitoring, ISjcjNetworkMonitoringService> {
	@Autowired
	private ISjcjNetworkMonitoringService sjcjNetworkMonitoringService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjNetworkMonitoring
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-分页列表查询")
	@ApiOperation(value = "网络监听配置表-分页列表查询", notes = "网络监听配置表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjNetworkMonitoring sjcjNetworkMonitoring,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjNetworkMonitoring> queryWrapper = QueryGenerator.initQueryWrapper(sjcjNetworkMonitoring,
				req.getParameterMap());
		Page<SjcjNetworkMonitoring> page = new Page<SjcjNetworkMonitoring>(pageNo, pageSize);
		IPage<SjcjNetworkMonitoring> pageList = sjcjNetworkMonitoringService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjNetworkMonitoring
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-添加")
	@ApiOperation(value = "网络监听配置表-添加", notes = "网络监听配置表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjNetworkMonitoring sjcjNetworkMonitoring) {
		sjcjNetworkMonitoringService.save(sjcjNetworkMonitoring);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjNetworkMonitoring
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-编辑")
	@ApiOperation(value = "网络监听配置表-编辑", notes = "网络监听配置表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjNetworkMonitoring sjcjNetworkMonitoring) {
		sjcjNetworkMonitoringService.updateById(sjcjNetworkMonitoring);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-通过id删除")
	@ApiOperation(value = "网络监听配置表-通过id删除", notes = "网络监听配置表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjNetworkMonitoringService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-批量删除")
	@ApiOperation(value = "网络监听配置表-批量删除", notes = "网络监听配置表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjNetworkMonitoringService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "网络监听配置表-通过id查询")
	@ApiOperation(value = "网络监听配置表-通过id查询", notes = "网络监听配置表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjNetworkMonitoring sjcjNetworkMonitoring = sjcjNetworkMonitoringService.getById(id);
		if (sjcjNetworkMonitoring == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjNetworkMonitoring);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjNetworkMonitoring
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjNetworkMonitoring sjcjNetworkMonitoring) {
		return super.exportXls(request, sjcjNetworkMonitoring, SjcjNetworkMonitoring.class, "网络监听配置表");
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
		return super.importExcel(request, response, SjcjNetworkMonitoring.class);
	}

	/**
	 * 根据网络监听ID列表获取其网络监听实体列表
	 */
	@GetMapping(value = "/getNmList")
	public Result<?> getNmList(
			@RequestParam(name = "sjcjNetworkMonitoringId", required = false) String sjcjNetworkMonitoringId) {
		List<Map<String, String>> list = new ArrayList<>();
		if (!StringUtils.isEmpty(sjcjNetworkMonitoringId)) {
			if (sjcjNetworkMonitoringId.contains(",")) {
				String[] idList = sjcjNetworkMonitoringId.split(",");
				if (idList.length > 0) {
					for (String id : idList) {
						Map<String, String> map = new HashMap<>();
						SjcjNetworkMonitoring sjcjNetworkMonitoring = sjcjNetworkMonitoringService.getById(id);
						map.put("nmIp", sjcjNetworkMonitoring.getNmIp());
						map.put("nmPort", sjcjNetworkMonitoring.getNmPort());
						list.add(map);
					}
				}
			}
		}
		return Result.ok(list);
	}
}
