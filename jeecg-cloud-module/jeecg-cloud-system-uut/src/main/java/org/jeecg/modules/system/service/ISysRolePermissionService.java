package org.jeecg.modules.system.service;

import java.util.List;

import org.jeecg.modules.system.entity.SysRolePermission;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
	
	/**
	 * 保存授权/先删后增
	 * @param roleId true
	 * @param permissionIds true
	 */
	public void saveRolePermission(String roleId,String permissionIds);
	
	/**
	 * 保存授权 将上次的权限和这次作比较 差异处理提高效率 
	 * @param roleId true
	 * @param permissionIds true
	 * @param lastPermissionIds true
	 */
	public void saveRolePermission(String roleId,String permissionIds,String lastPermissionIds);
	
	/**
	 * 保存授权 将上次的权限和这次作比较 差异处理提高效率 
	 * @param roleId true
	 * @param permissionIds true
	 * @param lastPermissionIds true
	 * @param projectId true
	 */
	public void saveProjectRolePermission(String roleId,String permissionIds,String lastPermissionIds,String projectId);

	/**
	 * 根据管理员的角色编码查询其全部的页面权限
	 */
	public List<String> queryPagePermission(String roleCode);
}
