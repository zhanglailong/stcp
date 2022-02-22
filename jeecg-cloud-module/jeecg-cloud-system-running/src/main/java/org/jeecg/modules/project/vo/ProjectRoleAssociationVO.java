package org.jeecg.modules.project.vo;

import java.util.List;

import lombok.Data;

/**
 * @Description: 角色配置弹窗保存所需字段
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Data
public class ProjectRoleAssociationVO {
	
	 
	/**
	 * 项目主键
	 */
	private String projectId;
	
    /**
     * 存放角色主键集合
     */
    private List<String> roleKeys;

	

}
