package org.jeecg.modules.tools.service.impl;

import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import org.jeecg.modules.tools.mapper.RunningToolsLicenselimitMapper;
import org.jeecg.modules.tools.service.IRunningToolsLicenselimitService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: License访问限制表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Service
public class RunningToolsLicenselimitServiceImpl extends ServiceImpl<RunningToolsLicenselimitMapper, RunningToolsLicenselimit> implements IRunningToolsLicenselimitService {
	
	@Autowired
	private RunningToolsLicenselimitMapper runningToolsLicenselimitMapper;
	
	@Override
	public List<RunningToolsLicenselimit> selectByMainId(String mainId) {
		return runningToolsLicenselimitMapper.selectByMainId(mainId);
	}
}
