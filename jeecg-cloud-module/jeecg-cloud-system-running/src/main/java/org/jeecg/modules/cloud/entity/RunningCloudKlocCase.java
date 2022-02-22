package org.jeecg.modules.cloud.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.cloudtools.Cases;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: kloc用例表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Data
@TableName("running_cloud_kloc_case")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_cloud_kloc_case对象", description="kloc用例表")
public class RunningCloudKlocCase extends Cases implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**服务端ip*/
	@Excel(name = "服务端ip", width = 15)
    @Dict(dicCode = "cloud_server_ip")
    @ApiModelProperty(value = "服务端ip")
    private String serverIp;
	/**客户端ip*/
	@Excel(name = "客户端ip", width = 15)
    @ApiModelProperty(value = "客户端ip")
    private String clientIp;
	/**kloc项目名称*/
	@Excel(name = "kloc项目名称", width = 15)
    @ApiModelProperty(value = "kloc项目名称")
    private String projectName;
	/**编译环境*/
	@Excel(name = "编译环境", width = 15)
    @Dict(dicCode = "compile_env")
    @ApiModelProperty(value = "编译环境")
    private String compileEnv;
	/**测试结果路径*/
	@Excel(name = "测试结果路径", width = 15)
    @ApiModelProperty(value = "测试结果路径")
    private String reportUrl;
	/**优先级*/
	@Excel(name = "优先级", width = 15)
    @Dict(dicCode = "priority")
    @ApiModelProperty(value = "优先级")
    private String priorityLevel;
	/**用例名称*/
	@Excel(name = "用例名称", width = 15)
    @ApiModelProperty(value = "用例名称")
    private String klocCaseName;
	/**测试状态*/
	@Excel(name = "测试状态", width = 15)
    @ApiModelProperty(value = "测试状态")
    @Dict(dicCode = "klocStatus")
    private String status;
    /**删除标记*/
    @Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private String delFlag;
    /**源码包路径*/
    @Excel(name = "源码包路径", width = 15)
    @ApiModelProperty(value = "源码包路径")
    private String sourceFile;
    @Dict(dicCode = "klocStep")
    private String execStep;
    
    
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date startTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date endTime;
}
