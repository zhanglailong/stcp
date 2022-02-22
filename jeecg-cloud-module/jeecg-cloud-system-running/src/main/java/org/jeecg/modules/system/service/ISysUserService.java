package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.SysUserCacheInfo;
import org.jeecg.modules.system.entity.SysUser;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
public interface ISysUserService extends IService<SysUser> {

	/**
	 * 重置密码
	 *
	 * @param username true
	 * @param oldpassword true
	 * @param newpassword true
	 * @param confirmpassword true
	 * @return result集合
	 */
	public Result<?> resetPassword(String username, String oldpassword, String newpassword, String confirmpassword);

	/**
	 * 修改密码
	 *
	 * @param sysUser true
	 * @return result集合
	 */
	public Result<?> changePassword(SysUser sysUser);

	/**
	 * 删除用户
	 * @param userId true
	 * @return 布尔值
	 */
	public boolean deleteUser(String userId);

	/**
	 * 批量删除用户
	 * @param userIds true
	 * @return 布尔值
	 */
	public boolean deleteBatchUsers(String userIds);
	/**
	 * 获取用户
	 * @param username true
	 * @return 布尔值
	 */
	public SysUser getUserByName(String username);
	
	/**
	 * 添加用户和用户角色关系
	 * @param user true
	 * @param roles true
	 */
	public void addUserWithRole(SysUser user,String roles);
	
	
	/**
	 * 修改用户和用户角色关系
	 * @param user true
	 * @param roles true
	 */
	public void editUserWithRole(SysUser user,String roles);

	/**
	 * 获取用户的授权角色
	 * @param username true
	 * @return list集合
	 */
	public List<String> getRole(String username);
	
	/**
	  * 查询用户信息包括 部门信息
	 * @param username true
	 * @return 没有返回值
	 */
	public SysUserCacheInfo getCacheUser(String username);

	/**
	 * 根据部门Id查询
	 * @param page true
	 * @param departId true
	 * @param username true
	 * @return Ipage集合
	 */
	public IPage<SysUser> getUserByDepId(Page<SysUser> page, String departId, String username);

	/**
	 * 根据部门Ids查询
	 * @param page true
	 * @param departIds true
	 * @param username true
	 * @return IPage集合
	 */
	public IPage<SysUser> getUserByDepIds(Page<SysUser> page, List<String> departIds, String username);

	/**
	 * 根据 userIds查询，查询用户所属部门的名称（多个部门名逗号隔开）
	 * @param userIds
	 * @return map集合
	 */
	public Map<String,String> getDepNamesByUserIds(List<String> userIds);

    /**
     * 根据部门 Id 和 QueryWrapper 查询
     *
     * @param page true
     * @param departId true
     * @param queryWrapper true
     * @return Ipage集合
     */
    public IPage<SysUser> getUserByDepartIdAndQueryWrapper(Page<SysUser> page, String departId, QueryWrapper<SysUser> queryWrapper);

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param orgCode true
	 * @param userParams 用户查询条件，可为空
	 * @param page 分页参数
	 * @return IPage集合
	 */
	IPage<SysUserSysDepartModel> queryUserByOrgCode(String orgCode, SysUser userParams, IPage page);

	/**
	 * 根据角色Id查询
	 * @param page true
	 * @param roleId true
	 * @param username true
	 * @return Ipage集合
	 */
	public IPage<SysUser> getUserByRoleId(Page<SysUser> page,String roleId, String username);

	/**
	 * 通过用户名获取用户角色集合
	 *
	 * @param username 用户名
	 * @return 角色集合
	 */
	Set<String> getUserRolesSet(String username);

	/**
	 * 通过用户名获取用户权限集合
	 *
	 * @param username 用户名
	 * @return 权限集合
	 */
	Set<String> getUserPermissionsSet(String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username true
	 * @param orgCode true
	 */
	void updateUserDepart(String username,String orgCode);
	
	/**
	 * 根据手机号获取用户名和密码
	 * @param phone true
	 * @return 没有返回值
	 */
	public SysUser getUserByPhone(String phone);


	/**
	 * 根据邮箱获取用户
	 * @param email true
	 * @return 没有返回值
	 */
	public SysUser getUserByEmail(String email);


	/**
	 * 添加用户和用户部门关系
	 * @param user true
	 * @param selectedParts true
	 */
	void addUserWithDepart(SysUser user, String selectedParts);

	/**
	 * 编辑用户和用户部门关系
	 * @param user true
	 * @param departs true
	 */
	void editUserWithDepart(SysUser user, String departs);
	
	/**
	   * 校验用户是否有效
	 * @param sysUser true
	 * @return result集合
	 */
	Result checkUserIsEffective(SysUser sysUser);

	/**
	 * 查询被逻辑删除的用户
	 * @return list集合
	 */
	List<SysUser> queryLogicDeleted();

	/**
	 * 查询被逻辑删除的用户（可拼装查询条件）
	 * @param wrapper true
	 * @return list集合
	 */
	List<SysUser> queryLogicDeleted(LambdaQueryWrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
	 * @param userIds true
	 * @param updateEntity true
	 * @return 布尔值
	 */
	boolean revertLogicDeleted(List<String> userIds, SysUser updateEntity);

	/**
	 * 彻底删除被逻辑删除的用户
	 * @param userIds true
	 * @return 布尔值
	 */
	boolean removeLogicDeleted(List<String> userIds);

	@Transactional(rollbackFor = Exception.class)
    /**
     * 更新手机号、邮箱空字符串为 null
     * @return 布尔值
     */
    boolean updateNullPhoneEmail();

	/**
	 * 保存第三方用户信息
	 * @param sysUser true
	 */
	void saveThirdUser(SysUser sysUser);

	/**
	 * 根据部门Ids查询
	 * @param departIds true
	 * @param username true
	 * @return list集合
	 */
	List<SysUser> queryByDepIds(List<String> departIds, String username);
	
	/**
	 * 根据用户ID 查询userName
	 * @param id true
	 * @return 没有返回值
	 */
	String getUserNameById(String id);
	/**
	 * 根据用户ID 查询userName
	 * @param id true
	 * @return 没有返回值
	 */
	String getRealNameById(String id);
	
	/**
	 * 通过projectId、roleId查询用户
	 *
	 * */
	List<String> getSelectUser (String projectId, String roleId);
}
