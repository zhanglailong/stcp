package org.jeecg.modules.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.entity.ClientSocket;
import org.jeecg.modules.entity.NtepmTask;
import org.jeecg.modules.entity.ResultSocket;
import org.jeecg.modules.entity.ToolsAndVm;
import org.jeecg.modules.service.INtepmTaskService;
import org.jeecg.modules.service.IToolsAndVmService;
import org.jeecg.modules.service.IpfsService;
import org.jeecg.modules.service.SocketService;
import org.jeecg.modules.socket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * @author zlf
 */
@Slf4j
@Service
public class SocketServiceImpl implements SocketService {

    @Value("${socket.server.vmPort}")
    private Integer serverVmPort;
    @Resource
    private INtepmTaskService iNtepmTaskService;
    @Resource
    private IToolsAndVmService iToolsAndVmService;
    @Autowired
    private IpfsService ipfsService;

    @Override
    public ClientSocket registerSocket(String ip) {
        log.info(ip + " " + serverVmPort);
        return new SocketHandler().registerServer(ip, serverVmPort);
    }

    @Override
    @Async
    public void sendMsg(ClientSocket clientSocket) {
        log.info("开始监听Agent回执消息");
        TimeInterval timer = DateUtil.timer();
        while (true) {
            try {
                if (SocketHandler.isSocketClosed(clientSocket)) {
                    log.info("clientSocket 关闭连接了，不在接收发送消息");
                    break;
                }
                ResultSocket resultSocket = new SocketHandler().onMessage(clientSocket);
                log.info("获取客户端发来的消息  >>>>>>>>>>>>>>>>>>" + resultSocket.getType());
                if (resultSocket != null) {
                    //String appScanPath = ipfsService.download(resultSocket.getPath(),resultSocket.getHash());
                    log.info("监听接收发送消息的回执信息为:" + JSON.toJSONString(resultSocket));
                    //TODO type 0 扫描文件
                    if (resultSocket.getType() != null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_0)) {
                        log.info("扫描文件path:" + resultSocket.getPath());
                    }
                    //appScan 1 url
                    if (resultSocket.getType() != null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_1)) {
                        log.info("url:" + resultSocket.getUrl());
                        //下载测试报告
                        NtepmTask ntepmTask = new NtepmTask();
                        //ntepmTask.setReportUrl(appScanPath.replace("c:","").replace("C:",""));
                        ntepmTask.setReportHash(resultSocket.getHash());
                        ntepmTask.setId(resultSocket.getId());
                        iNtepmTaskService.edit(ntepmTask);
                    }
                    //测试报告
                    if (resultSocket.getType() != null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_2)) {
                        log.info("报告path:" + resultSocket.getPath());
                        //下载测试报告
                        NtepmTask ntepmTask = new NtepmTask();
                        //ntepmTask.setReportUrl(appScanPath.replace("c:","").replace("C:",""));
                        ntepmTask.setReportHash(resultSocket.getHash());
                        ntepmTask.setId(resultSocket.getId());
                        iNtepmTaskService.edit(ntepmTask);
                    }
                    //TODO type 3 autoit
                    if (resultSocket.getType() != null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_3)) {
                        log.info("autoit:" + resultSocket.getTool());
                    }
                    //监控测试工具进程
                    if (resultSocket.getType() != null && resultSocket.getType().equals(CommonConstant.DATA_CODE_INT_4)) {
                        log.info("测试工具:" + resultSocket.getTool() + "测试工具编号:" + resultSocket.getToolId());
                        try {
                           /* if (iToolsAndVmService.saveOrUpdate(iToolsAndVmService.getById(resultSocket.getToolId()).
                                    setToolsRunStatus(resultSocket.getIsSuccess()))) {
                                log.info("测试工具运行状态 0不存在 1 存在>>>>>> ："+resultSocket.getIsSuccess());
                            }*/
                            if (iToolsAndVmService.update(new UpdateWrapper<ToolsAndVm>()
                                    .eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0)
                                    .eq(CommonConstant.DATA_STRING_ID, resultSocket.getToolId())
                                    .set(CommonConstant.DATA_STRING_TOOLS_RUN_STATUS, resultSocket.getIsSuccess()))) {
                                log.info("测试工具运行状态 0不存在 1 存在>>>>>> ：" + resultSocket.getIsSuccess());
                            }
                        } catch (Exception e) {
                            log.error("测试工具监控状态修改失败" + e.getMessage());
                        }

                    }
                    break;
                }
                long minute = timer.intervalMinute();
                //大于60分钟释放线程
                if (minute >= 60) {
                    break;
                }
            } catch (Exception e) {
                log.error("监听接收发送消息的回执信息异常:" + e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }

        }
        log.info("线程释放了");
    }
}
