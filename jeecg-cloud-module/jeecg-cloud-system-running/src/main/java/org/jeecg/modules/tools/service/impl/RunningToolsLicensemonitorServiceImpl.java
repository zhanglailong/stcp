package org.jeecg.modules.tools.service.impl;

import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import org.jeecg.modules.tools.mapper.RunningToolsLicensemonitorMapper;
import org.jeecg.modules.tools.service.IRunningToolsLicensemonitorService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: License监控表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Service
public class RunningToolsLicensemonitorServiceImpl extends ServiceImpl<RunningToolsLicensemonitorMapper, RunningToolsLicensemonitor> implements IRunningToolsLicensemonitorService {
	
	@Autowired
	private RunningToolsLicensemonitorMapper runningToolsLicensemonitorMapper;
	
	@Override
	public List<RunningToolsLicensemonitor> selectByMainId(String mainId) {
		return runningToolsLicensemonitorMapper.selectByMainId(mainId);
	}
}
