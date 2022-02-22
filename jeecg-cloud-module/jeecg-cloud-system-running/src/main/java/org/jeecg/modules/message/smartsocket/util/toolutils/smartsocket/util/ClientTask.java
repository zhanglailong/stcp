package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.util;

import lombok.Data;

import java.io.Serializable;

@Data
/**
 * @Author: test
 * */
public class ClientTask implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String taskId;
	
	private String url;
	
	private String clientIp;
	
	private String taskType;

}
