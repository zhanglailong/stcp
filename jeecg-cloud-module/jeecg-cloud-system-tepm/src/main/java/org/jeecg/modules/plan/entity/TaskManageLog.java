package org.jeecg.modules.plan.entity;

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
 * @Description: 任务管理操作日志表
 * @Author: jeecg-boot
 * @Date: 2021-01-14
 * @Version: V1.0
 */
@Data
@TableName("tepm_task_manage_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "task_manage_log对象", description = "任务管理操作日志表")
public class TaskManageLog implements Serializable {
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
     * 任务名称
     */
    @Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private java.lang.String taskName;
    /**
     * 任务标识
     */
    @Excel(name = "任务标识", width = 15)
    @ApiModelProperty(value = "任务标识")
    private java.lang.String taskSign;
    /**
     * 环境用途
     */
    @Excel(name = "环境用途", width = 15)
    @ApiModelProperty(value = "环境用途")
    private java.lang.String environmentUse;
    /**
     * 操作 0未开始 1开始 2暂停 3恢复 4终止
     */
    @Excel(name = "操作 0未开始 1开始 2暂停 3恢复 4终止", width = 15)
    @ApiModelProperty(value = "操作 0未开始 1开始 2暂停 3恢复 4终止")
    private java.lang.String handle;
    /**
     * 环境标识
     */
    @Excel(name = "环境标识", width = 15)
    @ApiModelProperty(value = "环境标识")
    private java.lang.String planSign;
    /**
     * 环境名称
     */
    @Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private java.lang.String planName;
    /**
     * 环境名称
     */
    @Excel(name = "任务管理id", width = 15)
    @ApiModelProperty(value = "任务管理id")
    private java.lang.String taskId;
}
