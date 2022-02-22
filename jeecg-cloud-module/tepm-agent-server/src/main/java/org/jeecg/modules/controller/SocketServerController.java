package org.jeecg.modules.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.common.WebToolUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.entity.ClientSocket;
import org.jeecg.modules.entity.NtepmTask;
import org.jeecg.modules.entity.ResultSocket;
import org.jeecg.modules.service.*;
import org.jeecg.modules.socket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;


/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/12
 * @Description: 用一句话描述该文件做什么)
 */
@Slf4j
@Api(tags = "Socket服务端")
@RestController
@RequestMapping("/socket/server")
public class SocketServerController {

    @Autowired
    private IVirCollectService iVirCollectService;
    @Autowired
    private SocketService socketService;
    @Autowired
    private IpfsService ipfsService;
    @Resource
    private INtepmTaskService iNtepmTaskService;

    @AutoLog(value = "SocketServer-查看IP地址是多少")
    @ApiOperation(value = "SocketServer-查看IP地址是多少", notes = "SocketServer-查看IP地址是多少")
    @PostMapping(value = "/getIp")
    public Result<?> sendToClientMsg() {
        try {
            String ip = WebToolUtils.getLocalIP();
            return Result.OK(ip);
        } catch (Exception e) {
            return Result.error("IP获取异常：" + e.getMessage());
        }
    }

    @AutoLog(value = "SocketServer-注册")
    @ApiOperation(value = "SocketServer-注册", notes = "SocketServer-注册")
    @PostMapping(value = "/register")
    public Result<?> registerSocket(String ip) {
        try {
            ClientSocket clientSocket = socketService.registerSocket(ip);
            if (clientSocket != null){
                return Result.OK(JSON.toJSONString(clientSocket));
            }
        } catch (Exception e) {
            log.error("注册异常:"+e.getMessage());
            return Result.error("注册异常:"+e.getMessage());
        }
        return Result.error("注册失败");
    }

    @AutoLog(value = "SocketServer-to发送消息")
    @ApiOperation(value = "SocketServer-to发送消息", notes = "SocketServer-to发送消息")
    @PostMapping(value = "/to/sendMsg")
    public Result<?> sendMsg(String ip,ResultSocket resultSocket) {
        try {
            ClientSocket clientSocket = socketService.registerSocket(ip);
            //把文件转换成 hash值发送到client
            if (resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_0)){
                if (StringUtils.isNotBlank(resultSocket.getPath())){
                    String upload = ipfsService.upload(resultSocket.getPath());
                    resultSocket.setHash(upload);
                }
            }
            if (clientSocket != null){
                SocketHandler.sendMessage(clientSocket,JSON.toJSONString(resultSocket));
                socketService.sendMsg(clientSocket);
                return Result.OK("发送成功!");
            }
            return Result.OK("发送失败!");
        } catch (Exception e) {
            log.error("注册异常:"+e.getMessage());
            return Result.error("注册异常:"+e.getMessage());
        }
    }

//    @AutoLog(value = "SocketServer-发送消息")
//    @ApiOperation(value = "SocketServer-发送消息", notes = "SocketServer-发送消息")
//    @PostMapping(value = "/sendMsg")
//    public boolean sendToClientMsg(String key, String msg, Integer state) {
//        try {
//            return socketSeverService.sendToClientMsg(key, msg, state);
//        } catch (Exception e) {
//            log.info("发送设备:" + key + " 发送数据: >>>>>>" + msg + "，异常！！！原因：" + e);
//            return false;
//        }
//    }

//    @AutoLog(value = "SocketServer-修改主服务表收集时间")
//    @ApiOperation(value = "SocketServer-修改主服务表收集时间", notes = "SocketServer-修改主服务表收集时间")
//    @GetMapping(value = "/updateCoolecTime")
//    public Result<?> updateCoolecTime(@RequestParam("userName") String userName, @RequestParam("time") String time) {
//        log.info("SocketServer-修改主服务表收集时间:userName:" + userName + ",time:" + time);
//        try {
//            if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(time)) {
//                SocketPool.updateCollectTime(userName, time);
//                return Result.OK("修改成功");
//            }
//        } catch (Exception e) {
//            return Result.error("异常:" + e.getMessage());
//        }
//        return Result.error("失败");
//    }

//    @AutoLog(value = "SocketServer-发送采集消息指定客户端")
//    @ApiOperation(value = "SocketServer-发送采集消息指定客户端", notes = "SocketServer-发送采集消息指定客户端")
//    @GetMapping(value = "/sendMsg/collect")
//    public boolean sendToClientMsgCollect(String key) {
//        try {
//            if (StringUtils.isNotEmpty(key)) {
//                SocketServer.handleCollecttstate = true;
//                SocketServer.key = key;
//                log.info("手动发指令采集客户端发过来的监控数据");
//                ClientSocket clientSocket = SocketPool.getClientSocket(key);
//                if (clientSocket != null && StringUtils.isNotEmpty(clientSocket.getKey())) {
//                    // 给客户端发送采集指令
//                    SocketHandler.sendMessage(clientSocket, JSON.toJSONString(Result.socketData("true")));
//                    // 监听客户端发送过来的数据
//                    Result<?> message = SocketHandler.onMessage(clientSocket);
//                    if (message != null && message.isSuccess()) {
//                        log.info(LocalDateTime.now() + "当前设备:" + clientSocket.getKey() + " 接收到数据: <<<<<<" + message.getMessage());
//                        // 收集Agent采集的数据入库
//                        String result = JSON.parseObject(message.getResult().toString()).getString("result");
//                        if (result.contains("ip") && result.contains("cpu") && result.contains("disk")) {
//                            VirCollect virCollect = JSON.parseObject(result, VirCollect.class);
//                            iVirCollectService.save(virCollect);
//                        }
//                        return true;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.info("采集消息指定客户端异常:" + e);
//            return false;
//        }
//        return false;
//    }

//    @AutoLog(value = "SocketServer-发送消息-启动指定客户端")
//    @ApiOperation(value = "SocketServer-发送消息-启动指定客户端", notes = "SocketServer-发送消息-启动指定客户端")
//    @PostMapping(value = "/sendMsg/start")
//    public boolean sendToClientMsgStart(String key) {
//        try {
//            return socketSeverService.sendToClientMsg(key, JSON.toJSONString(Result.socketData("true")), 1);
//        } catch (Exception e) {
//            log.info("发送设备:" + key + " 启动指定客户端异常！！！原因：" + e);
//            return false;
//        }
//    }

//    @AutoLog(value = "SocketServer-发送消息-停止指定客户端")
//    @ApiOperation(value = "SocketServer-发送消息-停止指定客户端", notes = "SocketServer-发送消息-停止指定客户端")
//    @PostMapping(value = "/sendMsg/stop")
//    public boolean sendToClientMsgStop(String key) {
//        try {
//            return socketSeverService.sendToClientMsg(key, JSON.toJSONString(Result.socketData("false")), 2);
//        } catch (Exception e) {
//            log.info("发送设备:" + key + " 停止指定客户端异常！！！原因：" + e);
//            return false;
//        }
//    }
}
