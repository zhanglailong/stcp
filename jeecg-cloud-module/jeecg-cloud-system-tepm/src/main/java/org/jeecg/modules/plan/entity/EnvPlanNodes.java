package org.jeecg.modules.plan.entity;

import lombok.Data;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/3/16
 * @description 用一句话描述该文件做什么)
 */
@Data
public class EnvPlanNodes {
    private String virCpu;
    private String vmName;
    private String top;
    private String ico;
    private String left;
    private String name;
    private String id;
    private String state;
    private String type;
    private String virInner;
    private String virDisk;
    private String netName;
    private String childNetName;
    private String cidr;
    private String image;
    private String mirror;
    private String size;
    private String hostRoutes;
    private String hostRoutesNew;
    private String netId;
    private String childnetId;
    private Boolean floating;
    /**
     * 虚拟机数量
     */
    private String vmNum;

    /**
     * 其他软件
     */
    private String otherSoftware;


    /**
     * 测试工具
     */
    private String testTools;


    /**
     * 系统版本
     */
    private String systemVersion;

    /**
     * 操作系统
     */
    private String sysType;

}
