package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 虚拟机冷迁移
 */
@Data
public class ColdMigration implements Serializable {
    private String host;
}
