package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
/**
 * @Author: test
 * */
public class TransactionSummary {

    private String transactionName;

    private String miniMum;

    private String average;

    private String maxiMum;

    private String stdDeviation;

    private String percent;

    private String pass;

    private String fail;

    private String stop;

}
