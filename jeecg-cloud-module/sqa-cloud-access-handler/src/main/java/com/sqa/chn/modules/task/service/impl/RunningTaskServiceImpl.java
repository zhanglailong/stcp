package com.sqa.chn.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqa.chn.modules.task.entity.RunningTask;
import com.sqa.chn.modules.task.mapper.RunningTaskMapper;
import com.sqa.chn.modules.task.service.IRunningTaskService;
import org.springframework.stereotype.Service;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Service
public class RunningTaskServiceImpl extends ServiceImpl<RunningTaskMapper, RunningTask> implements IRunningTaskService {

}
