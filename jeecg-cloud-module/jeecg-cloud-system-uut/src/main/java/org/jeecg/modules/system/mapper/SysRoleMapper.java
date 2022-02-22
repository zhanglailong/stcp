package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

	/**
	 * @Author scott
	 * @Date 2019/12/13 16:12
	 * @Description: 删除角色与用户关系
	 */
	@Delete("delete from sys_user_role where role_id = #{roleId}")
	/**
	 * 删除
	 * 
	 * @param roleId true
	 *
	 */
	void deleteRoleUserRelation(@Param("roleId") String roleId);

	/**
	 * @Author scott
	 * @Date 2019/12/13 16:12
	 * @Description: 删除角色与权限关系
	 */
	@Delete("delete from sys_role_permission where role_id = #{roleId}")
	/**
	 * 删除
	 * 
	 * @param roleId true
	 */
	void deleteRolePermissionRelation(@Param("roleId") String roleId);

	/**
	 * 获取角色列表
	 * 
	 * @param page         true
	 * @param roleGrouping true
	 * @return List<SysRole>
	 */
	List<SysRole> getRoleList(Page<SysRole> page, String roleGrouping);

	/**
	 * 根据id查询该角色的角色分组(系统/项目)
	 */
	@Select("SELECT role_grouping FROM sys_role WHERE id = #{id}")
	String getRoleGroupingById(String id);

	@Select("SELECT id FROM sys_role WHERE role_code = #{roleCode}")
	String getRoleIdByRoleCode(String roleCode);

	@Select("SELECT DISTINCT(r.id) FROM sys_role r "
			+ " JOIN sys_permission_project spp ON spp.role_id = r.id"
			+ " WHERE r.id = #{roleCode} AND spp.project_id = #{projectId}")
	String getRoleIdByRnp(String roleCode, String projectId);

}
