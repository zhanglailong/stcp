package org.jeecg.modules.collectionconfig.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.collectionconfig.entity.SjcjCollectionConfig;
import org.jeecg.modules.collectionconfig.entity.SysDictItem;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 文件采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
public interface SjcjCollectionConfigMapper extends BaseMapper<SjcjCollectionConfig> {
	/**
	 * 查询采集类型列表
	 */
	@Select("SELECT * FROM sys_dict_item WHERE dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'typeOfCollection')")
	public List<SysDictItem> getTypeOfCollection();
}
