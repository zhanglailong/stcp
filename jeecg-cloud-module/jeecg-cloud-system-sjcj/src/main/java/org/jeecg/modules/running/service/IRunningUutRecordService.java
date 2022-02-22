package org.jeecg.modules.running.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.running.entity.RunningUutRecord;

/**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface IRunningUutRecordService extends IService<RunningUutRecord> {
    /**
     * 根据字段查唯一实体
     * @param fieldname true
     * @param value true
     * @return 没有返回值
     */
    public RunningUutRecord findUniqueBy (String fieldname, String value);

    /**
     * 获取uutRecordId
     * @param id
     * @return
     */
    String getUutRecordId(String id);
}
