package org.jeecg.modules.monitorAgent.entity;

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
 * @Description: Agent代理服务管理
 * @Author: jeecg-boot
 * @Date: 2021-01-06
 * @Version: V1.0
 */
@Data
@TableName("tepm_agent_service_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "tepm_agent_service_manage对象", description = "Agent代理服务管理")
public class AgentServiceManagement implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * ip
     */
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    /**
     * 主机ip
     */
    @Excel(name = "主机ip", width = 15)
    @ApiModelProperty(value = "主机ip")
    private java.lang.String mainIp;
    /**
     * port
     */
    @Excel(name = "port", width = 15)
    @ApiModelProperty(value = "port")
    private java.lang.String port;
    /**
     * 主机端口号
     */
    @Excel(name = "主机端口号", width = 15)
    @ApiModelProperty(value = "主机端口号")
    private java.lang.String mainPort;
    /**
     * 服务运行状态
     */
    @Excel(name = "服务运行状态", width = 15)
    @ApiModelProperty(value = "服务运行状态")
    private java.lang.String objectServiceStatus;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date createTime;
}
