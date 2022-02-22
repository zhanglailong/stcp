package org.jeecg.modules.running.uut.service;

import org.jeecg.modules.running.uut.entity.RunningUutLink;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 审核记录中间表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IRunningUutLinkService extends IService<RunningUutLink> {
	
	/**
	 * 根据字段查唯一实体
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 */
	public RunningUutLink findUniqueBy (String fieldname, String value);

	/**
	 * 获取instanceid
	 * @param id
	 * @return
	 */
	public String getInstanceId(String id);

}
