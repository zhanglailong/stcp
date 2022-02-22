package org.jeecg.modules.sjcj.dbdatamanagement.entity;

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
 * @Description: 数据库数据管理表
 * @Author: jeecg-boot
 * @Date:   2021-04-15
 * @Version: V1.0
 */
@Data
@TableName("db_data_management")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="db_data_management对象", description="数据库数据管理表")
public class DbDataManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**数据库种类*/
	@Excel(name = "数据库种类", width = 15, dicCode = "dbType")
	@Dict(dicCode = "dbType")
    @ApiModelProperty(value = "数据库种类")
    private String dbType;
	/**数据库IP地址*/
	@Excel(name = "数据库IP地址", width = 15)
    @ApiModelProperty(value = "数据库IP地址")
    private String dbIp;
	/**端口号*/
	@Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private String port;
	/**数据库名称*/
	@Excel(name = "数据库名称", width = 15)
    @ApiModelProperty(value = "数据库名称")
    private String dbName;
	/**用户名*/
	@Excel(name = "用户名", width = 15)
    @ApiModelProperty(value = "用户名")
    private String userName;
	/**密码*/
	@Excel(name = "密码", width = 15)
    @ApiModelProperty(value = "密码")
    private String password;
	/**SQL语句*/
	@Excel(name = "SQL语句", width = 15)
    @ApiModelProperty(value = "SQL语句")
    private String sqlStatement;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String description;
	/**采集状态*/
	@Excel(name = "采集状态", width = 15)
    @ApiModelProperty(value = "采集状态")
    private String gatherStatus;
	/**数据文件名称*/
	@Excel(name = "数据文件名称", width = 15)
    @ApiModelProperty(value = "数据文件名称")
    private String fileName;
	/**数据文件路径*/
	@Excel(name = "数据文件路径", width = 15)
    @ApiModelProperty(value = "数据文件路径")
    private String filePath;
	/**采集时间*/
	@Excel(name = "采集时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "采集时间")
    private Date gatherTime;
	/**采集人*/
	@Excel(name = "采集人", width = 15)
    @ApiModelProperty(value = "采集人")
    private String gatherBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
}
