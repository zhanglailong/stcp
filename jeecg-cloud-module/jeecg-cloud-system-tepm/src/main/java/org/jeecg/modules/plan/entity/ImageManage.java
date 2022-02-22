package org.jeecg.modules.plan.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.modules.common.enums.ImageTypeEnum;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 镜像管理
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Data
@TableName("tepm_image_manage")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="image_manage对象", description="镜像管理")
public class ImageManage implements Serializable {
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
    /**工具镜像*/
    @Excel(name = "工具镜像", width = 15)
    @ApiModelProperty(value = "工具镜像")
    private java.lang.String toolImg;
    /**工具镜像名称*/
    @Excel(name = "工具镜像名称", width = 15)
    @ApiModelProperty(value = "工具镜像名称")
    private java.lang.String toolIname;
    /**被测对象*/
    @Excel(name = "被测对象", width = 15)
    @ApiModelProperty(value = "被测对象")
    private java.lang.String measurand;
    /**操作系统*/
    @Excel(name = "操作系统", width = 15)
    @ApiModelProperty(value = "操作系统")
    private java.lang.String sysType;
    /**虚拟删除*/
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private java.lang.Integer idel;
    /**镜像状态*/
    @Excel(name = "镜像状态",width = 15)
    @ApiModelProperty(value = "镜像状态")
    @TableField("image_status")
    private ImageTypeEnum imageStatus;
    /**镜像名称*/
    @Excel(name = "镜像名称", width = 15)
    @ApiModelProperty(value = "镜像名称")
    private java.lang.String imageName;
    /**镜像id*/
    @Excel(name = "镜像id", width = 15)
    @ApiModelProperty(value = "镜像id")
    private java.lang.String openstackId;
    /**虚拟机id(虚拟机标主键)*/
    @Excel(name = "虚拟机id(虚拟机标主键)", width = 15)
    @ApiModelProperty(value = "虚拟机id(虚拟机标主键)")
    private java.lang.String vmId;
    /**虚拟机serverId*/
    @Excel(name = "虚拟机serverId", width = 15)
    @ApiModelProperty(value = "虚拟机serverId")
    private java.lang.String serverId;
    /**虚拟机名称*/
    @Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private java.lang.String stackName;
    /**虚拟机状态*/
    @Excel(name = "虚拟机状态", width = 15)
    @ApiModelProperty(value = "虚拟机状态")
    private java.lang.String stackStatus;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**镜像名称(用户输入)*/
    @Excel(name = "镜像名称(用户输入)",width = 15)
    @ApiModelProperty(value = "镜像名称(用户输入)")
    private java.lang.String name;


    /**其他软件(用户输入)以逗号隔开*/
    @Excel(name = "其他软件(用户输入)以逗号隔开",width = 15)
    @ApiModelProperty(value = "其他软件(用户输入)以逗号隔开")
    private String  otherSoftware;

    /**测试工具(用户输入)以逗号隔开*/
    @Excel(name = "测试工具(用户输入)以逗号隔开",width = 15)
    @ApiModelProperty(value = "测试工具(用户输入)以逗号隔开")
    private String  testTools;
    /**测试工具名称(用户输入)以逗号隔开*/
    @Excel(name = "测试工具(用户输入)名称以逗号隔开",width = 15)
    @ApiModelProperty(value = "测试工具(用户输入)名称以逗号隔开")
    private String  testToolNames;

    /**系统版本*/
    @Excel(name = "系统版本",width = 15)
    @ApiModelProperty(value = "系统版本")
    private String  systemVersion;
}
