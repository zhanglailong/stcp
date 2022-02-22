package org.jeecg.modules.agentbindcase.entity;

import java.io.Serializable;
import java.util.Date;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

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

/**
 * @Description: 客户端绑定测试用例等信息
 */
@Data
@TableName("sjcj_agent_bind_case")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_agent_bind_case对象", description = "客户端绑定测试用例等信息")
public class SjcjAgentBindCase implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/** 创建日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/** 更新人 */
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
	/** 更新日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/** 项目ID */
	@Excel(name = "项目ID", width = 15)
	@ApiModelProperty(value = "项目ID")
	private java.lang.String projectId;
	/** 轮次ID */
	@Excel(name = "轮次ID", width = 15)
	@ApiModelProperty(value = "轮次ID")
	private java.lang.String turnId;
	/** 测试类型ID */
	@Excel(name = "测试类型ID", width = 15)
	@ApiModelProperty(value = "测试类型ID")
	private java.lang.String testTypeId;
	/** 测试项ID */
	@Excel(name = "测试项ID", width = 15)
	@ApiModelProperty(value = "测试项ID")
	private java.lang.String taskId;
	/** 测试用例ID */
	@Excel(name = "测试用例ID", width = 15)
	@ApiModelProperty(value = "测试用例ID")
	private java.lang.String caseId;
	/** 测试工具ID */
	@Excel(name = "测试工具ID", width = 15)
	@ApiModelProperty(value = "测试工具ID")
	private java.lang.String toolId;
	/** `sjcj_agent`表ID */
	@Excel(name = "`sjcj_agent`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_agent`表ID")
	private java.lang.String sjcjAgentId;

	/** 环境ID */
	@TableField(exist = false)
	private String environmentId;

	/** 客户端名称 */
	@TableField(exist = false)
	private String agentName;

	/** 客户端IP */
	@TableField(exist = false)
	private String agentIp;

	/** 客户端端口号 */
	@TableField(exist = false)
	private String agentPort;

	/** 客户端状态 */
	@TableField(exist = false)
	private String agentStatus;

	/** 客户端创建时间 */
	@TableField(exist = false)
	private Date saCreateTime;

	/** 项目名称 */
	@TableField(exist = false)
	private String projectName;

	/** 项目标识 */
	@TableField(exist = false)
	private String projectCode;

	/** 轮次名称 */
	@TableField(exist = false)
	private String turnName;

	/** 测试类型名称 */
	@TableField(exist = false)
	private String testTypeName;

	/** 测试类型标识 */
	@TableField(exist = false)
	private String testTypeCode;

	/** 测试项名称 */
	@TableField(exist = false)
	private String taskName;

	/** 测试项标识 */
	@TableField(exist = false)
	private String taskCode;

	/** 测试用例名称 */
	@TableField(exist = false)
	private String caseName;

	/** 测试用例标识 */
	@TableField(exist = false)
	private String caseCode;
}