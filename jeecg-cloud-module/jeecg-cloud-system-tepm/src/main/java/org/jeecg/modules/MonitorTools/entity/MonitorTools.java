package org.jeecg.modules.MonitorTools.entity;

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
 * @Description: 测试工具主服务表
 * @Author: jeecg-boot
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Data
@TableName("tepm_monitor_tools")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "monitor_tools对象", description = "测试工具主服务表")
public class MonitorTools implements Serializable {
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
     * 测试工具名称
     */
    @Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private java.lang.String toolName;
    /**
     * 测试工具状态
     */
    @Excel(name = "测试工具状态", width = 15)
    @ApiModelProperty(value = "测试工具状态")
    private java.lang.String toolStatus;
    /**
     * 测试工具用途
     */
    @Excel(name = "测试工具用途", width = 15)
    @ApiModelProperty(value = "测试工具用途")
    private java.lang.String toolUser;
    /**
     * 测试任务编号
     */
    @Excel(name = "测试任务编号", width = 15)
    @ApiModelProperty(value = "测试任务编号")
    private java.lang.String taskNum;
    /**
     * 测试工具ip
     */
    @Excel(name = "测试工具ip", width = 15)
    @ApiModelProperty(value = "测试工具ip")
    private java.lang.String toolIp;
    /**
     * 测试工具配置
     */
    @Excel(name = "测试工具配置", width = 15)
    @ApiModelProperty(value = "测试工具配置")
    private java.lang.String toolConfig;
    /**
     * 测试工具日志
     */
    @Excel(name = "测试工具日志", width = 15)
    @ApiModelProperty(value = "测试工具日志")
    private java.lang.String toolLog;
    /**
     * 测试名称
     */
    @Excel(name = "测试名称", width = 15)
    @ApiModelProperty(value = "测试名称")
    private java.lang.String testName;
    /**
     * 线程数
     */
    @Excel(name = "线程数", width = 15)
    @ApiModelProperty(value = "线程数")
    private java.lang.String numThreads;
    /**
     * 时间
     */
    @Excel(name = "时间", width = 15)
    @ApiModelProperty(value = "时间")
    private java.lang.String rampUp;
    /**
     * 循环次数
     */
    @Excel(name = "循环次数", width = 15)
    @ApiModelProperty(value = "循环次数")
    private java.lang.String loops;
    /**
     * 服务器名称或者IP
     */
    @Excel(name = "服务器名称或者IP", width = 15)
    @ApiModelProperty(value = "服务器名称或者IP")
    private java.lang.String domain;
    /**
     * 端口号
     */
    @Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private java.lang.String port;
    /**
     * 请求方法
     */
    @Excel(name = "请求方法", width = 15)
    @ApiModelProperty(value = "请求方法")
    private java.lang.String method;
    /**
     * 路径
     */
    @Excel(name = "路径", width = 15)
    @ApiModelProperty(value = "路径")
    private java.lang.String path;
    /**
     * 结果状态 0为启动1进行中2压力测试成功3压力测试失败
     */
    @Excel(name = "结果状态", width = 15)
    @ApiModelProperty(value = "结果状态")
    private java.lang.String resultStatus;
    /**
     * 测试用例地址
     */
    @Excel(name = "测试用例地址", width = 15)
    @ApiModelProperty(value = "测试用例地址")
    private java.lang.String jmeterPath;
}
