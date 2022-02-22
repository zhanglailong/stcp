package org.jeecg.modules.plan.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @author zlf
 * @Description: 环境设计json
 * @Date: 2020-12-29
 * @Version: V1.0
 */
@Data
public class EnvPlanJson implements Serializable {
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
    @ApiModelProperty(value = "环境规划名称")
    private String name;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 环境用途
     */
    @ApiModelProperty(value = "环境用途")
    private String purpose;
    /**
     * 设计详情
     */
    @ApiModelProperty(value = "设计详情")
    private List<EnvPlanNodes> nodeList;
    /**
     * 设计详情
     */
    @ApiModelProperty(value = "节点集合")
    private List<EnvPlanLines> lineList;
    /**
     * 虚拟删除
     */
    @ApiModelProperty(value = "虚拟删除")
    private String idel;
    /**
     * 规划状态
     */
    @ApiModelProperty(value = "规划状态")
    private String state;
    /**
     * 网络数量
     */
    @ApiModelProperty(value = "网络数量")
    private Integer netNum;
    /**
     * 子网数量
     */
    @ApiModelProperty(value = "子网数量")
    private Integer childNetNum;
    /**
     * 虚拟机数量
     */
    @ApiModelProperty(value = "虚拟机数量")
    private Integer virtualNum;
    /**
     * 路由数量
     */
    @ApiModelProperty(value = "路由数量")
    private Integer routeNum;
    /**
     * 规划json
     */
    @ApiModelProperty(value = "规划json")
    private String planJson;
    /**
     * 发送给openstack的json
     */
    @ApiModelProperty(value = "发送给openstack的json")
    private String openstackJson;
    /**
     * 镜像id
     */
    @ApiModelProperty(value = "镜像id")
    private String mirror;

}
