package org.jeecg.modules.system.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.model.TreeModel;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 菜单权限表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-21
 */
public interface ISysPermissionService extends IService<SysPermission> {
	/**
	 * 通过关联id查询
	 * @param parentId true
	 * @return list集合
	 * */
	public List<TreeModel> queryListByParentId(String parentId);
	
	/**
	 * 真实删除
	 * @param id true
	 * @throws JeecgBootException
	 * */
	public void deletePermission(String id) throws JeecgBootException;
	/**
	 * 逻辑删除
	 * @param id true
	 * @throws JeecgBootException
	 * */
	public void deletePermissionLogical(String id) throws JeecgBootException;
	/**
	 * 逻辑添加
	 * @param sysPermission true
	 * @throws JeecgBootException
	 * */
	public void addPermission(SysPermission sysPermission) throws JeecgBootException;
	/**
	 * 逻辑编辑
	 * @param sysPermission true
	 * @throws JeecgBootException
	 * */
	public void editPermission(SysPermission sysPermission) throws JeecgBootException;
	/**
	 * 获取用户信息
	 * @param username true
	 * @return list集合
	 * */
	public List<SysPermission> queryByUser(String username, String projectId);
	
	/**
	 * 根据permissionId删除其关联的SysPermissionDataRule表中的数据
	 * @param id true
	 */
	public void deletePermRuleByPermId(String id);
	
	/**
	  * 查询出带有特殊符号的菜单地址的集合
	 * @return List集合
	 */
	public List<String> queryPermissionUrlWithStar();

	/**
	 * 判断用户否拥有权限
	 * @param username true
	 * @param sysPermission true
	 * @return 布尔值
	 */
	public boolean hasPermission(String username, SysPermission sysPermission);

	/**
	 * 根据用户和请求地址判断是否有此权限
	 * @param username true
	 * @param url true
	 * @return 布尔值
	 */
	public boolean hasPermission(String username, String url);
}
