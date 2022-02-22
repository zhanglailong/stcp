package org.jeecg.modules.controller;

import cn.hutool.core.date.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.entity.EnvPlanLines;
import org.jeecg.modules.entity.EnvPlanNodes;
import org.jeecg.modules.entity.UnifyResultData;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/3/16
 * @Description: 用一句话描述该文件做什么)
 */
@Slf4j
public class Ceshi {
    public static void main(String[] args) throws Exception {
        //判断安装哪个工具
//        String command = "cmd /c " + "cd " + "C:\\Program Files (x86)\\AutoIt3\\" + " AutoIt3.exe " + "D:\\LoadRunner安装组件.au3";
        String command2 = "cmd /c "+" cd C:\\Program Files (x86)\\AutoIt3 "+"&& AutoIt3.exe D:\\LoadRunner安装组件.au3";
        //执行cmd命令
        String bin = "C:\\Program Files (x86)\\AutoIt3\\";
        String command = bin +  "AutoIt3 D:\\LoadRunner安装组件.au3";
        StringBuilder readCount = new StringBuilder();
        Process process = Runtime.getRuntime().exec("C:\\Program Files (x86)\\AutoIt3\\AutoIt3.exe"
                +"  D:\\LoadRunner安装组件.au3");
//        Process process2 = Runtime.getRuntime().exec("cmd /c AutoIt3.exe D:\\LoadRunner安装组件.au3");
        //关闭流释放资源
        log.info("关闭流释放资源");
        if (process != null) {
            process.getOutputStream().close();
            //以下为读取cmd窗口返回的内容
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            log.info("以下为读取cmd窗口返回的内容");
            while ((line = bufferedReader.readLine()) != null) {
                readCount.append(line).append("\n");
            }
        }
        log.info("结束");
        log.info("读取cmd窗口返回的内容:"+readCount.toString());


//        String processName = "AutoIt3.exe";
//        Process process = Runtime.getRuntime().exec("Taskkill /IM " + processName);
//        if (process != null) {
//            System.out.println("process:"+JSON.toJSONString(process));
//            process.getOutputStream().close();
//        }
//        killProcess(processName);

//        BufferedReader bufferedReader = null;
//        try {
//            int count = 0;
//            int num = 0;
//            String pid = null;
//            do {
//                count++;
//                num++;
//                Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName + '"');
//                bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    if (line.contains(processName)) {
//                        if (org.apache.commons.lang.StringUtils.isEmpty(pid)){
//                            String[] lineArray = line.split(" ");
//                            pid = lineArray[17].trim();
//                        }
//                        count = 1;
//                        System.out.println("count:" + count + ",name:" + line+",pid:"+pid);
//                        break;
//                    }
//                }
//                if (num >= 20){
//                    if (org.apache.commons.lang.StringUtils.isNotEmpty(pid)){
//                        killProcess(processName);
//                    }
//                    break;
//                }
//                System.out.println("count:" + count+",num:"+num);
//                TimeUnit.SECONDS.sleep(1);
//            } while (count < 10);
//        } catch (Exception ex) {
//            log.error("判断进程是否存在异常:"+ex.getMessage());
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (Exception ignored) {}
//            }
//        }


//        findProcess(NAME_STRING);
//        System.out.println("结束");
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, String> pMap = new LinkedMultiValueMap<String, String>();
//        pMap.add("grant_type", "password");
//        pMap.add("username", "admin");
//        pMap.add("password", "123456");
//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(pMap, headers);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://192.168.1.203:8001/api/identity/token", requestEntity, String.class);
//        System.out.println(JSON.toJSONString(responseEntity));
//        JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
//        System.out.println(jsonObject.get("access_token"));


        //        String time = "2021-04-15 06:55:58+00:00";
//        DateTime exTime = new DateTime(time, DatePattern.NORM_DATETIME_FORMAT);
//        DateTime newDate = DateUtil.offsetHour(new Date(), 12);
//        System.out.println("exTime day:"+ exTime.dayOfMonth());
//        System.out.println("newDate day:"+ newDate.dayOfMonth());
//        if (exTime.dayOfMonth()==newDate.dayOfMonth()){
//            System.out.println("token有效期正常");
//        }

//        Date date = DateUtil.parse(time, DatePattern.NORM_DATETIME_FORMAT);
//        System.out.println("年:"+DateUtil.year(date));
//        System.out.println("月:"+DateUtil.month(date));
//        System.out.println("日:"+DateUtil.dayOfMonth(date));
//        System.out.println("时:"+DateUtil.hour(date,true));
//        System.out.println("分:"+DateUtil.minute(date));
//        long betweenDay = DateUtil.between(date, new Date(), DateUnit.MS);
//        System.out.println("betweenDay:"+betweenDay);
//        String formatBetween = DateUtil.formatBetween(betweenDay, BetweenFormatter.Level.MINUTE);
//        System.out.println("formatBetween:"+formatBetween);

//        String resultJson = "{\"body\":\"{\\\"code\\\":0,\\\"msg\\\":\\\"创建栈成功\\\",\\\"data\\\":{\\\"stack\\\":{\\\"id\\\":\\\"91580e0d-032f-425d-9794-123e1a904701\\\",\\\"location\\\":{\\\"cloud\\\":\\\"defaults\\\",\\\"region_name\\\":\\\"RegionOne\\\",\\\"zone\\\":null,\\\"project\\\":{\\\"id\\\":\\\"187accf15ef5454fbfeef55406d02b57\\\",\\\"name\\\":\\\"demo\\\",\\\"domain_id\\\":null,\\\"domain_name\\\":\\\"Default\\\"}},\\\"action\\\":\\\"CREATE\\\",\\\"status\\\":\\\"IN_PROGRESS\\\",\\\"stack_status\\\":\\\"CREATE_IN_PROGRESS\\\",\\\"name\\\":\\\"ceshi-2021-04-07-1_13796097538502819851_003\\\",\\\"stack_name\\\":\\\"ceshi-2021-04-07-1_13796097538502819851_003\\\",\\\"created_at\\\":\\\"2021-04-12T10:58:25Z\\\",\\\"creation_time\\\":\\\"2021-04-12T10:58:25Z\\\",\\\"deleted_at\\\":null,\\\"deletion_time\\\":null,\\\"updated_at\\\":null,\\\"updated_time\\\":null,\\\"description\\\":\\\"No description\\\",\\\"is_rollback_enabled\\\":false,\\\"disable_rollback\\\":false,\\\"parent\\\":null,\\\"notification_topics\\\":[],\\\"parameters\\\":{\\\"OS::stack_id\\\":\\\"91580e0d-032f-425d-9794-123e1a904701\\\",\\\"OS::project_id\\\":\\\"187accf15ef5454fbfeef55406d02b57\\\",\\\"OS::stack_name\\\":\\\"ceshi-2021-04-07-1_13796097538502819851_003\\\"},\\\"outputs\\\":[],\\\"owner\\\":\\\"admin\\\",\\\"stack_owner\\\":\\\"admin\\\",\\\"status_reason\\\":\\\"Stack CREATE started\\\",\\\"stack_status_reason\\\":\\\"Stack CREATE started\\\",\\\"stack_user_project_id\\\":\\\"8e2590d2b9c84cfab9dcecf066cf907c\\\",\\\"tempate_description\\\":\\\"No description\\\",\\\"template_description\\\":\\\"No description\\\",\\\"timeout_mins\\\":60,\\\"tags\\\":[],\\\"identifier\\\":\\\"ceshi-2021-04-07-1_13796097538502819851_003/91580e0d-032f-425d-9794-123e1a904701\\\"}}}\",\"headers\":{\"date\":[\"Mon, 12 Apr 2021 10:58:23 GMT\"],\"server\":[\"uvicorn\"],\"content-length\":[\"1319\"],\"content-type\":[\"application/json\"]},\"statusCode\":\"OK\",\"statusCodeValue\":200}";
//        UnifyResultData resultData = JSON.parseObject(resultJson, UnifyResultData.class);
//        System.out.println("resultData:" + JSON.toJSONString(resultData));
//        if (resultData != null && resultData.getStatusCodeValue() == 200 && StringUtils.isNotEmpty(resultData.getBody())) {
//            JSONObject objObdy = JSONObject.parseObject(resultData.getBody());
//            if (objObdy != null && objObdy.containsKey("code") && "0".equals(objObdy.get("code").toString())
//                    && objObdy.containsKey("data") && StringUtils.isNotEmpty(objObdy.get("data").toString())) {
//                String stackId = objObdy.getJSONObject("data").getJSONObject("stack").get("id").toString();
//                if (StringUtils.isNotEmpty(stackId)) {
//                    System.out.println("stackId:" + stackId);
//                }
//            }
//        }


//        String data="{\"purpose\":\"ceshi\",\"lineList\":[{\"from\":\"zmsnfxqtgu\",\"to\":\"l2eb0k62w8\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"ihc3ouue16\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"uhfqoedokq\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"gddqp502w\"},{\"from\":\"7ygj5u22ge\",\"to\":\"4559oj8gw\"},{\"from\":\"7ygj5u22ge\",\"to\":\"sp1jfg7qpc\"},{\"from\":\"7ygj5u22ge\",\"to\":\"ocd8xdvrwq\"},{\"from\":\"7ygj5u22ge\",\"to\":\"howdd8eyxe\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"xlywj4z0k\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"a7cp2ye6y\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"oa83419nb\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"d1augvtbar\"},{\"from\":\"buc7uywt8\",\"to\":\"03x812dujj\"},{\"from\":\"03x812dujj\",\"to\":\"0g4ouvjk3l\"},{\"from\":\"03x812dujj\",\"to\":\"zmsnfxqtgu\"},{\"from\":\"03x812dujj\",\"to\":\"7ygj5u22ge\"},{\"from\":\"qfu2jwh89\",\"to\":\"yx45vrxg6\"},{\"from\":\"qfu2jwh89\",\"to\":\"ngvlgcxfd4\"},{\"from\":\"qfu2jwh89\",\"to\":\"toctbgilo8\"},{\"from\":\"qfu2jwh89\",\"to\":\"ym2kbvrh5j\"},{\"from\":\"jvavd10ld\",\"to\":\"lroq4hoo5c\"},{\"from\":\"jvavd10ld\",\"to\":\"721zwn32wt\"},{\"from\":\"jvavd10ld\",\"to\":\"xunyib5dg8\"},{\"from\":\"jvavd10ld\",\"to\":\"qg5jtt1c1h\"},{\"from\":\"81in3espue\",\"to\":\"rps5r9jkw\"},{\"from\":\"rps5r9jkw\",\"to\":\"jvavd10ld\"},{\"from\":\"rps5r9jkw\",\"to\":\"qfu2jwh89\"},{\"from\":\"43xmmfuq2c\",\"to\":\"julfsaceb\"},{\"from\":\"43xmmfuq2c\",\"to\":\"l3m8t4g8ms\"},{\"from\":\"43xmmfuq2c\",\"to\":\"1zbl990yvp\"},{\"from\":\"43xmmfuq2c\",\"to\":\"taa7z6l8eg\"},{\"from\":\"1ekdab0n3\",\"to\":\"2yspw39iz4\"},{\"from\":\"1ekdab0n3\",\"to\":\"e0fcug1poj\"},{\"from\":\"1ekdab0n3\",\"to\":\"olyjdbcfb\"},{\"from\":\"1ekdab0n3\",\"to\":\"2eo8k31i4b\"},{\"from\":\"95gt0wj6pr\",\"to\":\"xy3ojgxh2\"},{\"from\":\"xy3ojgxh2\",\"to\":\"43xmmfuq2c\"},{\"from\":\"xy3ojgxh2\",\"to\":\"1ekdab0n3\"},{\"from\":\"xy3ojgxh2\",\"to\":\"n9i027kgj4\"},{\"from\":\"rps5r9jkw\",\"to\":\"n9i027kgj4\"},{\"from\":\"03x812dujj\",\"to\":\"n9i027kgj4\"}],\"name\":\"ceshi-2021-03-16\",\"id\":\"\",\"nodeList\":[{\"id\":\"zmsnfxqtgu\",\"name\":\"虚拟机\",\"type\":\"virtual\",\"left\":\"235px\",\"top\":\"323px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\",\"vmName\":\"虚拟机\",\"virCpu\":\"1\",\"virInner\":\"1\",\"virDisk\":\"100\"},{\"id\":\"l2eb0k62w8\",\"name\":\"操作系统\",\"type\":\"system\",\"left\":\"125px\",\"top\":\"422px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"ihc3ouue16\",\"name\":\"硬件资源\",\"type\":\"hardWare\",\"left\":\"190px\",\"top\":\"422px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"uhfqoedokq\",\"name\":\"测试工具\",\"type\":\"testTool\",\"left\":\"275px\",\"top\":\"422px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"gddqp502w\",\"name\":\"被测对象\",\"type\":\"testObject\",\"left\":\"355px\",\"top\":\"422px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"buc7uywt8\",\"name\":\"网络\",\"type\":\"net\",\"left\":\"225.0078125px\",\"top\":\"48.0078125px\",\"ico\":\"iconfont iconnet\",\"state\":\"success\"},{\"id\":\"03x812dujj\",\"name\":\"子网\",\"type\":\"childNet\",\"left\":\"243px\",\"top\":\"158px\",\"ico\":\"iconfont iconchildnet\",\"state\":\"success\"},{\"id\":\"7ygj5u22ge\",\"name\":\"虚拟机1\",\"type\":\"virtual\",\"left\":\"610.0078125px\",\"top\":\"327.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"4559oj8gw\",\"name\":\"操作系统1\",\"type\":\"system\",\"left\":\"500px\",\"top\":\"427px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"sp1jfg7qpc\",\"name\":\"硬件资源1\",\"type\":\"hardWare\",\"left\":\"565px\",\"top\":\"427px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"ocd8xdvrwq\",\"name\":\"测试工具1\",\"type\":\"testTool\",\"left\":\"650px\",\"top\":\"427px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"howdd8eyxe\",\"name\":\"被测对象1\",\"type\":\"testObject\",\"left\":\"730px\",\"top\":\"427px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"0g4ouvjk3l\",\"name\":\"虚拟机2\",\"type\":\"virtual\",\"left\":\"68px\",\"top\":\"320px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"xlywj4z0k\",\"name\":\"操作系统2\",\"type\":\"system\",\"left\":\"66px\",\"top\":\"408px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"a7cp2ye6y\",\"name\":\"硬件资源2\",\"type\":\"hardWare\",\"left\":\"131px\",\"top\":\"408px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"oa83419nb\",\"name\":\"测试工具2\",\"type\":\"testTool\",\"left\":\"216px\",\"top\":\"408px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"d1augvtbar\",\"name\":\"被测对象2\",\"type\":\"testObject\",\"left\":\"296px\",\"top\":\"408px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"81in3espue\",\"name\":\"网络1\",\"type\":\"net\",\"left\":\"805.0078125px\",\"top\":\"48.0078125px\",\"ico\":\"iconfont iconnet\",\"state\":\"success\"},{\"id\":\"rps5r9jkw\",\"name\":\"子网1\",\"type\":\"childNet\",\"left\":\"798.008px\",\"top\":\"143.008px\",\"ico\":\"iconfont iconchildnet\",\"state\":\"success\"},{\"id\":\"qfu2jwh89\",\"name\":\"虚拟机3\",\"type\":\"virtual\",\"left\":\"919.0078125px\",\"top\":\"296.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"yx45vrxg6\",\"name\":\"操作系统3\",\"type\":\"system\",\"left\":\"809px\",\"top\":\"396px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"ngvlgcxfd4\",\"name\":\"硬件资源3\",\"type\":\"hardWare\",\"left\":\"874px\",\"top\":\"396px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"toctbgilo8\",\"name\":\"测试工具3\",\"type\":\"testTool\",\"left\":\"959px\",\"top\":\"396px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"ym2kbvrh5j\",\"name\":\"被测对象3\",\"type\":\"testObject\",\"left\":\"1039px\",\"top\":\"396px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"jvavd10ld\",\"name\":\"虚拟机4\",\"type\":\"virtual\",\"left\":\"836.008px\",\"top\":\"331.008px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"lroq4hoo5c\",\"name\":\"操作系统4\",\"type\":\"system\",\"left\":\"726px\",\"top\":\"431px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"721zwn32wt\",\"name\":\"硬件资源4\",\"type\":\"hardWare\",\"left\":\"791px\",\"top\":\"431px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"xunyib5dg8\",\"name\":\"测试工具4\",\"type\":\"testTool\",\"left\":\"876px\",\"top\":\"431px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"qg5jtt1c1h\",\"name\":\"被测对象4\",\"type\":\"testObject\",\"left\":\"956px\",\"top\":\"431px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"n9i027kgj4\",\"name\":\"路由\",\"type\":\"route\",\"left\":\"566.008px\",\"top\":\"106.008px\",\"ico\":\"iconfont iconroute\",\"state\":\"success\"},{\"id\":\"95gt0wj6pr\",\"name\":\"网络2\",\"type\":\"net\",\"left\":\"1261.0078125px\",\"top\":\"77.0078125px\",\"ico\":\"iconfont iconnet\",\"state\":\"success\"},{\"id\":\"xy3ojgxh2\",\"name\":\"子网2\",\"type\":\"childNet\",\"left\":\"1275px\",\"top\":\"195px\",\"ico\":\"iconfont iconchildnet\",\"state\":\"success\"},{\"id\":\"43xmmfuq2c\",\"name\":\"虚拟机5\",\"type\":\"virtual\",\"left\":\"1328.01px\",\"top\":\"339.008px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"julfsaceb\",\"name\":\"操作系统5\",\"type\":\"system\",\"left\":\"1218px\",\"top\":\"439px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"l3m8t4g8ms\",\"name\":\"硬件资源5\",\"type\":\"hardWare\",\"left\":\"1283px\",\"top\":\"439px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"1zbl990yvp\",\"name\":\"测试工具5\",\"type\":\"testTool\",\"left\":\"1368px\",\"top\":\"439px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"taa7z6l8eg\",\"name\":\"被测对象5\",\"type\":\"testObject\",\"left\":\"1448px\",\"top\":\"439px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"},{\"id\":\"1ekdab0n3\",\"name\":\"虚拟机6\",\"type\":\"virtual\",\"left\":\"1615.0078125px\",\"top\":\"342.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"state\":\"success\"},{\"id\":\"2yspw39iz4\",\"name\":\"操作系统6\",\"type\":\"system\",\"left\":\"1505px\",\"top\":\"442px\",\"ico\":\"iconfont iconsystem\",\"state\":\"success\"},{\"id\":\"e0fcug1poj\",\"name\":\"硬件资源6\",\"type\":\"hardWare\",\"left\":\"1570px\",\"top\":\"442px\",\"ico\":\"iconfont iconhardware\",\"state\":\"success\"},{\"id\":\"olyjdbcfb\",\"name\":\"测试工具6\",\"type\":\"testTool\",\"left\":\"1655px\",\"top\":\"442px\",\"ico\":\"iconfont icontextmachine\",\"state\":\"success\"},{\"id\":\"2eo8k31i4b\",\"name\":\"被测对象6\",\"type\":\"testObject\",\"left\":\"1735px\",\"top\":\"442px\",\"ico\":\"iconfont icontextobject\",\"state\":\"success\"}],\"remarks\":\"ceshi\"}";
////        String lineList="[{\"from\":\"zmsnfxqtgu\",\"to\":\"l2eb0k62w8\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"ihc3ouue16\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"uhfqoedokq\"},{\"from\":\"zmsnfxqtgu\",\"to\":\"gddqp502w\"},{\"from\":\"7ygj5u22ge\",\"to\":\"4559oj8gw\"},{\"from\":\"7ygj5u22ge\",\"to\":\"sp1jfg7qpc\"},{\"from\":\"7ygj5u22ge\",\"to\":\"ocd8xdvrwq\"},{\"from\":\"7ygj5u22ge\",\"to\":\"howdd8eyxe\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"xlywj4z0k\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"a7cp2ye6y\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"oa83419nb\"},{\"from\":\"0g4ouvjk3l\",\"to\":\"d1augvtbar\"},{\"from\":\"buc7uywt8\",\"to\":\"03x812dujj\"},{\"from\":\"03x812dujj\",\"to\":\"0g4ouvjk3l\"},{\"from\":\"03x812dujj\",\"to\":\"zmsnfxqtgu\"},{\"from\":\"03x812dujj\",\"to\":\"7ygj5u22ge\"},{\"from\":\"qfu2jwh89\",\"to\":\"yx45vrxg6\"},{\"from\":\"qfu2jwh89\",\"to\":\"ngvlgcxfd4\"},{\"from\":\"qfu2jwh89\",\"to\":\"toctbgilo8\"},{\"from\":\"qfu2jwh89\",\"to\":\"ym2kbvrh5j\"},{\"from\":\"jvavd10ld\",\"to\":\"lroq4hoo5c\"},{\"from\":\"jvavd10ld\",\"to\":\"721zwn32wt\"},{\"from\":\"jvavd10ld\",\"to\":\"xunyib5dg8\"},{\"from\":\"jvavd10ld\",\"to\":\"qg5jtt1c1h\"},{\"from\":\"81in3espue\",\"to\":\"rps5r9jkw\"},{\"from\":\"rps5r9jkw\",\"to\":\"jvavd10ld\"},{\"from\":\"rps5r9jkw\",\"to\":\"qfu2jwh89\"},{\"from\":\"43xmmfuq2c\",\"to\":\"julfsaceb\"},{\"from\":\"43xmmfuq2c\",\"to\":\"l3m8t4g8ms\"},{\"from\":\"43xmmfuq2c\",\"to\":\"1zbl990yvp\"},{\"from\":\"43xmmfuq2c\",\"to\":\"taa7z6l8eg\"},{\"from\":\"1ekdab0n3\",\"to\":\"2yspw39iz4\"},{\"from\":\"1ekdab0n3\",\"to\":\"e0fcug1poj\"},{\"from\":\"1ekdab0n3\",\"to\":\"olyjdbcfb\"},{\"from\":\"1ekdab0n3\",\"to\":\"2eo8k31i4b\"},{\"from\":\"95gt0wj6pr\",\"to\":\"xy3ojgxh2\"},{\"from\":\"xy3ojgxh2\",\"to\":\"43xmmfuq2c\"},{\"from\":\"xy3ojgxh2\",\"to\":\"1ekdab0n3\"},{\"from\":\"xy3ojgxh2\",\"to\":\"n9i027kgj4\"},{\"from\":\"rps5r9jkw\",\"to\":\"n9i027kgj4\"},{\"from\":\"03x812dujj\",\"to\":\"n9i027kgj4\"}]";
////        String nodeList="[{\"virCpu\":\"1\",\"vmName\":\"虚拟机\",\"top\":\"323px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"235px\",\"name\":\"虚拟机\",\"id\":\"zmsnfxqtgu\",\"state\":\"success\",\"type\":\"virtual\",\"virInner\":\"1\",\"virDisk\":\"100\"},{\"top\":\"422px\",\"ico\":\"iconfont iconsystem\",\"left\":\"125px\",\"name\":\"操作系统\",\"id\":\"l2eb0k62w8\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"422px\",\"ico\":\"iconfont iconhardware\",\"left\":\"190px\",\"name\":\"硬件资源\",\"id\":\"ihc3ouue16\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"422px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"275px\",\"name\":\"测试工具\",\"id\":\"uhfqoedokq\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"422px\",\"ico\":\"iconfont icontextobject\",\"left\":\"355px\",\"name\":\"被测对象\",\"id\":\"gddqp502w\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"48.0078125px\",\"ico\":\"iconfont iconnet\",\"left\":\"225.0078125px\",\"name\":\"网络\",\"id\":\"buc7uywt8\",\"state\":\"success\",\"type\":\"net\"},{\"top\":\"158px\",\"ico\":\"iconfont iconchildnet\",\"left\":\"243px\",\"name\":\"子网\",\"id\":\"03x812dujj\",\"state\":\"success\",\"type\":\"childNet\"},{\"top\":\"327.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"610.0078125px\",\"name\":\"虚拟机1\",\"id\":\"7ygj5u22ge\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"427px\",\"ico\":\"iconfont iconsystem\",\"left\":\"500px\",\"name\":\"操作系统1\",\"id\":\"4559oj8gw\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"427px\",\"ico\":\"iconfont iconhardware\",\"left\":\"565px\",\"name\":\"硬件资源1\",\"id\":\"sp1jfg7qpc\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"427px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"650px\",\"name\":\"测试工具1\",\"id\":\"ocd8xdvrwq\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"427px\",\"ico\":\"iconfont icontextobject\",\"left\":\"730px\",\"name\":\"被测对象1\",\"id\":\"howdd8eyxe\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"320px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"68px\",\"name\":\"虚拟机2\",\"id\":\"0g4ouvjk3l\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"408px\",\"ico\":\"iconfont iconsystem\",\"left\":\"66px\",\"name\":\"操作系统2\",\"id\":\"xlywj4z0k\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"408px\",\"ico\":\"iconfont iconhardware\",\"left\":\"131px\",\"name\":\"硬件资源2\",\"id\":\"a7cp2ye6y\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"408px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"216px\",\"name\":\"测试工具2\",\"id\":\"oa83419nb\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"408px\",\"ico\":\"iconfont icontextobject\",\"left\":\"296px\",\"name\":\"被测对象2\",\"id\":\"d1augvtbar\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"48.0078125px\",\"ico\":\"iconfont iconnet\",\"left\":\"805.0078125px\",\"name\":\"网络1\",\"id\":\"81in3espue\",\"state\":\"success\",\"type\":\"net\"},{\"top\":\"143.008px\",\"ico\":\"iconfont iconchildnet\",\"left\":\"798.008px\",\"name\":\"子网1\",\"id\":\"rps5r9jkw\",\"state\":\"success\",\"type\":\"childNet\"},{\"top\":\"296.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"919.0078125px\",\"name\":\"虚拟机3\",\"id\":\"qfu2jwh89\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"396px\",\"ico\":\"iconfont iconsystem\",\"left\":\"809px\",\"name\":\"操作系统3\",\"id\":\"yx45vrxg6\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"396px\",\"ico\":\"iconfont iconhardware\",\"left\":\"874px\",\"name\":\"硬件资源3\",\"id\":\"ngvlgcxfd4\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"396px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"959px\",\"name\":\"测试工具3\",\"id\":\"toctbgilo8\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"396px\",\"ico\":\"iconfont icontextobject\",\"left\":\"1039px\",\"name\":\"被测对象3\",\"id\":\"ym2kbvrh5j\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"331.008px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"836.008px\",\"name\":\"虚拟机4\",\"id\":\"jvavd10ld\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"431px\",\"ico\":\"iconfont iconsystem\",\"left\":\"726px\",\"name\":\"操作系统4\",\"id\":\"lroq4hoo5c\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"431px\",\"ico\":\"iconfont iconhardware\",\"left\":\"791px\",\"name\":\"硬件资源4\",\"id\":\"721zwn32wt\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"431px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"876px\",\"name\":\"测试工具4\",\"id\":\"xunyib5dg8\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"431px\",\"ico\":\"iconfont icontextobject\",\"left\":\"956px\",\"name\":\"被测对象4\",\"id\":\"qg5jtt1c1h\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"106.008px\",\"ico\":\"iconfont iconroute\",\"left\":\"566.008px\",\"name\":\"路由\",\"id\":\"n9i027kgj4\",\"state\":\"success\",\"type\":\"route\"},{\"top\":\"77.0078125px\",\"ico\":\"iconfont iconnet\",\"left\":\"1261.0078125px\",\"name\":\"网络2\",\"id\":\"95gt0wj6pr\",\"state\":\"success\",\"type\":\"net\"},{\"top\":\"195px\",\"ico\":\"iconfont iconchildnet\",\"left\":\"1275px\",\"name\":\"子网2\",\"id\":\"xy3ojgxh2\",\"state\":\"success\",\"type\":\"childNet\"},{\"top\":\"339.008px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"1328.01px\",\"name\":\"虚拟机5\",\"id\":\"43xmmfuq2c\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"439px\",\"ico\":\"iconfont iconsystem\",\"left\":\"1218px\",\"name\":\"操作系统5\",\"id\":\"julfsaceb\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"439px\",\"ico\":\"iconfont iconhardware\",\"left\":\"1283px\",\"name\":\"硬件资源5\",\"id\":\"l3m8t4g8ms\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"439px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"1368px\",\"name\":\"测试工具5\",\"id\":\"1zbl990yvp\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"439px\",\"ico\":\"iconfont icontextobject\",\"left\":\"1448px\",\"name\":\"被测对象5\",\"id\":\"taa7z6l8eg\",\"state\":\"success\",\"type\":\"testObject\"},{\"top\":\"342.0078125px\",\"ico\":\"iconfont iconvirmachine\",\"left\":\"1615.0078125px\",\"name\":\"虚拟机6\",\"id\":\"1ekdab0n3\",\"state\":\"success\",\"type\":\"virtual\"},{\"top\":\"442px\",\"ico\":\"iconfont iconsystem\",\"left\":\"1505px\",\"name\":\"操作系统6\",\"id\":\"2yspw39iz4\",\"state\":\"success\",\"type\":\"system\"},{\"top\":\"442px\",\"ico\":\"iconfont iconhardware\",\"left\":\"1570px\",\"name\":\"硬件资源6\",\"id\":\"e0fcug1poj\",\"state\":\"success\",\"type\":\"hardWare\"},{\"top\":\"442px\",\"ico\":\"iconfont icontextmachine\",\"left\":\"1655px\",\"name\":\"测试工具6\",\"id\":\"olyjdbcfb\",\"state\":\"success\",\"type\":\"testTool\"},{\"top\":\"442px\",\"ico\":\"iconfont icontextobject\",\"left\":\"1735px\",\"name\":\"被测对象6\",\"id\":\"2eo8k31i4b\",\"state\":\"success\",\"type\":\"testObject\"}]";
//        EnvPlan envPlan = JSON.parseObject(data, EnvPlan.class);
//        System.out.println("envPlan:"+JSON.toJSONString(envPlan));
//        System.out.println("nodes:"+JSON.toJSONString(envPlan.getNodeList()));
//        System.out.println("lines:"+JSON.toJSONString(envPlan.getLineList()));
//
//        Map<String, List<EnvPlanNodes>> nodes = new HashMap<>();
//        System.out.println("lines:"+JSON.toJSONString(nodes));
//        //获取type不同分类集合
//        envPlan.getNodeList().forEach(n->{
//            List<EnvPlanNodes> plans = nodes.get(n.getType());
//            if (plans==null){
//                plans = new ArrayList<>();
//            }
//            plans.add(n);
//            nodes.put(n.getType(),plans);
//        });
//
//        //统计
//        nodes.forEach((k,v)->{
//            if ("net".equals(k)){
//                System.out.println("网络数："+v.size());
//            }
//            if ("childNet".equals(k)){
//                System.out.println("子网数："+v.size());
//            }
//            if ("route".equals(k)){
//                System.out.println("路由数："+v.size());
//            }
//            if ("virtual".equals(k)){
//                System.out.println("虚拟机数："+v.size());
//            }
//        });
//        String planJson = getPlanJson(nodes.get("net"), nodes.get("childNet"), nodes.get("route"), nodes.get("virtual"),envPlan.getLineList());
//        System.out.println("planJson:"+planJson);
    }

//    public static void findProcess(String processName) {
//        BufferedReader bufferedReader = null;
//        try {
//            int count = 0;
//            while (true){
//                count++;
//                Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName +'"');
//                bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    if (line.contains(processName)) {
//                        count = 1;
//                        System.out.println("count:"+count+",name:"+line);
//                        break;
//                    }
//                    System.out.println("count:"+count);
//                    TimeUnit.SECONDS.sleep(1);
//                }
//                if (count >= 10){
//                    break;
//                }
//            }
//
//        } catch (Exception ex) {
//            log.error("判断进程是否存在异常:"+ex.getMessage());
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (Exception ignored) {}
//            }
//        }
//    }

    public void httpOpenStatic() {
        //        String json = "{\"heat_template_version\":\"2013-05-23\",\"resources\":{\"网络\":{\"type\":\"OS::Neutron::Net\",\"properties\":{\"port_security_enabled\":\"True\"}},\"网络1\":{\"type\":\"OS::Neutron::Net\",\"properties\":{\"port_security_enabled\":\"True\"}},\"子网\":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"cidr\":\"10.0.0.0/24\",\"enable_dhcp\":\"True\"}},\"子网1\":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"cidr\":\"10.0.0.0/24\",\"enable_dhcp\":\"True\"}},\"子网2\":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"网络1\"},\"cidr\":\"10.0.0.0/24\",\"enable_dhcp\":\"True\"}},\"router\":{\"type\":\"OS::Neutron::Router\",\"properties\":{\"name\":\"test-router\",\"external_gateway_info\":{\"network\":\"public-network\"}}},\"router_interface2\":{\"type\":\"OS::Neutron::RouterInterface\",\"properties\":{\"router_id\":{\"Ref\":\"router\"},\"subnet_id\":{\"Ref\":\"子网1\"}}},\"router_interface3\":{\"type\":\"OS::Neutron::RouterInterface\",\"properties\":{\"router_id\":{\"Ref\":\"router\"},\"subnet_id\":{\"Ref\":\"子网2\"}}},\"port-虚拟机\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机\"}}]}},\"port-虚拟机1\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机1\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机1\"}}]}},\"port-虚拟机2\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网1\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机2\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机2\"}}]}},\"port-虚拟机3\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络1\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网2\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机3\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机3\"}}]}},\"port-虚拟机4\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络1\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网2\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机4\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机4\"}}]}}}}";
        String json = "{\"heat_template_version\":\"2013-05-23\",\"resources\":{\"网络\":{\"type\":\"OS::Neutron::Net\",\"properties\":{\"port_security_enabled\":\"True\"}},\"子网\":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"cidr\":\"10.0.0.0/24\",\"enable_dhcp\":\"True\"}},\"port-虚拟机\":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"网络\"},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"子网\"}}],\"security_groups\":[\"default\"]}},\"Server-虚拟机\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"36d58b27-7db0-41f2-b385-ac48fccd389e\",\"flavor\":\"1c_1g_100G\",\"key_name\":\"oskey\",\"networks\":[{\"port\":{\"get_resource\":\"port-虚拟机\"}}]}}}}";
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置content-type
        String token = "Bearer gAAAAABgdAW5ug4mLeyxmhCW6cnVYsBJ0cl7cp8n-uh9FS52us5deOPOwjygRJJ1_7FJksMfzYP4EThaJcnXa_yINYhhEhPfD5HCCx2KlzKDpFbpj7bhfzStUsDTkHNEDxXh7LkOAz-KfsglZXY6k5EJsQUhaEFnag";
//        headers.add("Authorization", "Bearer gAAAAABgdAW5ug4mLeyxmhCW6cnVYsBJ0cl7cp8n-uh9FS52us5deOPOwjygRJJ1_7FJksMfzYP4EThaJcnXa_yINYhhEhPfD5HCCx2KlzKDpFbpj7bhfzStUsDTkHNEDxXh7LkOAz-KfsglZXY6k5EJsQUhaEFnag");
        headers.set("Authorization", "Bearer " + token);
        //设置编码格式
//        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
//        headers.setContentType(type);
        headers.add("accept", "application/json");
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "ceshi-2021-04-07-1_13796097538502819851_003");
        params.put("tags", null);
        params.put("template_content", json);
        params.put("rollback", true);
        //将请求头部和参数合成一个请求
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        ResponseEntity<String> responseEntity = client.postForEntity("http://192.168.1.203:8001/api/orchestration/stacks", requestEntity, String.class);
        System.out.println(responseEntity.toString());
        System.out.println(JSON.toJSONString(responseEntity));
    }


    private static String getPlanJson(List<EnvPlanNodes> nets, List<EnvPlanNodes> childNets, List<EnvPlanNodes> routes, List<EnvPlanNodes> virtuals, List<EnvPlanLines> lineList) {
        //子网添加网络名称
        nets.forEach(n -> {
            lineList.forEach(m -> {
                if (n.getId().equals(m.getFrom())) {
                    childNets.forEach(k -> {
                        if (k.getId().equals(m.getTo())) {
                            k.setNetName(n.getName());
                        }
                    });
                }
            });
        });
        //虚拟机添加子网名称
        childNets.forEach(n -> {
            lineList.forEach(m -> {
                if (n.getId().equals(m.getFrom())) {
                    virtuals.forEach(k -> {
                        if (k.getId().equals(m.getTo())) {
                            k.setChildNetName(n.getName());
                            k.setNetName(n.getNetName());
                        }
                    });
                }
            });
        });
        StringBuffer sb = new StringBuffer();
        sb.append("{").append("\"heat_template_version\": \"").append(DateUtil.now()).append("\"").append(",").append("\"resources\": {");
        //添加网络
        nets.forEach(n -> {
            sb.append("\"").append(n.getName()).append("\"").append(":{\"type\":\"OS::Neutron::Net\",\"properties\":{\"port_security_enabled\":\"True\"}},");
        });
        //添加子网
        childNets.forEach(n -> {
            sb.append("\"").append(n.getName()).append("\"").append(":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"");
            sb.append(n.getNetName()).append("\"").append("},\"cidr\":\"").append(StringUtils.isEmpty(n.getCidr()) ? "10.0.0.0/24" : n.getCidr()).append("\"");
            sb.append(",\"enable_dhcp\":\"True\"}},");
        });
        //添加路由
        sb.append("\"router\":{\"type\":\"OS::Neutron::Router\",\"properties\":{\"name\":\"test-router\",\"external_gateway_info\":{\"network\":\"public-network\"}}},");
        routes.forEach(n -> {
            lineList.forEach(m -> {
                if (n.getId().equals(m.getTo())) {
                    for (int i = 0; i < childNets.size(); i++) {
                        if (childNets.get(i).getId().equals(m.getFrom())) {
                            sb.append("\"router_interface").append(i + 1).append("\"");
                            sb.append(":{\"type\":\"OS::Neutron::RouterInterface\",\"properties\":{\"router_id\":{\"Ref\":\"router\"},\"subnet_id\":{\"Ref\":\"");
                            sb.append(childNets.get(i).getName()).append("\"}}},");
                        }
                    }
                }
            });
        });

        //添加虚拟机
        virtuals.forEach(n -> {
            sb.append("\"").append("port-").append(n.getName()).append("\"").append(":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":{\"get_resource\":\"");
            sb.append(n.getNetName()).append("\"").append("},\"fixed_ips\":[{\"subnet_id\":{\"Ref\":\"").append(n.getChildNetName()).append("\"}}],\"security_groups\":[\"default\"]}},");
            sb.append("\"").append("Server-").append(n.getName()).append("\"").append(":{\"type\":\"OS::Nova::Server\",\"properties\":{\"image\":\"");
            sb.append(n.getImage()).append("\",\"flavor\":\"").append(StringUtils.isEmpty(n.getVirCpu()) ? "1" : n.getVirCpu()).append("c");
            sb.append("_").append(StringUtils.isEmpty(n.getVirInner()) ? "1" : n.getVirInner()).append("g").append("_");
            sb.append(StringUtils.isEmpty(n.getVirDisk()) ? "100" : n.getVirDisk()).append("G");
            sb.append("\", \"key_name\": \"oskey\", \"networks\": [{\"port\":{\"get_resource\": \"");
            sb.append("port-").append(n.getName()).append("\"}}]}},");
        });
        //结尾
        sb.append("\"outputs\":{\"WebsiteURL\":{\"description\":\"list for servers\",\"value\":{\"str_replace\":{\"template\":\"server\",\"params\":{\"server\":{\"get_attr\":[\"Server\",\"networks\",\"private\",0]}}}}}}}}");
        return sb.toString();
    }

}
