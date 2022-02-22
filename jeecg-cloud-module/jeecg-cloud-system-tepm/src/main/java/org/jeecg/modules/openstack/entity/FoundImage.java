package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 创建镜像返回数据
 * @author hxsi
 * @date 2021年05月19日 16:30
 */
@Data
public class FoundImage implements Serializable {
    /**
     * 返回结果
     */
    private int code;
    /**
     * 返回说明
     */
    private String msg;
    /**
     * 镜像id
     */
    private String id;
    /**
     * 镜像名称
     */
    private String name;
    /**
     * 镜像状态
     */
    private String status;
}
