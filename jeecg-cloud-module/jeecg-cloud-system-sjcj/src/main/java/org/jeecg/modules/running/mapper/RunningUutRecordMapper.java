package org.jeecg.modules.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.running.entity.RunningUutRecord;

/**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
public interface RunningUutRecordMapper extends BaseMapper<RunningUutRecord> {
    /**
     * 查询被测对象
     * @param fieldname true
     * @param value  true
     * @return 没有返回值
     * */
    public RunningUutRecord findUniqueBy(@Param("fieldname") String fieldname, @Param("value") String value);


    /**
     * 获取uutRecordId
     * @param id
     * @return
     */
    @Select("SELECT id FROM running_uut_record WHERE uut_list_id = #{id} ")
    public String getUutRecordId(@Param("id") String id);

}
