package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

import lombok.Data;

/**
 * errors 实体
 */
@Data
/**
 * @Author: test
 * */
public class Errors {

    private String typeOfError;

    private String numberOfErrors;

    private String inErrors;

    private String inAllSamples;
}
