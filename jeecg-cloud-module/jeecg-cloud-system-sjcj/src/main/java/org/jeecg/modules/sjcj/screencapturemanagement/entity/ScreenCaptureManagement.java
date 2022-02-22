package org.jeecg.modules.sjcj.screencapturemanagement.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 截屏管理表
 * @Author: jeecg-boot
 * @Date:   2021-05-20
 * @Version: V1.0
 */
@Data
@TableName("screen_capture_management")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="screen_capture_management对象", description="截屏管理表")
public class ScreenCaptureManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "id")
	@Dict(dictTable = "running_project", dicText = "project_name", dicCode = "id")
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15, dictTable = "running_task", dicText = "task_name", dicCode = "id")
	@Dict(dictTable = "running_task", dicText = "task_name", dicCode = "id")
    @ApiModelProperty(value = "任务ID")
    private String taskId;
	/**测试用例ID*/
	@Excel(name = "测试用例ID", width = 15, dictTable = "running_case", dicText = "test_name", dicCode = "id")
	@Dict(dictTable = "running_case", dicText = "test_name", dicCode = "id")
    @ApiModelProperty(value = "测试用例ID")
    private String caseId;
	/**工具ID*/
	@Excel(name = "工具ID", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
	@Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    @ApiModelProperty(value = "工具ID")
    private String toolId;
	/**AgentIP*/
	@Excel(name = "AgentIP", width = 15)
    @ApiModelProperty(value = "AgentIP")
    private String agentIp;
	/**截屏名称*/
	@Excel(name = "截屏名称", width = 15)
    @ApiModelProperty(value = "截屏名称")
    private String name;
	/**截屏描述*/
	@Excel(name = "截屏描述", width = 15)
    @ApiModelProperty(value = "截屏描述")
    private String description;
	/**截屏存放路径*/
	@Excel(name = "截屏存放路径", width = 15)
    @ApiModelProperty(value = "截屏存放路径")
    private String capturePath;
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
    /**AgentManagerId*/
    @ApiModelProperty(value = "AgentManagerId")
    private String agentManagerId;
}
