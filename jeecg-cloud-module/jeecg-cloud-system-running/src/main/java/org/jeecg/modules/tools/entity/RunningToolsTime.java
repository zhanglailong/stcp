package org.jeecg.modules.tools.entity;

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
 * @Description: 测试工具时段表
 * @Author: jeecg-boot
 * @Date:   2020-12-18
 * @Version: V1.0
 */
@Data
@TableName("running_tools_time")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_tools_time对象", description="测试工具时段表")
public class RunningToolsTime implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**测试工具id*/
	@Excel(name = "测试工具id", width = 15)
    @ApiModelProperty(value = "测试工具id")
    private String toolsListId;
	/**测试工具名称*/
	@Excel(name = "测试工具名称", width = 15)
    @ApiModelProperty(value = "测试工具名称")
    private String toolsListName;
	/**总数量*/
	@Excel(name = "总数量", width = 15)
    @ApiModelProperty(value = "总数量")
    private Integer toolsTimeNum;
	/**已用数量*/
	@Excel(name = "已用数量", width = 15)
    @ApiModelProperty(value = "已用数量")
    private Integer toolsTimeAvnum;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
