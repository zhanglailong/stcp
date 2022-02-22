package org.jeecg.modules.collectionresult.entity;

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

@Data
@TableName("sjcj_collection_result")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_collection_result对象", description = "测试数据采集结果表")
public class SjcjCollectionResult implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 采集时间 */
	@Excel(name = "采集时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "采集时间")
	private java.util.Date collectionTime;
	/** 采集描述 */
	@Excel(name = "采集描述", width = 15)
	@ApiModelProperty(value = "采集描述")
	private java.lang.String collectionDescription;
	/** 采集类型 */
	@Excel(name = "采集类型", width = 15)
	@ApiModelProperty(value = "采集类型")
	private java.lang.String typeOfCollection;
	/** 采集结果 */
	@Excel(name = "采集结果", width = 15)
	@ApiModelProperty(value = "采集结果")
	private java.lang.String collectionResult;
	/** `sjcj_agent_bind_case`表ID */
	@Excel(name = "`sjcj_agent_bind_case`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_agent_bind_case`表ID")
	private java.lang.String sjcjAgentBindCaseId;
	/** `sjcj_collection_config`表ID */
	@Excel(name = "`sjcj_collection_config`表ID", width = 15)
	@ApiModelProperty(value = "`sjcj_collection_config`表ID")
	private java.lang.String sjcjCollectionConfigId;
	/** 采集文件存储路径 */
	@Excel(name = "采集文件存储路径", width = 15)
	@ApiModelProperty(value = "采集文件存储路径")
	private java.lang.String fileStoragePath;
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
