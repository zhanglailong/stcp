package org.jeecg.modules.running.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 任务管理
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Data
@TableName("running_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_task对象", description="任务管理")
public class RunningTask implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**项目ID*/
    /** @Excel (name = "项目ID", width = 15)*/
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private String taskName;
	/**任务标识*/
	@Excel(name = "任务标识", width = 15)
    @ApiModelProperty(value = "任务标识")
    private String taskCode;
	/**任务类型*/
    @ApiModelProperty(value = "任务类型")
    private String taskType;
	/**所属项目*/
	@Excel(name = "所属项目", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "project_name")
	@Dict(dictTable = "running_project", dicText = "id", dicCode = "project_name")
    @ApiModelProperty(value = "所属项目")
    private String projectInfo;
	/**任务负责人*/
	@Excel(name = "任务负责人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "任务负责人")
    private String taskPrincipal;
	/**测试工具*/
   /**@Excel (name = "测试工具", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "tools_name")*/
	@Dict(dicCode = "toolsName")
    @ApiModelProperty(value = "测试工具")
    private String taskTool;

	/**被测软件名称*/
	@Excel(name = "被测软件名称", width = 15)
    @ApiModelProperty(value = "被测软件名称")
    private String taskSoftName;
	/**被测软件版本*/
	@Excel(name = "被测软件版本", width = 15)
    @ApiModelProperty(value = "被测软件版本")
    private String taskSoftVersion;
	/**被测软件类型*/
	@Excel(name = "被测软件类型", width = 15)
    @ApiModelProperty(value = "被测软件类型")
    private String taskSoftType;
	/**资产库详情*/
	@Excel(name = "资产库详情", width = 15)
    @ApiModelProperty(value = "资产库详情")
    private String taskAssetsDetail;
	/**附件上传*/
	@Excel(name = "附件上传", width = 15)
    @ApiModelProperty(value = "附件上传")
    private String taskSoftFile;
	/**选择资产库*/
    @ApiModelProperty(value = "选择资产库")
    private String taskAssetsId;
	/**任务数据*/
    /**@Excel (name = "任务数据", width = 15)*/
    @ApiModelProperty(value = "任务数据")
    private String taskData;
	/**任务任务状态*/
	@Excel(name = "任务状态", width = 15,dicCode = "taskStatus")
	@Dict(dicCode = "taskStatus")
    @ApiModelProperty(value = "任务状态") 
    private String taskStatus;
	
	/**附件*/
    @ApiModelProperty(value = "附件")
    private String cuFile;
	
	/**开始时间*/
    @Excel(name = "开始时间", width = 15,format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private java.util.Date beginDate;
	/**结束时间*/
    @Excel(name = "结束时间", width = 15,format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date finishDate;
	/**备注*/
    @ApiModelProperty(value = "备注")
    private String remark;
    
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
    
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    
    @TableField(exist = false)
    private String state;
    
    @TableField(exist = false)
    private List<RunningTaskHistory> modifiedList;
    @TableField(exist = false)
    private Boolean isModified;
}
