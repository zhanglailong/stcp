package org.jeecg.modules.running.record.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.running.record.mapper.RunningUutRecordMapper;
import org.jeecg.modules.running.record.service.IRunningUutRecordService;
import org.jeecg.modules.running.record.entity.RunningUutRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
