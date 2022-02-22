package org.jeecg.modules.running.monitortools.entity;

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
 * @Description: 测试工具运行监控
 * @Author: jeecg-boot
 * @Date:   2021-07-15
 * @Version: V1.0
 */
@Data
@TableName("tepm_running_tools_and_vm")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_tools_and_vm对象", description="测试工具运行监控")
public class RunningToolsAndVm implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**测试工具名称*/
	@Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private java.lang.String toolsName;
	/**测试工具标识*/
	@Excel(name = "测试工具标识", width = 15)
    @ApiModelProperty(value = "测试工具标识")
    private java.lang.String toolsCode;
	/**测试工具位置*/
	@Excel(name = "测试工具位置", width = 15)
    @ApiModelProperty(value = "测试工具位置")
    private java.lang.String toolsLocation;
	/**测试工具类型*/
	@Excel(name = "测试工具类型", width = 15)
    @ApiModelProperty(value = "测试工具类型")
    private java.lang.String toolsType;
	/**虚拟删除*/
	@Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.Integer idel;
	/**测试工具进程名称*/
	@Excel(name = "测试工具进程名称", width = 15)
    @ApiModelProperty(value = "测试工具进程名称")
    private java.lang.String toolProcessName;
	/**虚拟机编号*/
	@Excel(name = "虚拟机编号", width = 15)
    @ApiModelProperty(value = "虚拟机编号")
    private java.lang.String vmId;
	/**测试工具端口*/
	@Excel(name = "测试工具端口", width = 15)
    @ApiModelProperty(value = "测试工具端口")
    private java.lang.String toolsPort;
	/**测试工具运行状态*/
	@Excel(name = "测试工具运行状态", width = 15)
    @ApiModelProperty(value = "测试工具运行状态")
    private java.lang.String toolsRunStatus;
	/**虚拟机名称*/
	@Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String vmName;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**项目编号*/
	@Excel(name = "项目编号", width = 15)
    @ApiModelProperty(value = "项目编号")
    private java.lang.String projectId;
    /**虚拟机ip地址*/
    @Excel(name = "虚拟机ip地址", width = 15)
    @ApiModelProperty(value = "虚拟机ip地址")
    private java.lang.String vmAddress;
    /**测试工具linux进程名称*/
    @Excel(name = "测试工具linux进程名称", width = 15)
    @ApiModelProperty(value = "测试工具linux进程名称")
    private String toolLinuxProcessName;
}
