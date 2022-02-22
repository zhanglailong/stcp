package org.jeecg.modules.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.management.OperatingSystemMXBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/13
 */
@Slf4j
@Component
@Data
public class SocketClentController {

    private static Boolean started = true;
    private static Boolean waitState = true;
    private static Boolean waitRegisterState = true;
    private static volatile Boolean registerState = false;
    private static Boolean collecState = true;
    private static Boolean receiveState = true;
    private static Boolean receiveController = true;
    private static Socket socket;
    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;
    private static String ip;

    @Value("${socket.server.collect.ip}")
    private String collectIp;
    @Value("${socket.server.collect.scheduler}")
    private String collectScheduler;
    @Value("${socket.server.collect.port}")
    private Integer collectPort;


    public void monitorVm(String eip) {
        String host = collectIp;
        Integer port = collectPort;
        log.info("eip:" + eip);
        ip = eip;
        while (started) {
            try {
                // 与服务端建立连接
                socket = new Socket(host, port);
                socket.setOOBInline(true);
                // 建立连接后获取输出流
                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());
                started = false;
                break;
            } catch (Exception e) {
                log.info("与服务端建立连接异常:{}", e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        try {
            new Thread(() -> {
                try {
                    clientCollectData();
                } catch (Exception e) {
                    log.info("接收服务端发送的信息异常2:{}：", e.getMessage());
                }
            }).start();
            waitRegister();
        } catch (Exception e) {
            log.info("客户端连接服务器异常:{}：", e.getMessage());
        }
    }

    /**
     * 等待注册中.......
     */
    public void waitRegister() throws InterruptedException {
        log.info("等待注册中.......");
        int successR = 0;
        while (waitState) {
            try {
                if (waitRegisterState) {
                    if (successR == 2) {
                        successR = 0;
                        socket = new Socket(collectIp, collectPort);
                        socket.setOOBInline(true);
                        // 建立连接后获取输出流
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        inputStream = new DataInputStream(socket.getInputStream());
                    }
                    outputStream.write(JSON.toJSONString(Result.socketRegister(ip)).getBytes());
                    log.info("注册中.......");
                    byte[] buff = new byte[1024];
                    StringBuilder readInputStream = new StringBuilder();
                    while (inputStream.read(buff) != -1){
                        readInputStream.append(new String(buff, StandardCharsets.UTF_8).trim());
                    }
//                    inputStream.read(buff);
//                    String sendMsg = new String(buff, StandardCharsets.UTF_8).trim();
                    String sendMsg = readInputStream.toString();
                    log.info("接收服务端发送的信息: {}", sendMsg);
                    if (StringUtils.isNotEmpty(sendMsg)) {
                        String backCode = JSON.parseObject(sendMsg).get("code").toString();
                        String result = JSON.parseObject(sendMsg).get("result").toString();
                        if (StringUtils.isEmpty(backCode) || StringUtils.isEmpty(result)) {
                            log.info("不存在，注册失败");
                        } else {
                            if (backCode.equals(CommonConstant.SOCKET_REGISTER_CODE_201.toString()) && "true".equals(result)) {
                                log.info("注册成功");
                                registerState = true;
                                waitRegisterState = false;
                                successR = 1;
                            }
                        }
                    }
                    if (successR == 0) {
                        log.info("注册失败,重新发数据");
                        socket = new Socket(collectIp, collectPort);
                        socket.setOOBInline(true);
                        // 建立连接后获取输出流
                        outputStream = new DataOutputStream(socket.getOutputStream());
                        inputStream = new DataInputStream(socket.getInputStream());
                        outputStream.write(JSON.toJSONString(Result.socketRegister(ip)).getBytes());
                    }
                }
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                log.info("注册异常：{}", e.getMessage());
                TimeUnit.SECONDS.sleep(20);
                successR = 2;
            }
        }
    }

    /**
     * 解析服务端返回的数据 注册知否成功 成功返回true 失败 false
     *
     * @param data data
     * @param code code
     * @return 布尔值
     */
    public boolean registerComeBack(String data, Integer code) {
        JSONObject objComeData = JSON.parseObject(data);
        String backCode = objComeData.get("code").toString();
        if (StringUtils.isEmpty(backCode)) {
            log.info("不存在，注册失败");
            return false;
        }
        if (backCode.equals(CommonConstant.SOCKET_REGISTER_CODE_201.toString())) {
            log.info("注册成功");
            return true;
        }
        return false;
    }

    /**
     * 客户端收集数据指令
     */
    public void clientCollectData() throws IOException {
        while (collecState) {
            try {
                if (registerState) {
                    byte[] buff = new byte[1024];
                    StringBuilder readInputStream = new StringBuilder();
                    while (inputStream.read(buff) != -1){
                        readInputStream.append(new String(buff, StandardCharsets.UTF_8).trim());
                    }
//                    inputStream.read(buff);
//                    String sendMsg = new String(buff, StandardCharsets.UTF_8).trim();
                    String sendMsg = readInputStream.toString();
                    JSONObject objServerData = JSON.parseObject(sendMsg);
                    if (objServerData != null) {
                        String backCode = objServerData.get("code").toString();
                        if (StringUtils.isNotEmpty(backCode) && backCode.equals(CommonConstant.SOCKET_REGISTER_CODE_202.toString())) {
                            String result = objServerData.get("result").toString();
                            if (StringUtils.isNotEmpty(result) && "true".equals(result)) {
                                log.info("Agent收到服务端发送收集数据指令");
                                // 这里是客户端收集数据往服务器发送数据
                                outputStream.write(JSON.toJSONString(Result.socketData(collectData())).getBytes());
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.info("接收服务端发送的信息异常:{}：", e.getMessage());
                registerState = false;
                waitRegisterState = true;
                socket.close();
            }
        }
    }

    public String collectData() {
        try {
//              打印处理器信息
            long free;
            long use;
            long total;
            int kb = 1024;
//              获取当前程序运行时
            Runtime rt = Runtime.getRuntime();
//              获取虚拟机名称
            String name = System.getProperty("java.vm.name");
//              获取虚拟机内存
            total = rt.totalMemory();
            free = rt.freeMemory();
            use = total - free;
            String ram = String.valueOf(use);
//                获取虚拟机CPU
            String cpu = String.valueOf(rt.availableProcessors());
//                获取虚拟机磁盘
            OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            long physicalFree = osmxb.getFreePhysicalMemorySize() / kb;
            long physicalTotal = osmxb.getTotalPhysicalMemorySize() / kb;
            long physicalUse = physicalTotal - physicalFree;
            String disk = String.valueOf(physicalUse);
//                获取虚拟机ip
            InetAddress addr = InetAddress.getLocalHost();
            String ip = addr.getHostAddress();

            Map<String, Object> virMap = new HashMap<>(5);
            virMap.put("name", name);
            virMap.put("ram", ram);
            virMap.put("disk", disk);
            virMap.put("cpu", cpu);
            virMap.put("ip", ip);
            return JSON.toJSONString(virMap);
        } catch (Exception e) {
            log.info("Agent收集数据异常{}", e.getMessage());
        }
        return null;
    }

}
