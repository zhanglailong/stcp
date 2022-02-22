package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

import lombok.Data;

import java.util.List;
@Data
/**
 * @Author: test
 * */
public class JmeterAnbalyseSeries {

    private List data;

    private Boolean isOverall;

    private String label;

    private Boolean isController;
}
