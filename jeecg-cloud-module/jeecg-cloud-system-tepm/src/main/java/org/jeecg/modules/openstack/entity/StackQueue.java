package org.jeecg.modules.openstack.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 堆栈序列-处理openstack相关接口
 * 2021-05-14
 * V1.0
 *
 * @author zlf
 */
@Data
@TableName("tepm_stack_queue")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "stack_queue对象", description = "堆栈序列-处理openstack相关接口")
public class StackQueue implements Serializable {
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
     * 处理类型-0环境1虚拟机
     */
    @Excel(name = "处理类型-0环境1虚拟机", width = 15)
    @ApiModelProperty(value = "处理类型-0环境1虚拟机")
    private java.lang.Integer type;
    /**
     * 处理状态
     */
    @Excel(name = "处理状态", width = 15)
    @ApiModelProperty(value = "处理状态")
    private java.lang.String status;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.Integer idel;
    /**
     * 消息
     */
    @Excel(name = "消息", width = 15)
    @ApiModelProperty(value = "消息")
    private java.lang.String msg;
    /**
     * 处理类型表的id
     */
    @Excel(name = "处理类型表的id", width = 15)
    @ApiModelProperty(value = "处理类型表的id")
    private java.lang.String handleId;
    /**
     * openstack 的Id
     */
    @Excel(name = "openstack 的Id", width = 15)
    @ApiModelProperty(value = "openstack 的Id")
    private java.lang.String openStackId;
    /**
     * 环境id
     */
    @Excel(name = "环境id", width = 15)
    @ApiModelProperty(value = "环境id")
    private java.lang.String envId;

    /**
     * 环境id
     */
    @Excel(name = "环境规划id", width = 15)
    @ApiModelProperty(value = "环境规划id")
    private java.lang.String planId;

    /**
     * name
     */
    @Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private java.lang.String name;
    /**
     * 执行次数
     */
    @Excel(name = "执行次数", width = 15)
    @ApiModelProperty(value = "执行次数")
    private java.lang.Integer count;
}
