package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 虚拟机操作记录列表
 */
@Data
public class VmAction implements Serializable {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;
    @Data
    public static class Datas {
        private List<Actions> actions;
    }
    @Data
    public static class Actions {
        private String action;
        private String instanceUuid;
        private String requestId;
        private String userId;
        private String projectId;
        private String startTime;
        private String updatedAt;
    }
}
