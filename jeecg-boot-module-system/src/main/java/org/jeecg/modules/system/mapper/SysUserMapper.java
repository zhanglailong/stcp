package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.system.model.SysUserSysDepartModel;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.jeecg.modules.system.vo.SysUserInfoVO;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @Author scott
 * @since 2018-12-20
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	  * 通过用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public SysUser getUserByName(@Param("username") String username);

	/**
	 *  根据部门Id查询用户信息
	 * @param page true
	 * @param departId true
	 * @param username true
	 * @return IPage<SysUser>
	 */
	IPage<SysUser> getUserByDepId(Page page, @Param("departId") String departId, @Param("username") String username);

	/**
	 *  根据用户Ids,查询用户所属部门名称信息
	 * @param userIds true
	 * @return List<SysUserDepVo>
	 */
	List<SysUserDepVo> getDepNamesByUserIds(@Param("userIds")List<String> userIds);

	/**
	 *  根据部门Ids,查询部门下用户信息
	 * @param page true
	 * @param departIds true
	 * @param username true
	 * @return IPage<SysUser>
	 */
	IPage<SysUser> getUserByDepIds(Page page, @Param("departIds") List<String> departIds, @Param("username") String username);

	/**
	 * 根据角色Id查询用户信息
	 * @param page true
	 * @param username true
	 * @param roleId true
	 * @return IPage<SysUser>
	 */
	IPage<SysUser> getUserByRoleId(Page page, @Param("roleId") String roleId, @Param("username") String username);
	
	/**
	 * 根据用户名设置部门ID
	 * @param username
	 * @param orgCode
	 */
	void updateUserDepart(@Param("username") String username,@Param("orgCode") String orgCode);
	
	/**
	 * 根据手机号查询用户信息
	 * @param phone true
	 * @return 没有返回值
	 */
	public SysUser getUserByPhone(@Param("phone") String phone);
	
	
	/**
	 * 根据邮箱查询用户信息
	 * @param email true
	 * @return 没有返回值
	 */
	public SysUser getUserByEmail(@Param("email")String email);

	/**
	 * 根据 orgCode 查询用户，包括子部门下的用户
	 *
	 * @param page 分页对象, xml中可以从里面进行取值,传递参数 Page 即自动分页,必须放在第一位(你可以继承Page实现自己的分页对象)
	 * @param orgCode true
	 * @param userParams 用户查询条件，可为空
	 * @return List<SysUserSysDepartModel>
	 */
	List<SysUserSysDepartModel> getUserByOrgCode(IPage page, @Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);


    /**
     * 查询 getUserByOrgCode 的Total
     *
     * @param orgCode true
     * @param userParams 用户查询条件，可为空
     * @return 没有返回值
     */
    Integer getUserByOrgCodeTotal(@Param("orgCode") String orgCode, @Param("userParams") SysUser userParams);

    /**
     * 批量删除角色与用户关系
     * @param roleIdArray true
     */
	void deleteBathRoleUserRelation(@Param("roleIdArray") String[] roleIdArray);

    /**
     * 批量删除角色与权限关系
     * @param roleIdArray true
     */
	void deleteBathRolePermissionRelation(@Param("roleIdArray") String[] roleIdArray);

	/**
	 * 查询被逻辑删除的用户
	 * @param wrapper true
	 * @return List<SysUser>
	 *
	 */
	List<SysUser> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<SysUser> wrapper);

	/**
	 * 还原被逻辑删除的用户
	 * @param entity true
	 * @param userIds true
	 * @return 没有
	 */
	int revertLogicDeleted(@Param("userIds") String userIds, @Param("entity") SysUser entity);

	/**
	 * 彻底删除被逻辑删除的用户
	 * @param userIds
	 * @return 没有返回
	 *
	 */
	int deleteLogicDeleted(@Param("userIds") String userIds);

    /**
     * 更新空字符串为null【此写法有sql注入风险，禁止随便用】
     * @param fieldName true
     * @return 没有返回值
     * */
    int updateNullByEmptyString(@Param("fieldName") String fieldName);
    
	/**
	 *  根据部门Ids,查询部门下用户信息
	 * @param departIds true
	 * @param username true
	 * @return List<SysUser>
	 */
	List<SysUser> queryByDepIds(@Param("departIds")List<String> departIds,@Param("username") String username);

	/**
	 *  查询所有用户信息（第三方同步用户数据用）
	 * @return List<SysUserInfoVO>
	 */
	List<SysUserInfoVO> pullAllUser();
	

	@Select(" select username from sys_user where id=#{id} ")
	/**
	 * 根据用户id查询userName
	 * @param id
	 * @return 没有返回值
	 */
	String getUserNameById(String id);
	
	

	@Select(" select realname from sys_user where id=#{id} ")
	/**
	 * 根据用户id查询realName
	 * @param id
	 * @return 没有返回值
	 */
	String getRealNameById(String id);
	
}
