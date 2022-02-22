package org.jeecg.modules.message.handle;

/**
 * @Author: test
 * */
public interface ISendMsgHandle {
	/**
	 * 发送信息
	 * @param esContent true
	 * @param esReceiver true
	 * @param esTitle true
	 *
	 * */
	void sendMsg(String esReceiver, String esTitle, String esContent);
}
