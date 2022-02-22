package org.jeecg.modules.Service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.Service.AppScanServiceExt;
import org.jeecg.modules.Service.ToolsService;
import org.jeecg.modules.Service.UnderStandServiceExt;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.entity.ResultSocket;
import org.jeecg.modules.socket.SocketServers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zlf
 */
@Slf4j
@Service
public class ToolsServiceImpl implements ToolsService {

    @Value("${socket.autoit.url}")
    private String autoitUrl;
    @Value("${socket.autoit.loadrunnerSub}")
    private String loadrunnerSub;
    @Value("${socket.autoit.loadrunner}")
    private String loadrunner;
    @Resource
    private UnderStandServiceExt underStandServiceExt;
    @Resource
    private AppScanServiceExt appScanServiceExt;

    @Override
    public boolean callAutoIt(String tool) {
        try {
            //判断安装哪个工具
            if (tool.equals(CommonConstant.DATA_TOOL_INT_1)) {
                String command = autoitUrl + " ";
                //先安装组件
                if (callPrint(command + loadrunner, CommonConstant.DATA_TOOL_PROCESS_1, CommonConstant.DATA_CODE_INT_1)) {
                    return true;
                }
//                if (callPrint(command+loadrunnerSub, CommonConstant.DATA_TOOL_PROCESS_1,CommonConstant.DATA_CODE_INT_1)) {
//                    if (callPrint(command+ loadrunner,CommonConstant.DATA_TOOL_PROCESS_1,CommonConstant.DATA_CODE_INT_1)){
//                        return true;
//                    }
//                }
            }
        } catch (Exception e) {
            log.error("调用autoit tool is " + tool + "异常:" + e.getMessage());
        }
        return false;
    }

    /**
     * 读取cmd窗口返回的内容
     *
     * @param command     执行命令
     * @param processName 进程名
     * @param type        1没有黑窗口  不需要读取
     * @throws Exception 异常
     */
    public boolean callPrint(String command, String processName, Integer type) throws Exception {
        StringBuilder readCount = new StringBuilder();
        //执行cmd命令
        Process process = Runtime.getRuntime().exec(command);
        //关闭流释放资源
        log.info("关闭流释放资源");
        if (!type.equals(CommonConstant.DATA_CODE_INT_1)) {
            if (process != null) {
                process.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
        }
        log.info("结束");
        log.info("读取cmd窗口返回的内容:" + readCount.toString());
        return findProcess(processName);
    }


    /**
     * 判断进程是否存在
     *
     * @param processName 进程名
     */
    public boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        TimeInterval timer = DateUtil.timer();
        try {
            int count = 0;
            int num = 0;
            do {
                count++;
                num++;
                Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + processName + '"');
                bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(processName)) {
                        count = 1;
                        break;
                    }
                }
                long minute = timer.intervalMinute();
                //大于60分钟释放线程
                if (minute >= CommonConstant.DATA_TIME_SECOND_60) {
                    log.info("超过1个小时自动退出");
                    Runtime.getRuntime().exec("Taskkill /IM " + processName);
                    return false;
                }
//                TimeUnit.SECONDS.sleep(1);
//                if (num >= CommonConstant.DATA_TIME_SECOND_3600){
//                    log.info("超过1个小时自动退出");
//                    Runtime.getRuntime().exec("Taskkill /IM " + processName);
//                    return false;
//                }
            } while (count < 10);
            return true;
        } catch (Exception ex) {
            log.error("判断进程是否存在异常:" + ex.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }


    /**
     * underStand工具
     *
     * @param path 测试文件地址
     * @return 报告地址
     */
    @Override
    public ResultSocket callUnderStandPath(String path) {
        try {
            //测试报告地址+hash
            ResultSocket resultSocket = underStandServiceExt.getUnderStand(path);
            if (resultSocket != null) {
                return resultSocket;
            }
        } catch (Exception e) {
            log.error("生成报告失败:" + e.getMessage());
        }
        log.error("生成报告失败!");
        return null;
    }

    /**
     * AppScan工具
     *
     * @param url 测试url
     * @return 测试报告
     */
    @Override
    public ResultSocket callAppScan(String url) {
        try {
            ResultSocket resultSocket = appScanServiceExt.getAppScan(url);
            if (resultSocket != null) {
                return resultSocket;
            }
        } catch (Exception e) {
            log.error("生成报告失败:" + e.getMessage());
        }
        log.error("生成报告失败!");
        return null;
    }

    @Override
    public boolean monitorProcess(String processName, String toolLinProcessName) {
        try {
            String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
            if (osName.contains(CommonConstant.WINDOWS)) {
                return windowMonitorToolStatus(processName);
            } else {
                return linuxMonitorToolStatus(toolLinProcessName);

            }
        } catch (Exception e) {
            log.error("监控进程失败：" + e.getMessage());
        }
        log.error("监控进程失败");
        return false;
    }

    /**
     * windows判断进程是否存在
     *
     * @param windowsProcessName 进程名
     */
    public boolean windowMonitorToolStatus(String windowsProcessName) {
        log.info("开始监测测试工具进程");
        BufferedReader bufferedReader = null;
        TimeInterval timer = DateUtil.timer();
        try {
            do {
                Process proc = Runtime.getRuntime().exec("tasklist -fi " + '"' + "imagename eq " + windowsProcessName + '"');
                bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(windowsProcessName)) {
                        log.info("监测" + windowsProcessName + "进程成功");
                        return true;
                    }
                }
                long minute = timer.intervalMinute();
                if (minute >= CommonConstant.DATA_TIME_SECOND_1) {
                    log.info("超过一分钟，没有找到进程，自动返回");
                    return false;
                }
            } while (true);
        } catch (Exception ex) {
            log.error("判断进程是否存在异常:" + ex.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }

    /**
     * linux判断进程是否存在
     *
     * @param toolLinProcessName linux测试工具进程名称
     */
    public boolean linuxMonitorToolStatus(String toolLinProcessName) {
        log.info("开始监测测试工具进程");
        BufferedReader bufferedReader = null;
        TimeInterval timer = DateUtil.timer();
        try {
            do {
                Process process = Runtime.getRuntime().exec("netstat -ntlp");
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.contains(toolLinProcessName)) {
                        log.info("监测" + toolLinProcessName + "进程成功");
                        return true;
                    }
                }
                long minute = timer.intervalMinute();
                if (minute >= CommonConstant.DATA_TIME_SECOND_1) {
                    log.info("超过一分钟，没有找到进程，自动返回");
                    return false;
                }
            } while (true);
        } catch (Exception ex) {
            log.error("判断进程是否存在异常:" + ex.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {
                }
            }
        }
        return false;
    }
}
