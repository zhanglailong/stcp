package org.jeecg.modules.openstack.entity;
import lombok.Data;

import java.io.Serializable;

/**
 * 测试串口返回结果
 * @author yeyl
 */
@Data
public class TestSerialResult  implements Serializable {
    /**
     * 返回结果
     */
    private int code;
    /**
     * 返回说明
     */
    private String msg;

    /**
     * true为服务端模式，false为客户端模式
     */
    private boolean serverMode;

    /**
     * 地址
     */
    private String host;

    /**
     * 虚拟串口的通讯端口
     */
    private Integer port;

    /**
     * 该测试结果是否成功
     */
    private boolean success;

}
