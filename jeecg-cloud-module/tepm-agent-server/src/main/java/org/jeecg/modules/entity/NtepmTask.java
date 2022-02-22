package org.jeecg.modules.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 测试任务
 * @Author: jeecg-boot
 * @Date:   2021-05-14
 * @Version: V1.0
 */
@Data
@TableName("ntepm_task")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ntepm_task对象", description="测试任务")
public class NtepmTask implements Serializable {
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
    /**测试类型（1：质量度量，2：静态分析，3：代码审查，4：单元测试，5：源码安全测试，6：安全测试）*/
    @Excel(name = "测试类型（1：质量度量，2：静态分析，3：代码审查，4：单元测试，5：源码安全测试，6：安全测试）", width = 15)
    @ApiModelProperty(value = "测试类型（1：质量度量，2：静态分析，3：代码审查，4：单元测试，5：源码安全测试，6：安全测试）")
    private java.lang.String type;
    /**测试工具（1：understand for C++，2：LEDA Testbed，3：人工，4：C++ Test，5：Fortify，6：AppScan，7：Nessus（开源））*/
    @Excel(name = "测试工具（1：understand for C++，2：LEDA Testbed，3：人工，4：C++ Test，5：Fortify，6：AppScan，7：Nessus（开源））", width = 15)
    @ApiModelProperty(value = "测试工具（1：understand for C++，2：LEDA Testbed，3：人工，4：C++ Test，5：Fortify，6：AppScan，7：Nessus（开源））")
    private java.lang.String tool;
    /**地址*/
    @Excel(name = "地址")
    @ApiModelProperty(value = "地址")
    private java.lang.String url;
    /**级别：（1：质量度量，源码安全测试，安全测试），2：（静态分析，单元测试）3：（代码审查)）*/
    @Excel(name = "级别：（1：质量度量，源码安全测试，安全测试），2：（静态分析，单元测试）3：（代码审查)）", width = 15)
    @ApiModelProperty(value = "级别：（1：质量度量，源码安全测试，安全测试），2：（静态分析，单元测试）3：（代码审查)）")
    private java.lang.Integer level;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String phone;
    /**邮箱*/
    @Excel(name = "邮箱", width = 15)
    @ApiModelProperty(value = "邮箱")
    private java.lang.String email;
    /**状态（0：未处理，1：处理成功，2：处理失败，3：异常）*/
    @Excel(name = "状态（0：未处理，1：处理成功，2：处理失败，3：异常）", width = 15)
    @ApiModelProperty(value = "状态（0：未处理，1：处理成功，2：处理失败，3：异常）")
    private java.lang.String status;
    /**订单表id*/
    @Excel(name = "订单表id", width = 15)
    @ApiModelProperty(value = "订单表id")
    private java.lang.String orderId;
    /**资源表id*/
    @Excel(name = "资源表id", width = 15)
    @ApiModelProperty(value = "资源表id")
    private java.lang.String resourceId;
    /**逻辑删除（0：未删除，1：已删除）*/
    @Excel(name = "逻辑删除（0：未删除，1：已删除）", width = 15)
    @ApiModelProperty(value = "逻辑删除（0：未删除，1：已删除）")
    @TableLogic(value = "0",delval = "1")
    private java.lang.Integer idel;
    /**优先级别（默认为0）*/
    @Excel(name = "优先级别（默认为0）", width = 15)
    @ApiModelProperty(value = "优先级别（默认为0）")
    private java.lang.Integer priority;
    /**登录账号*/
    @Excel(name = "登录账号", width = 15)
    @ApiModelProperty(value = "登录账号")
    private java.lang.String loginAccount;
    /**登录密码*/
    @Excel(name = "登录密码", width = 15)
    @ApiModelProperty(value = "登录密码")
    private java.lang.String loginPassword;
    /**项目描述*/
    @Excel(name = "项目描述", width = 15)
    @ApiModelProperty(value = "项目描述")
    private java.lang.String itemDesc;
    /**openstack虚拟机id*/
    @Excel(name = "openstack虚拟机id", width = 15)
    @ApiModelProperty(value = "openstack虚拟机id")
    private java.lang.String serverId;
    /**报告地址*/
    @Excel(name = "报告地址", width = 15)
    @ApiModelProperty(value = "报告地址")
    private java.lang.String reportUrl;
    /**测试文件hash值*/
    @Excel(name = "测试文件hash值", width = 15)
    @ApiModelProperty(value = "测试文件hash值")
    private java.lang.String upFileHash;
    /**测试报告hash值*/
    @Excel(name = "测试报告hash值", width = 15)
    @ApiModelProperty(value = "测试报告hash值")
    private java.lang.String reportHash;
}

