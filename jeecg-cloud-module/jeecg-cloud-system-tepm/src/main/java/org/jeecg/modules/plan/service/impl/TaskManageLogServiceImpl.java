package org.jeecg.modules.plan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.plan.entity.TaskManage;
import org.jeecg.modules.plan.entity.TaskManageLog;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.mapper.TaskManageLogMapper;
import org.jeecg.modules.plan.mapper.VmDesignMapper;
import org.jeecg.modules.plan.service.ITaskManageLogService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 任务管理操作日志表
 * @Author: jeecg-boot
 * @Date: 2021-01-14
 * @Version: V1.0
 */
@Slf4j
@Service
public class TaskManageLogServiceImpl extends ServiceImpl<TaskManageLogMapper, TaskManageLog> implements ITaskManageLogService {

    @Autowired
    private TaskManageLogMapper taskManageLogMapper;

    public List<TaskManageLog> getTasksByTaskId(String taskId) {
        QueryWrapper<TaskManageLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id", taskId);
        return taskManageLogMapper.selectList(queryWrapper);
    }

    public void saveLogAndSendSocketClent(TaskManage taskManage) {
        try {
            // 保存日志表
            TaskManageLog taskManageLog = new TaskManageLog();
            BeanUtils.copyProperties(taskManage, taskManageLog);
            taskManageLog.setTaskId(taskManage.getId());
            taskManageLog.setId(null);
            this.save(taskManageLog);
        } catch (Exception e) {
            log.info("保存任务操作日志/发送批量指令异常:" + e);
        }


    }
}
