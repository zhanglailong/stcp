package org.jeecg.modules.entity;

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
    private String tool;
    private String hash;
    /**
     * 0扫描文件 1是url 2报告 3autoit
     */
    private Integer type;
    private String toolProcessName;
    //进程是否存在   0不存在，1存在
    private String isSuccess;
    //测试工具id
    private String toolId;
    //测试工具端口号
    private String toolsPort;
    //测试工具linux 进程名称
    private String toolLinuxProcessName;

    public static ResultSocket oK() {
        ResultSocket r = new ResultSocket();
        r.setCode(200);
        return r;
    }

    public static ResultSocket oK(String id,String path,Integer type,String url,String tool) {
        ResultSocket r = new ResultSocket();
        r.setCode(200);
        r.setPath(path);
        r.setType(type);
        r.setUrl(url);
        r.setId(id);
        r.setTool(tool);
        return r;
    }
    public static ResultSocket oK(String id,String path,Integer type,String url,String tool,String toolProcessName,String isSuccess,String toolId,String toolsPort,String toolLinuxProcessName) {
        ResultSocket r = new ResultSocket();
        r.setCode(200);
        r.setPath(path);
        r.setType(type);
        r.setUrl(url);
        r.setId(id);
        r.setTool(tool);
        r.setToolProcessName(toolProcessName);
        r.setIsSuccess(isSuccess);
        r.setToolId(toolId);
        r.setToolsPort(toolsPort);
        r.setToolLinuxProcessName(toolLinuxProcessName);
        return r;
    }

    public static ResultSocket error() {
        ResultSocket r = new ResultSocket();
        r.setCode(500);
        return r;
    }
    public static ResultSocket error(String id,String path,Integer type,String url,String tool) {
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
