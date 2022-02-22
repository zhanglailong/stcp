package org.jeecg.modules.running.uut.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.jeecg.modules.running.uut.vo.RunningUutListVo;

import java.util.List;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@DS("uutDatabase")
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
	/**
	 * 查询所有被测对象
	 * @return RunningUutList
	 * */
	public RunningUutList getAll();

	/**
	 * 设置字段值
	 * */
	@Update("UPDATE running_uut_list SET ${field} = #{value} WHERE id = #{id}")
	public void setValue(String id, String field, String value);

	/**
	 * 根据某字段差被测对象列表
	 * */
	@Select("SELECT * " +
			"FROM running_uut_list " +
			"WHERE ${field} = #{value} AND delete_flag = #{deleteFlag}")
	public List<RunningUutList> getUutBySystemId(String field, String value, int deleteFlag);

	/**
	 * 被测对象列表查询
	 * */
	@Select("SELECT * " +
			"FROM running_uut_list " +
			"WHERE delete_flag = 0")
	public List<RunningUutList> getUutList();

	/**
	 * 变更评价体系
	 * */
	@Select("UPDATE running_uut_list " +
			"SET test_template = #{systemId} " +
			"WHERE id = #{uutId}")
	public void changeSystem(String uutId, String systemId);
}
