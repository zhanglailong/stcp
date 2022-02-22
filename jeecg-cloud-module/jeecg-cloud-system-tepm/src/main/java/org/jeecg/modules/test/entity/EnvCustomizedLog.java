package org.jeecg.modules.test.entity;

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
 * @Description: 测试环境定制日志
 * @Author: jeecg-boot
 * @Date: 2021-01-16
 * @Version: V1.0
 */
@Data
@TableName("tepm_env_customized_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "env_customized对象", description = "测试环境定制")
public class EnvCustomizedLog implements Serializable {
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
    private String state;
    /**
     * 虚拟删除
     */
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private Integer idel;
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
     * 规划id集合
     */
    @Excel(name = "规划id", width = 15)
    @ApiModelProperty(value = "规划id")
    private String planId;
    /**
     * 规划名称
     */
    @Excel(name = "规划名称", width = 15)
    @ApiModelProperty(value = "规划名称")
    private String planName;
    /**
     * 定制类型1手动2自动
     */
    @Excel(name = "定制类型", width = 15)
    @ApiModelProperty(value = "定制类型")
    private Integer type;
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
    private java.lang.String projectName;
    /**
     * 项目id
     */
    @Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;
}
