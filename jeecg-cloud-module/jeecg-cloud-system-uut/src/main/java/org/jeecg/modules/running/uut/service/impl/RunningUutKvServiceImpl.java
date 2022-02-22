package org.jeecg.modules.running.uut.service.impl;

import java.util.List;
import java.util.Map;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.running.uut.entity.RunningUutKv;
import org.jeecg.modules.running.uut.mapper.RunningUutKvMapper;
import org.jeecg.modules.running.uut.service.IRunningUutKvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 审核记录键值对表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutKvServiceImpl extends ServiceImpl<RunningUutKvMapper, RunningUutKv> implements IRunningUutKvService {
	@Autowired
	private RunningUutKvMapper runningUutKvMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<RunningUutKv> findKvListByLinkId(String linkId) {
		return runningUutKvMapper.findKvListByLinkId(linkId);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<Map<String, Object>> findKvMapByLinkId(String linkId) {
		return runningUutKvMapper.findKvMapByLinkId(linkId);
	}
}
