package org.jeecg.modules.running.uut.mapper;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.uut.entity.RunningUutList;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.running.uut.vo.RunningUutListVo;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface RunningUutListMapper extends BaseMapper<RunningUutList> {

	/**
	 * 查询被测对象
	 * @param fieldname 查询字段
	 * @param value  字段值
	 * @return RunningUutList
	 * */
	public RunningUutList findUniqueBy(@Param("fieldname") String fieldname, @Param("value") String value);
	/**
	 * 查询被测对象VO
	 * @param fieldname 查询字段
	 * @param value  字段值
	 * @return RunningUutListVo
	 * */
    public RunningUutListVo findUniqueVoBy(@Param("fieldname") String fieldname, @Param("value") String value);
}
