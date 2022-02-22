package org.jeecg.modules.eval.vo;

import lombok.Data;

@Data
/**
 * @Author: test
 * */
public class CompAnalysisCalcVO {

    private String parentId;
    private Double score;

    public CompAnalysisCalcVO(String parentId, Double score) {
        this.parentId = parentId;
        this.score = score;
    }
}
