package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.eval.entity.*;
import org.jeecg.modules.eval.mapper.EvalComprehensiveAnalysisMapper;
import org.jeecg.modules.eval.mapper.EvalAnalysisResultMapper;
import org.jeecg.modules.eval.mapper.EvalMeasureWeightMapper;
import org.jeecg.modules.eval.service.*;
import org.jeecg.modules.eval.util.EvalCalculationUtils;
import org.jeecg.modules.eval.vo.CompAnalysisCalcVO;
import org.jeecg.modules.eval.vo.CompAnalysisMainInfoVO;
import org.jeecg.modules.eval.vo.TemporaryAnalysisDataDto;
import org.jeecg.modules.running.project.entity.RunningProject;
import org.jeecg.modules.running.task.service.IRunningCaseService;
import org.jeecg.modules.running.uut.service.IRunningUutListService;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;
import org.jeecg.modules.sjcj.resultdataanalysis.service.IResultDataAnalysisService;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 评价体系公式信息表
 * @Author: jeecg-boot
 * @Date: 2021-03-16
 * @Version: V1.0
 */
@Service
public class EvalComprehensiveAnalysisServiceImpl
        extends ServiceImpl<EvalComprehensiveAnalysisMapper, CompAnalysisMainInfoVO>
        implements IComprehensiveAnalysisService {

    @Autowired
    EvalMeasureWeightMapper evalMeasureWeightMapper;

    @Autowired
    EvalAnalysisResultMapper evalAnalysisResultMapper;

    @Autowired
    IEvalResultDetailService evalResultDetailService;

    @Autowired
    IRunningCaseService runningCaseService;

    @Autowired
    IResultDataAnalysisService resultDataAnalysisService;

    @Autowired
    IEvalAnalysisResultService evalAnalysisResultService;

    @Autowired
    IEvalSystemService evalSystemService;

    @Autowired
    IEvalMethodInfoService evalMethodInfoService;

    @Autowired
    IRunningUutListService runningUutListService;

    @SneakyThrows
    @Override
    public Result<?> analysisProcess(String analyzeResultId) throws Exception {
        //ResultDataAnalysis resultDataAnalysis = resultDataAnalysisService.getById(resultDataId);
        EvalAnalysisResult evalAnalysisResult = evalAnalysisResultService.getById(analyzeResultId);
        RunningUutList runningUutList = runningUutListService.getById(evalAnalysisResult.getUutId());
        // 评价体系ID
        String systemId = runningUutList.getTestTemplate();
        // 调用存储过程
        this.getBaseMapper().sqaAnalysisData(runningUutList.getId());
        // 获取评价数据 12A 分数xx  12B 分数XX
        List<TemporaryAnalysisDataDto> dataList = this.getBaseMapper().queryList();
        // 获取度量信息
        List<CompAnalysisMainInfoVO> mainInfoList = this.getBaseMapper().selectMainInfo(systemId);
        // mainInfoList转Map  公式map  用于获取id 对应的公式
        /*Map<String, String> formulasMap = mainInfoList.stream().collect(Collectors.toMap(CompAnalysisMainInfoVO::getId, CompAnalysisMainInfoVO::getFormulaName, (k1, k2) -> k1));*/
        Map<String, String> formulasMap = new HashMap<>();
        for (CompAnalysisMainInfoVO compAnalysisMainInfoVO : mainInfoList) {
            if (!StringUtils.isEmpty(compAnalysisMainInfoVO.getFormulaName())) {
                formulasMap.put(compAnalysisMainInfoVO.getId(), compAnalysisMainInfoVO.getFormulaName());
            }
        }
        // 获取权重信息 所有权重信息
        List<EvalMeasureWeight> measureWeightList = this.getBaseMapper().measureWeight(systemId);
        Map<String, String> collect = measureWeightList.stream().collect(Collectors.toMap(EvalMeasureWeight::getMeasureId, EvalMeasureWeight::getWeight));
        // dataList 转map
        Map<String, String> dataMap = dataList.stream().collect(Collectors.toMap(TemporaryAnalysisDataDto::getKeyname, TemporaryAnalysisDataDto::getValuename, (k1, k2) -> k2));
        // 计算参数Map  存 A 值  B 值  当前三级度量元id下的
        Map<String, Object> calcParamMap = new HashMap<>(2000);
        // 质量子特性计算用map
        Map<String, CompAnalysisCalcVO> parentCalcMap = new HashMap<>(16);
        // 质量特性计算用map
        Map<String, Double> grandCalcMap = new HashMap<String, Double>(16);
        // 更新记录List<EvalResultDetail>
        List<EvalResultDetail> evalResultDetails = new ArrayList<>();

        String resultId = evalAnalysisResult.getId();
        for (int i = 0; i < mainInfoList.size(); i++) {
            String id = mainInfoList.get(i).getId();
            String methodId = mainInfoList.get(i).getMethodId();
            String parentId = mainInfoList.get(i).getParentId();
            List<String> calcParam = this.getBaseMapper().selectCalcParam(id.toString());
            for (int j = 0; j < calcParam.size(); j++) {
                // 这里拼接参数作跟dataMap作比较
                String nowKey = id.concat(calcParam.get(j));
                if (dataMap.containsKey(nowKey) && !StringUtils.isBlank(dataMap.get(nowKey))) {
                    calcParamMap.put(calcParam.get(j), Double.parseDouble(dataMap.get(nowKey)));
                } else {
                    calcParamMap.clear();
                }
            }
            EvalResultDetail evalResultDetail = new EvalResultDetail();
            double val = 0;
            // 计算
            if (calcParamMap.size() > 0) {
                // 评价方法为空计算，但是不进行区间判断
                if (!StringUtils.isEmpty(methodId)) {
                    // 参数为计算公式 和 calcParamMap
                    String expression = formulasMap.get(id);
                    int num = 0;
                    if(!StringUtils.isEmpty(expression)){
                        if(expression.indexOf("A") != -1){
                            if(expression.indexOf("m") != -1){//3
                                num = 3;
                            }else {//2
                                num = 2;
                            }
                        }else {//5
                            num = 5;
                        }
                        if(calcParamMap.keySet().size() == num){
                            val = EvalCalculationUtils.calc(expression, calcParamMap);
                            // 根据id查询数据库中的评价方法的范围
                            val = EvalCalculationUtils.originalCalc(formulasMap.get(id), calcParamMap);
                            String s = this.getBaseMapper().calcRange(methodId, val);
                            if (StringUtils.isBlank(s)) {
                                val = 0;
                            } else {
                                val = Double.parseDouble(s);
                            }
                        }

                    }

                } else {
                    String expression = formulasMap.get(id);
                    int num = 0;
                    if(!StringUtils.isEmpty(expression)){
                        if(expression.indexOf("A") != -1){
                            if(expression.indexOf("m") != -1){//3
                                num = 3;
                            }else {//2
                                num = 2;
                            }
                        }else {//5
                            num = 5;
                        }
                        if(calcParamMap.keySet().size() == num){
                            val = EvalCalculationUtils.calc(expression, calcParamMap);
                        }
                    }
                }
            }
            evalResultDetail.setId(id);
            evalResultDetail.setResultId(resultId);
            evalResultDetail.setScore(val);
            evalResultDetails.add(evalResultDetail);
            // 算二级度量的分数
            if(collect != null && collect.size() > 0){
                if (Integer.valueOf(collect.get(id)).intValue() == 100) {
                    double weightVal = val;
                    if (parentCalcMap.containsKey(parentId)) {
                        double curVal = parentCalcMap.get(parentId).getScore();
                        parentCalcMap.get(parentId).setScore(curVal + weightVal);
                    } else {
                        parentCalcMap.put(parentId, new CompAnalysisCalcVO(mainInfoList.get(i).getGrandId(), weightVal));
                    }
                } else {
                    double weightVal = val * Double.parseDouble(collect.get(parentId)) / 100;
                    if (parentCalcMap.containsKey(parentId)) {
                        double curVal = parentCalcMap.get(parentId).getScore();
                        parentCalcMap.get(parentId).setScore(curVal + weightVal);
                    } else {
                        parentCalcMap.put(parentId, new CompAnalysisCalcVO(mainInfoList.get(i).getGrandId(), weightVal));
                    }
                }
            }
        }
        parentCalcMap.forEach((k, v) -> {
            double weightVal = v.getScore() * Double.parseDouble(collect.get(k)) / 100;
            EvalResultDetail evalResultDetail = new EvalResultDetail();
            evalResultDetail.setId(k);
            evalResultDetail.setResultId(resultId);
            evalResultDetail.setScore(parentCalcMap.get(k).getScore());
            evalResultDetails.add(evalResultDetail);
            if (grandCalcMap.containsKey(v.getParentId())) {
                double curVal = grandCalcMap.get(v.getParentId());
                grandCalcMap.put(v.getParentId(), curVal + weightVal);
            } else {
                grandCalcMap.put(v.getParentId(), weightVal);
            }
        });
        double grandCalcVal = 0D;
        for (Map.Entry<String, Double> entry : grandCalcMap.entrySet()) {
            EvalResultDetail evalResultDetail = new EvalResultDetail();
            evalResultDetail.setId(entry.getKey());
            evalResultDetail.setResultId(resultId);
            evalResultDetail.setScore(entry.getValue());
            evalResultDetails.add(evalResultDetail);
            grandCalcVal += entry.getValue() * Double.parseDouble(collect.get(entry.getKey())) / 100;
        }
        EvalSystem evalSystem = evalSystemService.getById(systemId);
        String leval = evalMethodInfoService.getEvaluate(evalSystem.getMethodId(), grandCalcVal);
        evalAnalysisResult.setEvaluate(leval);
        // 计算当前评价体系的总得分更新到eval_analysis_result
        evalAnalysisResult.setScore(grandCalcVal);
        evalAnalysisResult.setProcessStatus(1);
        evalAnalysisResultMapper.updateById(evalAnalysisResult);
        // 批量更新eval_result_detail 记录表 test
        Map<String, Object> map = new HashMap<>();
        map.put("result_id", evalAnalysisResult.getId());
        evalResultDetailService.removeByMap(map);
        evalResultDetailService.saveBatch(evalResultDetails);

        return Result.ok("分析数据成功");
    }

}
