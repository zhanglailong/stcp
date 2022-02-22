package org.jeecg.modules.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/11
 * @Description: 自定义封装的连接的客户端
 */
@Slf4j
@Data
public class ClientSocket {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String key;
    private String message;
    private String collectTime;
    private String createBy;
    private String hash;
}
