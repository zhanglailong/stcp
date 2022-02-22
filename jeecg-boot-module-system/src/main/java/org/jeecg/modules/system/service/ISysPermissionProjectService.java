package org.jeecg.modules.system.service;

import java.util.List;

import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionProject;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目权限表
 * @Author: jeecg-boot
 * @Date:   2021-07-04
 * @Version: V1.0
 */
public interface ISysPermissionProjectService extends IService<SysPermissionProject> {
	
	/**
	 * 通过关联id查询
	 * @param parentIds true
	 * @param projectId true
	 * @param userId true
	 * @return list<SysPermission>
	 * */
	public List<SysPermission> getProjectPermission(List<String> parentIds, String projectId, String userId);
	
}
