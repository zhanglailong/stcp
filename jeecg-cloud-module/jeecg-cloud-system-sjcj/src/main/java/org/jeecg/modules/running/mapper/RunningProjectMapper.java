package org.jeecg.modules.running.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.entity.RunningProject;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Repository
public interface RunningProjectMapper extends BaseMapper<RunningProject> {
	/**
	 * 查看文件
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 * */
	public RunningProject findUniqueBy(@Param("fieldname") String fieldname, @Param("value") String value);
}
