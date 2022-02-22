package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeyl
 */
@Data
public class UkeyList  implements Serializable {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private UkeyList.Datas datas;

    @Data
    public static class Datas {
        private List<UkeyList.Ukeys> ukeys;
    }
    @Data
    public static class Ukeys {
        private String device;
        private Integer  id;
        private String name;
        private String vname;
        private String redirDomain;
        private String updatedAt;
        private String bus;
        private String hostId;
        private String pname;
        private Integer redirState;
        private String createdAt;
        private String deletedAt;
        private String hostName;
        private String redirStateMsg;
    }
}
