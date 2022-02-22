package org.jeecg.modules.eval.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
/**
 * @Author: test
 * */
public class CaseTreeIdModel implements Serializable {

    private static final long serialVersionUID = 1L;
    List<CaseTreeIdModel> children = new ArrayList<>();
    /**
    *主键ID
    */
    private String key;
    /**
     *主键ID
     */
    private String value;
   /**
    * 度量名称
    */
    private String title;

    private String weight;

    private String id;

    private String sid;

    private String systemId;

    private String parentId;

    private String rootId;

    private String grandId;

    private int level;

    private Double score;

    private String formula;

    public CaseTreeIdModel(EvalMeasureInfoVo caseTreeVO,String grandId,Integer level) {
        this.setTitle(caseTreeVO.getName());
        this.setKey(caseTreeVO.getId());
        this.setValue(caseTreeVO.getId());
        this.setWeight(caseTreeVO.getWeight());
        this.setId(caseTreeVO.getId());
        this.setParentId(caseTreeVO.getIdParent());
        this.setRootId(caseTreeVO.getIdGrand());
        this.setGrandId(grandId);
        this.setLevel(level);
    }

    public CaseTreeIdModel(String rootId,String title,String weight,String id,Double score,Integer level) {
        this.setKey(rootId);
        this.setValue(rootId);
        this.setTitle(title);
        this.setWeight(weight);
        this.setId(id);
        this.setScore(score);
        this.setLevel(level);
    }

    public CaseTreeIdModel(String pid,String title,String weight,String id,String rootId,Double score,Integer level) {
        this.setKey(pid);
        this.setValue(pid);
        this.setTitle(title);
        this.setWeight(weight);
        this.setId(id);
        this.setRootId(rootId);
        this.setScore(score);
        this.setLevel(level);
    }

    public CaseTreeIdModel(EvalMeasureInfoVo caseTreeVO) {
        //this.setTitle(caseTreeVO.getName());
        this.setKey(caseTreeVO.getSid());
        this.setValue(caseTreeVO.getSid());
        this.setWeight(caseTreeVO.getWeight());
        this.setId(caseTreeVO.getId());
        this.setParentId(caseTreeVO.getIdParent());
        this.setRootId(caseTreeVO.getIdGrand());
    }

    public CaseTreeIdModel(String weight,String id) {
        this.setWeight(weight);
        this.setId(id);
    }

    public CaseTreeIdModel(String weight,String id,String rootId) {
        this.setWeight(weight);
        this.setId(id);
        this.setRootId(rootId);
    }

    public CaseTreeIdModel(String title,String weight,String id,String rootId,String formula) {
        this.setTitle(title);
        this.setWeight(weight);
        this.setId(id);
        this.setRootId(rootId);
        this.setFormula(formula);
    }

    public CaseTreeIdModel() {
    }

    public static long getSerialVersionUid() {
        return serialVersionUID;
    }
}
