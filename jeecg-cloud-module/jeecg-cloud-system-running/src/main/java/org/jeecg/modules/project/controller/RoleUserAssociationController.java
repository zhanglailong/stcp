package org.jeecg.modules.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.project.entity.ProjectUserAssociation;
import org.jeecg.modules.project.entity.RoleUserAssociation;
import org.jeecg.modules.project.service.IRoleUserAssociationService;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: role_user_association
 * @Author: jeecg-boot
 * @Date:   2021-04-01
 * @Version: V1.0
 */
@Api(tags="role_user_association")
@RestController
@RequestMapping("/project/roleUserAssociation")
@Slf4j
public class RoleUserAssociationController extends JeecgController<RoleUserAssociation, IRoleUserAssociationService> {
	/**
	 * 项目和人关联服务类
	 */
	@Autowired
	private ProjectUserAssociationService projectUserAssociationService;
	/**
	 * 角色和人关联服务类
	 */
	@Autowired
	private IRoleUserAssociationService roleUserAssociationService;
	
	@Autowired
	private ISysUserRoleService sysUserRoleService;
	
	/**
	 * 获取项目成员
	 *
	 * @param projectId
	 * @return
	 */
	@AutoLog(value = "role_user_association-获取项目成员")
	@ApiOperation(value="role_user_association-获取项目成员", notes="role_user_association-获取项目成员")
	@GetMapping(value = "/getProjectUser")
	public Result<?> getProjectUser(@RequestParam(name="projectId",required=true) String projectId) {
	
		//查询当前项目下,所有用户
		QueryWrapper<ProjectUserAssociation> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("project_id", projectId);
		List<ProjectUserAssociation> userList =projectUserAssociationService.list(queryWrapper);
		
		List<Map<String,Object>> resultList;
		if(userList!=null) {
		 //得到最终项目成员下拉框所需数据结构
		 resultList = roleUserAssociationService.getUserData(userList);
		
		}else {
			return Result.error("未找到对应数据");
		}
		return Result.ok(resultList);
	}

	/**
	 * 通过项目ID、成员ID查询角色信息
	 *
	 * @param userId
	 * @return
	 */
	@AutoLog(value = "role_user_association-通过projectId、userId查询")
	@ApiOperation(value="role_user_association-通过projectId、userId查询", notes="role_user_association-通过projectId、userId查询")
	@GetMapping(value = "/queryByUserId")
	public Result<?> queryByUserId(@RequestParam(name="userId",required=true) String userId,
			@RequestParam(name="projectId",required=true) String projectId) {
		//一个人对应一个角色的数据回填方法
		//一个人对应多个角色的数据回填方法
		List<Map<String,Object>> resultList =new ArrayList<>();
		List<String> roleIdList =  roleUserAssociationService.getRoleIdsByProIdAndUserId(projectId, userId);
		for(String roleId:roleIdList) {
			 Map<String,Object> map = roleUserAssociationService.getRoleListInfo(roleId);
			 resultList.add(map);
		}
		if(resultList.isEmpty()) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(resultList);
	}
	
	/**
	 *   根据角色分配人员
	 */
	@SuppressWarnings("unchecked")
	@AutoLog(value = "role_user_association-添加")
	@ApiOperation(value="role_user_association-添加", notes="role_user_association-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody Map<String, Object> params) {
		String projectId = (String) params.get("projectId"); // 项目ID
		String roleId = (String) params.get("roleId"); // 角色ID
		sysUserRoleService.delByProAndRole(projectId, roleId); // 删除此项目的此角色的所有记录
		Object object = params.get("userId"); // 用户ID
		if (object instanceof String) { // 项目角色为项目负责人时
			this.newSysUserRole((String) object, roleId, projectId) ;
		} else if (object instanceof List<?>) { // 项目角色不为项目负责人时
			List<String> userId = new ArrayList<>();
			userId = (List<String>) object;
			if(userId.size() > 0) {
				for(String temp : userId) {
					this.newSysUserRole(temp, roleId, projectId);
				}
			}
		}
		return Result.ok("操作成功！");
	}
	
	/**
	 * 根据项目及角色获取相关人员列表
	 */
	@GetMapping(value = "/userListByProAndRole")
	public Result<?> getUserIdByProAndRole(@RequestParam(name="projectId", required=true)String projectId,
         @RequestParam(name="roleId", required = true)String roleId) {
		List<String> userIdList = new ArrayList<>();
		userIdList = sysUserRoleService.getUserIdByProAndRole(projectId, roleId);
	    return Result.ok(userIdList);
	}
	
	/**
	 * 新增SysUserRole
	 */
	public void newSysUserRole(String userId, String roleId, String projectId) {
		SysUserRole SysUserRole = new SysUserRole(userId, roleId, projectId);
		sysUserRoleService.save(SysUserRole);
	}
	
}
