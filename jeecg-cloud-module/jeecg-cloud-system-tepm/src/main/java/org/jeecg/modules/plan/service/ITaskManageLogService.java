package org.jeecg.modules.plan.service;

import org.jeecg.modules.plan.entity.TaskManage;
import org.jeecg.modules.plan.entity.TaskManageLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 任务管理操作日志表
 * @Author: jeecg-boot
 * @Date: 2021-01-14
 * @Version: V1.0
 */
public interface ITaskManageLogService extends IService<TaskManageLog> {

    List<TaskManageLog> getTasksByTaskId(String taskId);

    void saveLogAndSendSocketClent(TaskManage taskManage);
}
