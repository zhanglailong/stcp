package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 环境事件返回数据
 * @author hxsi
 * @date 2021年05月17日 10:42
 */
@Data
public class EventsList implements Serializable {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<Events> events;
    }

    @Data
    public static class Events {
        private String id;
        private String eventTime;
        private String logicalResourceId;
        private String resourceName;
        private String physicalResourceId;
        private String resourceStatus;
        private String resourceStatusReason;
    }
}
