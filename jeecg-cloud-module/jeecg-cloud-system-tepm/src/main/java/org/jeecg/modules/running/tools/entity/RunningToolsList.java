package org.jeecg.modules.running.tools.entity;

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
 * @Description: 测试工具列表
 * @Author: jeecg-boot
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Data
@TableName("running_tools_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_tools_list对象", description="测试工具列表")
public class RunningToolsList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**测试工具名称*/
	@Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private String toolsName;
	/**测试工具标识*/
	@Excel(name = "测试工具标识", width = 15)
    @ApiModelProperty(value = "测试工具标识")
    private String toolsCode;
	/**测试工具位置*/
	@Excel(name = "测试工具位置", width = 15)
    @ApiModelProperty(value = "测试工具位置")
    private String toolsLocation;
	/**测试工具位置*/
//	@Excel(name = "测试工具客户端位置", width = 15)
//    @ApiModelProperty(value = "测试工具客户端位置")
//	private java.lang.String toolsClientLocation;
	/**测试工具类型*/
	@Excel(name = "测试工具类型", width = 15)
    @ApiModelProperty(value = "测试工具类型")
    private String toolsType;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
    /**是否需要监控，0:否 1:是*/
    @Excel(name = "是否需要监控", width = 15, dicCode = "yn")
    @Dict(dicCode = "yn")
    @ApiModelProperty(value = "是否需要监控")
    private Integer needMonitorOrNot;
    /**监控工具可执行文件ftp地址，多个地址逗号分隔*/
    @ApiModelProperty(value = "监控工具可执行文件ftp地址，多个地址逗号分隔")
    private String toolsFileFtpLocations;
    /**删除标记*/
    @Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
    /**测试工具进程名称*/
    @Excel(name = "测试工具进程名称", width = 15)
    @ApiModelProperty(value = "测试工具进程名称")
    private String toolProcessName;

    /**测试工具端口号*/
    @Excel(name = "测试工具端口号", width = 15)
    @ApiModelProperty(value = "测试工具端口号")
    private String toolsPort;

   /**测试工具linux进程名称*/
    @Excel(name = "测试工具linux进程名称", width = 15)
    @ApiModelProperty(value = "测试工具linux进程名称")
    private String toolLinuxProcessName;

    /**测试工具运行状态*/
  /*  @Excel(name = "测试工具运行状态", width = 15)
    @ApiModelProperty(value = "测试工具运行状态")
    private java.lang.String toolsRunStatus;*/
}
