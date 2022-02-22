package org.jeecg.modules.cloudtools.runex.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;
import org.jeecg.modules.cloudtools.runex.entity.RunStore;
import org.jeecg.modules.cloudtools.runex.pojo.SimpleBlock;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;
import org.jeecg.modules.cloudtools.runex.service.IRunStoreService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: xrun工具使用ip库
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Api(tags="xrun工具使用ip库")
@RestController
@RequestMapping("/xrunex/xrunIpStore")
@Slf4j
public class RunIpStoreController extends JeecgController<RunStore, IRunStoreService> {
	@Autowired
	private IRunStoreService runStoreService;
	@Autowired
	private IRunParamsSetService runParamsSetService;
	@Autowired
	private ISysDictService sysDictService;
	
	@AutoLog(value = "xrun工具使用ip库查询")
	@ApiOperation(value="xrun工具使用ip库查询", notes="xrun工具使用ip库查询")
	@GetMapping(value = "/getIpStore")
	public Result<?> getIpStoreByProjId(){
		
		List<RunStore> list = runStoreService.list();
		
		runStoreService.list().stream().map(store->{
			store.setChildren(store.getType() == 1?new ArrayList<RunStore>():null);
			store.setLeaf(store.getType() == 1?false:true);
			return store;
		}).collect(Collectors.toList());
		List<JSONObject> res = new ArrayList<>();
		transToJson(res, list,null);
		
		Result result = Result.ok(res);
		
		return result;
	}
	
	@AutoLog(value = "xrun工具规则获取")
	@ApiOperation(value="xrun工具规则获取", notes="xrun工具规则获取")
	@GetMapping(value = "/initRules")
	public Result<?> initRules(){
		Result result = Result.ok();
		QueryWrapper<RunParamsSet> wrapper = QueryGenerator.initQueryWrapper(new RunParamsSet(), null);
		wrapper.orderByAsc("type");
		List<RunParamsSet> list = runParamsSetService.list();
		//勾选项数据字典
		String dictCheckList = new String();
		//单选项数据字典
		List<String> dictSelectList = new ArrayList<String>();
		//填写项
		List<SimpleBlock> blockList = new ArrayList<SimpleBlock>();
		for(RunParamsSet xrun:list) {
			if(xrun.getType()==1) {
				dictCheckList = xrun.getValue();
			}else if(xrun.getType()==2) {
				dictSelectList.add(xrun.getValue());
			}else {
				SimpleBlock sb = new SimpleBlock(xrun);
				blockList.add(sb);
			}
		}
		JSONObject json = new JSONObject();
		//勾选集合
		json.put("type1", sysDictService.queryDictItemsByCode(dictCheckList));
		//单选集合,由于是从缓存中获取，for循环获取即可
		List<DictModel> type2 = new ArrayList<DictModel>();
		dictSelectList.forEach(dict->{
			type2.addAll(sysDictService.queryDictItemsByCode(dict));
		});
		json.put("type2", type2);
		
		json.put("type3", blockList);
		result.setResult(json);
		return result;
	}
	
	
	
	/**
	 * 分页列表查询
	 *
	 * @param req
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-分页列表查询")
	@ApiOperation(value="xrun工具使用ip库-分页列表查询", notes="xrun工具使用ip库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(HttpServletRequest req) {
		QueryWrapper<RunStore> queryWrapper =new QueryWrapper();
//		Page<RunStore> page = new Page<RunStore>(pageNo, pageSize);
//		IPage<RunStore> pageList = runStoreService.page(page, queryWrapper);
		List<RunStore> list = runStoreService.list(queryWrapper);
		list = list.stream().map(l->{
			l.setChildren(l.getType() == 1?new ArrayList<RunStore>():null);
			l.setLeaf(l.getType() == 1?false:true);
			return l;
		}).collect(Collectors.toList());
		List<RunStore> res = new ArrayList<>();
		trans(res, list,null);
		
		return Result.ok(res);
	}
	
	private void trans(List<RunStore> target,List<RunStore> list,RunStore item) {
		for(int i = 0;i<list.size();i++) {
			RunStore store = list.get(i);
			//顶层
			if(StringUtils.isEmpty(store.getParentId())) {
				//转移
				target.add(store);
				list.remove(store);
				i--;
			}else if(!oConvertUtils.isEmpty(item)){
				if(store.getParentId().equals(item.getId())) {
					item.getChildren().add(store);
					list.remove(store);
					i--;
				}
			}
			if(store.getType() == 1&&list.size()>0) {
				trans(target, list, store);
			}
		}
	}
	
	private void transToJson(List<JSONObject> target,List<RunStore> list,JSONObject item) {
		for(int i = 0;i<list.size();i++) {
			RunStore store = list.get(i);
			
			JSONObject json = new JSONObject();
			json.put("title",store.getName());
			json.put("value", StringUtils.isEmpty(store.getUrl())?store.getId():store.getUrl());
			json.put("id", store.getId());
//			if(store.getChildren()!=null) {
				json.put("children", new ArrayList<JSONObject>());
//			}
			//顶层
			if(StringUtils.isEmpty(store.getParentId())) {
				//转移
				target.add(json);
				list.remove(store);
				i--;
			}else if(!oConvertUtils.isEmpty(item)){
				if(store.getParentId().equals(item.getString("id"))) {
					((ArrayList) item.get("children")).add(json);
					list.remove(store);
					i--;
				}
			}
			if(store.getType() == 1&&list.size()>0) {
				transToJson(target, list, json);
			}
		}
	}
	
	/**
	 *   添加
	 *
	 * @param runStore
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-添加")
	@ApiOperation(value="xrun工具使用ip库-添加", notes="xrun工具使用ip库-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunStore runStore) {
		runStoreService.save(runStore);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runStore
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-编辑")
	@ApiOperation(value="xrun工具使用ip库-编辑", notes="xrun工具使用ip库-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunStore runStore) {
		runStoreService.updateById(runStore);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-通过id删除")
	@ApiOperation(value="xrun工具使用ip库-通过id删除", notes="xrun工具使用ip库-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runStoreService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-批量删除")
	@ApiOperation(value="xrun工具使用ip库-批量删除", notes="xrun工具使用ip库-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runStoreService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "xrun工具使用ip库-通过id查询")
	@ApiOperation(value="xrun工具使用ip库-通过id查询", notes="xrun工具使用ip库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunStore runStore = runStoreService.getById(id);
		if(runStore==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runStore);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param runStore
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunStore runStore) {
		List<RunStore> list = runStoreService.list();

		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String title = "ip库";
		// Step.3 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "title"); //此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, RunStore.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:" + sysUser.getRealname(), title));
		mv.addObject(NormalExcelConstants.DATA_LIST, list);
		return mv;


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
        return super.importExcel(request, response, RunStore.class);
    }

}
