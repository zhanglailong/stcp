package org.jeecg.modules.number.entity;

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
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Data
@TableName("number_rule_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="number_rule_info对象", description="编号信息表")
public class NumberRuleInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**ruleType*/
	@Excel(name = "ruleType", width = 15)
    @ApiModelProperty(value = "ruleType")
    private String ruleType;
	/**typeInfoId*/
	@Excel(name = "typeInfoId", width = 15)
    @ApiModelProperty(value = "typeInfoId")
    private String typeInfoId;
	/**sort*/
	@Excel(name = "sort", width = 15)
    @ApiModelProperty(value = "sort")
    private Integer sort;
	/**name*/
	@Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private String name;
	/**value*/
	@Excel(name = "value", width = 15)
    @ApiModelProperty(value = "value")
    private String value;
	/**tenantId*/
	@Excel(name = "tenantId", width = 15)
    @ApiModelProperty(value = "tenantId")
    private String tenantId;
	/**startValue*/
	@Excel(name = "startValue", width = 15)
    @ApiModelProperty(value = "startValue")
    private String startValue;
	/**endValue*/
	@Excel(name = "endValue", width = 15)
    @ApiModelProperty(value = "endValue")
    private String endValue;
	/**digits*/
	@Excel(name = "digits", width = 15)
    @ApiModelProperty(value = "digits")
    private String digits;
	/**zizeng*/
	@Excel(name = "zizeng", width = 15)
    @ApiModelProperty(value = "zizeng")
    private String zizeng;
	/**nowNumber*/
	@Excel(name = "nowNumber", width = 15)
    @ApiModelProperty(value = "nowNumber")
    private String nowNumber;
	/**dateFormat*/
	@Excel(name = "dateFormat", width = 15)
    @ApiModelProperty(value = "dateFormat")
    private String dateFormat;
	/**abbreviation*/
	@Excel(name = "abbreviation", width = 15)
    @ApiModelProperty(value = "abbreviation")
    private String abbreviation;
    /**创建时间*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
