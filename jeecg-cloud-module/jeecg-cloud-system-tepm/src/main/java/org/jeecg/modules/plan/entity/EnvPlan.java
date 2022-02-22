package org.jeecg.modules.plan.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: 环境设计
 * @Date: 2020-12-29
 * @Version: V1.0
 */
@Data
@TableName("tepm_env_plan")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "tepm_env_plan对象", description = "环境设计")
public class EnvPlan implements Serializable {
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
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;
    /**
     * 规划状态
     */
    @Excel(name = "规划状态", width = 15)
    @ApiModelProperty(value = "规划状态")
    private java.lang.Integer state;
    /**
     * 网络数量
     */
    @Excel(name = "网络数量", width = 15)
    @ApiModelProperty(value = "网络数量")
    private java.lang.Integer netNum;
    /**
     * 子网数量
     */
    @Excel(name = "子网数量", width = 15)
    @ApiModelProperty(value = "子网数量")
    private java.lang.Integer childNetNum;
    /**
     * 虚拟机数量
     */
    @Excel(name = "虚拟机数量", width = 15)
    @ApiModelProperty(value = "虚拟机数量")
    private java.lang.Integer virtualNum;
    /**
     * 路由数量
     */
    @Excel(name = "路由数量", width = 15)
    @ApiModelProperty(value = "路由数量")
    private java.lang.Integer routeNum;

    /**
     * 串口数量
     */
    @Excel(name = "串口数量", width = 15)
    @ApiModelProperty(value = "串口数量")
    private java.lang.Integer  portNum;

    /**
     * 外部工装数量
     */
    @Excel(name = "外部工装数量", width = 15)
    @ApiModelProperty(value = "外部工装数量")
    private java.lang.Integer  externaldevicesNum;

    /**
     * 规划json
     */
    @Excel(name = "规划json", width = 15)
    @ApiModelProperty(value = "规划json")
    private java.lang.String planJson;
    /**
     * 发送给openstack的json
     */
    @Excel(name = "发送给openstack的json", width = 15)
    @ApiModelProperty(value = "发送给openstack的json")
    private java.lang.String openstackJson;

}
