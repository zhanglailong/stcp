package org.jeecg.modules.running.project.entity;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 项目用户关联表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Data
@TableName("project_user_association")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="project_user_association对象", description="项目用户关联表")
public class ProjectUserAssociation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**项目用户关联表主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**项目主键*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**项目成员ID*/
    @ApiModelProperty(value = "项目成员ID")
//    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    private String projectMemberIds;
    
	/**项目成员,数据库中没有该字段*/
//	@Excel(name = "项目成员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
//	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@TableField(exist = false)
    @ApiModelProperty(value = "项目成员")
    private String projectMembers;

    @TableField(exist = false)
    private Boolean isUserModify; //用户数据是否改变

    @TableField(exist = false)
    private String originMemberIds; //用户改动前的数据

}
