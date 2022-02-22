package org.jeecg.modules.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.entity.NtepmTask;

import java.util.List;

/**
 *  测试任务
 * @author jeecg-boot
 * @date   2021-05-14
 * @version V1.0
 */
public interface INtepmTaskService extends IService<NtepmTask> {
    void delete(String id);

    void deleteBatchIds(List<String> ids);

    /**
     * 根据订单id获取任务集合
     * @param orderId 订单id
     * @return 任务集合
     */
    List<NtepmTask> getTasksByOrderId(String orderId);

    void addNtepmTask(NtepmTask ntepmTask);

    void edit(NtepmTask ntepmTask);
}
