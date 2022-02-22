package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.TreeModel;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
@Mapper
public interface SysDepartMapper extends BaseMapper<SysDepart> {
	
	/**
	 * 根据用户ID查询部门集合
	 * @param userId  true
	 * @return  List<SysDepart>
	 */
	public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username true
	 * @return List<SysDepart>
	 */
	public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

	@Select("select id from sys_depart where org_code=#{orgCode}")
	/**
	 * 根据用户名查询部门
	 *
	 * @param orgCode true
	 * @return 没有返回值
	 */
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

	@Select("select id,parent_id from sys_depart where id=#{departId}")
	/**
	 * 根据用户名查询部门
	 *
	 * @param departId true
	 * @return 没有返回值
	 */
	public SysDepart getParentDepartId(@Param("departId") String departId);

	/**
	 *  根据部门Id查询,当前和下级所有部门IDS
	 * @param departId true
	 * @return List<String>
	 */
	List<String> getSubDepIdsByDepId(@Param("departId") String departId);

	/**
	 * 根据部门编码获取部门下所有IDS
	 * @param orgCodes true
	 * @return List<String>
	 */
	List<String> getSubDepIdsByOrgCodes(@org.apache.ibatis.annotations.Param("orgCodes") String[] orgCodes);

}
