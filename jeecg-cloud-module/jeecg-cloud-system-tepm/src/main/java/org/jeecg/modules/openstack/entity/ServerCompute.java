package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 创建单独虚拟机返回数据
 * @author hxsi
 * @date 2021年05月19日 11:08
 */
@Data
public class ServerCompute implements Serializable {
    /**
     * 返回结果
     */
    private int code;
    /**
     * 返回说明
     */
    private String msg;
    /**
     * 虚拟机id
     */
    private String id;
    /**
     * 虚拟机名称
     */
    private String name;
    /**
     * 虚拟机镜像
     */
    private String image;
    /**
     * 虚拟机状态
     */
    private String status;
    /**
     * cpu+内存+磁盘
     */
    private String originalName;
}
