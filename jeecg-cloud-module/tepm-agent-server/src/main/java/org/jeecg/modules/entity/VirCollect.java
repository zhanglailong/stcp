package org.jeecg.modules.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @Description: 虚拟机收集信息表
 * @Author: jeecg-boot
 * @Date: 2021-01-14
 * @Version: V1.0
 */
@Data
@TableName("tepm_vir_collect")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vir_collect对象", description = "虚拟机收集信息表")
public class VirCollect implements Serializable {
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
     * 虚拟机名称
     */
    @Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String name;
    /**
     * 虚拟机内存
     */
    @Excel(name = "虚拟机内存", width = 15)
    @ApiModelProperty(value = "虚拟机内存")
    private java.lang.String ram;
    /**
     * 虚拟机磁盘
     */
    @Excel(name = "虚拟机磁盘", width = 15)
    @ApiModelProperty(value = "虚拟机磁盘")
    private java.lang.String disk;
    /**
     * 虚拟机cpu
     */
    @Excel(name = "虚拟机cpu", width = 15)
    @ApiModelProperty(value = "虚拟机cpu")
    private java.lang.String cpu;
    /**
     * 虚拟机ip
     */
    @Excel(name = "虚拟机ip", width = 15)
    @ApiModelProperty(value = "虚拟机ip")
    private java.lang.String ip;
}
