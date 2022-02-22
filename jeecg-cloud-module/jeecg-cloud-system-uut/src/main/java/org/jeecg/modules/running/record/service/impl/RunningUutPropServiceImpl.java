package org.jeecg.modules.running.record.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.running.record.entity.RunningUutProp;
import org.jeecg.modules.running.record.mapper.RunningUutPropMapper;
import org.jeecg.modules.running.record.service.IRunningUutPropService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 被测对象kv支撑表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutPropServiceImpl extends ServiceImpl<RunningUutPropMapper, RunningUutProp> implements IRunningUutPropService {
	
	@Autowired
	private RunningUutPropMapper runningUutPropMapper;
	
	@Override
	public List<RunningUutProp> selectByMainId(String mainId) {
		return runningUutPropMapper.selectByMainId(mainId);
	}
}
