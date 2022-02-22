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
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tepm_vm_removal")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vm_removal对象", description = "虚拟机迁移")
public class VmRemoval implements Serializable {

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
     * 原主机名称
     */
    @ApiModelProperty(value = "原主机名称")
    private String primevalHostName;
    /**
     * 现主机名称
     */
    @ApiModelProperty(value = "现主机名称")
    private String targetHostName;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 修改日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改日期")
    private String updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private String updateBy;
    /**
     * 是否删除
     */
    @ApiModelProperty(value = "是否删除")
    private Integer idel;
    /**
     * 迁移状态
     */
    @ApiModelProperty(value = "迁移状态")
    private ServerStatusEnum status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String description;
    /**
     * 迁移方式 0 热迁移  1 冷迁移
     */
    @ApiModelProperty(value = "迁移方式")
    private Integer manner;

    /**
     * 迁移结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "迁移结束时间")
    private Date endTime;
}
