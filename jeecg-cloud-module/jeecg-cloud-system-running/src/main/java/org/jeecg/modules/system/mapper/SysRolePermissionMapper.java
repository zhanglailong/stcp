package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysRolePermission;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
	
	@Select("select id from sys_permission where id in(\r\n" + 
			"select permission_id from sys_role_permission where role_id =(\r\n" + 
			"select id from sys_role where role_code = #{roleCode}) group by permission_id) and menu_type = 0 or menu_type = 1 and del_flag = 0")
	List<String> queryPagePermission(String roleCode);
	
}
