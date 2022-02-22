package org.jeecg.modules.running.project.service;

import java.util.List;
import org.jeecg.modules.running.project.entity.ProjectUserAssociation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目用户关联
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface ProjectUserAssociationService extends IService<ProjectUserAssociation> {

	
	/**
	 * 根据项目id 查询项目用户关联表 看当前项目是否存在用户
	 * @param projectId
	 * @return
	 */
	List<ProjectUserAssociation> getInfoByProjectId(String projectId);
	
	
	void deleteDataByProjectId(String projectId);
	
	/**
	 * 删除项目用户关联表中拥有该用户的数据信息
	 * @param userId
	 */
	void deleteInfoByUserId(String userId);
	
	/**
	 * 根据项目ID查询项目成员ID集合
	 * @param projectId
	 * @return userIdList
	 */
	List<String> getUserIdsByProjectId(String projectId);
	
	
}
