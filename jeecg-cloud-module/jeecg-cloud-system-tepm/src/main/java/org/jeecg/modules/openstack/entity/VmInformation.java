package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class VmInformation implements Serializable {
        private String id;
        private String name;
        private String status;
}
