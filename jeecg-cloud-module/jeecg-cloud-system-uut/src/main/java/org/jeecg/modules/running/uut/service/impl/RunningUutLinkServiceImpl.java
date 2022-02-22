package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.running.uut.entity.RunningUutLink;
import org.jeecg.modules.running.uut.mapper.RunningUutLinkMapper;
import org.jeecg.modules.running.uut.service.IRunningUutLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 审核记录中间表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutLinkServiceImpl extends ServiceImpl<RunningUutLinkMapper, RunningUutLink> implements IRunningUutLinkService {
	
	@Autowired
	private RunningUutLinkMapper runningUutLinkMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningUutLink findUniqueBy(String fieldname, String value) {

		return runningUutLinkMapper.findUniqueBy(fieldname, value);
	}
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String getInstanceId(String id) {
		return runningUutLinkMapper.getInstanceId(id);
	}
}
