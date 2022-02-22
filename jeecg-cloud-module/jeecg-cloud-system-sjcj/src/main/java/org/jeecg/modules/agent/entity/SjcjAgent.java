package org.jeecg.modules.agent.entity;

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
 * @Description: 采集客户端表
 * @Author: jeecg-boot
 * @Date: 2021-07-24
 * @Version: V1.0
 */
@Data
@TableName("sjcj_agent")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_agent对象", description = "采集客户端表")
public class SjcjAgent implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 环境ID */
	@Excel(name = "环境ID", width = 15)
	@ApiModelProperty(value = "环境ID")
	private java.lang.String environmentId;
	/** 客户端名称 */
	@Excel(name = "客户端名称", width = 15)
	@ApiModelProperty(value = "客户端名称")
	private java.lang.String agentName;
	/** 客户端IP */
	@Excel(name = "客户端IP", width = 15)
	@ApiModelProperty(value = "客户端IP")
	private java.lang.String agentIp;
	/** 客户端端口号 */
	@Excel(name = "客户端端口号", width = 15)
	@ApiModelProperty(value = "客户端端口号")
	private java.lang.String agentPort;
	/** 客户端状态 */
	@Excel(name = "客户端状态", width = 15)
	@ApiModelProperty(value = "客户端状态")
	private java.lang.String agentStatus;
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
	/** 预留字段1 */
	@Excel(name = "预留字段1", width = 15)
	@ApiModelProperty(value = "预留字段1")
	private java.lang.String attr1;
}
