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
public class ImageList implements Serializable {

    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<Images> images;
    }

    @Data
    public static class Images {

        private String containerFormat;
        private Date createdAt;
        private String diskFormat;
        private boolean isHidden;
        private boolean isProtected;
        private int minDisk;
        private int minRam;
        private String name;
        private String owner;
        private String ownerId;
        private long size;
        private String status;
        private Date updatedAt;
        private String visibility;
        private String file;
        private String instanceUuid;
        private boolean hwQemuGuestAgent;
        private String schema;
        private String id;
        private List<String> tags;
        private String ownerName;
        private Properties properties;
    }
    @Data
    public static class Properties {
        private String   osArch;
        private String   osDistribution;
    }


}




