package org.jeecg.modules.system.controller;


import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.service.ISysLogService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-26
 */
@RestController
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {

	@Autowired
	private ISysLogService sysLogService;

	/**
	 * @功能：查询日志记录
	 * @param syslog
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysLog>> queryPageList(SysLog syslog,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
											   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysLog>> result = new Result<IPage<SysLog>>();
		QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(syslog, req.getParameterMap());
		Page<SysLog> page = new Page<SysLog>(pageNo, pageSize);
		//日志关键词
		String keyWord = req.getParameter("keyWord");
		if(oConvertUtils.isNotEmpty(keyWord)) {
			queryWrapper.like("log_content",keyWord);
		}
		//TODO 过滤逻辑处理
		//TODO begin、end逻辑处理
		String createTimebegin = req.getParameter("queryParam.createTime_begin");
		String createTimeEnd = req.getParameter("queryParam.createTime_end");
		System.out.println(createTimebegin +","+ createTimeEnd);

		String userName = req.getParameter("userName");
		if(oConvertUtils.isNotEmpty(userName)){
			queryWrapper.like("username",userName);
		}
		//TODO 一个强大的功能，前端传一个字段字符串，后台只返回这些字符串对应的字段
		//创建时间/创建人的赋值
		IPage<SysLog> pageList = sysLogService.page(page, queryWrapper);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：删除单个日志记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public Result<SysLog> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysLog> result = new Result<SysLog>();
		SysLog sysLog = sysLogService.getById(id);
		if(sysLog==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysLogService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：批量，全部清空日志记录
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	public Result<SysRole> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysRole> result = new Result<SysRole>();
		String allclear="allclear";
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			if(allclear.equals(ids)) {
				this.sysLogService.removeAll();
				result.success("清除成功!");
			}
			this.sysLogService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 导出excel dq add
	 *
	 * @param request
	 * @param sysLog
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(SysLog sysLog, HttpServletRequest request) {
		// Step.1 组装查询条件
		QueryWrapper<SysLog> queryWrapper = QueryGenerator.initQueryWrapper(sysLog, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		String selections = request.getParameter("selections");
		if(!oConvertUtils.isEmpty(selections)){
			queryWrapper.in("id",selections.split(","));
		}
		//日志关键词
		String keyWord = request.getParameter("keyWord");
		if(oConvertUtils.isNotEmpty(keyWord)) {
			queryWrapper.like("log_content",keyWord);
		}
		//创建时间/创建人的赋值
		List<SysLog> pageList = sysLogService.list(queryWrapper);
		//导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "日志列表");
		mv.addObject(NormalExcelConstants.CLASS, SysLog.class);
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		ExportParams exportParams = new ExportParams("日志数据", "导出人:"+user.getRealname(), "导出信息");
		mv.addObject(NormalExcelConstants.PARAMS, exportParams);
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}
}
