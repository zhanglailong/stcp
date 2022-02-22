package org.jeecg.modules.openstack.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 环境的恢复
 * 2021-05-12
 * V1.0
 *
 * @author zlf
 */
@Data
@TableName("tepm_stack_recovery")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "stack_recovery对象", description = "环境的恢复")
public class StackRecovery implements Serializable {
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
     * 快照/备份名称
     */
    @Excel(name = "快照/备份名称", width = 15)
    @ApiModelProperty(value = "快照/备份名称")
    private java.lang.String name;
    /**
     * 快照/备份别名
     */
    @Excel(name = "快照/备份别名", width = 15)
    @ApiModelProperty(value = "快照/备份别名")
    private java.lang.String stackName;
    /**
     * 恢复类型-0快照1备份
     */
    @Excel(name = "恢复类型-0快照1备份", width = 15)
    @ApiModelProperty(value = "恢复类型-0快照1备份")
    private java.lang.Integer type;
    /**
     * 定制环境id
     */
    @Excel(name = "定制环境id", width = 15)
    @ApiModelProperty(value = "定制环境id")
    private java.lang.String envId;
    /**
     * openstack栈环境id
     */
    @Excel(name = "openstack栈环境id", width = 15)
    @ApiModelProperty(value = "openstack栈环境id")
    private java.lang.String stackId;
    /**
     * 环境快照/备份id
     */
    @Excel(name = "环境快照/备份id", width = 15)
    @ApiModelProperty(value = "环境快照/备份id")
    private java.lang.String snapshotId;
    /**
     * 快照/备份的状态
     */
    @Excel(name = "快照/备份的状态", width = 15)
    @ApiModelProperty(value = "快照/备份的状态")
    private java.lang.String status;
    /**
     * 状态的原因
     */
    @Excel(name = "状态的原因", width = 15)
    @ApiModelProperty(value = "状态的原因")
    private java.lang.String statusReason;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;

    /**
     * 备份/快照 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "备份开始时间")
    private java.util.Date backupsBeginTime;
    /**
     * 备份/快照 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "备份结束时间")
    private java.util.Date backupsEndTime;

    /**
     * 虚拟机id
     */
    @Excel(name = "虚拟机id", width = 15)
    @ApiModelProperty(value = "虚拟机id")
    private String vmId;

    /**
     * 快照/备份 恢复 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "快照/备份 恢复 开始时间")
    private Date restoreBeginTime;

    /**
     * 快照/备份 恢复 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "快照/备份 恢复 结束时间")
    private Date restoreEndTime;
}
