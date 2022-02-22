package org.jeecg.modules.dbdata.entity;

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
@TableName("sjcj_db_data")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "sjcj_db_data对象", description = "数据库数据采集配置表")
public class SjcjDbData implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private java.lang.String id;
	/** 数据库数据采集描述 */
	@Excel(name = "数据库数据采集描述", width = 15)
	@ApiModelProperty(value = "数据库数据采集描述")
	private java.lang.String dbDataDescription;
	/** 数据库种类 */
	@Excel(name = "数据库种类", width = 15)
	@ApiModelProperty(value = "数据库种类")
	private java.lang.String dbType;
	/** 数据库种类名称 */
	@Excel(name = "数据库种类名称", width = 15)
	@ApiModelProperty(value = "数据库种类名称")
	private java.lang.String dbTypeName;
	/** 数据库IP地址 */
	@Excel(name = "数据库IP地址", width = 15)
	@ApiModelProperty(value = "数据库IP地址")
	private java.lang.String dbIp;
	/** 端口号 */
	@Excel(name = "端口号", width = 15)
	@ApiModelProperty(value = "端口号")
	private java.lang.String dbPort;
	/** 数据库名称 */
	@Excel(name = "数据库名称", width = 15)
	@ApiModelProperty(value = "数据库名称")
	private java.lang.String dbName;
	/** 用户名 */
	@Excel(name = "用户名", width = 15)
	@ApiModelProperty(value = "用户名")
	private java.lang.String userName;
	/** 密码 */
	@Excel(name = "密码", width = 15)
	@ApiModelProperty(value = "密码")
	private java.lang.String password;
	/** SQL语句 */
	@Excel(name = "SQL语句", width = 15)
	@ApiModelProperty(value = "SQL语句")
	private java.lang.String sqlStatement;
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
