package org.jeecg.modules.running.uut.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.uut.entity.RunningUutLink;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 审核记录中间表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
@Mapper
public interface RunningUutLinkMapper extends BaseMapper<RunningUutLink> {
	/**
	 * 查询
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 * */
	public RunningUutLink findUniqueBy(@Param("fieldname") String fieldname, @Param("value") String value);

	/**
	 * 查询instanceid
	 * @param id
	 * @return
	 */
	public String getInstanceId(String id);
}
