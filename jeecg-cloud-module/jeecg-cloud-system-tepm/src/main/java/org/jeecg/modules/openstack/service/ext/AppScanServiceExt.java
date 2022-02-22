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
 * @date 2021年06月16日 10:09
 */
@Service
@Slf4j
public class AppScanServiceExt extends OpenStackService {
    /**
     * 环境里面appScan位置
     */
    @Value(value = "${testtool.appScan}")
    public String appScan;
    /**
     * 文件保存位置
     */
    @Value(value = "${testtool.pathJmx}")
    public String pathJmx;

    public String getAppScan(String path) throws IOException {
        String osName = System.getProperties().getProperty(CommonConstant.OS_NAME).toLowerCase();
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstant.Y_M_D);
        String dateNowStr = sdf.format(new Date());
        //报告保存位置
        String appScanPath = null;
        if (osName.contains(CommonConstant.WINDOWS)) {
            appScanPath = pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.APP_SCAN_PATH + dateNowStr;
        }else {
            appScanPath = CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.APP_SCAN_PATH + dateNowStr;
        }
        //判断文件是否存在
        File fileJmx = null;
        if (osName.contains(CommonConstant.WINDOWS)) {
            fileJmx = new File(pathJmx + dateNowStr + CommonConstant.SYMBOL +CommonConstant.APP_SCAN_PATH + dateNowStr);
        }else {
            fileJmx = new File(CommonConstant.HOME + dateNowStr + CommonConstant.SYMBOL + CommonConstant.APP_SCAN_PATH + dateNowStr);
        }
        if (fileJmx.exists()){
            //有文件就删除
            Boolean result = FileUtil.delFiles(fileJmx);
            if (!result){
                log.error("文件删除失败");
                return null;
            }
        }
        File file = new File(appScanPath);
        if(!file.exists()){
            file.mkdirs();
        }
        //执行cmd 命令
        StringBuilder readCount = new StringBuilder();
        Process process = Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.APP_SCAN + path + CommonConstant.D_D + appScanPath + CommonConstant.SCAN + CommonConstant.REPORT_FILE + appScanPath + CommonConstant.PDF_SCAN,null,
                new File(appScan));
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
        return appScanPath;
    }
}
