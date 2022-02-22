package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 * @description 资源使用率
 * @author hxsi
 * @date 2021年05月21日 15:35
 */
@Data
public class ResourceUtilization implements Serializable {
    private int code;
    private String msg;

    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<Stats> stats;
    }

    @Data
    public static class Stats {
        private String instanceId;
        private String cpuUsage;
        private String memoryTotal;
        private String memoryUsed;
        private String memoryUsage;
        private Disks disks;
        private Networks networks;

    }

    @Data
    public static class Disks {
        private All all;
    }

    @Data
    public static class All {
        private String readBytesSec;
        private String writeBytesSec;
    }

    @Data
    public static class Networks {
        private All all;

        @Data
        public static class All {
            private String rxBytesSec;
            private String txBytesSec;
        }
    }
}
