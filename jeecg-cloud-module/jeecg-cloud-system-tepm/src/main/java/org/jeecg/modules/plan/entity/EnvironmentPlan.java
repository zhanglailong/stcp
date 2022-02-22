package org.jeecg.modules.plan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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

/**
 * @author zlf
 * @Description: 环境规划
 * @Date: 2020-12-25
 * @Version: V1.0
 */
@Data
@TableName("tepm_environment_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "environment_plan对象", description = "环境规划")
public class EnvironmentPlan implements Serializable {
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
     * 规划名称
     */
    @Excel(name = "规划名称", width = 15)
    @ApiModelProperty(value = "规划名称")
    private java.lang.String planName;
    /**
     * 规划路径
     */
    @Excel(name = "规划路径", width = 15)
    @ApiModelProperty(value = "规划路径")
    private java.lang.String planUrl;
    /**
     * 规划文件名
     */
    @Excel(name = "规划文件名", width = 15)
    @ApiModelProperty(value = "规划文件名")
    private java.lang.String planFileName;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
    /**
     * 状态
     */
    @Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private java.lang.String state;
    /**
     * 数据存在状态
     */
    @Excel(name = "数据存在状态", width = 15)
    @ApiModelProperty(value = "数据存在状态")
    private java.lang.Integer idel;
    /**
     * 任务id
     */
    @Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private java.lang.String taskId;
    /**
     * 任务名称
     */
    @Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private java.lang.String taskName;
    /**
     * 环境用途
     */
    @Excel(name = "环境用途", width = 15)
    @ApiModelProperty(value = "环境用途")
    private java.lang.String purpose;
}
