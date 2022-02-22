package org.jeecg.modules.tools.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.tools.vo.RunningToolsMonitorListVO;

/**
 * @Description: License监控表
 * @Author: jeecg-boot
 * @Date:   2021-01-07
 * @Version: V1.0
 */
public interface RunningToolsLicensemonitorMapper extends BaseMapper<RunningToolsLicensemonitor> {

	/**
	 * 删除
	 * @param mainId true
	 * @return 布尔值
	 * */
	public boolean deleteByMainId(@Param("mainId") String mainId);
	/**
	 * 查询
	 * @param mainId true
	 * @return List<RunningToolsLicensemonitor>
	 * */
	public List<RunningToolsLicensemonitor> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 分页查询License监控列表
	 * @param page true
	 * @param toolName true
	 * @return IPage<RunningToolsMonitorListVO>
	 */
	IPage<RunningToolsMonitorListVO> selectMonitorList(Page<RunningToolsMonitorListVO> page, String toolName);
}
