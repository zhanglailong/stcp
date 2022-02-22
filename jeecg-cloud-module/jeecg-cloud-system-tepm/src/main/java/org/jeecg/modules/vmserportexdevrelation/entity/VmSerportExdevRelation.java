package org.jeecg.modules.vmserportexdevrelation.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 虚拟机串口外部工装关系表
 * @Author: jeecg-boot
 * @Date:   2021-07-19
 * @Version: V1.0
 */
@Data
@TableName("tepm_vm_serport_exdev_relation")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="vm_serport_exdev_relation对象", description="虚拟机串口外部工装关系表")
public class VmSerportExdevRelation implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
	/**项目Id*/
	@Excel(name = "项目Id", width = 15)
    @ApiModelProperty(value = "项目Id")
    private java.lang.String projectId;
	/**项目名称*/
	@Excel(name = "项目名称", width = 15)
    @ApiModelProperty(value = "项目名称")
    private java.lang.String projectName;
	/**规划id*/
	@Excel(name = "规划id", width = 15)
    @ApiModelProperty(value = "规划id")
    private java.lang.String planId;
	/**规划名称*/
	@Excel(name = "规划名称", width = 15)
    @ApiModelProperty(value = "规划名称")
    private java.lang.String planName;
	/**环境id*/
	@Excel(name = "环境id", width = 15)
    @ApiModelProperty(value = "环境id")
    private java.lang.String envId;
	/**环境名称*/
	@Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private java.lang.String envName;
	/**设备名称*/
	@Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String equipmentName;
	/**设备类型 0串口 1 外部工装*/
	@Excel(name = "设备类型 0串口 1 外部工装", width = 15)
    @ApiModelProperty(value = "设备类型 0串口 1 外部工装")
    private java.lang.String equipmentType;
	/**虚拟机id*/
	@Excel(name = "虚拟机id", width = 15)
    @ApiModelProperty(value = "虚拟机id")
    private java.lang.String vmId;
	/**虚拟机名称*/
	@Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String vmName;

	/**虚拟机ServerId*/
    @Excel(name = "虚拟机ServerId", width = 15)
    @ApiModelProperty(value = "虚拟机ServerId")
    private java.lang.String vmServerId;
	/**状态0 绑定 1 未绑定*/
	@Excel(name = "状态0 绑定 1 未绑定", width = 15)
    @ApiModelProperty(value = "状态0 绑定 1 未绑定")
    private java.lang.String status;
	/**服务模式0 客户端 1 服务端*/
	@Excel(name = "服务模式0 客户端 1 服务端", width = 15)
    @ApiModelProperty(value = "服务模式0 客户端 1 服务端")
    private java.lang.String serviceMode;
	/**目标地址（串口地址）*/
	@Excel(name = "目标地址（串口地址）", width = 15)
    @ApiModelProperty(value = "目标地址（串口地址）")
    private java.lang.String desAddress;
	/**端口号*/
	@Excel(name = "端口号", width = 15)
    @ApiModelProperty(value = "端口号")
    private Integer portNumber;
	/**被绑定usb(外部工装有的)*/
	@Excel(name = "被绑定usb(外部工装有的)", width = 15)
    @ApiModelProperty(value = "被绑定usb(外部工装有的)")
    private java.lang.String openstackBoundUsb;
	/**usb主机*/
	@Excel(name = "主机", width = 15)
    @ApiModelProperty(value = "主机")
    private java.lang.String openstackHost;
    /**usb主机Id*/
    @Excel(name = "主机Id", width = 15)
    @ApiModelProperty(value = "主机Id")
    private java.lang.String openstackHostId;

    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private Integer idel;

    /**设备Id*/
    @Excel(name = "设备Id", width = 15)
    @ApiModelProperty(value = "设备Id")
    private String openstackDeviceId;
}
