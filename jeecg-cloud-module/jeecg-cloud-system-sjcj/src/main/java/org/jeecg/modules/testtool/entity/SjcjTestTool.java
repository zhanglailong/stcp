package org.jeecg.modules.testtool.entity;

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
 * @Description: 测试工具采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
@Data
@TableName("sjcj_test_tool")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_test_tool对象", description = "测试工具采集配置表")
public class SjcjTestTool implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 测试工具ID */
	@Excel(name = "测试工具ID", width = 15)
	@ApiModelProperty(value = "测试工具ID")
	private java.lang.String testToolId;
	/** 测试工具ID */
	@Excel(name = "测试工具标识", width = 15)
	@ApiModelProperty(value = "测试工具标识")
	private java.lang.String testToolCode;
	/** 测试工具名称 */
	@Excel(name = "测试工具名称", width = 15)
	@ApiModelProperty(value = "测试工具名称")
	private java.lang.String testToolName;
	/** 配置采集路径 */
	@Excel(name = "配置采集路径", width = 15)
	@ApiModelProperty(value = "配置采集路径")
	private java.lang.String configCollectionPath;
	/** 配置采集文件种类 */
	@Excel(name = "配置采集文件种类", width = 15)
	@ApiModelProperty(value = "配置采集文件种类")
	private java.lang.String configFileType;
	/** 过程采集路径 */
	@Excel(name = "过程采集路径", width = 15)
	@ApiModelProperty(value = "过程采集路径")
	private java.lang.String processCollectionPath;
	/** 过程采集文件种类 */
	@Excel(name = "过程采集文件种类", width = 15)
	@ApiModelProperty(value = "过程采集文件种类")
	private java.lang.String processFileType;
	/** 结果采集路径 */
	@Excel(name = "结果采集路径", width = 15)
	@ApiModelProperty(value = "结果采集路径")
	private java.lang.String resultCollectionPath;
	/** 结果采集文件种类 */
	@Excel(name = "结果采集文件种类", width = 15)
	@ApiModelProperty(value = "结果采集文件种类")
	private java.lang.String resultFileType;
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
