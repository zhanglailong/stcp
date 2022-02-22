package org.jeecg.modules.openstack.service.ext;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.FileUtil;
import org.jeecg.modules.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hxsi
 * @date 2021年06月16日 14:08
 */
@Service
@Slf4j
public class UnderStandServiceExt extends OpenStackService {
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
    public static String underStandPath = null;

    /**
     * UnderStand 测试工具
     * @param path 测试文件地址
     * @return true
     */
    public String getUnderStand(String path) throws IOException {
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        //报告保存位置
        if (osName.contains(CommonConstant.WINDOWS)) {
            underStandPath = pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.UNDER_STAND + dateNowStr;
        }else {
            underStandPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr;
        }
        //判断文件是否存在
        File fileJmx = null;
        if (osName.contains(CommonConstant.WINDOWS)) {
            fileJmx = new File(pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.UNDER_STAND + dateNowStr);
        }else {
            fileJmx = new File(CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.UNDER_STAND + dateNowStr);
        }
        if (fileJmx.exists()){
            //有文件就删除
            Boolean result = FileUtil.delFiles(fileJmx);
            if (!result){
                log.error("文件删除失败");
                return null;
            }
        }
        File file = new File(underStandPath);
        if(!file.exists()){
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
        return underStandPath;
    }
}
