package org.jeecg.modules.socket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.Service.DownFileService;
import org.jeecg.modules.Service.ToolsService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.SpringUtils;
import org.jeecg.modules.entity.ClientSocket;
import org.jeecg.modules.entity.ResultSocket;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/6/22
 * socket服务中心
 */
@Slf4j
@Component
public class SocketServers {

    @Value("${socket.server.collect.port}")
    private String collectPort;

    @Resource
    private ToolsService toolsService;
    @Autowired
    private IpfsService ipfsService;


    public void socketServer(){
        ServerSocket serverSocket = null;
        while (serverSocket == null) {
            serverSocket = start();
        }
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(10000);
                socket.setKeepAlive(true);
                SocketHandler socketHandler = new SocketHandler();
                ClientSocket register = socketHandler.register(socket);
                if (register != null) {
                    SocketServers bean = SpringUtils.getBean(SocketServers.class);
                    bean.handlerSocket(register);
                }
            }catch (IOException e){
                log.error("客户端连接失败： {}", e.getMessage());
            }
        }
    }

    @Async
    public void handlerSocket(ClientSocket clientSocket) {
        log.info("监听客户端发送过来的数据");
        while (true){
            try {
                // 监听客户端发送过来的数据
                if (SocketHandler.isSocketClosed(clientSocket)) {
                    log.info("clientSocket 关闭连接了，不在接收发送消息");
                    break;
                }
                ResultSocket resultSocket = new SocketHandler().onMessage(clientSocket);
                if (resultSocket != null){
                    log.info("resultSocket:"+JSON.toJSONString(resultSocket));
                    //type 0 扫描文件
                    if (resultSocket.getType()!= null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_0)){
                        log.info("扫描文件path:"+resultSocket.getPath());
                        //文件地址
                        String path = ipfsService.download(resultSocket.getPath(),resultSocket.getHash());
                        log.info("文件路径："+ path);
                        //调用UnderStand 测试工具进行测试 并产生报告 把报告上传转成hash
                        ResultSocket resultSockets = toolsService.callUnderStandPath(path);
                        if (resultSockets != null ){
                            //测试报告转化为 hash
                            resultSocket.setHash(resultSockets.getHash());
                            resultSocket.setType(CommonConstant.LOG_TYPE_2);
                            resultSocket.setPath(resultSockets.getPath());
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_200);
                            SocketHandler.sendMessage(clientSocket, JSON.toJSONString(resultSocket));
                        }
                    }
                    //type 1 url
                    if (resultSocket.getType()!= null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_1)){
                        log.info("url:"+resultSocket.getUrl());
                        //调用appScan 测试工具进行测试 并产生报告 把报告上传转成hash
                        ResultSocket resultSockets = toolsService.callAppScan(resultSocket.getUrl());
                        if (resultSockets != null){
                            resultSocket.setPath(resultSockets.getPath());
                            resultSocket.setHash(resultSockets.getHash());
                            resultSocket.setType(CommonConstant.LOG_TYPE_1);
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_200);
                            SocketHandler.sendMessage(clientSocket,JSON.toJSONString(resultSocket));
                        }
                    }
                    //TODO type 3 autoit
                    if (resultSocket.getType()!= null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_3)){
                        log.info("测试工具标识:"+resultSocket.getTool());
                        if (toolsService.callAutoIt(resultSocket.getTool())) {
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_200);
                        }else{
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_500);
                        }
                        SocketHandler.sendMessage(clientSocket,JSON.toJSONString(resultSocket));
                    }
                    //监控测试工具进程
                    if (resultSocket.getType()!= null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_4)){
                        log.info("测试工具标识:"+resultSocket.getTool());
                        if (toolsService.monitorProcess(resultSocket.getToolProcessName(),resultSocket.getToolLinuxProcessName())) {
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_200);
                            resultSocket.setIsSuccess(CommonConstant.STATUS_1);
                        }else{
                            resultSocket.setCode(CommonConstant.DATA_CODE_INT_500);
                            resultSocket.setIsSuccess(CommonConstant.STATUS_0);
                        }
                        SocketHandler.sendMessage(clientSocket,JSON.toJSONString(resultSocket));
                    }
                }
                TimeUnit.SECONDS.sleep(3);
            }catch (Exception e){
                log.error("监听客户端发送过来的数据异常:"+e.getMessage());
            }
        }
    }

    private ServerSocket start() {
        try {
            String port = collectPort;
            if (StringUtils.isEmpty(port)) {
                port = "9050";
            }
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port));
            log.info("Socket服务已启动，占用端口： {}", serverSocket.getLocalPort());
            return serverSocket;
        } catch (IOException e) {
            log.error("端口冲突,异常信息：{}", e.getMessage());
            return null;
        }
    }

}
