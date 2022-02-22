package org.jeecg.modules.openstack.entity;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zlf
 */
@Data
public class ServerInfo implements Serializable {

    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private Server server;
    }

    @Data
    public static class Server {
        private Addresses addresses;
    }

    @Data
    public static class Addresses {
        private List<Networks> networks;
    }

    @Data
    public static class Networks {
        private Integer version;
        private String addr;
        /**
         * fixed 内网  floating 浮动地址
         */
        @JSONField(name = "OS-EXT-IPS:type")
        private String type;
        @JSONField(name = "OS-EXT-IPS-MAC:mac_addr")
        private String macAddr;
    }
}




