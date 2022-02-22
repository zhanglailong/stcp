package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.system.entity.SysUserRole;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
	/**
	 * 根据项目ID和角色ID删除相关数据
	 */
	int delByProAndRole(String projectId, String roleId);
	
	/**
	 * 根据项目及角色获取相关人员列表
	 */
	List<String> getUserIdByProAndRole(String projectId, String roleId);
}
