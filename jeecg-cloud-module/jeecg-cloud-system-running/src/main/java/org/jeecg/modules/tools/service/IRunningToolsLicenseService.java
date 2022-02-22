package org.jeecg.modules.tools.service;

import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import org.jeecg.modules.tools.entity.RunningToolsLicense;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: License云平台适配
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IRunningToolsLicenseService extends IService<RunningToolsLicense> {

	/**
	 * 添加一对多
	 * @param runningToolsLicensemonitorList true
	 * @param runningToolsLicenselimitList true
	 */
	public void saveMain(RunningToolsLicense runningToolsLicense,List<RunningToolsLicensemonitor> runningToolsLicensemonitorList,List<RunningToolsLicenselimit> runningToolsLicenselimitList) ;
	
	/**
	 * 修改一对多
	 * @param runningToolsLicensemonitorList true
	 * @param runningToolsLicenselimitList true
	 */
	public void updateMain(RunningToolsLicense runningToolsLicense,List<RunningToolsLicensemonitor> runningToolsLicensemonitorList,List<RunningToolsLicenselimit> runningToolsLicenselimitList);
	
	/**
	 * 删除一对多
	 * @param id true
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 * @param idList true
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
