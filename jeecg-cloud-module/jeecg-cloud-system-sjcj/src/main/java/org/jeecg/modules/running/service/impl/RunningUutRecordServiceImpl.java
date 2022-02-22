package org.jeecg.modules.running.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.running.entity.RunningUutRecord;
import org.jeecg.modules.running.mapper.RunningUutRecordMapper;
import org.jeecg.modules.running.service.IRunningUutRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutRecordServiceImpl extends ServiceImpl<RunningUutRecordMapper, RunningUutRecord> implements IRunningUutRecordService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RunningUutRecord findUniqueBy(String fieldname, String value) {
        return this.getBaseMapper().findUniqueBy(fieldname, value);
    }


    @Override
    public String getUutRecordId(String id) {
        return this.getBaseMapper().getUutRecordId(id);
    }
}
