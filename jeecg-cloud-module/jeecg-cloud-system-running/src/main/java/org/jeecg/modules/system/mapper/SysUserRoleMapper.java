package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

	@Select("select role_code from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
	/**
	 * 获取角色名
	 * @param username true
	 * @return List<String>
	 * */
	List<String> getRoleByUserName(@Param("username") String username);

	@Select("select id from sys_role where id in (select role_id from sys_user_role where user_id = (select id from sys_user where username=#{username}))")
	/**
	 * 获取角色名
	 * @param username true
	 * @return List<String>
	 * */
	List<String> getRoleIdByUserName(@Param("username") String username);
	/**
	 * 获取角色名
	 * @param userId true
	 * @return  List<SysUserRole>
	 * */
    List<SysUserRole> getRolesByUserId(String userId);
    
    @Delete("delete from sys_user_role where project_id=#{projectId} and role_id=#{roleId}")
    int delByProAndRole(String projectId, String roleId);

    @Select("select user_id from sys_user_role where project_id=#{projectId} and role_id=#{roleId}")
    List<String> getUserIdByProAndRole(String projectId, String roleId);
}
