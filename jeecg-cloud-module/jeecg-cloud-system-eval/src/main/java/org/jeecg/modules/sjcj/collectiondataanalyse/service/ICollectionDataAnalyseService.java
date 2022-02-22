package org.jeecg.modules.sjcj.collectiondataanalyse.service;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.CollectionDataAnalyse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.List;

/**
 * @Description: 采集数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
public interface ICollectionDataAnalyseService extends IService<CollectionDataAnalyse> {
    /**
     * 添加CollectionDataAnalyse
     * @param list /
     * @throws IOException
     * @return /
     */
    void addCollectionDataAnalyse(List<CollectionDataAnalyse> list) throws IOException;
    /**
     * 添加addCharts
     * @param url /
     * @throws IOException
     * @throws ScriptException
     * @return /
     */
    String addCharts(String url) throws IOException, ScriptException;
    /**
     * 添加Dashboard
     * @param resultDataAnalysis /
     * @throws Exception
     * @return /
     */
    JSONObject addDashboard(ResultDataAnalysis resultDataAnalysis) throws Exception;
}
