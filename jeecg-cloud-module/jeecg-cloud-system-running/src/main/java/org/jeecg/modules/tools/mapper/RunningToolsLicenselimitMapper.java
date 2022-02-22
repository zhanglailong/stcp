package org.jeecg.modules.tools.mapper;

import java.util.List;
import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: License访问限制表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface RunningToolsLicenselimitMapper extends BaseMapper<RunningToolsLicenselimit> {
	/**
	 * 删除主id
	 * @param mainId true
	 * @return 布尔值
	 * */
	public boolean deleteByMainId(@Param("mainId") String mainId);
	/**
	 * 查询
	 * @param mainId true
	 * @return List <RunningToolsLicenselimit>
	 * */
	public List<RunningToolsLicenselimit> selectByMainId(@Param("mainId") String mainId);
}
