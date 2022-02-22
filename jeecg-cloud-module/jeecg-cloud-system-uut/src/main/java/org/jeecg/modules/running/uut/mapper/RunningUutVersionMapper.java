package org.jeecg.modules.running.uut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.running.uut.entity.RunningUutVersion;

public interface RunningUutVersionMapper extends BaseMapper<RunningUutVersion> {

    @Select(" SELECT id FROM running_uut_version WHERE uut_list_id = #{uutListId} and version = #{version} ")
    public String getUutVersionId(String uutListId, String version);

}
