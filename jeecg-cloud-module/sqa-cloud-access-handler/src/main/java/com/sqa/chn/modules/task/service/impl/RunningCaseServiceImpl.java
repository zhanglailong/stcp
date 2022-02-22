package com.sqa.chn.modules.task.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqa.chn.modules.task.entity.RunningCase;
import com.sqa.chn.modules.task.mapper.RunningCaseMapper;
import com.sqa.chn.modules.task.service.IRunningCaseService;
import org.springframework.stereotype.Service;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class RunningCaseServiceImpl extends ServiceImpl<RunningCaseMapper, RunningCase> implements IRunningCaseService {

}
