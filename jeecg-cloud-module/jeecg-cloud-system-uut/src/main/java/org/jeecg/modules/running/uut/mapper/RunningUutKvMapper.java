package org.jeecg.modules.running.uut.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.uut.entity.RunningUutKv;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 审核记录键值对表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface RunningUutKvMapper extends BaseMapper<RunningUutKv> {
	/**
	 * 查询
	 * @param linkId true
	 * @return List<RunningUutKv>
	 * */
	public List<RunningUutKv> findKvListByLinkId(@Param("linkId") String linkId);
	/**
	 * 查询
	 * @param linkId true
	 * @return List<Map<String, Object>>
	 * */
	public List<Map<String, Object>> findKvMapByLinkId(@Param("linkId") String linkId);
	
}
