package org.jeecg.modules.mass.vo;

import lombok.Data;

import java.io.Serializable;
@Data
/**
 * @Author: test
 * */
public class MassResultDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1级id
     */
    private String rootId;

    /**
     * 质量特性
     */
    private String rootName;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 质量子特性
     */
    private String parentName;

    /**
     * 序号id
     */
    private String id;

    /**
     * 度量元
     */
    private String name;

    /**
     * 原始分数
     */
    private String originalScore;

    /**
     * 用户打分
     */
    private String userScore;

    /**
     * 分析结果id
     */
    private String resultId;
}
