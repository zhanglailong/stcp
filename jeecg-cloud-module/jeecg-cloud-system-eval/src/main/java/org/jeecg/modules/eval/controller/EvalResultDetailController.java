package org.jeecg.modules.eval.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalAnalysisResult;
import org.jeecg.modules.eval.entity.EvalResultDetail;
import org.jeecg.modules.eval.service.IEvalAnalysisResultService;
import org.jeecg.modules.eval.service.IEvalResultDetailService;
import org.jeecg.modules.eval.vo.SaveScoreVO;
import org.jeecg.modules.running.project.entity.RunningProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: 分析结果详情表
* @Author: jeecg-boot
* @Date:   2020-12-31
* @Version: V1.0
*/
@Api(tags="分析结果详情表")
@RestController
@RequestMapping("/eval/evalResultDetail")
@Slf4j
public class EvalResultDetailController extends JeecgController<EvalResultDetail, IEvalResultDetailService> {

    @Autowired
    IEvalResultDetailService evalResultDetailService;

    @Autowired
    IEvalAnalysisResultService iEvalAnalysisResultService;

   /**
    * 树形列表查询
    *
    * @param resultId
    * @return result
    */
   @AutoLog(value = "分析结果详情表-树形")
   @ApiOperation(value="分析结果详情表-树形", notes="分析结果详情表-树形")
   @GetMapping(value = "/list")
   public Result<List<CaseTreeIdModel>> queryPageList(@RequestParam(name = "systemId", required = true) String systemId,
                                                    @RequestParam(name = "resultId", required = true) String resultId) {
       Result result = new Result();
       try {
           // TODO 这里有一段从内存读取的code
           List<CaseTreeIdModel> list = evalResultDetailService.queryTreeList(systemId, resultId);
           result.setResult(list);
           result.setSuccess(true);
       } catch (Exception e) {
           log.error(e.getMessage(), e);
       }
       return result;
   }


    /**
     * 树形列表查询
     *
     * @param projectId
     * @return result
     */
    @AutoLog(value = "分析结果详情表-树形")
    @ApiOperation(value="分析结果详情表-树形", notes="分析结果详情表-树形")
    @GetMapping(value = "/listByProject")
    public Result<List<CaseTreeIdModel>> listByProject(@RequestParam(name = "projectId", required = true) String projectId) {
        List<CaseTreeIdModel> list = null;
        try {
            //分析结果
            QueryWrapper queryWrapper = QueryGenerator.initQueryWrapper(new EvalAnalysisResult(),null);
            queryWrapper.inSql("id", "SELECT MAX(id) FROM eval_analysis_result WHERE project_id = '" + projectId + "'");
            EvalAnalysisResult evalAnalysisResult = iEvalAnalysisResultService.getOne(queryWrapper);
            // TODO 这里有一段从内存读取的code
            list = evalResultDetailService.queryTreeList(evalAnalysisResult.getSystemId(), evalAnalysisResult.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return Result.ok(list);
    }

   /**
    * 得分保存
    *
    * @param resultId
    * @param data
    * @return
    */
   @ApiOperation(value = "得分保存", notes = "得分保存")
   @PostMapping("/save")
   @Transactional(rollbackFor = Exception.class)
   public Result<?> save(@RequestParam String resultId, @RequestBody List<SaveScoreVO> data) {
        //list
        List<EvalResultDetail> list = new ArrayList<>();
        //定义result
        EvalAnalysisResult ear = new EvalAnalysisResult();
        //遍历data
        for (SaveScoreVO vo : data) {

            if (vo.getLevel() == 0) {
                ear.setId(resultId);
                ear.setScore(vo.getScore());
                continue;
            }
            EvalResultDetail erd = new EvalResultDetail();
            erd.setResultId(resultId);
            erd.setId(vo.getId());
            erd.setScore(vo.getScore());
            list.add(erd);
        }
//       try {
           evalResultDetailService.remove(
                   new LambdaQueryWrapper<EvalResultDetail>().eq(EvalResultDetail::getResultId, resultId));
//       } catch (Exception e) {
//
//       }
       //没有判断list为空的情况
        boolean updateStatus = evalResultDetailService.saveBatch(list);
        if (!updateStatus) {
            return Result.error("保存失败");
        }

        boolean resultUpdStatus = iEvalAnalysisResultService.updateById(ear);

        if (!resultUpdStatus) {
           return Result.error("保存失败");
        }

        return Result.ok("保存成功");
    }
}
