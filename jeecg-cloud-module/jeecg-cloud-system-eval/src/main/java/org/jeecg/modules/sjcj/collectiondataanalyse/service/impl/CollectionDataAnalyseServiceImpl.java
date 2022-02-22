package org.jeecg.modules.sjcj.collectiondataanalyse.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.opencensus.metrics.export.Summary;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.CollectionDataAnalyse;
import org.jeecg.modules.sjcj.collectiondataanalyse.mapper.CollectionDataAnalyseMapper;
import org.jeecg.modules.sjcj.collectiondataanalyse.service.ICollectionDataAnalyseService;
import org.jeecg.modules.sjcj.collectiondataanalyse.util.*;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 采集数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Service
public class CollectionDataAnalyseServiceImpl extends ServiceImpl<CollectionDataAnalyseMapper, CollectionDataAnalyse> implements ICollectionDataAnalyseService {

    public static final String OVER_TIME = "OverTime";

    public static final String THROUGHPUT = "Throughput";

    public static final String RESPONSE_TIMES = "ResponseTimes";

    public static final String ANALYSIS_SUMMARY = "analysisSummary";

    public static final String RUNNING_VUSERS = "runningVusers";

    public static final String HIT_SPER_SECOND = "hitSperSecond";

    public static final String APDEX = "apdex";

    public static final String STATISTICS = "statistics";

    public static final String ERRORS = "errors";

    public static final String  DASHBOARD = "dashboard";

    @Autowired
    private CollectionDataAnalyseMapper collectionDataAnalyseMapper;

    @Override
    public void addCollectionDataAnalyse(List<CollectionDataAnalyse> list) throws IOException {
        this.collectionDataAnalyseMapper.batchInster(list);
    }


    /**
     * 解析  Charts页面 js
     * @throws IOException
     * @throws ScriptException
     */
    @Override
    public String addCharts(String url) throws IOException, ScriptException {

        // 创建list集合
        List<CollectionDataAnalyse> list = new ArrayList<>();
        String path = HttpClientUtils.getResponseBody("http://192.168.1.90:9000/test/testbaidu/JMeter/content/js/graph.js");
        path = path.substring(path.indexOf("var"));
        // 接收路径
        // Response Times
        JSONObject result =  JmeterChartsUtils.anbalyseJs(path,"responseTimeDistributionInfos");
        JSONObject responseTimePercentiles = JmeterChartsUtils.anbalyseJs(path,"responseTimePercentilesInfos");
        JSONObject timeVsThreads  = JmeterChartsUtils.anbalyseJs(path, "timeVsThreadsInfos");

        // Over Time
        JSONObject responseTimesOverTime = JmeterChartsUtils.anbalyseJs(path, "responseTimesOverTimeInfos");
        JSONObject responseTimePercentilesOverTime = JmeterChartsUtils.anbalyseJs(path, "responseTimePercentilesOverTimeInfos");
        JSONObject activeThreadsOverTime = JmeterChartsUtils.anbalyseJs(path, "activeThreadsOverTimeInfos");
        JSONObject bytesThroughputOverTime = JmeterChartsUtils.anbalyseJs(path, "bytesThroughputOverTimeInfos");
        JSONObject latenciesOverTime = JmeterChartsUtils.anbalyseJs(path, "latenciesOverTimeInfos");
        JSONObject connectTimeOverTime = JmeterChartsUtils.anbalyseJs(path, "connectTimeOverTimeInfos");

        // Throughput
        JSONObject hitsPerSecond = JmeterChartsUtils.anbalyseJs(path, "hitsPerSecondInfos");
        JSONObject codesPerSecond = JmeterChartsUtils.anbalyseJs(path, "codesPerSecondInfos");
        JSONObject transactionsPerSecond = JmeterChartsUtils.anbalyseJs(path, "transactionsPerSecondInfos");
        JSONObject totalTps = JmeterChartsUtils.anbalyseJs(path, "totalTPSInfos");
        JSONObject responseTimeVsRequest = JmeterChartsUtils.anbalyseJs(path, "responseTimeVsRequestInfos");
        JSONObject latenciesVsRequest = JmeterChartsUtils.anbalyseJs(path, "latenciesVsRequestInfos");

        // responseTimes
        // 创建 新的实体对象
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonResponseTimesArray = new JSONArray();
        jsonResponseTimesArray.add(result);
        jsonResponseTimesArray.add(responseTimePercentiles);
        jsonResponseTimesArray.add(timeVsThreads);
        jsonObject.put(RESPONSE_TIMES,jsonResponseTimesArray);
        CollectionDataAnalyse responseTimes = setValue(RESPONSE_TIMES, jsonObject.toString(), "192.168.1.110", "1");
        list.add(responseTimes);

        // throughput
        JSONObject jsonObject1 = new JSONObject();
        JSONArray jsonThroughputArray = new JSONArray();
        jsonThroughputArray.add(hitsPerSecond);
        jsonThroughputArray.add(codesPerSecond);
        jsonThroughputArray.add(transactionsPerSecond);
        jsonThroughputArray.add(totalTps);
        jsonThroughputArray.add(responseTimeVsRequest);
        jsonThroughputArray.add(latenciesVsRequest);
        jsonObject1.put(THROUGHPUT,jsonThroughputArray);
        CollectionDataAnalyse throughput = setValue(THROUGHPUT,jsonObject1.toString(),"192.168.1.110","1");
        list.add(throughput);

        // OverTime
        JSONObject jsonObject2 = new JSONObject();
        JSONArray jsonOverTimeArray = new JSONArray();
        jsonOverTimeArray.add(responseTimesOverTime);
        jsonOverTimeArray.add(responseTimePercentilesOverTime);
        jsonOverTimeArray.add(bytesThroughputOverTime);
        jsonOverTimeArray.add(activeThreadsOverTime);
        jsonOverTimeArray.add(latenciesOverTime);
        jsonOverTimeArray.add(connectTimeOverTime);
        jsonObject2.put(OVER_TIME,jsonOverTimeArray);
        CollectionDataAnalyse overTime = setValue(OVER_TIME,jsonObject2.toString(),"192.168.1.110","1");
        list.add(overTime);

        // 批量插入
        this.collectionDataAnalyseMapper.batchInster(list);
        return list.toString();
    }


    /**
     * 解析  Dashboard的html页面 service
     * @throws Exception
     */
    @Override
    public JSONObject addDashboard(ResultDataAnalysis resultDataAnalysis) throws Exception {
        String url = resultDataAnalysis.getUrl();
        url = url + "index.html";

        JSONObject apedx = JmeterDashboardUtils.analysisApdex(url);

        JSONObject statistics = JmeterDashboardUtils.analysisStatistics(url);

        JSONObject error = JmeterDashboardUtils.analysisErrors(url);

        JSONObject timeAndItem = JmeterDashboardUtils.analysisReport(url);

        // 创建jsonObject
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("apedx",apedx);
        jsonObject.put("statistics",statistics.containsKey("statistics")? JSONArray.parseArray(statistics.getString("statistics")): "");
        jsonObject.put("error",error);
        jsonObject.put("timeAndItem",timeAndItem);
        CollectionDataAnalyse collectionDataAnalyse = setValue(DASHBOARD,jsonObject.toString(),resultDataAnalysis.getAgentIp(),resultDataAnalysis.getToolId());

        this.save(collectionDataAnalyse);
        return jsonObject;
    }

    /**
     * setvalue方法
     * @param dataType
     * @param resultData
     * @param ip
     * @param toolsId
     * @return
     */
    public static CollectionDataAnalyse setValue(String dataType, String resultData, String ip, String toolsId) {
        CollectionDataAnalyse collectionDataAnalyse = new CollectionDataAnalyse();
        collectionDataAnalyse.setDataType(dataType);
        collectionDataAnalyse.setResultData(resultData);
        collectionDataAnalyse.setIp(ip);
        collectionDataAnalyse.setToolsId(toolsId);
        return collectionDataAnalyse;
    }
}
