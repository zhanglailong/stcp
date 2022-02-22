package org.jeecg.modules.dbdata.service.impl;

import org.jeecg.modules.dbdata.entity.SjcjDbData;
import org.jeecg.modules.dbdata.mapper.SjcjDbDataMapper;
import org.jeecg.modules.dbdata.service.ISjcjDbDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SjcjDbDataServiceImpl extends ServiceImpl<SjcjDbDataMapper, SjcjDbData> implements ISjcjDbDataService {

	@Autowired
	SjcjDbDataMapper sjcjDbDataMapper;

	@Override
	public String getDbTypeName(String dbType) {
		return sjcjDbDataMapper.getDbTypeName(dbType);
	}

}
