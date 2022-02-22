package org.jeecg.modules.openstack.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author zlf
 */
@Data
public class StackList implements Serializable {

    /**
     * 返回值，0为成功，其他为失败
     */
    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<Resources> resources;
    }

    @Data
    public static class Resources {

        private Date updatedTime;
        private Date creationTime;
        private String logicalResourceId;
        /**
         * 在该环境中的资源名
         */
        private String resourceName;
        /**
         * 资源在原组件中的id
         */
        private String physicalResourceId;
        /**
         * 资源状态
         */
        private String resourceStatus;
        /**
         * 资源状态的原因
         */
        private String resourceStatusReason;
        /**
         * 资源类型：
         * OS::Nova::Server 虚拟机
         * OS::Neutron::Subnet 子网
         * OS::Neutron::Net 网络
         * OS::Neutron::Port 网络端口
         * OS::Neutron::FloatingIP 浮动ip
         * OS::Cinder::Volume 云硬盘
         */
        private String resourceType;
        private List<String> requiredBy;
    }
}




