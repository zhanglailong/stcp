package org.jeecg.modules.tools.entity;

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
 * @Description: License监控表
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Data
@TableName("running_tools_licensemonitor")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_tools_licensemonitor对象", description="License监控表")
public class RunningToolsLicensemonitor implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**license的id*/
	@Excel(name = "license的id", width = 15)
    @ApiModelProperty(value = "license的id")
    @Dict(dictTable = "running_tools_license", dicText = "tools_license_name", dicCode = "id")
    private String licenseId;
	/**使用者id*/
	@Excel(name = "使用者id", width = 15)
    @ApiModelProperty(value = "使用者id")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    private String userId;
	/**使用者ip*/
	@Excel(name = "使用者ip", width = 15)
    @ApiModelProperty(value = "使用者ip")
    private String userIp;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @Dict(dicCode = "license_task_status")
    private String status;
	/**使用开始时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用开始时间")
    private Date createTime;
	/**使用结束时间*/
	@Excel(name = "使用结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "使用结束时间")
    private Date endTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    @Dict(dictTable = "running_task", dicText = "task_name", dicCode = "id")
    private String taskId;
    /**工具id*/
    @Excel(name = "工具id", width = 15)
    @ApiModelProperty(value = "工具id")
    @Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    private String toolsId;
}
