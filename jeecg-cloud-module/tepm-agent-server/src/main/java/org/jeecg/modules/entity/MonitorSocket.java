package org.jeecg.modules.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 监控服务管理
 * @Author: jeecg-boot
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Data
@TableName("tepm_monitor_socket")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "monitor_socket对象", description = "监控服务管理")
public class MonitorSocket implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
    /**
     * ip
     */
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    /**
     * 端口
     */
    @Excel(name = "端口", width = 15)
    @ApiModelProperty(value = "端口")
    private java.lang.String port;
    /**
     * 收集时间
     */
    @Excel(name = "收集时间", width = 15)
    @ApiModelProperty(value = "收集时间")
    private java.lang.String collectTime;
    /**
     * 服务状态
     */
    @Excel(name = "服务状态", width = 15)
    @ApiModelProperty(value = "服务状态")
    private java.lang.String state;
    /**
     * Agent_id
     */
    @Excel(name = "Agent_id", width = 15)
    @ApiModelProperty(value = "Agent_id")
    private java.lang.String agentId;
    /**
     * Agent_ip
     */
    @Excel(name = "Agent_ip", width = 15)
    @ApiModelProperty(value = "Agent_ip")
    private java.lang.String agentIp;
    /**
     * Agent状态
     */
    @Excel(name = "Agent状态", width = 15)
    @ApiModelProperty(value = "Agent状态")
    private java.lang.String agentState;
    /**
     * Agent服务运行状态
     */
    @Excel(name = "Agent服务运行状态", width = 15)
    @ApiModelProperty(value = "Agent服务运行状态")
    private java.lang.String agentServiceState;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.String idel;
}
