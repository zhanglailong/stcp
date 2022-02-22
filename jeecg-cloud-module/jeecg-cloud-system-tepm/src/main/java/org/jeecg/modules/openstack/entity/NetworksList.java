package org.jeecg.modules.openstack.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class NetworksList implements Serializable {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<Networks> networks;
    }

    @Data
    public static class Networks {
        private String id;
        private String name;
        private String tenantId;
        private String status;
        private String availabilityZones;
        private String projectId;
        private List<SubnetsDetails> subnetsDetailsList;
    }

    /**
     * 子网详细信息
     */
    @Data
    public static class SubnetsDetails{
        private String id;
        private String name;
        private String tenantId;
        private String networkId;
        private String ipVersion;
    }
}
