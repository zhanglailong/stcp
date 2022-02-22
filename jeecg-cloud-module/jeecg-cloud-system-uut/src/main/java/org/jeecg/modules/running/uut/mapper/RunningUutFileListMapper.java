package org.jeecg.modules.running.uut.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 被测件附件更动表
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
public interface RunningUutFileListMapper extends BaseMapper<RunningUutFileList> {

    @Select("SELECT * FROM running_uut_file_list WHERE uut_list_id = #{uutListId}")
    public List<RunningUutFileList> getFileListByUutListId(String uutListId);

    @Delete("DELETE FROM running_uut_file_list WHERE uut_list_id = #{uutListId}")
    public Integer deleteByUutListId(String uutListId);

    // 下拉选层级列表
    List<DictModel> getUutVersionOptions(String uutListId);

    @Select("SELECT * FROM running_uut_file_list WHERE file_name = #{fileName} AND file_type = #{fileType} AND uut_list_id = #{uutListId}")
    public List<RunningUutFileList> getByFileNameAndFileType(String fileName, String fileType, String uutListId);

}
