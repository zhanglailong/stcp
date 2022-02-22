package org.jeecg.modules.running.uut.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import org.jeecg.modules.running.uut.entity.RunningUutFileListHistory;

import java.util.List;

/**
 * @Description: 被测件附件更动履历表
 * @Author: jeecg-boot
 * @Date:   2021-08-27
 * @Version: V1.0
 */
public interface RunningUutFileListHistoryMapper extends BaseMapper<RunningUutFileListHistory> {

    @Select("SELECT * FROM running_uut_file_list_history WHERE uut_version_id = #{uutVersionId}")
    public List<RunningUutFileListHistory> getFileNames(String uutVersionId);

    @Select("SELECT * FROM running_uut_file_list_history WHERE uut_list_id = #{uutListId} AND uut_version_id = #{uutVersionId}")
    public List<RunningUutFileListHistory> getFileByUutListIdAndUutVersionId(String uutListId, String uutVersionId);


}
