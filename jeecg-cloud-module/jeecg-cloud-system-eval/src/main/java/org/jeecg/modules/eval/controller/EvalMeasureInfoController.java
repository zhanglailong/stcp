package org.jeecg.modules.eval.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;
import org.jeecg.modules.eval.entity.EvalSystem;
import org.jeecg.modules.eval.service.IEvalMeasureInfoService;
import org.jeecg.modules.eval.service.IEvalSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Description: 评价体系度量信息表
 * @Author: jeecg-boot
 * @Date:   2021-02-26
 * @Version: V1.0
 */
@Api(tags="评价体系度量信息表")
@RestController
@RequestMapping("/eval/evalMeasureInfo")
@Slf4j
public class EvalMeasureInfoController extends JeecgController<EvalMeasureInfo, IEvalMeasureInfoService> {
	@Autowired
	private IEvalMeasureInfoService evalMeasureInfoService;

	@Autowired
	IEvalSystemService evalSystemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param evalMeasureInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-分页列表查询")
	@ApiOperation(value="评价体系度量信息表-分页列表查询", notes="评价体系度量信息表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(EvalMeasureInfo evalMeasureInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<EvalMeasureInfo> queryWrapper = QueryGenerator.initQueryWrapper(evalMeasureInfo, req.getParameterMap());
		Page<EvalMeasureInfo> page = new Page<EvalMeasureInfo>(pageNo, pageSize);
		IPage<EvalMeasureInfo> pageList = evalMeasureInfoService.page(page, queryWrapper);
		
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param evalMeasureInfo
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-添加")
	@ApiOperation(value="评价体系度量信息表-添加", notes="评价体系度量信息表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody EvalMeasureInfo evalMeasureInfo) {
		String name = evalMeasureInfoService.selectNameById(evalMeasureInfo.getId());
		evalMeasureInfo.setName(name);
		evalMeasureInfoService.save(evalMeasureInfo);
		EvalSystem es = new EvalSystem();
		es.setId(evalMeasureInfo.getSystemId());
		es.setWeightDone(0);
		evalSystemService.updateById(es);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  根据id 获取计算公式信息
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表--公式表")
	@ApiOperation(value="获取度量绑定的公式信息", notes="评价体系度量信息表-公式表")
	@PostMapping(value = "/getFormulaById")
	public Result<?> selectFormulaById(@RequestBody HashMap<String, String> map) {
		String id = map.get("id");
		String sysId = map.get("sysId");
		String formual = evalMeasureInfoService.selectFormulaById(id,sysId);
		return Result.ok(formual);
	}
	
	/**
	 *  编辑
	 *
	 * @param evalMeasureInfo
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-编辑")
	@ApiOperation(value="评价体系度量信息表-编辑", notes="评价体系度量信息表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody EvalMeasureInfo evalMeasureInfo) {
		String name = evalMeasureInfoService.selectNameById(evalMeasureInfo.getId());
		evalMeasureInfo.setName(name);
		evalMeasureInfoService.updateById(evalMeasureInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-通过id删除")
	@ApiOperation(value="评价体系度量信息表-通过id删除", notes="评价体系度量信息表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		evalMeasureInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-批量删除")
	@ApiOperation(value="评价体系度量信息表-批量删除", notes="评价体系度量信息表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.evalMeasureInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "评价体系度量信息表-通过id查询")
	@ApiOperation(value="评价体系度量信息表-通过id查询", notes="评价体系度量信息表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		EvalMeasureInfo evalMeasureInfo = evalMeasureInfoService.getById(id);
		if(evalMeasureInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(evalMeasureInfo);
	}

	 /**
	  * 通过id查询
	  *
	  * @param systemId
	  * @return
	  */
	 @AutoLog(value = "评价体系度量信息表-通过systemId查询")
	 @ApiOperation(value="评价体系度量信息表-通过systemId查询", notes="评价体系度量信息表-通过systemId查询")
	 @GetMapping(value = "/queryBySystemId")
	 public Result<?> queryBySystemId(@RequestParam(name="systemId",required=true) String systemId) {
		 List<EvalMeasureInfo> evalMeasureInfo = evalMeasureInfoService.getListBySystemId(systemId);
		 if(evalMeasureInfo==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(evalMeasureInfo);
	 }
    /**
    * 导出excel
    *
    * @param request
    * @param evalMeasureInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EvalMeasureInfo evalMeasureInfo) {
        return super.exportXls(request, evalMeasureInfo, EvalMeasureInfo.class, "评价体系度量信息表");
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
        return super.importExcel(request, response, EvalMeasureInfo.class);
    }

	/**
	* @return
	*/
	@RequestMapping(value = "/getTreeData", method = RequestMethod.POST)
	public Result<?> getTreeData() {
		return Result.ok(evalMeasureInfoService.getTreeData());
	}

	/**
	 * 根据测试用例ID获取其关联的客户端列表
	 */
	@RequestMapping(value = "/saveTreeData", method = RequestMethod.POST)
	public Result<?> saveTreeData(@RequestBody Map<String, Object> params) {
		List<String> idList = (List<String>) params.get("checkedKeys");
		String systemId = (String) params.get("systemId");
		String methodId = (String) params.get("methodId");
		if (idList.size() > 0) {
			Map<String, Object> map = new HashMap<>();
			map.put("system_id", systemId);
			evalMeasureInfoService.removeByMap(map);
			for (String temp : idList) {
				EvalMeasureStructureVo evalMeasureStructureVo = evalMeasureInfoService.getEvalMeasuerStructureVo(temp);
				if(null != evalMeasureStructureVo){
					EvalMeasureInfo evalMeasureInfo = new EvalMeasureInfo();
					evalMeasureInfo.setSid(evalMeasureStructureVo.getId());
					evalMeasureInfo.setName(evalMeasureStructureVo.getName());
					evalMeasureInfo.setParentId(evalMeasureStructureVo.getParentId());
					evalMeasureInfo.setGrandId(evalMeasureStructureVo.getGrandId());
					evalMeasureInfo.setFormula(evalMeasureStructureVo.getFormula());
					evalMeasureInfo.setSystemId(systemId);
					evalMeasureInfo.setMethodId(methodId);
					evalMeasureInfoService.save(evalMeasureInfo);
				}
			}
		}
		return Result.ok();
	}
}
