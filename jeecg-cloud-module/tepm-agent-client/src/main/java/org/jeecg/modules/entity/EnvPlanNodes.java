package org.jeecg.modules.entity;

import lombok.Data;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/3/16
 * @Description: 用一句话描述该文件做什么)
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
}
