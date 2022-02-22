package org.jeecg.modules.eval.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
/**
 * @Author: test
 * */
public class EvalMeasureInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    private String id;
    private String sid;
    private String ids;
    private String idParent;
    private String idGrand;

    /**
     * 度量
     */
    private String name;

    /**
     * 质量子特id
     */
    private String parentId;

    /**
     * 质量子特性
     */
    private String parentName;

    /**
     * 质量特性id
     */
    private String grandId;

    /**
     * 质量特性
     */
    private String grandName;

    private String weight;

    private String parentWeight;

    private String grandWeight;

    private String formula;
}
