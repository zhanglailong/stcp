package org.jeecg.modules.MonitorTools.service;

import org.jeecg.modules.MonitorTools.entity.MonitorTools;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 测试工具主服务表
 * @Author: jeecg-boot
 * @Date: 2021-02-01
 * @Version: V1.0
 */
public interface IMonitorToolsService extends IService<MonitorTools> {

    void startTools(MonitorTools monitorTools);
}
