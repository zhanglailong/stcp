package org.jeecg.modules.collectionconfig.entity;

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
 * @Description: 文件采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-09
 * @Version: V1.0
 */
@Data
@TableName("sjcj_collection_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_collection_config对象", description = "文件采集配置表")
public class SjcjCollectionConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 采集描述 */
	@Excel(name = "采集描述", width = 15)
	@ApiModelProperty(value = "采集描述")
	private java.lang.String collectionDescription;
	/** 采集类型 */
	@Excel(name = "采集类型", width = 15)
	@ApiModelProperty(value = "采集类型")
	private java.lang.String typeOfCollection;
	/** 测试工具_工具ID */
	@Excel(name = "测试工具ID", width = 15)
	@ApiModelProperty(value = "测试工具ID")
	private java.lang.String toolId;
	/** 被测件_自定义采集路径 */
	@Excel(name = "被测件_自定义采集路径", width = 15)
	@ApiModelProperty(value = "被测件_自定义采集路径")
	private java.lang.String uutCollectionPath;
	/** 被测件_自定义采集文件种类 */
	@Excel(name = "被测件_自定义采集文件种类", width = 15)
	@ApiModelProperty(value = "被测件_自定义采集文件种类")
	private java.lang.String uutFileType;
	/** 其他_自定义采集路径 */
	@Excel(name = "其他_自定义采集路径", width = 15)
	@ApiModelProperty(value = "其他_自定义采集路径")
	private java.lang.String otherCollectionPath;
	/** 其他_自定义采集文件种类 */
	@Excel(name = "其他_自定义采集文件种类", width = 15)
	@ApiModelProperty(value = "其他_自定义采集文件种类")
	private java.lang.String otherFileType;
	/** `sjcj_test_tool`表ID */
	@Excel(name = "`sjcj_test_tool`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_test_tool`表ID")
	private java.lang.String sjcjTestToolId;
	/** `sjcj_network_monitoring`表ID */
	@Excel(name = "`sjcj_network_monitoring`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_network_monitoring`表ID")
	private java.lang.String sjcjNetworkMonitoringId;
	/** `sjcj_db_data`表ID */
	@Excel(name = "`sjcj_db_data`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_db_data`表ID")
	private java.lang.String sjcjDbDataId;
	/** `sjcj_agent`表ID */
	@Excel(name = "`sjcj_agent`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_agent`表ID")
	private java.lang.String sjcjAgentId;
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
