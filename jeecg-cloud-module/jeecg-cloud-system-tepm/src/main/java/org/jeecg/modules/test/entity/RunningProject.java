package org.jeecg.modules.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shx
 * 项目
 * 2021-07-06
 * V1.0
 */
@Data
@TableName("running_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "running_project对象", description = "项目")
public class RunningProject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 项目标识
     */
    @ApiModelProperty(value = "项目标识")
    private String projectCode;
    /**
     * 被测对象id
     */
    @ApiModelProperty(value = "被测对象id")
    private String uutListId;
    /**
     * 被测对象名称
     *//*
    @ApiModelProperty(value = "被测对象名称")
    private String uutName;
    *//**
     * 被测对象标识
     *//*
    @ApiModelProperty(value = "被测对象标识")
    private String uutCode;
    *//**
     * 被测对象版本
     *//*
    @ApiModelProperty(value = "被测对象版本")
    private String uutVersion;
    *//**
     * 被测对象类型
     *//*
    @ApiModelProperty(value = "被测对象类型")
    private String uutType;*/
    /**
     * 项目成员
     */
    @ApiModelProperty(value = "项目成员")
    private String projectMembers;
    /**
     * 被测对象状态
     */
    @ApiModelProperty(value = "被测对象状态")
    private Integer projectState;
    /**
     * 资产库详情
     *//*
    @ApiModelProperty(value = "资产库详情")
    private String uutAssetsDetail;*/
    /**
     * 附件上传
     *//*
    @ApiModelProperty(value = "附件上传")
    private String uutFile;*/
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private String cuFile;
    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date beginDate;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date finishDate;
    /**
     * 项目状态0 未完成1已完成
     */
    @ApiModelProperty(value = "项目状态0 未完成1已完成")
    private Integer finishStatus;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 数据统计
     */
    @ApiModelProperty(value = "数据统计")
    private String dataStatistics;
    /**
     * 删除标记0存在1删除
     */
    @ApiModelProperty(value = "删除标记0存在1删除")
    private Integer delFlag;
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
     * 选择资产库
     *//*
    @ApiModelProperty(value = "选择资产库")
    private String uutAssetsId;*/
    /**
     * 是否发送到期未完成消息
     */
    @ApiModelProperty(value = "是否发送到期未完成消息")
    private Integer bNotFinishMsg;
    /**
     * 评审资料
     */
    @ApiModelProperty(value = "评审资料")
    private String reviewFile;
    /**
     * 项目成员id
     */
    @ApiModelProperty(value = "项目成员id")
    private String projectMemberIds;
}
