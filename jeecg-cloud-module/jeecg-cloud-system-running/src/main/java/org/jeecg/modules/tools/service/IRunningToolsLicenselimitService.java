package org.jeecg.modules.tools.service;

import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: License访问限制表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IRunningToolsLicenselimitService extends IService<RunningToolsLicenselimit> {
	/**
	 * 根据主ID查询
	 * @param mainId true
	 * @return 没有返回值
	 * */
	public List<RunningToolsLicenselimit> selectByMainId(String mainId);
}
