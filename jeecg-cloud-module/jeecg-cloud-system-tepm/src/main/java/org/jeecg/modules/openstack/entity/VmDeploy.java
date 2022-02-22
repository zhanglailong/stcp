package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hxsi
 * @description 调整虚拟机配置传参
 * @date 2021年06月08日 19:45
 */
@Data
public class VmDeploy implements Serializable {
    private String flavor;
}
