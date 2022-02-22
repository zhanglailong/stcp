package org.jeecg.modules.testtooldistribute.fegin.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *socket通信基础类
 * @author zlf
 */
@Data
public class ResultSocket implements Serializable {
    private String id;
    private Integer code;
    private String path;
    private String url;
    /**
     * 测试工具标识
     */
    private String tool;
    /**
     * 0扫描文件 1是url 2报告 3autoit
     */
    private Integer type;

    public static ResultSocket oK() {
        ResultSocket r = new ResultSocket();
        r.setCode(200);
        return r;
    }

    public static ResultSocket oK(String id, String path, Integer type, String url, String tool) {
        ResultSocket r = new ResultSocket();
        r.setCode(200);
        r.setPath(path);
        r.setType(type);
        r.setUrl(url);
        r.setTool(tool);
        r.setId(id);
        return r;
    }

    public static ResultSocket error() {
        ResultSocket r = new ResultSocket();
        r.setCode(500);
        return r;
    }
    public static ResultSocket error(String id, String path, Integer type, String url, String tool) {
        ResultSocket r = new ResultSocket();
        r.setCode(500);
        r.setPath(path);
        r.setType(type);
        r.setUrl(url);
        r.setId(id);
        r.setTool(tool);
        return r;
    }
}
