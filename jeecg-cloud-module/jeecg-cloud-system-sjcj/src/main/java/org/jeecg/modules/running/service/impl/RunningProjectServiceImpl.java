package org.jeecg.modules.running.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.running.entity.RunningProject;
import org.jeecg.modules.running.mapper.RunningProjectMapper;
import org.jeecg.modules.running.service.IRunningProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
public class RunningProjectServiceImpl extends ServiceImpl<RunningProjectMapper, RunningProject> implements IRunningProjectService {

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningProject findUniqueBy(String fieldname, String value) {
		return this.getBaseMapper().findUniqueBy(fieldname, value);
	}

}
