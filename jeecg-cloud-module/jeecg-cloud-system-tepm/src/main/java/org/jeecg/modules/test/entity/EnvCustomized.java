package org.jeecg.modules.test.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zlf
 * 测试环境定制
 * 2021-01-16
 * V1.0
 */
@Data
@TableName("tepm_env_customized")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "env_customized对象", description = "测试环境定制")
public class EnvCustomized implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 环境名称
     */
    @Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private String envName;
    /**
     * 环境标识
     */
    @Excel(name = "环境标识", width = 15)
    @ApiModelProperty(value = "环境标识")
    private String envSign;
    /**
     * 环境用途
     */
    @Excel(name = " 环境用途", width = 15)
    @ApiModelProperty(value = " 环境用途")
    private String environmentUse;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    @TableField("state")
    private EnvStackStatusEnum state;
    /**
     * 虚拟删除
     */
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;
    /**
     * 备注
     */
    @Excel(name = " 备注", width = 15)
    @ApiModelProperty(value = " 备注")
    private String remarks;
    /**
     * 规划id集合
     */
    @Excel(name = "规划id集合", width = 15)
    @ApiModelProperty(value = "规划id集合")
    private String plans;
    /**
     * 规划id
     */
    @Excel(name = "规划id", width = 15)
    @ApiModelProperty(value = "规划id")
    private java.lang.String planId;
    /**
     * stack id
     */
    @Excel(name = "stackId", width = 15)
    @ApiModelProperty(value = "stackId")
    private java.lang.String stackId;
    /**
     * stack name
     */
    @Excel(name = "stackName", width = 15)
    @ApiModelProperty(value = "stackName")
    private java.lang.String stackName;
    /**
     * 规划名称
     */
    @Excel(name = "规划名称", width = 15)
    @ApiModelProperty(value = "规划名称")
    private java.lang.String planName;
    /**
     * 定制类型1手动2自动
     */
    @Excel(name = "定制类型", width = 15)
    @ApiModelProperty(value = "定制类型")
    private java.lang.Integer type;
    /**
     * 消息
     */
    @Excel(name = "消息", width = 15)
    @ApiModelProperty(value = "消息")
    private java.lang.String msg;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projectNames;
    /**
     * 项目id
     */
    @Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private String projectId;
}
