package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysDataLog;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Administrator
 *
 * */
public interface ISysDataLogService extends IService<SysDataLog> {
	
	/**
	 * 添加数据日志
	 * @param tableName true
	 * @param dataId true
	 * @param dataContent true
	 */
	public void addDataLog(String tableName, String dataId, String dataContent);

}
