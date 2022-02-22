package org.jeecg.modules.SjcjCaseAgent.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.SjcjCaseAgent.entity.SjcjCaseAgent;
import org.jeecg.modules.SjcjCaseAgent.service.ISjcjCaseAgentService;
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

@Api(tags = "测试用例与客户端关系表")
@RestController
@RequestMapping("/SjcjCaseAgent/sjcjCaseAgent")
@Slf4j
public class SjcjCaseAgentController extends JeecgController<SjcjCaseAgent, ISjcjCaseAgentService> {
	@Autowired
	private ISjcjCaseAgentService sjcjCaseAgentService;

	/**
	 * 分页列表查询
	 *
	 * @param sjcjCaseAgent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-分页列表查询")
	@ApiOperation(value = "测试用例与客户端关系表-分页列表查询", notes = "测试用例与客户端关系表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjCaseAgent sjcjCaseAgent,
			@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		QueryWrapper<SjcjCaseAgent> queryWrapper = QueryGenerator.initQueryWrapper(sjcjCaseAgent,
				req.getParameterMap());
		Page<SjcjCaseAgent> page = new Page<SjcjCaseAgent>(pageNo, pageSize);
		IPage<SjcjCaseAgent> pageList = sjcjCaseAgentService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param sjcjCaseAgent
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-添加")
	@ApiOperation(value = "测试用例与客户端关系表-添加", notes = "测试用例与客户端关系表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjCaseAgent sjcjCaseAgent) {
		sjcjCaseAgentService.save(sjcjCaseAgent);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param sjcjCaseAgent
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-编辑")
	@ApiOperation(value = "测试用例与客户端关系表-编辑", notes = "测试用例与客户端关系表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjCaseAgent sjcjCaseAgent) {
		sjcjCaseAgentService.updateById(sjcjCaseAgent);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-通过id删除")
	@ApiOperation(value = "测试用例与客户端关系表-通过id删除", notes = "测试用例与客户端关系表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
		sjcjCaseAgentService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-批量删除")
	@ApiOperation(value = "测试用例与客户端关系表-批量删除", notes = "测试用例与客户端关系表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		this.sjcjCaseAgentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用例与客户端关系表-通过id查询")
	@ApiOperation(value = "测试用例与客户端关系表-通过id查询", notes = "测试用例与客户端关系表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
		SjcjCaseAgent sjcjCaseAgent = sjcjCaseAgentService.getById(id);
		if (sjcjCaseAgent == null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjCaseAgent);
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param sjcjCaseAgent
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SjcjCaseAgent sjcjCaseAgent) {
		return super.exportXls(request, sjcjCaseAgent, SjcjCaseAgent.class, "测试用例与客户端关系表");
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
		return super.importExcel(request, response, SjcjCaseAgent.class);
	}

}
