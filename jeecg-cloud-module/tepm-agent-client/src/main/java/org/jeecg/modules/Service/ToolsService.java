package org.jeecg.modules.Service;

import org.jeecg.modules.entity.ResultSocket;

/**
 * @author zlf
 */
public interface ToolsService {

    /**
     * 调用autoIt安装软件
     * @param tool 脚本地址
     * @return 成功还是失败
     */
    boolean callAutoIt(String tool);

    /**
     * @param path 测试文件地址
     * @return 报告地址
     */
    ResultSocket callUnderStandPath(String path);

    /**
     *
     * @param url 测试url
     * @return 测试报告
     */
    ResultSocket callAppScan(String url);

    /**
     *
     * @param processName windows测试工具进程
     * @param toolsLinuxProcessName linux测试工具进程
     * @return 测试工具运行状态
     */
    boolean monitorProcess(String processName,String toolsLinuxProcessName);
}
