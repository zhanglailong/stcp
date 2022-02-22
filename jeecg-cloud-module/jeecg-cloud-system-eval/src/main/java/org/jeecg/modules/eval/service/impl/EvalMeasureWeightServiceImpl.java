package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.*;
import org.jeecg.modules.eval.mapper.EvalMeasureWeightMapper;
import org.jeecg.modules.eval.service.IEvalMeasureInfoService;
import org.jeecg.modules.eval.service.IEvalMeasureWeightService;
import org.jeecg.modules.eval.service.IEvalSystemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: eval_measure_weight
 * @Author: jeecg-boot
 * @Date: 2021-02-25
 * @Version: V1.0
 */
@Service
public class EvalMeasureWeightServiceImpl extends ServiceImpl<EvalMeasureWeightMapper, EvalMeasureWeight> implements IEvalMeasureWeightService {

    @Autowired
    private IEvalMeasureWeightService evalMeasureWeightService;

    @Autowired
    private EvalMeasureWeightMapper evalMeasureWeightMapper;

    @Autowired
    private IEvalMeasureInfoService evalMeasureInfoService;

    @Autowired
    private IEvalSystemService evalSystemService;

    @Override
    public List<CaseTreeIdModel> queryTreeList(String systemId) {
        /* 获取树形下拉列表数据*/
        List<EvalMeasureInfoVo> list = evalMeasureWeightMapper.queryTreeList(systemId);
        List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();
        /*处理中的子特性*/
        String prePid = null;
        /*处理中的质量特性*/
        String preRootId = null;
        /*质量特性节点*/
        CaseTreeIdModel rootModel = null;
        /*子特性节点*/
        CaseTreeIdModel parentModel = null;
        if (null != list && list.size() > 0) {
            for (EvalMeasureInfoVo caseTreeVO : list) {
                String pid = caseTreeVO.getParentId();
                String rootId = caseTreeVO.getGrandId();
                /*初期设定：将处理中的质量特性ID和子特性ID记录下来*/
                if (preRootId == null && prePid == null) {
                    prePid = pid;
                    preRootId = rootId;
                    rootModel = new CaseTreeIdModel(caseTreeVO.getGrandWeight(), caseTreeVO.getIdGrand());
                    rootModel.setKey(rootId);
                    rootModel.setValue(rootId);
                    rootModel.setLevel(1);
                    parentModel = new CaseTreeIdModel(caseTreeVO.getParentWeight(), caseTreeVO.getIdParent(), caseTreeVO.getIdGrand());
                    parentModel.setKey(pid);
                    parentModel.setValue(pid);
                    parentModel.setGrandId(preRootId);
                    parentModel.setLevel(2);
                }
                // 比较处理中的质量特性ID和当前质量特性ID
                if (preRootId.equals(rootId)) {
                    /*比较处理中的子特性ID和当前子特性ID*/
                    if (prePid.equals(pid)) {
                        /*相同的情况下，添加度量节点*/
                        CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO);
                        childModel.setGrandId(preRootId);
                        childModel.setLevel(3);
                        parentModel.getChildren().add(childModel);
                    } else {
                        /*不相同的情况下，将处理中的子特性信息添加到处理中的质量特性的节点下方*/
                        rootModel.getChildren().add(parentModel);
                        /*重新初始化子特性ID*/
                        prePid = pid;
                        /*创建子特性节点*/
                        parentModel = new CaseTreeIdModel(caseTreeVO.getParentWeight(), caseTreeVO.getIdParent(), caseTreeVO.getIdGrand());
                        parentModel.setKey(pid);
                        parentModel.setValue(pid);
                        parentModel.setGrandId(preRootId);
                        parentModel.setLevel(2);
                        // 添加度量节点
                        CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO);
                        childModel.setGrandId(preRootId);
                        childModel.setLevel(3);
                        parentModel.getChildren().add(childModel);
                    }
                } else {
                    /*更换了质量特性ID的时候，将处理中的子特性信息添加到处理中的质量特性的节点下方*/
                    rootModel.getChildren().add(parentModel);
                    treeList.add(rootModel);
                    /*重新初始化处理中的质量特性ID和当前质量特性ID*/
                    preRootId = rootId;
                    prePid = pid;
                    /*创建质量特性节点*/
                    rootModel = new CaseTreeIdModel(caseTreeVO.getGrandWeight(), caseTreeVO.getIdGrand());
                    rootModel.setKey(rootId);
                    rootModel.setValue(rootId);
                    rootModel.setLevel(1);
                    /*创建子特性节点*/
                    parentModel = new CaseTreeIdModel(caseTreeVO.getParentWeight(), caseTreeVO.getIdParent(), caseTreeVO.getIdGrand());
                    parentModel.setKey(pid);
                    parentModel.setValue(pid);
                    parentModel.setGrandId(preRootId);
                    parentModel.setLevel(2);
                    /*添加度量节点*/
                    CaseTreeIdModel childModel = new CaseTreeIdModel(caseTreeVO);
                    childModel.setGrandId(preRootId);
                    childModel.setLevel(3);
                    parentModel.getChildren().add(childModel);
                }
            }
        }
        /*将最后的处理的子特性信息添加到处理中的质量特性的节点下方*/
        if (null != rootModel) {
            rootModel.getChildren().add(parentModel);
        }
        treeList.add(rootModel);
        return treeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveList(List<CaseTreeIdModel> list, String systemId) {
        QueryWrapper<EvalMeasureWeight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_id", systemId);
        evalMeasureWeightService.remove(queryWrapper);
        List<EvalMeasureWeight> result = new ArrayList<>();
        /*权重保存*/
        for (CaseTreeIdModel caseTreeIdModel : list) {
            EvalMeasureWeight evalMeasureWeight1 = new EvalMeasureWeight();
            BeanUtils.copyProperties(caseTreeIdModel, evalMeasureWeight1);
            evalMeasureWeight1.setSystemId(systemId);
            evalMeasureWeight1.setMeasureId(caseTreeIdModel.getKey());
            result.add(evalMeasureWeight1);
            for (CaseTreeIdModel list1 : caseTreeIdModel.getChildren()) {
                EvalMeasureWeight evalMeasureWeight2 = new EvalMeasureWeight();
                BeanUtils.copyProperties(list1, evalMeasureWeight2);
                evalMeasureWeight2.setSystemId(systemId);
                evalMeasureWeight2.setMeasureId(list1.getKey());
                result.add(evalMeasureWeight2);
                for (CaseTreeIdModel list2 : list1.getChildren()) {
                    EvalMeasureWeight evalMeasureWeight3 = new EvalMeasureWeight();
                    BeanUtils.copyProperties(list2, evalMeasureWeight3);
                    evalMeasureWeight3.setSystemId(systemId);
                    evalMeasureWeight3.setMeasureId(list2.getKey());
                    result.add(evalMeasureWeight3);
                }
            }
        }
        boolean flag = evalMeasureWeightService.saveBatch(result);
        EvalSystem es = new EvalSystem();
        es.setId(systemId);
        es.setWeightDone(1);
        evalSystemService.updateById(es);
        return flag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWeight(String systemId, CaseTreeIdModel caseTreeIdModel) {
        int level3 = 3;
        int level2 = 2;
        int level = caseTreeIdModel.getLevel();
        //删除三级
        if (level == level3) {
            return levelThird(systemId, caseTreeIdModel);
        }

        //删除二级
        if (level == level2) {
            return levelTwo(systemId, caseTreeIdModel);
        }

        //删除一级
        if (level == 1) {
            return levelOne(systemId, caseTreeIdModel);
        }
        return true;
    }

    @Override
    public List<EvalMeasureWeight> getListBySystemId(String systemId) {
        return evalMeasureWeightMapper.getListBySystemId(systemId);
    }

    private boolean levelOne(String systemId, CaseTreeIdModel caseTreeIdModel) {
        //权重信息id
        List<String> ids = new ArrayList<>();
        ids.add(caseTreeIdModel.getId());
        for (CaseTreeIdModel children : caseTreeIdModel.getChildren()) {
            ids.add(children.getId());
            for (CaseTreeIdModel childrenList : children.getChildren()) {
                ids.add(childrenList.getId());
            }
        }
        //删除权重表数据
        evalMeasureWeightMapper.deleteBatchIds(ids);
        //删除info表信息
        LambdaQueryWrapper<EvalMeasureInfo> queryWrapper = new LambdaQueryWrapper<EvalMeasureInfo>();
        queryWrapper.eq(EvalMeasureInfo::getGrandId, caseTreeIdModel.getKey());
        queryWrapper.eq(EvalMeasureInfo::getSystemId, systemId);
        evalMeasureInfoService.remove(queryWrapper);
        return true;
    }

    private boolean levelTwo(String systemId, CaseTreeIdModel caseTreeIdModel) {
        //权重信息id
        List<String> ids = new ArrayList<>();
        //权重二级存在个数
        int num = 2;
        if (caseTreeIdModel.getLevel() == num) {
            EvalMeasureStructureVo evalMeasureStructureVo = evalMeasureWeightMapper.queryGrand(caseTreeIdModel.getGrandId(), systemId, caseTreeIdModel.getKey());
            EvalMeasureStructureVo result = evalMeasureWeightMapper.queryGrandOne(caseTreeIdModel.getGrandId(), systemId);
            //一级下一个二级
            if (result.getCountId() == 1) {
                ids.add(caseTreeIdModel.getId());
                ids.add(caseTreeIdModel.getRootId());
                for (CaseTreeIdModel children : caseTreeIdModel.getChildren()) {
                    ids.add(children.getId());
                }
            }
            //一级下多个二级
            if (evalMeasureStructureVo.getCountId() == 0) {
                ids.add(caseTreeIdModel.getId());
                ids.add(caseTreeIdModel.getRootId());
                for (CaseTreeIdModel children : caseTreeIdModel.getChildren()) {
                    ids.add(children.getId());
                }
            } else {
                ids.add(caseTreeIdModel.getId());
                for (CaseTreeIdModel children : caseTreeIdModel.getChildren()) {
                    ids.add(children.getId());
                }
            }
        }
        //删除权重表数据
        evalMeasureWeightMapper.deleteBatchIds(ids);
        //删除info表信息
        LambdaQueryWrapper<EvalMeasureInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EvalMeasureInfo::getParentId, caseTreeIdModel.getKey());
        lambdaQueryWrapper.eq(EvalMeasureInfo::getSystemId, systemId);
        evalMeasureInfoService.remove(lambdaQueryWrapper);
        return true;
    }

    private boolean levelThird(String systemId, CaseTreeIdModel caseTreeIdModel) {
        //权重信息id
        List<String> ids = new ArrayList<>();
        //权重三级个数
        EvalMeasureStructureVo evalMeasureStructureVo3 = evalMeasureWeightMapper.queryCount(caseTreeIdModel.getKey());
        //权重二级个数
        EvalMeasureStructureVo result = evalMeasureWeightMapper.queryGrandOne(caseTreeIdModel.getGrandId(), systemId);
        //一个三级，一个二级
        if (evalMeasureStructureVo3.getCountId() == 1 && result.getCountId() == 1) {
            ids.add(caseTreeIdModel.getId());
            ids.add(caseTreeIdModel.getParentId());
            ids.add(caseTreeIdModel.getRootId());
        } else if (evalMeasureStructureVo3.getCountId() != 1) {
            ids.add(caseTreeIdModel.getId());
        } else {
            ids.add(caseTreeIdModel.getId());
            ids.add(caseTreeIdModel.getParentId());
        }
        //删除权重表数据
        evalMeasureWeightMapper.deleteBatchIds(ids);
        //删除info信息
        LambdaQueryWrapper<EvalMeasureInfo> lambdaQueryWrapper = new LambdaQueryWrapper<EvalMeasureInfo>();
        lambdaQueryWrapper.eq(EvalMeasureInfo::getSid, caseTreeIdModel.getKey());
        lambdaQueryWrapper.eq(EvalMeasureInfo::getSystemId, systemId);
        evalMeasureInfoService.remove(lambdaQueryWrapper);
        return true;
    }
}