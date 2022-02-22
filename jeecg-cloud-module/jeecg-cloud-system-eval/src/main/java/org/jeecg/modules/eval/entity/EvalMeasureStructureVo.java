package org.jeecg.modules.eval.entity;

import lombok.Data;

import java.io.Serializable;

@Data
/**
* @Author: test
* */
public class EvalMeasureStructureVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String countParent;

    private int countId;

    private String grandName;

    private String grandId;

    private  String parentName;

    private String parentId;

    private String name;

    private String id;

    private String formula;

    private Integer delFlag;

}
