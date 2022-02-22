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

@Data
@TableName("tepm_vm_design")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vm_design对象", description = "虚拟机设计")
public class VmProjectVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 虚拟机id
     */
    @ApiModelProperty(value = "虚拟机id")
    private java.lang.String vmId;
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
     * 虚拟机名称
     */
    @Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String vmName;
    /**
     * cpu
     */
    @Excel(name = "cpu", width = 15)
    @ApiModelProperty(value = "cpu")
    private java.lang.String virCpu;
    /**
     * 内存
     */
    @Excel(name = "内存", width = 15)
    @ApiModelProperty(value = "内存")
    private java.lang.String virInner;
    /**
     * 磁盘
     */
    @Excel(name = "磁盘", width = 15)
    @ApiModelProperty(value = "磁盘")
    private java.lang.String virDisk;
    /**
     * 操作系统
     */
    @Excel(name = "操作系统", width = 15)
    @ApiModelProperty(value = "操作系统")
    private java.lang.String sysType;
    /**
     * 被测对象
     */
    @Excel(name = "被测对象", width = 15)
    @ApiModelProperty(value = "被测对象")
    private java.lang.String measurand;
    /**
     * 测试工具
     */
    @Excel(name = "测试工具", width = 15)
    @ApiModelProperty(value = "测试工具")
    private java.lang.String testTools;
    /**
     * 环境id
     */
    @Excel(name = "环境id", width = 15)
    @ApiModelProperty(value = "环境id")
    private java.lang.String planId;
    /**
     * 环境名称
     */
    @Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private java.lang.String planName;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;
    /**
     * 运行支撑软件
     */
    @Excel(name = "运行支撑软件", width = 15)
    @ApiModelProperty(value = "运行支撑软件")
    private java.lang.String supportSoftware;
    /**
     * 资产库
     */
    @Excel(name = "资产库", width = 15)
    @ApiModelProperty(value = "资产库")
    private java.lang.String assetPool;
    /**
     * 虚拟机状态
     */
    @Excel(name = "虚拟机状态", width = 15)
    @ApiModelProperty(value = "虚拟机状态")
    @TableField("status")
    private String status;
    /**
     * 虚拟机描述
     */
    @Excel(name = "虚拟机描述", width = 15)
    @ApiModelProperty(value = "虚拟机描述")
    private java.lang.String description;
    /**
     * 规格ID
     */
    @Excel(name = "规格ID", width = 15)
    @ApiModelProperty(value = "规格ID")
    private java.lang.String openVmId;
    /**
     * 是否公用
     */
    @Excel(name = "是否公用", width = 15)
    @ApiModelProperty(value = "是否公用")
    private java.lang.String osFlavorAccess;
    /**
     * 规格的链接
     */
    @Excel(name = "规格的链接", width = 15)
    @ApiModelProperty(value = "规格的链接")
    private java.lang.String links;
    /**
     * 默认是1.0
     */
    @Excel(name = "默认是1.0", width = 15)
    @ApiModelProperty(value = "默认是1.0")
    private java.lang.String rxtxFactor;
    /**
     * openstack虚拟机id
     */
    @Excel(name = "openstack虚拟机id", width = 15)
    @ApiModelProperty(value = "openstack虚拟机id")
    private java.lang.String serverId;

    /**
     * 环境名称
     */
    @Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private java.lang.String envName;
    /**
     * 项目id
     */
    @Excel(name = "项目id", width = 15)
    @ApiModelProperty(value = "项目id")
    private java.lang.String projectId;

    /**
     * openstack镜像id
     */
    @Excel(name = "openstack镜像id", width = 15)
    @ApiModelProperty(value = "openstack镜像id")
    private java.lang.String openstackImageId;

    //--------------------------------------------网络------------------------------
    /**
     * 网络对象id
     */
    @Excel(name = "网络对象id", width = 15)
    @ApiModelProperty(value = "网络对象id")
    private java.lang.String networkFromId;
    /**
     * 网络名称
     */
    @Excel(name = "网络名称", width = 15)
    @ApiModelProperty(value = "网络名称")
    private java.lang.String networkName;
    /**
     * 网络类型
     */
    @Excel(name = "网络类型", width = 15)
    @ApiModelProperty(value = "网络类型")
    private java.lang.String networkType;
    /**
     * 共享状态
     */
    @Excel(name = "共享状态", width = 15)
    @ApiModelProperty(value = "共享状态")
    private java.lang.String sharedState;

    //--------------------------------------------子网------------------------------
    /**
     * 子网id
     */
    @Excel(name = "子网id", width = 15)
    @ApiModelProperty(value = "子网id")
    private java.lang.String childNetId;
    /**
     * 网络ID
     */
    @Excel(name = "网络ID", width = 15)
    @ApiModelProperty(value = "网络ID")
    private java.lang.String networkId;
    /**
     * 子网的CIDR
     */
    @Excel(name = "子网的CIDR", width = 15)
    @ApiModelProperty(value = "子网的CIDR")
    private java.lang.String cidr;
    /**
     * 子网名称
     */
    @Excel(name = "子网名称", width = 15)
    @ApiModelProperty(value = "子网名称")
    private java.lang.String subnetName;
    /**
     * 网络地址
     */
    @Excel(name = "网络地址", width = 15)
    @ApiModelProperty(value = "网络地址")
    private java.lang.String networkAddress;
    /**
     * 浮动地址
     */
    @Excel(name = "浮动地址", width = 15)
    @ApiModelProperty(value = "浮动地址")
    private java.lang.String floatIp;
    /**
     * IP版本
     */
    @Excel(name = "IP版本", width = 15)
    @ApiModelProperty(value = "IP版本")
    private java.lang.String ipVersion;
    /**
     * 网关IP
     */
    @Excel(name = "网关IP", width = 15)
    @ApiModelProperty(value = "网关IP")
    private java.lang.String gatewayIp;
    /**
     * 禁用网关
     */
    @Excel(name = "禁用网关", width = 15)
    @ApiModelProperty(value = "禁用网关")
    private java.lang.String disableGateway;
    /**
     * DHCP激活
     */
    @Excel(name = "DHCP激活", width = 15)
    @ApiModelProperty(value = "DHCP激活")
    private java.lang.String dhcpActivation;
    /**
     * 地址池
     */
    @Excel(name = "地址池", width = 15)
    @ApiModelProperty(value = "地址池")
    private java.lang.String addressPool;
    /**
     * DNS服务器
     */
    @Excel(name = "DNS服务器", width = 15)
    @ApiModelProperty(value = "DNS服务器")
    private java.lang.String dnsServer;

    //--------------------------------------------路由------------------------------
    /**
     * 路由对象id
     */
    @Excel(name = "路由对象id", width = 15)
    @ApiModelProperty(value = "路由对象id")
    private java.lang.String routeFromId;
    /**
     * 路由名称
     */
    @Excel(name = "路由名称", width = 15)
    @ApiModelProperty(value = "路由名称")
    private java.lang.String routeName;
    /**
     * 管理状态
     */
    @Excel(name = "管理状态", width = 15)
    @ApiModelProperty(value = "管理状态")
    private java.lang.String routeState;
    /**
     * 外部网络
     */
    @Excel(name = "外部网络", width = 15)
    @ApiModelProperty(value = "外部网络")
    private java.lang.String extranet;
    /**
     * 物理机名称
     */
    @ApiModelProperty(value = "物理机名称")
    private String hostName;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 环境id
     */
    @ApiModelProperty(value = "环境id")
    private String stackId;
    /**
     * 虚拟机标识
     */
    @ApiModelProperty(value = "虚拟机标识")
    private String vmCode;
}
