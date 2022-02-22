package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 虚拟机迁移
 * @author hxsi
 * @date 2021年07月21日
 */
@Data
public class VmRemovalList implements Serializable {
    private String host;
    private Boolean force;
}
