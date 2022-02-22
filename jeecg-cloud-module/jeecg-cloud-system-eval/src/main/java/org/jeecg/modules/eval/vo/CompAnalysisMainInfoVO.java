package org.jeecg.modules.eval.vo;

import lombok.Data;

@Data
/**
 * @Author: test
 * */
public class CompAnalysisMainInfoVO {

    private String systemId;
    private String id;
    private String name;
    private String parentId;
    private String parentName;
    private String grandId;
    private String grandName;
    private String formula;
    private String formulaName;
    private String methodId;
}
