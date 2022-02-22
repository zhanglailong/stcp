package org.jeecg.modules.sjcj.sjcjwzjcjzl.controller;

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
import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SjcjWzjcjzl;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SysDictItem;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.service.ISjcjWzjcjzlService;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.service.ISysDictItemService;
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
 * @Description: 数据采集位置及采集种类
 * @Author: jeecg-boot
 * @Date:   2020-12-29
 * @Version: V1.0
 */
@Api(tags="数据采集位置及采集种类")
@RestController
@RequestMapping("/sjcjwzjcjzl/sjcjWzjcjzl")
@Slf4j
public class SjcjWzjcjzlController extends JeecgController<SjcjWzjcjzl, ISjcjWzjcjzlService> {
	@Autowired
	private ISjcjWzjcjzlService sjcjWzjcjzlService;
	@Autowired
	private ISysDictItemService sysDictItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sjcjWzjcjzl
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-分页列表查询")
	@ApiOperation(value="数据采集位置及采集种类-分页列表查询", notes="数据采集位置及采集种类-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(SjcjWzjcjzl sjcjWzjcjzl,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SjcjWzjcjzl> queryWrapper = QueryGenerator.initQueryWrapper(sjcjWzjcjzl, req.getParameterMap());
		Page<SjcjWzjcjzl> page = new Page<SjcjWzjcjzl>(pageNo, pageSize);
		IPage<SjcjWzjcjzl> pageList = sjcjWzjcjzlService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sjcjWzjcjzl
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-添加")
	@ApiOperation(value="数据采集位置及采集种类-添加", notes="数据采集位置及采集种类-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody SjcjWzjcjzl sjcjWzjcjzl) {
		sjcjWzjcjzlService.save(sjcjWzjcjzl);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sjcjWzjcjzl
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-编辑")
	@ApiOperation(value="数据采集位置及采集种类-编辑", notes="数据采集位置及采集种类-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody SjcjWzjcjzl sjcjWzjcjzl) {
		sjcjWzjcjzlService.updateById(sjcjWzjcjzl);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-通过id删除")
	@ApiOperation(value="数据采集位置及采集种类-通过id删除", notes="数据采集位置及采集种类-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		sjcjWzjcjzlService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-批量删除")
	@ApiOperation(value="数据采集位置及采集种类-批量删除", notes="数据采集位置及采集种类-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sjcjWzjcjzlService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "数据采集位置及采集种类-通过id查询")
	@ApiOperation(value="数据采集位置及采集种类-通过id查询", notes="数据采集位置及采集种类-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		SjcjWzjcjzl sjcjWzjcjzl = sjcjWzjcjzlService.getById(id);
		if(sjcjWzjcjzl==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(sjcjWzjcjzl);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sjcjWzjcjzl
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SjcjWzjcjzl sjcjWzjcjzl) {
        return super.exportXls(request, sjcjWzjcjzl, SjcjWzjcjzl.class, "数据采集位置及采集种类");
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
        return super.importExcel(request, response, SjcjWzjcjzl.class);
    }

    /**
     * 获取采集类型列表
     */
    @GetMapping(value = "/getTypeOfCollection")
    public Result<?> getTypeOfCollection(){
    	List<Map<String,Object>> resultList = new ArrayList<>();
    	List<SysDictItem> list = sysDictItemService.getTypeOfCollection();
    	if(list != null) {
    		for(SysDictItem temp : list) {
    			Map<String,Object> map = new HashMap<>(2000);
				map.put("label", temp.getItemText());
				map.put("value", temp.getItemValue());
				resultList.add(map);
    		}
    	}
    	return Result.ok(resultList);
    }
}
