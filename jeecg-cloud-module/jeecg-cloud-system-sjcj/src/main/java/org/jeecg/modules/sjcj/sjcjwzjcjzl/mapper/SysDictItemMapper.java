package org.jeecg.modules.sjcj.sjcjwzjcjzl.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SysDictItem;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysDictItemMapper extends BaseMapper<SysDictItem> {
    
    /**
     * 查询采集类型列表
     */
    @Select("SELECT * FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'typeOfCollection')")
    public List<SysDictItem> getTypeOfCollection();
}
