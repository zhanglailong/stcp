package org.jeecg.modules.entity;

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
import java.util.Date;

/**
 * @Description: 虚拟机设计
 * @Author: jeecg-boot
 * @Date: 2021-01-06
 * @Version: V1.0
 */
@Data
@TableName("tepm_vm_design")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vm_design对象", description = "虚拟机设计")
public class VmDesign implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 虚拟机id
     */
    @ApiModelProperty(value = "虚拟机id")
    private String vmId;
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
     * 虚拟机名称
     */
    @Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private String vmName;
    /**
     * cpu
     */
    @Excel(name = "cpu", width = 15)
    @ApiModelProperty(value = "cpu")
    private String virCpu;
    /**
     * 内存
     */
    @Excel(name = "内存", width = 15)
    @ApiModelProperty(value = "内存")
    private String virInner;
    /**
     * 磁盘
     */
    @Excel(name = "磁盘", width = 15)
    @ApiModelProperty(value = "磁盘")
    private String virDisk;
    /**
     * 操作系统
     */
    @Excel(name = "操作系统", width = 15)
    @ApiModelProperty(value = "操作系统")
    private String sysType;
    /**
     * 被测对象
     */
    @Excel(name = "被测对象", width = 15)
    @ApiModelProperty(value = "被测对象")
    private String measurand;
    /**
     * 测试工具
     */
    @Excel(name = "测试工具", width = 15)
    @ApiModelProperty(value = "测试工具")
    private String testTools;
    /**
     * 环境规划
     */
    @Excel(name = "环境规划", width = 15)
    @ApiModelProperty(value = "环境规划")
    private String planId;
    /**
     * 规划名称
     */
    @Excel(name = "规划名称", width = 15)
    @ApiModelProperty(value = "规划名称")
    private String planName;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    private String idel;
    /**
     * 运行支撑软件
     */
    @Excel(name = "运行支撑软件", width = 15)
    @ApiModelProperty(value = "运行支撑软件")
    private String supportSoftware;
    /**
     * 资产库
     */
    @Excel(name = "资产库", width = 15)
    @ApiModelProperty(value = "资产库")
    private String assetPool;
    /**
     * 虚拟机状态
     */
    @Excel(name = "虚拟机状态", width = 15)
    @ApiModelProperty(value = "虚拟机状态")
    private String status = "0";
    /**
     * 虚拟机描述
     */
    @Excel(name = "虚拟机描述", width = 15)
    @ApiModelProperty(value = "虚拟机描述")
    private String description;
    /**
     * 规格ID
     */
    @Excel(name = "规格ID", width = 15)
    @ApiModelProperty(value = "规格ID")
    private String openVmId;
    /**
     * 是否公用
     */
    @Excel(name = "是否公用", width = 15)
    @ApiModelProperty(value = "是否公用")
    private String osFlavorAccess;
    /**
     * 规格的链接
     */
    @Excel(name = "规格的链接", width = 15)
    @ApiModelProperty(value = "规格的链接")
    private String links;
    /**
     * 默认是1.0
     */
    @Excel(name = "默认是1.0", width = 15)
    @ApiModelProperty(value = "默认是1.0")
    private String rxtxFactor;

    //--------------------------------------------网络------------------------------
    /**
     * 网络对象id
     */
    @Excel(name = "网络对象id", width = 15)
    @ApiModelProperty(value = "网络对象id")
    private String networkFromId;
    /**
     * 网络名称
     */
    @Excel(name = "网络名称", width = 15)
    @ApiModelProperty(value = "网络名称")
    private String networkName;
    /**
     * 网络类型
     */
    @Excel(name = "网络类型", width = 15)
    @ApiModelProperty(value = "网络类型")
    private String networkType;
    /**
     * 共享状态
     */
    @Excel(name = "共享状态", width = 15)
    @ApiModelProperty(value = "共享状态")
    private String sharedState;

    //--------------------------------------------子网------------------------------
    /**
     * 子网id
     */
    @Excel(name = "子网id", width = 15)
    @ApiModelProperty(value = "子网id")
    private String childNetId;
    /**
     * 网络ID
     */
    @Excel(name = "网络ID", width = 15)
    @ApiModelProperty(value = "网络ID")
    private String networkId;
    /**
     * 子网的CIDR
     */
    @Excel(name = "子网的CIDR", width = 15)
    @ApiModelProperty(value = "子网的CIDR")
    private String cidr;
    /**
     * 子网名称
     */
    @Excel(name = "子网名称", width = 15)
    @ApiModelProperty(value = "子网名称")
    private String subnetName;
    /**
     * 网络地址
     */
    @Excel(name = "网络地址", width = 15)
    @ApiModelProperty(value = "网络地址")
    private String networkAddress;
    /**
     * IP版本
     */
    @Excel(name = "IP版本", width = 15)
    @ApiModelProperty(value = "IP版本")
    private String ipVersion;
    /**
     * 网关IP
     */
    @Excel(name = "网关IP", width = 15)
    @ApiModelProperty(value = "网关IP")
    private String gatewayIp;
    /**
     * 禁用网关
     */
    @Excel(name = "禁用网关", width = 15)
    @ApiModelProperty(value = "禁用网关")
    private String disableGateway;
    /**
     * DHCP激活
     */
    @Excel(name = "DHCP激活", width = 15)
    @ApiModelProperty(value = "DHCP激活")
    private String dhcpActivation;
    /**
     * 地址池
     */
    @Excel(name = "地址池", width = 15)
    @ApiModelProperty(value = "地址池")
    private String addressPool;
    /**
     * DNS服务器
     */
    @Excel(name = "DNS服务器", width = 15)
    @ApiModelProperty(value = "DNS服务器")
    private String dnsServer;

    //--------------------------------------------路由------------------------------
    /**
     * 路由对象id
     */
    @Excel(name = "路由对象id", width = 15)
    @ApiModelProperty(value = "路由对象id")
    private String routeFromId;
    /**
     * 路由名称
     */
    @Excel(name = "路由名称", width = 15)
    @ApiModelProperty(value = "路由名称")
    private String routeName;
    /**
     * 管理状态
     */
    @Excel(name = "管理状态", width = 15)
    @ApiModelProperty(value = "管理状态")
    private String routeState;
    /**
     * 外部网络
     */
    @Excel(name = "外部网络", width = 15)
    @ApiModelProperty(value = "外部网络")
    private String extranet;
}
