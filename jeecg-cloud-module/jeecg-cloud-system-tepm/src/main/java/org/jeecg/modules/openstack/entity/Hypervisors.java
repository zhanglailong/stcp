package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Hypervisors implements Serializable {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<hypervisors> hypervisors;
    }
    @Data
    public static class hypervisors {
        private String status;
        private String state;
        private String name;
    }
}
