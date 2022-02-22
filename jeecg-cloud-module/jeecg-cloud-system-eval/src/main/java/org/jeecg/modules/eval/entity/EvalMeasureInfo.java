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
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @Description: 评价体系度量信息表
 * @Author: jeecg-boot
 * @Date: 2021-02-26
 * @Version: V1.0
 */
@Data
@TableName("eval_measure_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "eval_measure_info对象", description = "评价体系度量信息表")
public class EvalMeasureInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * eval_measure_structure表id
     */
    @ApiModelProperty(value = "sid")
    private String sid;
    /**
     * 度量名称
     */
    @Excel(name = "度量名称", width = 15)
    @ApiModelProperty(value = "度量名称")
    private String name;
    /**
     * 质量子特性
     */
    @Excel(name = "质量子特性", width = 15)
    @ApiModelProperty(value = "质量子特性")
    @Dict(dicCode = "id",dictTable="eval_master",dicText="attribute_name")
    private String parentId;
    /**
     * 质量特性
     */
    @Excel(name = "质量特性", width = 15)
    @ApiModelProperty(value = "质量特性")
    @Dict(dicCode = "id",dictTable="eval_master",dicText="attribute_name")
    private String grandId;
    /**
     * 是否必选 1 必选 2 有条件必选 3 可选
     */
    @Excel(name = "是否必选 1 必选 2 有条件必选 3 可选", width = 15)
    @ApiModelProperty(value = "是否必选 1 必选 2 有条件必选 3 可选")
    private String selected;
    /**
     * 度量等级 1质量特性 2质量子特性 3度量元
     */
    @Excel(name = "度量等级 1质量特性 2质量子特性 3度量元", width = 15)
    @ApiModelProperty(value = "度量等级 1质量特性 2质量子特性 3度量元")
    private String level;
    /**
     * 度量元计算公式
     */
    @Excel(name = "度量元计算公式", width = 15)
    @ApiModelProperty(value = "度量元计算公式")
    @Dict(dicCode = "id",dictTable="eval_formula",dicText="formula")
    private String formula;
    /**
     * 绑定评价体系id（eval_system主键）
     */
    @Excel(name = "绑定评价体系id（eval_system主键）", width = 15)
    @ApiModelProperty(value = "绑定评价体系id（eval_system主键）")
    private String systemId;
    /**
     * 评价方法
     */
    @Excel(name = "评价方法", width = 15)
    @ApiModelProperty(value = "评价方法")
    @Dict(dicCode = "id",dictTable="eval_method",dicText="name")
    private String methodId;
    /**
     * 目标值
     */
    @Excel(name = "目标值", width = 15)
    @ApiModelProperty(value = "目标值")
    private String targetValue;
    /**
     * 阀值
     */
    @Excel(name = "阀值", width = 15)
    @ApiModelProperty(value = "阀值")
    private String threshold;
    /**
     * 是否选中  0选中  1未选择
     */
    @Excel(name = "是否选中  0选中  1未选择", width = 15)
    @ApiModelProperty(value = "是否选中  0选中  1未选择")
    private String checked;
    /**
     * 是否是内置 0内置  1自定义
     */
    @Excel(name = "是否是内置 0内置  1自定义", width = 15)
    @ApiModelProperty(value = "是否是内置 0内置  1自定义")
    private String buildIn;
    /**
     * 是否需要打分
     */
    @Excel(name = "是否需要打分 ", width = 15)
    @ApiModelProperty(value = "是否需要打分 ")
    private String needFlag;
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
}
