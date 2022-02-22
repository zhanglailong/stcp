package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目权限表
 * @Author: jeecg-boot
 * @Date:   2021-07-04
 * @Version: V1.0
 */
public interface SysPermissionProjectMapper extends BaseMapper<SysPermissionProject> {

	/**
	 * 根据父权限id差项目子权限
	 * @param parentIds true
	 * @param projectId true
	 * @param userId true
	 * @return List<SysPermission>
	 * */
	public List<SysPermission> getProjectPermission(@Param("parentIds")List<String> parentIds, @Param("projectId")String projectId, @Param("userId")String userId);
}
