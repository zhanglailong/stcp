package org.jeecg.modules.networkmonitoring.entity;

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
 * @Description: 网络监听配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
@Data
@TableName("sjcj_network_monitoring")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_network_monitoring对象", description = "网络监听配置表")
public class SjcjNetworkMonitoring implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 被监听的IP */
	@Excel(name = "被监听的IP", width = 15)
	@ApiModelProperty(value = "被监听的IP")
	private java.lang.String nmIp;
	/** 被监听的端口号 */
	@Excel(name = "被监听的端口号", width = 15)
	@ApiModelProperty(value = "被监听的端口号")
	private java.lang.String nmPort;
	/** `sjcj_collection_config`表ID */
	@Excel(name = "`sjcj_collection_config`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_collection_config`表ID")
	private java.lang.String sjcjCollectionConfigId;
	/** `sjcj_agent_bind_case`表ID */
	@Excel(name = "`sjcj_agent_bind_case`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_agent_bind_case`表ID")
	private java.lang.String sjcjAgentBindCaseId;
	/** 创建日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "创建日期")
	private java.util.Date createTime;
	/** 创建人 */
	@ApiModelProperty(value = "创建人")
	private java.lang.String createBy;
	/** 更新日期 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@ApiModelProperty(value = "更新日期")
	private java.util.Date updateTime;
	/** 更新人 */
	@ApiModelProperty(value = "更新人")
	private java.lang.String updateBy;
}
