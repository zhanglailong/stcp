package org.jeecg.modules.monitor.entity;

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
 * @Description: 主监控服务管理
 * @Author: jeecg-boot
 * @Date: 2021-01-06
 * @Version: V1.0
 */
@Data
@TableName("tepm_monitors_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "tepm_monitors_manage对象", description = "主监控服务管理")
public class MasterMonitorServiceManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * Agent_id
     */
    @Excel(name = "Agent_id", width = 15)
    @ApiModelProperty(value = "Agent_id")
    private java.lang.String agentId;
    /**
     * Agent名称
     */
    @Excel(name = "Agent名称", width = 15)
    @ApiModelProperty(value = "Agent名称")
    private java.lang.String agentName;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String status;
    /**
     * 服务运行状态
     */
    @Excel(name = "服务运行状态", width = 15)
    @ApiModelProperty(value = "服务运行状态")
    private java.lang.String serviceRunStatus;
    /**
     * ip地址
     */
    @Excel(name = "ip地址", width = 15)
    @ApiModelProperty(value = "ip地址")
    private java.lang.String ipAdress;
    /**
     * 端口号
     */
    @Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private java.lang.String port;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
}
