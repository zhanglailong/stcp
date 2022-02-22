package org.jeecg.modules.openstack.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description 九宫格监控返回数据
 * @author hxsi
 * @date 2021年05月11日 16:04
 */
@Data
public class MultipleVnc implements Serializable {

    private int code;
    private String msg;
    @JSONField(name = "data")
    private Datas datas;

    @Data
    public static class Datas {
        private List<RemoteCons> remoteConsoles;
    }

    @Data
    public static class RemoteCons {
        private String serverId;
        private String serverName;
        private String url;
    }

}
