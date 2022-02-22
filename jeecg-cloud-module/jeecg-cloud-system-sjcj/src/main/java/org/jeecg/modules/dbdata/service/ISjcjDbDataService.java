package org.jeecg.modules.dbdata.service;

import org.jeecg.modules.dbdata.entity.SjcjDbData;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ISjcjDbDataService extends IService<SjcjDbData> {
	// 根据数据库种类标识获取数据库种类名称
	String getDbTypeName(String dbType);
}
