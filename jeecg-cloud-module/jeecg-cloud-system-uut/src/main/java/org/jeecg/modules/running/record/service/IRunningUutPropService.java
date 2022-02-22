package org.jeecg.modules.running.record.service;

import org.jeecg.modules.running.record.entity.RunningUutProp;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 被测对象kv支撑表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
public interface IRunningUutPropService extends IService<RunningUutProp> {
	/**
	 * 查询主ID
	 * @param mainId true
	 * @return 没有返回值
	 * */
	public List<RunningUutProp> selectByMainId(String mainId);
}
