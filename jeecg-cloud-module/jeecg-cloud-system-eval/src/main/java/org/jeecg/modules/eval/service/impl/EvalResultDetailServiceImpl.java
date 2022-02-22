package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalMeasureInfoVo;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;
import org.jeecg.modules.eval.entity.EvalResultDetail;
import org.jeecg.modules.eval.mapper.EvalMeasureWeightMapper;
import org.jeecg.modules.eval.mapper.EvalResultDetailMapper;
import org.jeecg.modules.eval.service.IEvalResultDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 分析结果详情表
 * @Author: jeecg-boot
 * @Date:   2020-12-31
 * @Version: V1.0
 */
@Service
public class EvalResultDetailServiceImpl extends ServiceImpl<EvalResultDetailMapper, EvalResultDetail> implements IEvalResultDetailService {

	@Autowired
	EvalResultDetailMapper evalResultDetailMapper;

	@Autowired
	EvalMeasureWeightMapper evalMeasureWeightMapper;

	@Override
	public List<CaseTreeIdModel> queryTreeList(String systemId, String resultId) {
		// 获取树形下拉列表数据
		List<EvalMeasureInfoVo> list = evalMeasureWeightMapper.queryTreeList(systemId);
		// 获取得分信息
		List<EvalResultDetail> scoreList = evalResultDetailMapper.selectList(
				new LambdaQueryWrapper<EvalResultDetail>().eq(EvalResultDetail::getResultId, resultId));
		if (scoreList == null || scoreList.size() <= 0) {
			return new ArrayList<CaseTreeIdModel>();
		}
		// 将得分信息转换成Map
		Map<String, Double> scoreMap = scoreList.stream().collect(Collectors.toMap(EvalResultDetail::getId, EvalResultDetail::getScore));
		//结果列表
		List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();
		// 处理中的子特性
		String prePid = null;
		// 处理中的质量特性
		String preRootId = null;
		// 质量特性节点
		CaseTreeIdModel rootModel = null;
		// 子特性节点
		CaseTreeIdModel parentModel = null;
		for (EvalMeasureInfoVo caseTreeVO : list) {
			String pid = caseTreeVO.getParentId();
			String rootId = caseTreeVO.getGrandId();
			// 初期设定：将处理中的质量特性ID和子特性ID记录下来
			if (preRootId == null && prePid == null) {
				prePid = pid;
				preRootId = rootId;
				rootModel = new CaseTreeIdModel(rootId,caseTreeVO.getGrandName(),caseTreeVO.getGrandWeight(),caseTreeVO.getIdGrand(),scoreMap.get(rootId),1);
				parentModel = new CaseTreeIdModel(pid,caseTreeVO.getParentName(),caseTreeVO.getParentWeight(),caseTreeVO.getIdParent(),caseTreeVO.getIdGrand(),scoreMap.get(pid),2);
				parentModel.setGrandId(preRootId);
			}
			// 比较处理中的质量特性ID和当前质量特性ID
			if (preRootId.equals(rootId)) {
				// 比较处理中的子特性ID和当前子特性ID
				if (prePid.equals(pid)) {
					// 相同的情况下，添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO,preRootId,3);
					childModel.setScore(scoreMap.get(caseTreeVO.getSid()));
					parentModel.getChildren().add(childModel);
				} else {
					// 不相同的情况下，将处理中的子特性信息添加到处理中的质量特性的节点下方
					rootModel.getChildren().add(parentModel);
					// 重新初始化子特性ID
					prePid = pid;
					// 创建子特性节点
					parentModel = new CaseTreeIdModel(pid,caseTreeVO.getParentName(),caseTreeVO.getParentWeight(),caseTreeVO.getIdParent(),caseTreeVO.getIdGrand(),scoreMap.get(pid),2);
					parentModel.setGrandId(preRootId);;
					// 添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO,preRootId,3);
					childModel.setScore(scoreMap.get(caseTreeVO.getSid()));
					parentModel.getChildren().add(childModel);
				}
			} else {
				// 更换了质量特性ID的时候，将处理中的子特性信息添加到处理中的质量特性的节点下方
				rootModel.getChildren().add(parentModel);
				treeList.add(rootModel);
				// 重新初始化处理中的质量特性ID和当前质量特性ID
				preRootId = rootId;
				prePid = pid;
				// 创建质量特性节点
				rootModel = new CaseTreeIdModel(rootId,caseTreeVO.getGrandName(),caseTreeVO.getGrandWeight(),caseTreeVO.getIdGrand(),scoreMap.get(rootId),1);
				// 创建子特性节点
				parentModel = new CaseTreeIdModel(pid,caseTreeVO.getGrandName(),caseTreeVO.getGrandWeight(),caseTreeVO.getIdParent(),caseTreeVO.getIdGrand(),scoreMap.get(pid),2);
				parentModel.setGrandId(preRootId);
				// 添加度量节点
				CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO,preRootId,3);
				childModel.setScore(scoreMap.get(caseTreeVO.getSid()));
				parentModel.getChildren().add(childModel);
			}
		}
		if (rootModel != null) {
			// 将最后的处理的子特性信息添加到处理中的质量特性的节点下方
			rootModel.getChildren().add(parentModel);
			treeList.add(rootModel);
		}
		return treeList;
	}
}
