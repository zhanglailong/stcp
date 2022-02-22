package org.jeecg.modules.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.MonitorTools.entity.MonitorTools;
import org.jeecg.modules.MonitorTools.service.IMonitorToolsService;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.StackQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zll
 * @description 定时查看测试工具进程
 * @date 2021年07月14日 16:12
 */
@Slf4j
@Component
public class TesttoosSheculed {


    @Autowired
    private IMonitorToolsService monitorToolsService;

    /**
     * 定时查询测试工具列表
     */
   //    @Scheduled(cron = "0/30 * * * * ?")
    public synchronized void handleStackQueue() {
        List<MonitorTools> list = monitorToolsService.list();

    }
}
