package org.jeecg.modules.eval.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;
import org.jeecg.modules.eval.service.IEvalMeasureWeightService;
import org.jeecg.modules.eval.service.IEvalSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: eval_measure_weight
 * @Author: jeecg-boot
 * @Date: 2021-02-25
 * @Version: V1.0
 */
@Api(tags = "eval_measure_weight")
@RestController
@RequestMapping("/eval/evalMeasureWeight")
@Slf4j
public class EvalMeasureWeightController extends JeecgController<EvalMeasureWeight, IEvalMeasureWeightService> {
    @Autowired
    private IEvalMeasureWeightService evalMeasureWeightService;
    @Autowired
    IEvalSystemService evalSystemService;

    /**
     * 分页列表查询
     *
     * @param evalMeasureWeight
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "eval_measure_weight-分页列表查询")
    @ApiOperation(value = "eval_measure_weight-分页列表查询", notes = "eval_measure_weight-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EvalMeasureWeight evalMeasureWeight,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<EvalMeasureWeight> queryWrapper = QueryGenerator.initQueryWrapper(evalMeasureWeight, req.getParameterMap());
        Page<EvalMeasureWeight> page = new Page<EvalMeasureWeight>(pageNo, pageSize);
        IPage<EvalMeasureWeight> pageList = evalMeasureWeightService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param evalMeasureWeight
     * @return
     */
    @AutoLog(value = "eval_measure_weight-添加")
    @ApiOperation(value = "eval_measure_weight-添加", notes = "eval_measure_weight-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EvalMeasureWeight evalMeasureWeight) {
        evalMeasureWeightService.save(evalMeasureWeight);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param evalMeasureWeight
     * @return
     */
    @AutoLog(value = "eval_measure_weight-编辑")
    @ApiOperation(value = "eval_measure_weight-编辑", notes = "eval_measure_weight-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EvalMeasureWeight evalMeasureWeight) {
        evalMeasureWeightService.updateById(evalMeasureWeight);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_measure_weight-通过id删除")
    @ApiOperation(value = "eval_measure_weight-通过id删除", notes = "eval_measure_weight-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        evalMeasureWeightService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "eval_measure_weight-批量删除")
    @ApiOperation(value = "eval_measure_weight-批量删除", notes = "eval_measure_weight-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.evalMeasureWeightService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_measure_weight-通过id查询")
    @ApiOperation(value = "eval_measure_weight-通过id查询", notes = "eval_measure_weight-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EvalMeasureWeight evalMeasureWeight = evalMeasureWeightService.getById(id);
        if (evalMeasureWeight == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(evalMeasureWeight);
    }

    /**
     * 通过systemId查询
     *
     * @param systemId
     * @return
     */
    @AutoLog(value = "eval_measure_weight-通过systemId查询")
    @ApiOperation(value = "eval_measure_weight-通过systemId查询", notes = "eval_measure_weight-通过systemId查询")
    @GetMapping(value = "/queryBySystemId")
    public Result<?> queryBySystemId(@RequestParam(name = "systemId", required = true) String systemId) {
        List<EvalMeasureWeight> evalMeasureWeight = evalMeasureWeightService.getListBySystemId(systemId);
        if (evalMeasureWeight == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(evalMeasureWeight);
    }
    /**
     * 导出excel
     *
     * @param request
     * @param evalMeasureWeight
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EvalMeasureWeight evalMeasureWeight) {
        return super.exportXls(request, evalMeasureWeight, EvalMeasureWeight.class, "eval_measure_weight");
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
        return super.importExcel(request, response, EvalMeasureWeight.class);
    }

    /**
     * 删除权重信息
     * @param systemId
     * @param caseTreeIdModel
     * @return
     */
    @ApiOperation(value = "删除权重", notes = "删除权重")
    @PostMapping("/deleteWeight")
    public Result<?> deleteWeight(@RequestParam String systemId,@RequestBody CaseTreeIdModel caseTreeIdModel) {
        return Result.ok(evalMeasureWeightService.deleteWeight(systemId,caseTreeIdModel));
    }

    /**
     * 质量权重保存
     *
     * @param systemId
     * @param list
     * @return
     */
    @ApiOperation(value = "质量权重保存", notes = "质量权重保存")
    @PostMapping("/saveWeightList")
    public Result<?> saveWeightList(@RequestParam String systemId, @RequestBody List<CaseTreeIdModel> list) {
        return Result.ok(evalMeasureWeightService.saveList(list,systemId));
    }

    /**
     * 权重分配树形
     * @param systemId
     * @return
     */
    @ApiOperation(value = "权重分配树形",notes = "权重分配树形")
    @GetMapping("/queryTreeList")
    public Result<?> queryTreeList(@RequestParam String systemId){
        List<CaseTreeIdModel> list = evalMeasureWeightService.queryTreeList(systemId);
        return Result.ok(list);
    }

}
