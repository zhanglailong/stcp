package org.jeecg.modules.testtooldistribute.entity;

import java.io.Serializable;
import java.util.Date;
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
 * 2021/07/02
 * @author yeyl
 */
@Data
@TableName("tepm_test_tool_distribute")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_tool_distribute对象", description="测试工具分发")
public class TestToolDistribute implements Serializable {
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
	/**环境id*/
	@Excel(name = "环境id", width = 15)
    @ApiModelProperty(value = "环境id")
    private String envirId;
	/**环境名称*/
	@Excel(name = "环境名称", width = 15)
    @ApiModelProperty(value = "环境名称")
    private String envirName;
	/**虚拟机Id*/
	@Excel(name = "虚拟机Id", width = 15)
    @ApiModelProperty(value = "虚拟机Id")
    private String vmId;
	/**虚拟机名称*/
	@Excel(name = "虚拟机名称", width = 15)
    @ApiModelProperty(value = "虚拟机名称")
    private String vmName;
	/**虚拟机Ip*/
	@Excel(name = "虚拟机Ip", width = 15)
    @ApiModelProperty(value = "虚拟机Ip")
    private String vmIp;
	/**测试工具Id*/
	@Excel(name = "测试工具Id", width = 15)
    @ApiModelProperty(value = "测试工具Id")
    private String testToolId;
	/**测试工具名称*/
	@Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private String testToolName;
    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private Integer idel;

    /**
     * 分发状态
     */
    @Excel(name = "分发状态", width = 15)
    @ApiModelProperty(value = "分发状态")
    private Integer distributeState;
}
