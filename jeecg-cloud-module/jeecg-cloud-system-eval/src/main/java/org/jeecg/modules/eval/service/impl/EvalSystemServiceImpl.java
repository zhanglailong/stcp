package org.jeecg.modules.eval.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;
import org.jeecg.modules.eval.entity.EvalSystem;
import org.jeecg.modules.eval.mapper.EvalMeasureInfoMapper;
import org.jeecg.modules.eval.mapper.EvalMeasureWeightMapper;
import org.jeecg.modules.eval.mapper.EvalSystemMapper;
import org.jeecg.modules.eval.service.IEvalSystemService;
import org.jeecg.modules.running.project.entity.RunningProject;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * @Description: eval_system
 * @Author: jeecg-boot
 * @Date: 2021-02-24
 * @Version: V1.0
 */
@Service
public class EvalSystemServiceImpl extends ServiceImpl<EvalSystemMapper, EvalSystem> implements IEvalSystemService {

    @Autowired
    EvalMeasureInfoMapper evalMeasureInfoMapper;

    @Autowired
    EvalMeasureWeightMapper evalMeasureWeightMapper;

    @Override
    @DS("uutDatabase")
    public List<RunningUutList> getUutList(String systemId){
        return this.getBaseMapper().getUutList(systemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMeasureAndWeightInfo(String systemId) {
        // 删除【eval_measure_weight】表
        evalMeasureWeightMapper.delete(new LambdaQueryWrapper<EvalMeasureWeight>().eq(EvalMeasureWeight::getSystemId, systemId));

        // 删除【eval_measure_info】表
        evalMeasureInfoMapper.delete(new LambdaQueryWrapper<EvalMeasureInfo>().eq(EvalMeasureInfo::getSystemId, systemId));

        // 删除【eval_system】表
        removeById(systemId);
    }

    @Override
    public RunningProject findUniqueBy(String fieldName, String value) {
        return this.getBaseMapper().findUniqueBy(fieldName, value);
    }

    @Override
    public int getTurnNum(String projectId) {
        return this.getBaseMapper().getTurnNum(projectId);
    }

    @Override
    public String getTurnName(String turnId) {
        return this.getBaseMapper().getTurnName(turnId);
    }

    @Override
    public List<String> getTaskIds(String projectId, String turnId, int delStatus) {
        return this.getBaseMapper().getTaskIds(projectId, turnId, delStatus);
    }

    @Override
    public List<String> getCaseIds(List<String> taskIds, String turnId) {
        return this.getBaseMapper().getCaseIds(taskIds, turnId);
    }

    @Override
    public List<String> getAccessCaseIds(List<String> taskIds, String turnId) {
        return this.getBaseMapper().getAccessCaseIds(taskIds, turnId);
    }

    @Override
    public int getQuestionIds(List<String> caseIds, String turnId) {
        return this.getBaseMapper().getQuestionIds(caseIds, turnId);
    }

    @Override
    public int getSolvedQuestionIds(List<String> caseIds, String turnId) {
        return this.getBaseMapper().getSolvedQuestionIds(caseIds, turnId);
    }

    @Override
    public int getExecutedCaseCount(List<String> taskIds, String turnId) {
        return this.getBaseMapper().getExecutedCaseCount(taskIds, turnId);
    }

    @Override
    public List<String> getTurnList(String projectId) {
        return this.getBaseMapper().getTurnList(projectId);
    }

    @Override
    public Map<String, Object> getTurnTask(String projectId, String taskStatus) {
        return this.getBaseMapper().getTurnTask(projectId, taskStatus, 0);
    }

    @Override
    public List<Map<String, Object>> getTurnCase(String projectId, String taskStatus) {
        return this.getBaseMapper().getTurnCase(projectId, taskStatus, 0);
    }

    @Override
    public List<Map<String, Object>> getTurnCaseExecute(String projectId, String taskStatus) {
        return this.getBaseMapper().getTurnCaseExecute(projectId, taskStatus, 0);
    }

    @Override
    public List<Map<String, Object>> getTurnQuestionSolved(String projectId, String taskStatus) {
        return this.getBaseMapper().getTurnQuestionSolved(projectId, taskStatus, 0);
    }

    @Override
    public List<Map<String, Object>> getParamsBySid(String sid) {
        return this.getBaseMapper().getParamsBySid(sid);
    }
}
