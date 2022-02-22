package org.jeecg.modules.running.record.mapper;

import java.util.List;

import org.jeecg.modules.running.record.entity.RunningUutProp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 被测对象kv支撑表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
public interface RunningUutPropMapper extends BaseMapper<RunningUutProp> {
	/**
	 * 删除
	 * @param mainId
	 * @return 布尔值
	 * */
	public boolean deleteByMainId(@Param("mainId") String mainId);
	/**
	 * 查询
	 * @param mainId
	 * @return List<RunningUutProp>
	 * */
	public List<RunningUutProp> selectByMainId(@Param("mainId") String mainId);
}
