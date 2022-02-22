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

import java.util.List;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/7
 * @Description: 用一句话描述该文件做什么)
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vo_env_plan对象", description = "环境设计")
public class VoEnvPlan {
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
     * 环境规划名称
     */
    @Excel(name = "环境规划名称", width = 15)
    @ApiModelProperty(value = "环境规划名称")
    private java.lang.String name;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remarks;
    /**
     * 环境用途
     */
    @Excel(name = "环境用途", width = 15)
    @ApiModelProperty(value = "环境用途")
    private java.lang.String purpose;
    /**
     * 设计详情
     */
    @Excel(name = "设计详情", width = 15)
    @ApiModelProperty(value = "设计详情")
    private java.lang.String nodeList;
    /**
     * 设计详情
     */
    @Excel(name = "节点集合", width = 15)
    @ApiModelProperty(value = "节点集合")
    private java.lang.String lineList;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private java.lang.String idel;

    /**
     * 虚拟机集合
     */
    @ApiModelProperty(value = "虚拟机集合")
    private List<VmDesign> vmList;
}
