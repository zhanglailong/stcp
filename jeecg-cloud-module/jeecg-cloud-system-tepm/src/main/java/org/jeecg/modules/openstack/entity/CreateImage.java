package org.jeecg.modules.openstack.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 虚拟机传参使用
 * @author hxsi
 * @date 2021年05月19日 16:46
 */
@Data
public class CreateImage implements Serializable {
    private String name;
}
