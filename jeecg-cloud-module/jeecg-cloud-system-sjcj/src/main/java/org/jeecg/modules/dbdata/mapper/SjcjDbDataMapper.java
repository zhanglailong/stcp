package org.jeecg.modules.dbdata.mapper;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.dbdata.entity.SjcjDbData;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SjcjDbDataMapper extends BaseMapper<SjcjDbData> {
	// 根据数据库种类标识获取数据库种类名称
	@Select("SELECT item_text AS dbTypeName FROM sys_dict_item WHERE dict_id = "
			+ "(SELECT id FROM sys_dict WHERE dict_code = 'dbType') AND item_value = #{dbType}")
	public String getDbTypeName(String dbType);
}
