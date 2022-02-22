package org.jeecg.modules.running.uut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.running.uut.entity.RunningUutListUser;

import java.util.List;


/**
 * @Description: 被测对象出库
 * @Author: jeecg-boot
 * @Date:   2020-12-17
 * @Version: V1.0
 */
public interface RunningUutListUserMapper extends BaseMapper<RunningUutListUser> {

    @Select("SELECT * FROM running_uut_list_user WHERE uut_id = #{id}")
    public List<RunningUutListUser> getUutUserById(@Param("id") String id);

}
