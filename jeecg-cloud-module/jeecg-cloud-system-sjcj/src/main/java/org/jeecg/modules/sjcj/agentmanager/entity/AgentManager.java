package org.jeecg.modules.sjcj.agentmanager.entity;

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
 * @Description: 记录当前已开启的代理
 * @Author: jeecg-boot
 * @Date:   2021-01-06
 * @Version: V1.0
 */
@Data
@TableName("agent_manager")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="agent_manager对象", description="记录当前已开启的代理")
public class AgentManager implements Serializable {
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
	/**配置表ID*/
	@Excel(name = "配置表ID", width = 15, dictTable = "sjcj_wzjcjzl", dicText = "remark", dicCode = "id")
	@Dict(dictTable = "sjcj_wzjcjzl", dicText = "remark", dicCode = "id")
    @ApiModelProperty(value = "配置表ID")
    private String configId;
	/**agent状态*/
//	@Excel(name = "agent状态", width = 15)
    @ApiModelProperty(value = "agent状态")
    private String agentStatus;
	/**agent运行状态*/
//	@Excel(name = "agent运行状态", width = 15)
    @ApiModelProperty(value = "agent运行状态")
    private String runningStatus;
	/**agentIP*/
	@Excel(name = "agentIP", width = 15)
    @ApiModelProperty(value = "agentIP")
    private String agentIp;
	/**端口号*/
	@Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private String agentPort;
	/**采集结果*/
	@Excel(name = "采集结果", width = 15)
    @ApiModelProperty(value = "采集结果")
    private String cjjg;
	/**配置采集路径*/
	@Excel(name = "配置采集路径", width = 15)
    @ApiModelProperty(value = "配置采集路径")
    private String pzurl;
	/**采集描述*/
	@Excel(name = "采集描述", width = 15)
    @ApiModelProperty(value = "采集描述")
    private String description;
	/**配置采集文件种类*/
	@Excel(name = "配置采集文件种类", width = 15)
    @ApiModelProperty(value = "配置采集文件种类")
    private String pzcjwjzl;
	/**过程采集路径*/
	@Excel(name = "过程采集路径", width = 15)
    @ApiModelProperty(value = "过程采集路径")
    private String gcurl;
	/**过程采集文件种类*/
	@Excel(name = "过程采集文件种类", width = 15)
    @ApiModelProperty(value = "过程采集文件种类")
    private String gccjwjzl;
	/**结果采集路径*/
	@Excel(name = "结果采集路径", width = 15)
    @ApiModelProperty(value = "结果采集路径")
    private String jgurl;
	/**结果采集文件种类*/
	@Excel(name = "结果采集文件种类", width = 15)
    @ApiModelProperty(value = "结果采集文件种类")
    private String jgcjwjzl;
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
}
