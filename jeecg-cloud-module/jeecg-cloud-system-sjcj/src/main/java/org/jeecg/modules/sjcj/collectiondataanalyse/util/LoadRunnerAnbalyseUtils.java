package org.jeecg.modules.sjcj.collectiondataanalyse.util;

import com.alibaba.fastjson.JSONObject;
import com.opencsv.CSVReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.Graph;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.HttpResponsesSummary;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.TransactionSummary;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * @Author: test
 * */
public class LoadRunnerAnbalyseUtils {

    public static void main(String[] args) throws IOException {

        // 解析合计结果
//        analysisSummary("E:\\LoadRunner\\htm_result\\Test001\\Test001Report\\summary.html");
//
//        analysisGraph("E:\\LoadRunner\\htm_result\\Test001\\Test001Report\\Report0.html");
//
//        analysisGraph("E:\\LoadRunner\\htm_result\\Test001\\Test001Report\\Report1.html");

//        anbalyseCsv();
        String url = "http://192.168.1.69:9000/sjcj/gatherResult/1342660167866642433/192_168_1_69/2021_01_19_09_34_41/result/Test001Report/summary.html";
        HttpGet get = new HttpGet(url);

        // 创建httpClient实例
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 执行http get请求
        CloseableHttpResponse response = httpclient.execute(get);
        //获取返回实体
        HttpEntity entity = response.getEntity();
        // 获取页面内容
        String  responseBody =  EntityUtils.toString(entity, "utf-8");
        Document document = Jsoup.parse(responseBody);
        Elements title = document.select("title");
        Element statisticsSummary = document.getElementsByTag("table").get(4);
        System.out.println(statisticsSummary);

    }

    /**
     * 解析 合计结果
     * @throws IOException
     */
    public static JSONObject analysisSummary(String path) throws IOException {
        String responseBody = HttpClientUtils.getResponseBody(path);
        Document document = Jsoup.parse(responseBody);

        // 获取第三个table以获取周转时间
        Element duration = document.getElementsByTag("table").get(1);
        Elements durationTrs = duration.select("tr");
        Elements durationTds = durationTrs.get(0).getElementsByTag("td");
        String durationResult = durationTds.get(1).text();
        String timeInterval = durationResult.substring(durationResult.indexOf(":")+1);
        String beginTime = timeInterval.substring(0, timeInterval.indexOf("-") - 1).trim();
        String endTime = timeInterval.substring(timeInterval.indexOf("-") +1).trim();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime localBeginDateTime = LocalDateTime.parse(beginTime, formatter);
        LocalDateTime localEndDateTime = LocalDateTime.parse(endTime, formatter);
        long durationTime = localEndDateTime.toEpochSecond(ZoneOffset.of("+8")) - localBeginDateTime.toEpochSecond(ZoneOffset.of("+8"));
        // 获取第五个statisticsSummary 标签
        Element statisticsSummary = document.getElementsByTag("table").get(4);

        // 获取该table下所有tr标签
        Elements statisticsTrs = statisticsSummary.select("tr");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Duration", durationTime);

        for (Element tr : statisticsTrs) {
            Elements tds = tr.select("td");
            Elements b = tds.get(0).getElementsByTag("b");
            String k = b.text();
            Elements span = tds.get(2).getElementsByTag("span");
            String v = span.text();
            if (CollectionUtils.isEmpty(span)) {
                v = tds.get(2).text();
            }
            jsonObject.put(k,v);
        }

        // 获取第五个summaryTable 标签
        Element transactionSummary = document.getElementsByTag("table").get(8);

        Elements transactionTrs = transactionSummary.select("tr");

        List<TransactionSummary> transactionSummaries = transactionSummary(transactionTrs);

        jsonObject.put("TransactionSummary", transactionSummaries);

        // 获取 HttpResponsesSummary
        Element httpResponsesSummary = document.getElementsByTag("table").get(11);

        Elements httpResponsesSummaryTrs = httpResponsesSummary.select("tr");

        List<HttpResponsesSummary> httpResponsesSummaries = httpResponsesSummary(httpResponsesSummaryTrs);
        jsonObject.put("HttpResponsesSummary", httpResponsesSummaries);

        return jsonObject;
    }

    /**
     * 解析 Running Vusers
     */
    public static JSONObject analysisGraph(String path) throws IOException {
        String responseBody = HttpClientUtils.getResponseBody(path);
        Document document = Jsoup.parse(responseBody);

        // 获取Running Vusers数据表标签
        Elements analysisRunningVuers = document.getElementsByClass("legendRow");
        Elements td = analysisRunningVuers.select("td");
        Graph graph = new Graph();
        graph.setScale(td.get(1).text());
        graph.setGraphMinimum(td.get(3).text());
        graph.setGraphAverage(td.get(4).text());
        graph.setGraphMaximum(td.get(5).text());
        graph.setGraphMedian(td.get(6).text());
        graph.setGraphStdDeviation(td.get(7).text());
        System.out.println(graph.toString());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("graph", graph);
        return jsonObject;
    }

    /**
     * 解析Transaction Summary 方法
     * @param transactionTrs tr
     * @return 返回list
     */
    public static List<TransactionSummary> transactionSummary(Elements transactionTrs) {

        List<TransactionSummary> transactionSummaries = new ArrayList<>();

        for (int i = 1; i < transactionTrs.size(); i++) {
            Elements tds = transactionTrs.get(i).select("td");
            TransactionSummary transaction = new TransactionSummary();

            String transactionName = tds.get(0).getElementsByTag("span").text();
            transaction.setTransactionName(transactionName);

            String miniMum = tds.get(2).getElementsByTag("span").text();
            transaction.setMiniMum(miniMum);

            String average = tds.get(3).getElementsByTag("span").text();
            transaction.setAverage(average);

            String maxiMum = tds.get(4).getElementsByTag("span").text();
            transaction.setMaxiMum(maxiMum);

            String stdDeviation = tds.get(5).getElementsByTag("span").text();
            transaction.setStdDeviation(stdDeviation);

            String percent = tds.get(6).getElementsByTag("span").text();
            transaction.setPercent(percent);

            String pass = tds.get(7).getElementsByTag("span").text();
            transaction.setPass(pass);

            String fail = tds.get(8).getElementsByTag("span").text();
            transaction.setFail(fail);

            String stop = tds.get(9).getElementsByTag("span").text();
            transaction.setStop(stop);

            transactionSummaries.add(transaction);

        }


        return transactionSummaries;
    }

    /**
     * 解析 HTTP Responses Summary
     * @param httpResponsesSummaryTrs tr
     * @return 返回list
     */
    public static List<HttpResponsesSummary> httpResponsesSummary(Elements httpResponsesSummaryTrs) {

        List<HttpResponsesSummary> transactionSummaries = new ArrayList<>();

        for (int i = 1; i < httpResponsesSummaryTrs.size() ; i++) {
            Elements tds = httpResponsesSummaryTrs.get(i).select("td");
            HttpResponsesSummary httpResponsesSummary = new HttpResponsesSummary();

            String httpResponses = tds.get(0).getElementsByTag("span").text();
            httpResponsesSummary.setHttpResponses(httpResponses);

            String total = tds.get(1).getElementsByTag("span").text();
            httpResponsesSummary.setTotal(total);

            String perSecond = tds.get(2).getElementsByTag("span").text();
            httpResponsesSummary.setPerSecond(perSecond);

            transactionSummaries.add(httpResponsesSummary);

        }
        return transactionSummaries;
    }

    public static void anbalyseCsv() throws IOException {
        String path = "C:\\Users\\sentxi001\\Desktop\\test.csv";
        String charset = "gbk";
        Reader reader = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            reader = new InputStreamReader(fileInputStream, charset);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CSVReader csvReader = new CSVReader(reader);
        List<String[]> list = csvReader.readAll();

//        List<TestBean> beans = new CsvToBeanBuilder<TestBean>(reader)
//                .withType(TestBean.class).build().parse();
//        System.out.println(beans);
    }

}
