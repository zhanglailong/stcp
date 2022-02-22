package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.client.service.impl.RunningClientServiceImpl;
import org.jeecg.modules.message.smartsocket.util.toolutils.ToolUtil;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * 
 * @author Admin
 *
 */
@Slf4j
@Service
public class MessageProcessor extends AbstractMessageProcessor<String>{
	
	private ToolUtil util;
	private IRunningClientService runningClientService;
	private TaskDispatcher taskDispatcher;
	
	private void init() {
		if(util == null) {
			runningClientService = (RunningClientServiceImpl)SpringContextUtils.getBean("runningClientServiceImpl");
			taskDispatcher = (TaskDispatcher)SpringContextUtils.getBean("taskDispatcher");
			util = (ToolUtil) SpringContextUtils.getBean("toolUtil");
		}
	}
	
	@Override
	public void process0(AioSession session, String msg) {
		log.info("收到消息：{}",msg);
		
		//获取地址
		String ip = null;
		try {
			ip = session.getRemoteAddress().toString();
			ip = StringUtils.split(ip, ":")[0].replace("/", "");
		} catch (IOException e) {
			log.debug(e.getMessage(),e);
		}
		init();
		if(msg.indexOf("Kloc")!=-1) {
			util.process(msg, ip);
		}else if(msg.indexOf("TestBed")!=-1){
			log.info("ip:{},port:{}",ip,msg);
			util.processTestBed(msg, ip);
		}else {
			log.info("ip:{},port:{}",ip,msg);
			SocketHandler.getPortMap().put(ip, msg);
			//修改状态
			runningClientService.changeState(ip,"1");

		}

	}



	@Override
	public void stateEvent0(AioSession session, StateMachineEnum stateMachineEnum, Throwable throwable) {

		init();
		//获取地址
		String address = null;
		try {
			address = session.getRemoteAddress()==null?null:session.getRemoteAddress().toString();
		} catch (IOException e) {
			log.debug(e.getMessage(),e);
		}
		if(StringUtils.isEmpty(address)) {
			address = SocketHandler.getSessionIdMap().get(session.getSessionID());
		}else {
			address = StringUtils.split(address, ":")[0].replace("/", "");
		}


		//状态判断
		switch (stateMachineEnum) {
		case NEW_SESSION:

			//注册client
			log.info("注册client：{}",address);
			SocketHandler.addSocketMap(address, session);
			break;
		case INPUT_SHUTDOWN:
			
			log.debug("input通道关闭，address={}",address);
			
			break;
		case PROCESS_EXCEPTION:
			
		case DECODE_EXCEPTION:
			
		case INPUT_EXCEPTION:
			
		case OUTPUT_EXCEPTION:
			
		case ACCEPT_EXCEPTION:
			
			log.debug("address:{}出现{}异常",address,stateMachineEnum.toString());
			
			if (throwable != null) {
	            log.error(stateMachineEnum + " exception:", throwable);
	        }else {
	        	break;
	        }
			break;
		case SESSION_CLOSED:
			
			//移除session
			SocketHandler.removeSocketMap(address);
			
			//更改客户端状态
			runningClientService.changeState(address,"0");
			
			break;
		case SESSION_CLOSING:
			
			//关闭中，需要啥操作没想好...
			
			break;
		default:
			break;
		}
		
	}

}
