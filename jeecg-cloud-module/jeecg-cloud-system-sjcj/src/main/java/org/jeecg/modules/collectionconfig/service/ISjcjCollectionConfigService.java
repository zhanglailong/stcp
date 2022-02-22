package org.jeecg.modules.collectionconfig.service;

import java.util.List;

import org.jeecg.modules.collectionconfig.entity.SjcjCollectionConfig;
import org.jeecg.modules.collectionconfig.entity.SysDictItem;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 文件采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
public interface ISjcjCollectionConfigService extends IService<SjcjCollectionConfig> {
	/**
	 * 查询采集类型列表
	 */
	public List<SysDictItem> getTypeOfCollection();
}
