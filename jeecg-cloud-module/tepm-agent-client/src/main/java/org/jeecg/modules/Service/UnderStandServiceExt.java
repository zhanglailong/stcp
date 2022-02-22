package org.jeecg.modules.Service;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtil;
import org.jeecg.modules.entity.ResultSocket;
import org.jeecg.modules.service.IpfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author hxsi
 * @date 2021年06月22日
 */
@Service
@Slf4j
public class UnderStandServiceExt {

    public static String underStandPath = null;
    /**
     * 环境里面underStand位置
     */
    @Value(value = "${testtool.underStand}")
    public String underStand;
    /**
     * 文件保存位置
     */
    @Value(value = "${testtool.pathJmx}")
    public String pathJmx;

    @Autowired
    private IpfsService ipfsService;

    /**
     * UnderStand 测试工具
     *
     * @param path 测试文件地址
     * @return true
     */
    public ResultSocket getUnderStand(String path) {
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        //报告保存位置
        if (osName.contains(CommonConstant.WINDOWS)) {
            underStandPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr;
        } else {
            underStandPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr;
        }
        //判断文件是否存在
        File fileJmx = null;
        if (osName.contains(CommonConstant.WINDOWS)) {
            fileJmx = new File(pathJmx + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr);
        } else {
            fileJmx = new File(CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr);
        }
        if (fileJmx.exists()) {
            //有文件就删除
            Boolean result = FileUtil.delFiles(fileJmx);
            if (!result) {
                log.error("文件删除失败");
                return null;
            }
        }
        try {
            File file = new File(underStandPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            file.createNewFile();

            StringBuilder readCount = new StringBuilder();
            //执行第一条 cmd 命令
            Process process = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.UND_CREATE + underStandPath + CommonConstant.SYMBOL + CommonConstant.MYDB_UDB, null,
                    new File(underStand));
            //关闭流释放资源
            log.info("关闭流释放资源");
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
            log.info("underStand第一条cmd");
            Process proces = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.UND_ADD + path + underStandPath + CommonConstant.SYMBOL + CommonConstant.MYDB_UDB, null,
                    new File(underStand));
            if (proces != null) {
                proces.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proces.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
            log.info("underStand第二条cmd");
            Process proce = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.UND_ANALYZE + underStandPath + CommonConstant.SYMBOL + CommonConstant.MYDB_UDB, null,
                    new File(underStand));
            if (proce != null) {
                proce.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proce.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
            log.info("underStand第三条cmd");
            Process pr = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.UND_REPORT + underStandPath + CommonConstant.SYMBOL + CommonConstant.MYDB_UDB, null,
                    new File(underStand));
            if (pr != null) {
                pr.getOutputStream().close();
                //以下为读取cmd窗口返回的内容
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
                String line;
                log.info("以下为读取cmd窗口返回的内容");
                while ((line = bufferedReader.readLine()) != null) {
                    readCount.append(line).append("\n");
                }
            }
            log.info("underStand第四条cmd");
            String underPath = underStandPath.replace("c:", "").replace("C:", "");
            String upStr = ipfsService.upload(underPath);
            ResultSocket resultSocket = new ResultSocket();
            resultSocket.setHash(upStr);
            resultSocket.setPath(underPath);
            return resultSocket;
        } catch (IOException e) {
            log.error("文件上传异常：" + e.getMessage());
        }
        return null;
    }
}
