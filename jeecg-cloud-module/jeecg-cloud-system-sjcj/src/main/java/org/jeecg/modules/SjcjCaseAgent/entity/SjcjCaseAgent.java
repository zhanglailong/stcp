package org.jeecg.modules.SjcjCaseAgent.entity;

import java.io.Serializable;

import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 测试用例与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-02
 * @Version: V1.0
 */
@Data
@TableName("sjcj_case_agent")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_case_agent对象", description = "测试用例与客户端关系表")
public class SjcjCaseAgent implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 项目ID */
	@Excel(name = "项目ID", width = 15)
	@ApiModelProperty(value = "项目ID")
	private java.lang.String projectId;
	/** 轮次ID */
	@Excel(name = "轮次ID", width = 15)
	@ApiModelProperty(value = "轮次ID")
	private java.lang.String turnId;
	/** 测试类型 */
	@Excel(name = "测试类型", width = 15, dicCode = "testType")
	@Dict(dicCode = "testType")
	@ApiModelProperty(value = "测试类型")
	private java.lang.String testType;
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
	/** 采集客户端ID */
	@Excel(name = "采集客户端ID", width = 15)
	@ApiModelProperty(value = "采集客户端ID")
	private java.lang.String sjcjAgentId;
	/** 创建日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/** 更新日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/** 更新人 */
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
}
