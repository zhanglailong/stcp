package org.jeecg.modules.service;


import org.jeecg.modules.entity.ClientSocket;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/12
 * @description 用一句话描述该文件做什么)
 */
public interface SocketService {

    /**
     * 注册到socket
     * @param ip ip
     * @return 布尔值
     */
    ClientSocket registerSocket(String ip);

    /**
     *向Agent发送消息
     * @param clientSocket clientSocket
     */
    void sendMsg(ClientSocket clientSocket);
}
