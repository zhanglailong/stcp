package org.jeecg.modules.eval.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: eval_method_info
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@ApiModel(value = "eval_method对象", description = "eval_method")
@Data
@TableName("eval_method_info")
public class EvalMethodInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 评价方法id（eval_method表主键）
     */
    @ApiModelProperty(value = "评价方法id（eval_method表主键）")
    private String methodId;
    /**
     * 区间最小值
     */
    @Excel(name = "区间最小值", width = 15)
    @ApiModelProperty(value = "区间最小值")
    private String minValue;
    /**
     * 区间最大值
     */
    @Excel(name = "区间最大值", width = 15)
    @ApiModelProperty(value = "区间最大值")
    private String maxValue;
    /**
     * 得分
     */
    @Excel(name = "得分", width = 15)
    @ApiModelProperty(value = "得分")
    private String score;
    /**
     * 评价等级
     */
    @Excel(name = "评价等级", width = 15)
    @ApiModelProperty(value = "评价等级")
    private String level;
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
