package org.jeecg.modules.openstack.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * @author zlf
 */
@Data
public class HostRoute implements Serializable {
    private String destination;
    private String nexthop;
}
