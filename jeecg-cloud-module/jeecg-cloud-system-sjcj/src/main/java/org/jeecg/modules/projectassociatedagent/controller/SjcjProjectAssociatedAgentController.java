package org.jeecg.modules.projectassociatedagent.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.projectassociatedagent.entity.SjcjProjectAssociatedAgent;
import org.jeecg.modules.projectassociatedagent.service.ISjcjProjectAssociatedAgentService;
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
 * @Description: 项目与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Api(tags = "项目与客户端关系表")
@RestController
@RequestMapping("/projectassociatedagent/sjcjProjectAssociatedAgent")
@Slf4j
public class SjcjProjectAssociatedAgentController
		extends JeecgController<SjcjProjectAssociatedAgent, ISjcjProjectAssociatedAgentService> {
	@Autowired
	private ISjcjProjectAssociatedAgentService sjcjProjectAssociatedAgentService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjProjectAssociatedAgent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-分页列表查询")
	@ApiOperation(value = "项目与客户端关系表-分页列表查询", notes = "项目与客户端关系表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjProjectAssociatedAgent sjcjProjectAssociatedAgent,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjProjectAssociatedAgent> queryWrapper = QueryGenerator
				.initQueryWrapper(sjcjProjectAssociatedAgent, req.getParameterMap());
		Page<SjcjProjectAssociatedAgent> page = new Page<SjcjProjectAssociatedAgent>(pageNo, pageSize);
		IPage<SjcjProjectAssociatedAgent> pageList = sjcjProjectAssociatedAgentService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjProjectAssociatedAgent
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-添加")
	@ApiOperation(value = "项目与客户端关系表-添加", notes = "项目与客户端关系表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjProjectAssociatedAgent sjcjProjectAssociatedAgent) {
		sjcjProjectAssociatedAgentService.save(sjcjProjectAssociatedAgent);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjProjectAssociatedAgent
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-编辑")
	@ApiOperation(value = "项目与客户端关系表-编辑", notes = "项目与客户端关系表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjProjectAssociatedAgent sjcjProjectAssociatedAgent) {
		sjcjProjectAssociatedAgentService.updateById(sjcjProjectAssociatedAgent);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-通过id删除")
	@ApiOperation(value = "项目与客户端关系表-通过id删除", notes = "项目与客户端关系表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjProjectAssociatedAgentService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-批量删除")
	@ApiOperation(value = "项目与客户端关系表-批量删除", notes = "项目与客户端关系表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjProjectAssociatedAgentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目与客户端关系表-通过id查询")
	@ApiOperation(value = "项目与客户端关系表-通过id查询", notes = "项目与客户端关系表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjProjectAssociatedAgent sjcjProjectAssociatedAgent = sjcjProjectAssociatedAgentService.getById(id);
		if (sjcjProjectAssociatedAgent == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjProjectAssociatedAgent);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjProjectAssociatedAgent
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjProjectAssociatedAgent sjcjProjectAssociatedAgent) {
		return super.exportXls(request, sjcjProjectAssociatedAgent, SjcjProjectAssociatedAgent.class, "项目与客户端关系表");
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
		return super.importExcel(request, response, SjcjProjectAssociatedAgent.class);
	}

}
