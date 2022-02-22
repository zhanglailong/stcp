package org.jeecg.modules.running.project.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.running.project.entity.ProjectUserAssociation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目用户关联
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
public interface ProjectUserAssociationMapper extends BaseMapper<ProjectUserAssociation> {
	
	@Select(" SELECT id,project_id as projectId, \n "
			+ " project_member_ids as projectMemberIds \n"
			+ " FROM project_user_association"
			+ " WHERE project_id=#{projectId} ")
	List<ProjectUserAssociation> getInfoByProjectId(String projectId);
	
	@Delete(" delete from project_user_association where project_id=#{projectId} ")
	void deleteDataByProjectId(String projectId);
	
	@Select(" select * from project_user_association where user_id=#{userId} ")
	List<ProjectUserAssociation> getInfoByUserId(String userId);
	
	@Delete(" delete from project_user_association where user_id=#{userId} ")
	void deleteInfoByUserId(String userId);
	
	@Select(" select project_member_ids from project_user_association where project_id = #{projectId} ")
	List<String> getUserIdsByProjectId(String projectId);
	
}
