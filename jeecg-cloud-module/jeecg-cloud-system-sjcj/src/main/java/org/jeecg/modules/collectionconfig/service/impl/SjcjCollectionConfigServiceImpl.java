package org.jeecg.modules.collectionconfig.service.impl;

import java.util.List;

import org.jeecg.modules.collectionconfig.entity.SjcjCollectionConfig;
import org.jeecg.modules.collectionconfig.entity.SysDictItem;
import org.jeecg.modules.collectionconfig.mapper.SjcjCollectionConfigMapper;
import org.jeecg.modules.collectionconfig.service.ISjcjCollectionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 文件采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
@Service
public class SjcjCollectionConfigServiceImpl extends ServiceImpl<SjcjCollectionConfigMapper, SjcjCollectionConfig>
		implements ISjcjCollectionConfigService {
	@Autowired
	private SjcjCollectionConfigMapper sjcjCollectionConfigMapper;

	@Override
	public List<SysDictItem> getTypeOfCollection() {
		return sjcjCollectionConfigMapper.getTypeOfCollection();
	}

}
