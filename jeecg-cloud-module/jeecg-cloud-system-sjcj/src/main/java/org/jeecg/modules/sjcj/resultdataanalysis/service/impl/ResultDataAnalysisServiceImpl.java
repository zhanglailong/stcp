package org.jeecg.modules.sjcj.resultdataanalysis.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.running.entity.RunningProject;
import org.jeecg.modules.running.entity.RunningUutRecord;
import org.jeecg.modules.running.service.IRunningProjectService;
import org.jeecg.modules.running.service.IRunningUutRecordService;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.CollectionDataAnalyse;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.Graph;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.TestItem;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.TransactionSummary;
import org.jeecg.modules.sjcj.collectiondataanalyse.mapper.CollectionDataAnalyseMapper;
import org.jeecg.modules.sjcj.collectiondataanalyse.mapper.TestItemMapper;
import org.jeecg.modules.sjcj.collectiondataanalyse.service.ICollectionDataAnalyseService;
import org.jeecg.modules.sjcj.collectiondataanalyse.service.impl.CollectionDataAnalyseServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.sjcj.collectiondataanalyse.util.LoadRunnerAnbalyseUtils;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysisKv;
import org.jeecg.modules.sjcj.resultdataanalysis.mapper.ResultDataAnalysisMapper;
import org.jeecg.modules.sjcj.resultdataanalysis.service.IResultDataAnalysisKvService;
import org.jeecg.modules.sjcj.resultdataanalysis.service.IResultDataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description: 结果数据分析
 * @Author: jeecg-boot
 * @Date: 2021-01-18
 * @Version: V1.0
 */
@Service
public class ResultDataAnalysisServiceImpl extends ServiceImpl<ResultDataAnalysisMapper, ResultDataAnalysis> implements IResultDataAnalysisService {

    public static final String ANALYSIS_SUMMARY = "analysisSummary";

    public static final String RUNNING_VUSERS = "runningVusers";

    public static final String HIT_SPER_SECOND = "hitSperSecond";

    public static final String AVERAGE_RESPONSE = "AverageResponse";
    public static final String AVERAGE_TURNOVER = "AverageTurnover";
    public static final String MAXIMUM = "Maximum";
    public static final String DURATION = "Duration";
    public static final String AVERAGE = "Average";
    public static final String GRAPHMAXIMUM = "GraphMaximum";


    public static final String WAITINGTIME = "WaitingTime";
    public static final String BADTRANSTIMERATIO = "BadTransTimeRatio";
    public static final String BADRESPTIMERATIO = "BadRespTimeRatio";
    public static final String AVERAGETURNOVER = "AverageTurnover";
    public static final String TRANSCATION = "Transcation";
    public static final String AVERAGERESPONSE = "AverageResponse";

    public static final  ArrayList<String> TOOLID=new ArrayList<String>(Arrays.asList("1341647180284678146","1342658577659195394","1342658900054372353","1342659301919027201","1342659460316917762","1342659707554361346","1342659955639054337","1342660167866642433","1342660509505286145"));
    public static final int NUM2=2;public static final int NUM3=3;public static final int NUM4=4;public static final int NUM5=5;public static final int NUM6=6;public static final int NUM7=7;public static final int NUM8=8;
    public static final String TIMEANDITEM="timeAndItem";
    public static final String STATISTICS="statistics";
    @Autowired
    private CollectionDataAnalyseMapper collectionDataAnalyseMapper;

    @Autowired
    private TestItemMapper testItemMapper;

    @Autowired
    private ICollectionDataAnalyseService collectionDataAnalyseService;

    @Autowired
    private IResultDataAnalysisKvService resultDataAnalysisKvService;

    @Autowired
    private IRunningProjectService runningProjectService;

    @Autowired
    private IRunningUutRecordService runningUutRecordService;

    @Override
    public void add(ResultDataAnalysis resultDataAnalysisInput) throws Exception {
        // TODO 数据封装
        ResultDataAnalysis resultDataAnalysis = resolver(resultDataAnalysisInput);
        // 保存到数据库
        this.save(resultDataAnalysis);
    }

    public ResultDataAnalysis resolver(ResultDataAnalysis resultDataAnalysisInput) throws Exception {
        RunningProject runningProject = runningProjectService.getById(resultDataAnalysisInput.getProjectId());
        RunningUutRecord runningUutRecord = runningUutRecordService
                .findUniqueBy("uut_list_id", runningProject.getUutListId());
        if(null == runningUutRecord){
            runningUutRecord = new RunningUutRecord();
        }
        List<ResultDataAnalysisKv> resultDataAnalysisKvList = new ArrayList<>();
        if (StringUtils.equals(TOOLID.get(0), resultDataAnalysisInput.getToolId())) {
            // LDRA TESTBED
        } else if (StringUtils.equals(TOOLID.get(1), resultDataAnalysisInput.getToolId())) {
            // SQATEST-AT
        } else if (StringUtils.equals(TOOLID.get(NUM2), resultDataAnalysisInput.getToolId())) {
            // SQATEST-CoBot
        } else if (StringUtils.equals(TOOLID.get(NUM3), resultDataAnalysisInput.getToolId())) {
            // LoadRunner
            // 找result下一级目录名
            // 调用方法，取目录
            if(Optional.ofNullable(resultDataAnalysisInput).isPresent()) {
                String data = null;
                try {
                    data = loadRunnerResolver(resultDataAnalysisInput);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 根据test_item表 过滤数据
                LambdaQueryWrapper<TestItem> queryWrapper = new LambdaQueryWrapper();
                queryWrapper.eq(TestItem::getToolsId, resultDataAnalysisInput.getToolId());
                List<TestItem> testItems = testItemMapper.selectList(queryWrapper);
                JSONObject jsonObject = JSONObject.parseObject(data);
                JSONObject summary = (JSONObject) jsonObject.get("summary");
                JSONObject hitsPerSecond = (JSONObject)jsonObject.get("hitsPerSecond");
                Object graphJson = hitsPerSecond.get("graph");
                Graph graph = new ObjectMapper().convertValue(graphJson, Graph.class);
                JSONObject result = new JSONObject();
                JSONArray transactionSummary = summary.getJSONArray("TransactionSummary");
                List<TransactionSummary> transactionSummaries = JSONObject.parseArray(transactionSummary.toJSONString(), TransactionSummary.class);
                Optional<TransactionSummary> actionTransaction = transactionSummaries.stream().filter(h -> "Action_Transaction".equals(h.getTransactionName())).findFirst();
                for (TestItem testItem : testItems) {
//                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE_RESPONSE)) { if (actionTransaction.isPresent()) { result.put(testItem.getAbbr(), actionTransaction.get().getAverage()); } }
//                    if (StringUtils.equals(testItem.getEnglishName(), MAXIMUM)) { result.put(testItem.getAbbr(), actionTransaction.get().getMaxiMum()); }
//                    if (StringUtils.equals(testItem.getEnglishName(), DURATION)) { Object o = summary.get(DURATION);result.put(testItem.getAbbr(), o.toString()); }
//                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE_TURNOVER)) { Object o = summary.get(AVERAGE_TURNOVER);result.put(testItem.getAbbr(), actionTransaction.get().getAverage()); }
//                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE)) { result.put(testItem.getAbbr(), graph.getGraphAverage()); }
//                    if (StringUtils.equals(testItem.getEnglishName(), GRAPHMAXIMUM)) { result.put(testItem.getAbbr(), graph.getGraphMaximum()); }
                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE_RESPONSE)) { if (actionTransaction.isPresent()) { runningUutRecord.setAverageResponseTime(actionTransaction.get().getAverage());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), actionTransaction.get().getAverage(),new Date())); } }
                    if (StringUtils.equals(testItem.getEnglishName(), MAXIMUM)) { runningUutRecord.setMaximumResponseTime(actionTransaction.get().getMaxiMum());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), actionTransaction.get().getMaxiMum(),new Date())); }
                    if (StringUtils.equals(testItem.getEnglishName(), DURATION)) { Object o = summary.get(DURATION);runningUutRecord.setMaximumTurnaroundTime(o.toString());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), o.toString(),new Date())); }
                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE_TURNOVER)) { Object o = summary.get(AVERAGE_TURNOVER);runningUutRecord.setAverageTurnaroundTime(o.toString());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), actionTransaction.get().getAverage(),new Date())); }
                    if (StringUtils.equals(testItem.getEnglishName(), AVERAGE)) { runningUutRecord.setAverageThroughput(graph.getGraphAverage());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), graph.getGraphAverage(),new Date())); }
                    if (StringUtils.equals(testItem.getEnglishName(), GRAPHMAXIMUM)) { runningUutRecord.setMaximumThroughput(graph.getGraphMaximum());resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(), graph.getGraphMaximum(),new Date())); }
                };
                resultDataAnalysisInput.setResultData(result.toString());
            };
        } else if (StringUtils.equals(TOOLID.get(NUM4), resultDataAnalysisInput.getToolId())) {
            // TCS
        } else if (StringUtils.equals(TOOLID.get(NUM5), resultDataAnalysisInput.getToolId())) {
            // JMeer
            // jmeterHtml
            String data = null;
            try {
                data = jmeterHtml(resultDataAnalysisInput);
            }catch (IOException e){
                e.printStackTrace();
            }
            // 创建LambdaQueryWrapper表达式
            LambdaQueryWrapper<TestItem> queryWrapper = new LambdaQueryWrapper();
            queryWrapper.eq(TestItem::getToolsId, resultDataAnalysisInput.getToolId());
            List<TestItem> testItems = testItemMapper.selectList(queryWrapper);
            // string类型转为 jsonObject
            JSONObject jsonObject = JSONObject.parseObject(data);
            JSONObject result = new JSONObject();
            for (TestItem testItem : testItems) {
//                if(StringUtils.equals(testItem.getEnglishName(),WAITINGTIME)){ result.put(testItem.getAbbr(),jsonObject.getString(WAITINGTIME)); }
//                if(StringUtils.equals(testItem.getEnglishName(),BADTRANSTIMERATIO)){ result.put(testItem.getAbbr(),jsonObject.getString(BADTRANSTIMERATIO)); }
//                if(StringUtils.equals(testItem.getEnglishName(),BADRESPTIMERATIO)){ result.put(testItem.getAbbr(),jsonObject.getString(BADRESPTIMERATIO)); }
//                if(StringUtils.equals(testItem.getEnglishName(),AVERAGETURNOVER)){ result.put(testItem.getAbbr(),jsonObject.getString(AVERAGETURNOVER)); }
//                if(StringUtils.equals(testItem.getEnglishName(),TRANSCATION)){ result.put(testItem.getAbbr(),jsonObject.getString(TRANSCATION)); }
//                if(StringUtils.equals(testItem.getEnglishName(),AVERAGERESPONSE)){ result.put(testItem.getAbbr(),jsonObject.getString(AVERAGERESPONSE)); }
                if(StringUtils.equals(testItem.getEnglishName(),WAITINGTIME)){ runningUutRecord.setWaitTime(jsonObject.getString(WAITINGTIME));resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(WAITINGTIME),new Date())); }
                if(StringUtils.equals(testItem.getEnglishName(),BADTRANSTIMERATIO)){ resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(BADTRANSTIMERATIO),new Date())); }
                if(StringUtils.equals(testItem.getEnglishName(),BADRESPTIMERATIO)){ resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(BADRESPTIMERATIO),new Date())); }
                if(StringUtils.equals(testItem.getEnglishName(),AVERAGETURNOVER)){ runningUutRecord.setAverageTurnaroundTime(jsonObject.getString(AVERAGETURNOVER));resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(AVERAGETURNOVER),new Date())); }
                if(StringUtils.equals(testItem.getEnglishName(),TRANSCATION)){ runningUutRecord.setAverageThroughput(jsonObject.getString(TRANSCATION));resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(TRANSCATION),new Date())); }
                if(StringUtils.equals(testItem.getEnglishName(),AVERAGERESPONSE)){ runningUutRecord.setAverageResponseTime(jsonObject.getString(AVERAGERESPONSE));resultDataAnalysisKvList.add(new ResultDataAnalysisKv(resultDataAnalysisInput.getId(),testItem.getAbbr(),jsonObject.getString(AVERAGERESPONSE),new Date())); }
            };
            // 添加结果
            resultDataAnalysisInput.setResultData(result.toString());
            // JMeterJS
        } else if (StringUtils.equals(TOOLID.get(NUM6), resultDataAnalysisInput.getToolId())) {
            // SQATEST-Dcheck
        } else if (StringUtils.equals(TOOLID.get(NUM7), resultDataAnalysisInput.getToolId())) {
            // RTInsigth
        } else if (StringUtils.equals(TOOLID.get(NUM8), resultDataAnalysisInput.getToolId())) {
            // Xcelium
        }
        resultDataAnalysisKvService.saveBatch(resultDataAnalysisKvList);
        return resultDataAnalysisInput;
    }

//    /**
//     * JMeter js方法
//     * @param url
//     * @throws IOException
//     * @throws ScriptException
//     */
//    public String JMeterJs(String url) throws IOException, ScriptException {
//        return collectionDataAnalyseService.addCharts(url);
//    }

    /**
     * JMeter html方法
     *
     * @param resultDataAnalysis
     * @return
     * @throws Exception
     */
    public String jmeterHtml(ResultDataAnalysis resultDataAnalysis) throws Exception {
        // 创建jsonObjec对象
        JSONObject jsonObject = new JSONObject();
        // 等待时间,时间格式化
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy hh:mm aa", Locale.ENGLISH);
        JSONObject jsonObject1 = collectionDataAnalyseService.addDashboard(resultDataAnalysis);
        String startTime = "startTime";
        String endTime = "endTime";
        if (jsonObject1.containsKey(TIMEANDITEM)) {
            String timeAndItem = jsonObject1.getString("timeAndItem");
            JSONObject json = JSONObject.parseObject(timeAndItem);
            if (json.containsKey(startTime)) { startTime = json.getString(startTime); }
            if (json.containsKey(endTime)) { endTime = json.getString(endTime); }
            if (!startTime.isEmpty() && !endTime.isEmpty()) {
                Date startTimes = format.parse(startTime);
                Date endTimes = format.parse(endTime);
                String result = String.valueOf((endTimes.getTime() - startTimes.getTime()) / 1000);
                jsonObject.put("WaitingTime", result);
            }
        }
        // 最坏情况下周转时间的比率 1.最坏情况
        String last = "";
            // 2.大情况
        String max = "";
        if (jsonObject1.containsKey(STATISTICS)) {
            String jsArray = jsonObject1.getString("statistics");
            JSONArray jsonStatistics = JSONArray.parseArray(jsArray);
            for (int i = 0; i < jsonStatistics.size(); i++) {
                JSONObject json = jsonStatistics.getJSONObject(i);
                if ("99th pct".equals(json.getString("lable"))) { last = json.getString("total"); }
                if ("Max".equals(json.getString("lable"))) { max = json.getString("total"); }
            }
            BigDecimal bigDecimal1 = new BigDecimal(last);
            BigDecimal bigDecimal2 = new BigDecimal(max);
            BigDecimal rate = bigDecimal1.divide(bigDecimal2, 2, RoundingMode.HALF_UP);
            String result = rate.toString();
            jsonObject.put("BadTransTimeRatio", result);
        }
        // 最坏情况下响应时间比率
        String lastTime = "";
        // 最大情况
        String maxTime = "";
        if (jsonObject1.containsKey(STATISTICS)) {
            String jsArray = jsonObject1.getString("statistics");
            JSONArray jsonStatistics = JSONArray.parseArray(jsArray);
            for (int i = 0; i < jsonStatistics.size(); i++) {
                JSONObject json = jsonStatistics.getJSONObject(i);
                if ("99th pct".equals(json.getString("lable"))) { lastTime = json.getString("total"); }
                if ("Max".equals(json.getString("lable"))) { maxTime = json.getString("total"); }
            }
            BigDecimal bigDecimal1 = new BigDecimal(lastTime);
            BigDecimal bigDecimal2 = new BigDecimal(maxTime);
            BigDecimal rateTime = bigDecimal1.divide(bigDecimal2, 2, RoundingMode.HALF_UP);
            String reslut = rateTime.toString();
            jsonObject.put("BadRespTimeRatio", reslut);
        }
        // 平均周转时间
        String average = "";
        if (jsonObject1.containsKey(STATISTICS)) {
            String jsArray = jsonObject1.getString("statistics");
            JSONArray jsonStatistics = JSONArray.parseArray(jsArray);
            for (int i = 0; i < jsonStatistics.size(); i++) {
                JSONObject json = jsonStatistics.getJSONObject(i);
                if ("Average".equals(json.getString("lable"))) { average = json.getString("total"); }
            }
        }
        jsonObject.put("AverageTurnover", average);
        // 平均吞吐量
        String transcation = "";
        if (jsonObject1.containsKey(STATISTICS)) {
            String jsArray = jsonObject1.getString("statistics");
            JSONArray jsonStatistics = JSONArray.parseArray(jsArray);
            for (int i = 0; i < jsonStatistics.size(); i++) {
                JSONObject json = jsonStatistics.getJSONObject(i);
                if ("Transactions/s".equals(json.getString("lable"))) { transcation = json.getString("total"); }
            }
            jsonObject.put("Transcation", transcation);
        }
        // 平均响应时间
        jsonObject.put("AverageResponse", average);
        return jsonObject.toString();
    }

    /**
     * loadRunnerResolver
     * @param resultDataAnalysisInput
     * @return
     * @throws IOException
     */
    public String loadRunnerResolver(ResultDataAnalysis resultDataAnalysisInput) throws IOException {
        JSONObject jsonObject = new JSONObject();
        String summary = resultDataAnalysisInput.getUrl() + "summary.html";
        String runningVusers = resultDataAnalysisInput.getUrl() + "Report0.html";
        String hitsPerSecond = resultDataAnalysisInput.getUrl() + "Report1.html";
        List<CollectionDataAnalyse> collectionDataAnalyses = new ArrayList<>();
        JSONObject analysisSummary = LoadRunnerAnbalyseUtils.analysisSummary(summary);
        CollectionDataAnalyse collectionSummary = CollectionDataAnalyseServiceImpl.setValue(ANALYSIS_SUMMARY, analysisSummary.toString(), resultDataAnalysisInput.getAgentIp(), resultDataAnalysisInput.getToolId());
        collectionDataAnalyses.add(collectionSummary);

        JSONObject analysisRunningVusers = LoadRunnerAnbalyseUtils.analysisGraph(runningVusers);
        CollectionDataAnalyse collectionRunningVusers = CollectionDataAnalyseServiceImpl.setValue(RUNNING_VUSERS, analysisRunningVusers.toString(), resultDataAnalysisInput.getAgentIp(), resultDataAnalysisInput.getToolId());
        collectionDataAnalyses.add(collectionRunningVusers);

        JSONObject analysisHitsPerSecond = LoadRunnerAnbalyseUtils.analysisGraph(hitsPerSecond);
        CollectionDataAnalyse collectionHitSperSecond = CollectionDataAnalyseServiceImpl.setValue(HIT_SPER_SECOND, analysisHitsPerSecond.toString(), resultDataAnalysisInput.getAgentIp(), resultDataAnalysisInput.getToolId());
        collectionDataAnalyses.add(collectionHitSperSecond);

        collectionDataAnalyseService.addCollectionDataAnalyse(collectionDataAnalyses);
        jsonObject.put("summary", analysisSummary);
        jsonObject.put("runningVusers", analysisRunningVusers);
        jsonObject.put("hitsPerSecond", analysisHitsPerSecond);
        return jsonObject.toString();
    }
}