package org.jeecg.modules.running.uut.service;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.running.uut.entity.RunningUutKv;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 审核记录键值对表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface IRunningUutKvService extends IService<RunningUutKv> {
	
	/**
	 * 根据字段查唯一实体
	 * @param linkId true
	 * @return 没有返回值
	 */
	public List<RunningUutKv> findKvListByLinkId (String linkId);

	/**
	 * 根据字段查唯一实体
	 * @param linkId
	 * @return 没有返回值
	 */
	public List<Map<String, Object>> findKvMapByLinkId (String linkId);
	
}
