package org.jeecg.modules.tools.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @Description: License云平台适配
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Data
@ApiModel(value = "running_tools_licensePage对象", description = "License云平台适配")
public class RunningToolsLicensePage {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String id;
    /**
     * 工具表id
     */
    @Excel(name = "工具表id", width = 15)
    @ApiModelProperty(value = "工具表id")
    private String toolsId;
    /**
     * License名称
     */
    @Excel(name = "License名称", width = 15)
    @ApiModelProperty(value = "License名称")
    private String toolsLicenseName;
    /**
     * License位置
     */
    @Excel(name = "License位置", width = 15)
    @ApiModelProperty(value = "License位置")
    private String toolsLicenseLocation;
    /**
     * License数量
     */
    @Excel(name = "License数量", width = 15)
    @ApiModelProperty(value = "License数量")
    private Integer toolsLicenseNum;
    /**
     * License可用数量
     */
    @Excel(name = "License可用数量", width = 15)
    @ApiModelProperty(value = "License可用数量")
    private Integer toolsLicenseAvnum;
    /**
     * License类型
     */
    @Excel(name = "License类型", width = 15)
    @ApiModelProperty(value = "License类型")
    private String toolsLicenseType;
    /**
     * License授权总数
     */
    @Excel(name = "License授权总数", width = 15)
    @ApiModelProperty(value = "License授权总数")
    private Integer toolsLicenseAuthNum;
    /**
     * License信息
     */
    @Excel(name = "License信息", width = 15)
    @ApiModelProperty(value = "License信息")
    private List<LicenseInfoVO> toolsLicenseInfo;
    /**
     * 删除标记
     */
    @Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
}
