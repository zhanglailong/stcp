package org.jeecg.modules.openstack.service.ext;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author hxsi
 * @date 2021年06月03日 17:45
 */
@Service
@Slf4j
public class LoadRunnerServiceExt extends OpenStackService {
    /**
     * 环境里面loadRunner位置
     */
    @Value(value = "${testtool.loadRunner}")
    public String loadRunner;

    /**
     * 文件保存位置
     */
    @Value(value = "${testtool.pathJmx}")
    public String pathJmx;

    /**
     *
     * @param lrsPath 测试用例地址
     * @return lrrPath
     * @throws IOException
     */
    public String  getLoadRunner(String lrsPath) throws IOException {
        File PropertiesFile = new File(lrsPath);
        //获取测试用例名字
        String lrsName = StrUtil.removeSuffix(PropertiesFile.getName(),CommonConstant.LRS);
        //创建一个存放的文件夹
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        //lrr文件保存路径
        String lrrPath = pathJmx + dateNowStr + CommonConstant.SYMBOL + lrsName + CommonConstant.SYMBOL + lrsName+CommonConstant.LRR;
        //执行cmd 命令 获取 lrr文件
        Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START+CommonConstant.WLRUN_RUN+lrsPath+" "+lrrPath,null,
                new File(loadRunner));
        //查询 lrrPath 路径下 是否有 .lrr文件
        File file = new File(lrrPath);
        if (StringUtils.isNotEmpty(lrrPath) && file.exists()){
            //执行cmd 命令 获取 HTML报告
            Runtime.getRuntime().exec(CommonConstant.CMD_EXE_START + CommonConstant.RESULTPATH + lrrPath + CommonConstant.SEPARATORS + lrsName + CommonConstant.HTML_NAME,null,
                    new File(loadRunner));
            return lrrPath;
        }
        return null;
    }
}
