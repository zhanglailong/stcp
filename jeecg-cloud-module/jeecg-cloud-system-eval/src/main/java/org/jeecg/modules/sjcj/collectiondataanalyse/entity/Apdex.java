package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

import lombok.Data;

/**
 * apdex 实体
 */

@Data
/**
 * @Author: test
 * */
public class Apdex {

    private String apdex;

    private String tolerationThreshold;

    private String frustrationThreshold;

    private String label;

}
