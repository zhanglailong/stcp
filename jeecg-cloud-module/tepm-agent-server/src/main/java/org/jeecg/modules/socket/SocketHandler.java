package org.jeecg.modules.socket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.entity.ClientSocket;
import org.jeecg.modules.entity.ResultSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/11
 * @Description: Socket操作处理类
 */
@Slf4j
public class SocketHandler {

    /**
     * 将连接的Socket注册到Socket池中
     *
     * @param socket
     * @return
     */
    public ClientSocket register(Socket socket) {
        ClientSocket clientSocket = new ClientSocket();
        clientSocket.setSocket(socket);
        try {
            clientSocket.setInputStream(new DataInputStream(socket.getInputStream()));
            clientSocket.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            return clientSocket;
        } catch (IOException e) {
            log.error("注册异常:"+e.getMessage());
        }
        return null;
    }

    /**
     * 注册到虚拟机客户端
     * @param ip ip
     * @param serverPort 端口号
     * @return socket连接
     */
    public ClientSocket registerServer(String ip,Integer serverPort){
        try {
            // 与服务端建立连接
            Socket socket = new Socket(ip, serverPort);
            socket.setOOBInline(true);
            ClientSocket clientSocket = new ClientSocket();
            clientSocket.setSocket(socket);
            clientSocket.setKey(ip);
            clientSocket.setInputStream(new DataInputStream(socket.getInputStream()));
            clientSocket.setOutputStream(new DataOutputStream(socket.getOutputStream()));
            return clientSocket;
        }catch (Exception e){
            log.error("注册socket ip: "+ip+"异常:"+e.getMessage());
        }
        return null;
    }

    /**
     * 向指定客户端发送信息
     *
     * @param clientSocket
     * @param message
     */
    public static void sendMessage(ClientSocket clientSocket, String message) {
        try {
            log.info("发送消息到客户端" + clientSocket.getKey() + ": >>>>>" + message);
            clientSocket.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("发送信息异常：{}", e.getMessage());
            close(clientSocket);
        }
    }

    /**
     * 获取指定客户端的上传信息
     *
     * @param clientSocket
     * @return
     */
    public ResultSocket onMessage(ClientSocket clientSocket) {
        try {
            if (isSocketClosed(clientSocket)) {
                log.info("clientSocket 关闭连接了，不在接收发送消息");
                return null;
            }
            if (clientSocket.getSocket() != null) {
                String msg = readMessage(clientSocket.getInputStream());
                if (StringUtils.isNotEmpty(msg)) {
                    return JSON.parseObject(msg,ResultSocket.class);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            close(clientSocket);
            return null;
        }
        return null;
    }

    public static String readMessage(DataInputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        if (read != -1){
            return new String(bytes,0,read, StandardCharsets.UTF_8).trim();
        }else{
            return null;
        }
    }

    /**
     * 指定Socket资源回收
     *
     * @param clientSocket
     */
    public static void close(ClientSocket clientSocket) {
        log.info("进行资源回收");
        if (clientSocket != null) {
            log.info("开始回收socket相关资源，其Key为{}", clientSocket.getKey());
            Socket socket = clientSocket.getSocket();

            try {
                if (socket != null) {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                }
            } catch (IOException e) {
                log.error("关闭输入输出流异常，{}", e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log.error("关闭socket异常{}", e.getMessage());
                }
            }
        }
    }


    /**
     * 发送数据包，判断数据连接状态
     *
     * @param clientSocket
     * @return
     */
    public static boolean isSocketClosed(ClientSocket clientSocket) {
        try {
            clientSocket.getSocket().sendUrgentData(1);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
