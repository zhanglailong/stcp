package org.jeecg.modules.cloudtools.runex.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 设置工具可用参数
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Data
@TableName("run_params_set")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="xrun_params_set对象", description="设置工具可用参数")
public class RunParamsSet implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**类型（1勾选、2选择、3手填）*/
	@Excel(name = "类型（1勾选、2选择、3手填）", width = 15)
    @ApiModelProperty(value = "类型（1勾选、2选择、3手填）")
    private Integer type;
	/**参数*/
	@Excel(name = "参数", width = 15)
    @ApiModelProperty(value = "参数")
    private String param;
	/**参数描述*/
	@Excel(name = "参数描述", width = 15)
    @ApiModelProperty(value = "参数描述")
    private String des;
	/**参数值(数据字典)*/
	@Excel(name = "参数值(数据字典)", width = 15)
    @ApiModelProperty(value = "参数值(数据字典)")
    private String value;
	/**正则表达式*/
	@Excel(name = "正则表达式", width = 15)
    @ApiModelProperty(value = "正则表达式")
    private String pattern;
	
	private String charge;
	@TableField("`order`")
	private Integer orders;
	@TableField(exist = false)
	private double helpOrder;
	
	private String cola;
	private String colb;
	private String colc;
	
	private Date createTime;
	
	private String controlSetId;

	private String ChargeLimit;

	@TableField(exist = false)
	private List<RunParamsSet> children;
	@TableField(exist = false)
	private String key;

}
