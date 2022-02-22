package org.jeecg.modules.tools.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 测试工具操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Data
@TableName("running_tools_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_tools_history对象", description="测试工具操作历史表")
public class RunningToolsHistory implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**测试工具名称*/
	@Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private String toolsName;
	/**测试工具标识*/
	@Excel(name = "测试工具标识", width = 15)
    @ApiModelProperty(value = "测试工具标识")
    private String toolsCode;
	/**测试工具位置*/
	@Excel(name = "测试工具位置", width = 15)
    @ApiModelProperty(value = "测试工具位置")
    private String toolsLocation;
	/**测试工具类型*/
	@Excel(name = "测试工具类型", width = 15)
    @ApiModelProperty(value = "测试工具类型")
    private String toolsType;
	/**是否需要监控*/
	@Excel(name = "是否需要监控", width = 15)
    @ApiModelProperty(value = "是否需要监控")
    private Integer needMonitorOrNot;
	/**工具上传位置*/
	@Excel(name = "工具上传位置", width = 15)
    @ApiModelProperty(value = "工具上传位置")
    private String toolsFileFtpLocations;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    @Dict(dicCode = "operate_type")
    private Integer operationType;
	/**工具id*/
	@Excel(name = "工具id", width = 15)
    @ApiModelProperty(value = "工具id")
    private String runningToolsId;

    /**dq add*/
    @TableField(exist = false)
    /**查询开始日期*/
    private String startDate;
    @TableField(exist = false)
    /**查询结束日期*/
    private String endDate;
}
