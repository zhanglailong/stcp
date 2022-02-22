package org.jeecg.modules.running.uut.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.uut.entity.RunningUutCatagory;
import org.jeecg.modules.running.uut.entity.RunningUutNode;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 被测对象流程分类表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface RunningUutCatagoryMapper extends BaseMapper<RunningUutCatagory> {
	/**
	 * 获取uut信息
	 * @param nodeId true
	 * @param uutType true
	 * @return 没有返回值
	 * */
	public RunningUutNode findNextStep(@Param("uutType") String uutType, @Param("nodeId") String nodeId);
	
}
