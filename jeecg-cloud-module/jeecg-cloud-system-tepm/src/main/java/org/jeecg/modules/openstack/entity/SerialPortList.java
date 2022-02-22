package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author yeyl
 */
@Data
public class SerialPortList {
    private int code;
    private String msg;
    @JSONField(name = "data")
    private SerialPortList.Datas datas;

    @Data
    public static class Datas {
        private List<SerialPortList.serial> serial;
    }
    @Data
    public static class serial {
        /**
         * 虚拟机名称
         */
        private String domainName;


        /**
         * 虚拟机Id
         */
        private String domainId;
        private String createdAt;
        /**
         * 设备id
         */
        private Integer  id;
        private String host;
        private String updatedAt;
        private boolean serverMode;
        private Integer  port;

    }



}
