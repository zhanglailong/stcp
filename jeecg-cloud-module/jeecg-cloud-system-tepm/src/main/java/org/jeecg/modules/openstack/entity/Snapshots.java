package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 创建环境快照返回数据
 * @author hxsi
 * @date 2021年05月10日 19:21
 */
@Data
public class Snapshots implements Serializable {
    private String code;
    private String msg;
    private String id;
    private String name;
    private String status;
    private String statusReason;
    private String time;
}
