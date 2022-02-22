package org.jeecg.modules.sjcj.collectiondataanalyse.util;

import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang.StringUtils;

import javax.script.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: test
 * */
public class JmeterChartsUtils {

    public static void main(String[] args) throws IOException, ScriptException {
        // 获取url路径
        String url = "http://192.168.1.90:9000/test/testbaidu/JMeter/content/js/graph.js";
        String responseBody = HttpClientUtils.getResponseBody(url);
        // 截取var之后的数据
        responseBody = responseBody.substring(responseBody.indexOf("var"));
        // 解析js
        // Response Times
        Map<String, Object> responseTimeDistribution = anbalyseJs(responseBody, "responseTimeDistributionInfos");
        // Map<String, Object> syntheticResponseTimeDistribution = anbalyseJs(responseBody, "syntheticResponseTimeDistributionInfos");
        Map<String, Object> responseTimePercentiles = anbalyseJs(responseBody, "responseTimePercentilesInfos");
        Map<String, Object> timeVsThreads  = anbalyseJs(responseBody, "timeVsThreadsInfos");

        // Over Time
        Map<String, Object> responseTimesOverTime = anbalyseJs(responseBody, "responseTimesOverTimeInfos");
        Map<String, Object> responseTimePercentilesOverTime = anbalyseJs(responseBody, "responseTimePercentilesOverTimeInfos");
        Map<String, Object> activeThreadsOverTime = anbalyseJs(responseBody, "activeThreadsOverTimeInfos");
        Map<String, Object> bytesThroughputOverTime = anbalyseJs(responseBody, "bytesThroughputOverTimeInfos");
        Map<String, Object> latenciesOverTime = anbalyseJs(responseBody, "latenciesOverTimeInfos");
        Map<String, Object> connectTimeOverTime = anbalyseJs(responseBody, "connectTimeOverTimeInfos");

        // Throughput
        Map<String, Object> hitsPerSecond = anbalyseJs(responseBody, "hitsPerSecondInfos");
        Map<String, Object> codesPerSecond = anbalyseJs(responseBody, "codesPerSecondInfos");
        Map<String, Object> transactionsPerSecond = anbalyseJs(responseBody, "transactionsPerSecondInfos");
        Map<String, Object> totalTps = anbalyseJs(responseBody, "totalTPSInfos");
        Map<String, Object> responseTimeVsRequest = anbalyseJs(responseBody, "responseTimeVsRequestInfos");
        Map<String, Object> latenciesVsRequest = anbalyseJs(responseBody, "latenciesVsRequestInfos");
    }


    /**解析js方法*/
    public static JSONObject anbalyseJs(String path, String parameter) throws ScriptException, IOException {

        /*获取执行JavaScript的执行引擎*/
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        /*为文件注入全局变量*/
        Bindings bindings = engine.createBindings();
        /*设置绑定参数的作用域*/
        engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

        // 获取js
        engine.eval(path);
        // 解析传过来的js方法 (var后面的方法名)
        Map<String, Object> map = (Map) bindings.get(parameter);
        // 解析方法下的data
        Map<String, Object> data = (Map) map.get("data");
        // 解析data下的result
        Map<String, Object> result = (Map) data.get("result");
        // 解析result下的title
        String title = (String) result.get("title");
        // 解析result下的series
        Map<String, Object> series = (Map) result.get("series");
        // 解析series下的object (转换为map类型key-value形式，索引从0开始）
        Map<String, Object> dataKey = (Map) series.get("0");
        // 解析object下的data
        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror) dataKey.get("data");
        // 数据转换  （强制转换）
        Object dataList = toObject(scriptObjectMirror);
        // 创建map集合
        Map<String, Object> resultMap = new HashMap<>(2000);
        JSONObject jsonObject = new JSONObject();
        // put方法
        jsonObject.put(StringUtils.deleteWhitespace(title), dataList);
        // 输出resultMap
        System.out.println(resultMap.toString());
        // 返回resultMap
        return jsonObject;
    }


    /***
     * 转换数据
     *
     * @param mirror
     * @date 2021/01/15 15:00
     */
    public static Object toObject(ScriptObjectMirror mirror) {
        // 判断 mirror是否为空
        if (mirror.isEmpty()) {
            return null;
        }
        if (mirror.isArray()) {
            // 创建list集合
            List<Object> list = new ArrayList<>();
            // for 循环  map集合
            for (Map.Entry<String, Object> entry : mirror.entrySet()) {
                // 获取value值
                Object result = entry.getValue();
                // 判断 左边的对象 是否是 它右边的类的实例
                if (result instanceof ScriptObjectMirror) {
                    // 添加
                    list.add(toObject((ScriptObjectMirror) result));
                } else {
                    // 添加 左边的对象 result
                    list.add(result);
                }
            }
            return list;
        }
        // 创建map集合
        Map<String, Object> map = new HashMap<>(2000);
        for (Map.Entry<String, Object> entry : mirror.entrySet()) {
            Object result = entry.getValue();
            if (result instanceof ScriptObjectMirror) {
                map.put(entry.getKey(), toObject((ScriptObjectMirror) result));
            } else {
                map.put(entry.getKey(), result);
            }
        }
        return map;
    }


}
