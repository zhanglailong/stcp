package org.jeecg.modules.sjcj.collectiondataanalyse.util;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.lang.StringUtils;

import javax.script.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @Author: test
 * */
public class JmeterAnbalyseUtils {

    public static void main(String[] args) throws IOException, ScriptException {
//        String jsPath = amendJS("E:/graph.js");
        String url = "http://192.168.1.90:9000/test/testbaidu/JMeter/content/js/graph.js";
        
//        Map<String, Object> stringObjectMap = anbalyseJs(jsPath);
        String responseBody = HttpClientUtils.getResponseBody(url);
        responseBody= responseBody.substring(responseBody.indexOf("var"));
        anbalyseJs(responseBody);
    }

    public static String amendJs(String path) throws FileNotFoundException, ScriptException {

        File file = new File(path);
        File saveFile = new File("E:/test.js");
        //将读取出来的数据存入list集合、一个备份、一个正常运行
        String script = "";
            //判断文件是否存在、并且有内容
            boolean existed =file.exists() && !(file.exists() && file.length() == 0);
            if(existed) {
                System.out.println("文件存在并且有内容");

                String str = null;
                try {
                    List<String> list = new ArrayList<>();
                    //BufferedReader是可以按行读取文件
                    FileInputStream inputStream = new FileInputStream(path);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    while((str = bufferedReader.readLine()) != null)
                    {
//                        //如果有空格、去掉空格
                        //如果不是空串、添加到list、然后提交
                        if(!"".equals(str)) {
                            list.add(str);
                        }
                    }
                    bufferedReader.close();
                    inputStream.close();
                    //删除第一行、、写回到文本
                    int start=32;
                    for (int i = start; i >= 0; --i) {
                        list.remove(i);
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                    if(list.size() != 0) {
                        for (String string : list) {
                            bw.write(string);
                            bw.newLine();
                        }
                    }
                    bw.flush();
                    bw.close();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("文件不存在、或者文件无内容");
            }
        return saveFile.getPath();
    }

    public static Map<String, Object> anbalyseJs(String parh) throws FileNotFoundException, ScriptException {
        /*获取执行JavaScript的执行引擎*/
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        /*为文件注入全局变量*/
        Bindings bindings = engine.createBindings();
        /*设置绑定参数的作用域*/
        engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

        // 获得js文件
        engine.eval(parh);
        Map<String,Object> activeThreadsOverTimeInfos = (Map)bindings.get("activeThreadsOverTimeInfos");
        Map<String, Object> data = (Map)activeThreadsOverTimeInfos.get("data");
        Map<String, Object> result = (Map)data.get("result");
        String title = (String) result.get("title");
        Map<String, Object> series = (Map)result.get("series");
        Map<String, Object> dataKey = (Map)series.get("0");
        ScriptObjectMirror scriptObjectMirror = (ScriptObjectMirror)dataKey.get("data");
        Object dataList = toObject(scriptObjectMirror);
        Map<String, Object> resultMap = new HashMap<>(2000);
        resultMap.put(StringUtils.deleteWhitespace(title), dataList);
        System.out.println(resultMap.toString());
        return resultMap;
    }

    /**
     * 转换数据
     * @param mirror
     * @return
     */
    public static Object toObject(ScriptObjectMirror mirror) {
            if (mirror.isEmpty()) {
                return null;
            }
            if (mirror.isArray()) {
                List<Object> list = new ArrayList<>();
                for (Map.Entry<String, Object> entry : mirror.entrySet()) {
                    Object result = entry.getValue();
                    if (result instanceof ScriptObjectMirror) {
                        list.add(toObject((ScriptObjectMirror) result));
                    } else {
                        list.add(result);
                    }
                }
                return list;
            }

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

    public static LocalDateTime toDate(BigDecimal b) {
         b = new BigDecimal(1.610436E12);
        LocalDateTime ldt = Instant.ofEpochMilli(b.longValue())
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(ldt);
        return ldt;
    }

}
