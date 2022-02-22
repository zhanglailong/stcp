package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.system.entity.SysSecretKey;
import org.jeecg.modules.system.service.ISysSecretKeyService;
import org.jeecg.modules.system.util.RandomStringUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 定时任务-更新token
 */
@Slf4j
/**
 * @Author: test
 * */
public class TokenJob implements Job {

    @Autowired
    private ISysSecretKeyService sysSecretKeyService;

    /**
     * 定时更新各个系统的token
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<SysSecretKey> secretKeyList = sysSecretKeyService.list();
        secretKeyList.forEach(sysSecretKey -> {
            sysSecretKey.setToken(RandomStringUtil.getToken());
            sysSecretKey.setUpdateBy(sysSecretKey.getCreateBy());
            sysSecretKey.setUpdateTime(new Date());
            sysSecretKeyService.updateById(sysSecretKey);
        });
        log.info("token定时任务执行时间=>" + new Date());
    }
}
