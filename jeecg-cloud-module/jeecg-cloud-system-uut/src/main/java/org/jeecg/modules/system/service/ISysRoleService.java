package org.jeecg.modules.system.service;

import java.util.List;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {

    /**
     * 导入 excel ，检查 roleCode 的唯一性
     *
     * @param file true
     * @param params true
     * @throws Exception
     * @return result集合
     */
    Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception;

    /**
     * 删除角色
     * @param roleid true
     * @return 布尔值
     */
    public boolean deleteRole(String roleid);

    /**
     * 批量删除角色
     * @param roleids true
     * @return 布尔值
     */
    public boolean deleteBatchRole(String[] roleids);
    
    /**
	 * 分页查询项目角色列表
     * @param page true
     * @param roleGrouping true
     * @return page集合
	 */
	Page<SysRole> rolePageList(Page<SysRole> page, String roleGrouping);
	
	/**
	 * 根据id查询该角色的角色分组(系统/项目)
	 */
	String getRoleGroupingById(String id);

	/**
	 * 根据角色编码查询其角色id
	 */
	String getRoleIdByRoleCode(String roleCode);

	/**
	 * 根据角色编码查询其角色id
	 */
	String getRoleIdByRnp(String roleCode, String projectId);
	
	
	
}
