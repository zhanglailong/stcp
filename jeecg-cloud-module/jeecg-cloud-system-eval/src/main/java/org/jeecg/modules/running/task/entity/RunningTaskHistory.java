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
import java.util.Date;

/**
 * @Description: 任务管理历史记录
 * @Author: jeecg-boot
 * @Date:   2021-06-18
 * @Version: V1.0
 */
@Data
@TableName("running_task_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_task_history对象", description="任务管理历史记录")
public class RunningTaskHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15)
    @ApiModelProperty(value = "任务ID")
    private String taskId;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15)
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
	@Excel(name = "任务类型", width = 15)
    @ApiModelProperty(value = "任务类型")
    private String taskType;
	/**任务负责人*/
	@Excel(name = "任务负责人", width = 15)
    @ApiModelProperty(value = "任务负责人")
    private String taskPrincipal;
	/**测试工具*/
	@Excel(name = "测试工具", width = 15, dictTable = "running_tools_list", dicText = "id", dicCode = "tools_name")
	@Dict(dictTable = "running_tools_list", dicText = "id", dicCode = "tools_name")
    @ApiModelProperty(value = "测试工具")
    private String taskTool;
	/**所属项目*/
	@Excel(name = "所属项目", width = 15, dictTable = "running_project", dicText = "id", dicCode = "project_name")
	@Dict(dictTable = "running_project", dicText = "id", dicCode = "project_name")
    @ApiModelProperty(value = "所属项目")
    private String projectInfo;
	/**被测软件名称*/
	@Excel(name = "被测软件名称", width = 15)
    @ApiModelProperty(value = "被测软件名称")
    private String taskSoftName;
	/**被测软件版本*/
	@Excel(name = "被测软件版本", width = 15)
    @Dict(dictTable = "running_task_history", dicText = "id", dicCode = "task_soft_version")
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
	@Excel(name = "选择资产库", width = 15)
    @ApiModelProperty(value = "选择资产库")
    private String taskAssetsId;
	/**任务数据*/
	@Excel(name = "任务数据", width = 15)
    @ApiModelProperty(value = "任务数据")
    private String taskData;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
    @ApiModelProperty(value = "修改人")
    private String reviser;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private String operationType;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remake;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @TableField(exist = false)
    /**查询开始日期*/
    private String startDate;
    @TableField(exist = false)
    /**查询结束日期*/
    private String endDate;
}
