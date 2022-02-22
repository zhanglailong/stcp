package org.jeecg.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description: eval_measure_weight
 * @Author: jeecg-boot
 * @Date: 2021-02-25
 * @Version: V1.0
 */
@Data
@TableName("eval_measure_weight")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "eval_measure_weight对象", description = "eval_measure_weight")
public class EvalMeasureWeight implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
/**@GeneratedValue(strategy = GenerationType.IDENTITY)*/
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 度量id（eval_measure_info表主键）
     */
    @Excel(name = "度量id（eval_measure_info表主键）", width = 15)
    @ApiModelProperty(value = "度量id（eval_measure_info表主键）")
    private String measureId;
    /**
     * 权重
     */
    @Excel(name = "权重", width = 15)
    @ApiModelProperty(value = "权重")
    private String weight;
    /**
     * 评价体系id（eval_system表主键）
     */
    @Excel(name = "评价体系id（eval_system表主键）", width = 15)
    @ApiModelProperty(value = "评价体系id（eval_system表主键）")
    private String systemId;
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
    private Date createTime;
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
    private Date updateTime;
}
