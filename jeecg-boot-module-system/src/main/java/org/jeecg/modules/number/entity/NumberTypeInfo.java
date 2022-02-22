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
@TableName("number_type_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="number_type_info对象", description="编号信息表")
public class NumberTypeInfo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**typeId*/
	@Excel(name = "typeId", width = 15)
    @ApiModelProperty(value = "typeId")
    private String typeId;
	/**sort*/
	@Excel(name = "sort", width = 15)
    @ApiModelProperty(value = "sort")
    private Integer sort;
	/**tenantId*/
	@Excel(name = "tenantId", width = 15)
    @ApiModelProperty(value = "tenantId")
    private String tenantId;
	/**ruleId*/
	@Excel(name = "ruleId", width = 15)
    @ApiModelProperty(value = "ruleId")
    private String ruleId;
}
