package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.jeecg.modules.running.uut.mapper.RunningUutListMapper;
import org.jeecg.modules.running.uut.service.IRunningUutListService;
import org.jeecg.modules.running.uut.vo.RunningUutListVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutListServiceImpl extends ServiceImpl<RunningUutListMapper, RunningUutList> implements IRunningUutListService {
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningUutList findUniqueBy(String fieldname, String value) {
		return this.getBaseMapper().findUniqueBy(fieldname, value);
	}

	@Override
	public RunningUutListVo findUniqueVoBy(String fieldname, String value) {
		return this.getBaseMapper().findUniqueVoBy(fieldname, value);
	}
}
