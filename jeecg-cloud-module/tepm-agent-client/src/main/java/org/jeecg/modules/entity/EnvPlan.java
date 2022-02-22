package org.jeecg.modules.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 环境设计
 * @Author: jeecg-boot
 * @Date: 2020-12-29
 * @Version: V1.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "env_plan对象", description = "环境设计")
public class EnvPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
    private java.util.Date createTime;
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
    private java.util.Date updateTime;
    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**
     * 环境规划名称
     */
    @Excel(name = "环境规划名称", width = 15)
    @ApiModelProperty(value = "环境规划名称")
    private String name;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 环境用途
     */
    @Excel(name = "环境用途", width = 15)
    @ApiModelProperty(value = "环境用途")
    private String purpose;
    /**
     * 设计详情
     */
    @Excel(name = "设计详情", width = 15)
    @ApiModelProperty(value = "设计详情")
    private List<EnvPlanNodes> nodeList;
    /**
     * 设计详情
     */
    @Excel(name = "节点集合", width = 15)
    @ApiModelProperty(value = "节点集合")
    private List<EnvPlanLines> lineList;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private String idel;
    /**
     * 规划状态
     */
    @Excel(name = "规划状态", width = 15)
    @ApiModelProperty(value = "规划状态")
    private String state;

}
