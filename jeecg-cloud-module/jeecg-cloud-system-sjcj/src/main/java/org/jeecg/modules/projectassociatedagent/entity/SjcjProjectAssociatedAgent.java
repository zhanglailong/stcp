package org.jeecg.modules.projectassociatedagent.entity;

import java.io.Serializable;

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
 * @Description: 项目与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Data
@TableName("sjcj_project_associated_agent")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_project_associated_agent对象", description = "项目与客户端关系表")
public class SjcjProjectAssociatedAgent implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 项目ID */
	@Excel(name = "项目ID", width = 15)
	@ApiModelProperty(value = "项目ID")
	private java.lang.String projectId;
	/** 测试工具ID */
	@Excel(name = "测试工具ID", width = 15)
	@ApiModelProperty(value = "测试工具ID")
	private java.lang.String testToolId;
	/** `sjcj_agent`表ID */
	@Excel(name = "`sjcj_agent`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_agent`表ID")
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
