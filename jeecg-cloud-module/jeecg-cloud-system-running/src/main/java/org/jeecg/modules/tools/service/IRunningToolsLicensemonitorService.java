package org.jeecg.modules.tools.service;

import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: License监控表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IRunningToolsLicensemonitorService extends IService<RunningToolsLicensemonitor> {
	/**
	 * 根据主ID查询
	 * @param mainId true
	 * @return 没有返回值
	 * */
	public List<RunningToolsLicensemonitor> selectByMainId(String mainId);
}
