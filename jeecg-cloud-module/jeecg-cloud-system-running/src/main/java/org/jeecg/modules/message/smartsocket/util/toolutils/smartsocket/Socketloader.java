package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.client.entity.RunningClient;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.util.BufferPageMonitor;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.util.Monitor;
import org.smartboot.socket.Protocol;
import org.smartboot.socket.buffer.BufferPagePool;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
@DependsOn("springContextUtils")
@Component
/**
 * @Author: test
 * */
public class Socketloader {
	@Autowired
	private AbstractMessageProcessor<String> processor;
	@Autowired
	private Protocol<String> stringProtocol;
	@Autowired
	private IRunningClientService runningClientService;
	private AioQuickServer<String> server;
	
	@Value("${smartsocket.port}")
	private Integer port;
	
	@PostConstruct
	public void setClientStatus() {
		UpdateWrapper<RunningClient> upWrapper = new UpdateWrapper();
		upWrapper.set("client_state", "0");
		runningClientService.update(upWrapper);
	}
	
	@PostConstruct
	public void startSocket() throws IOException {
        

    	BufferPagePool bufferPagePool = new BufferPagePool(1024 * 1024 * 16, Runtime.getRuntime().availableProcessors() + 1, true);
        AbstractMessageProcessor<String> processor = new MessageProcessor();
        
        //注册心跳插件：每隔25秒发送一次心跳请求，60秒内未收到消息超时关闭连接
        processor.addPlugin(new HeartPlugin<String>(25, 60, TimeUnit.SECONDS) {
            @Override
            public void sendHeartRequest(AioSession session) throws IOException {
                WriteBuffer writeBuffer = session.writeBuffer();
                byte[] heartBytes = "heart_req".getBytes();
                writeBuffer.writeInt(heartBytes.length);
                writeBuffer.write(heartBytes);
                writeBuffer.flush();
                log.info("发送心跳请求至客户端:{}", session.getSessionID());
            }

            @Override
            public boolean isHeartMessage(AioSession session, String msg) {
            	String ip = null;
				try {
					ip = session.getRemoteAddress().toString();
					if(!StringUtils.isEmpty(ip)) {
                        ip = ip.substring(1).split(":")[0];
                    }
				} catch (IOException e1) {
					 log.info(e1.getMessage());
				}
                //心跳请求消息,返回响应
                String heartReq="heart_req";
                if (heartReq.equals(msg)) {
                    try {
                        WriteBuffer writeBuffer = session.writeBuffer();
                        byte[] heartBytes = "heart_rsp".getBytes();
                        writeBuffer.writeInt(heartBytes.length);
                        writeBuffer.write(heartBytes);
                        writeBuffer.flush();
                    } catch (Exception e) {
                    }
                    return true;
                }
                //是否为心跳响应消息
                String heartRsp="heart_rsp";
                if (heartRsp.equals(msg)) {
                    log.info("收到来自客户端:{} 的心跳响应消息",ip );
                    runningClientService.changeState(ip,"1");
                    return true;
                }
                return false;
            }
        });
        
        SocketHandler.init();
    	server = new AioQuickServer<>(port,stringProtocol,processor );
        server.setReadBufferSize(1024 * 1024)
                .setThreadNum(Runtime.getRuntime().availableProcessors() + 1)
                .setBufferFactory(() -> bufferPagePool)
                .setWriteBuffer(4096, 512);
        processor.addPlugin(new BufferPageMonitor(server, 30));
        processor.addPlugin(new Monitor(30));
        try {
			server.start();
		} catch (IOException e) {
			log.debug(e.getMessage(),e);
		}
	            
	}
	
	@PreDestroy
	public void endSocket() throws IOException {
		server.shutdown();
	}
}
