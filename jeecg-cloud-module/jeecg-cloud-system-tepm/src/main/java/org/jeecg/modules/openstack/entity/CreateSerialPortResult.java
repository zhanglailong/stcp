package org.jeecg.modules.openstack.entity;

import lombok.Data;

/**
 * @author yeyl
 */
@Data
public class CreateSerialPortResult {

    /**
     * 结果
     */
    private int code;
    /**
     * 返回说明
     */
    private String msg;

    /**
     * 虚拟机名称
     */
    private String domainName;


    /**
     * 虚拟机Id
     */
    private String domainId;

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
     * 创建时间
     */
    private String createdAt;

    /**
     * 串口Id
     */
    private String id;

}
