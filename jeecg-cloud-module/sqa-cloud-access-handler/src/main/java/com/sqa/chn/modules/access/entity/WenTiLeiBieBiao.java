package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题类别表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("问题类别表")
public class WenTiLeiBieBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("问题类别ID")
    private String wenTiLeiBieID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("说明")
    private String shuoMing;

    @TableField("预留字段1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    private String yuLiuZiDuan5;

    @TableField("标识")
    private String biaoShi;

    @TableField("父节点ID")
    private String fuJieDianID;

    @TableField("问题类别名称")
    private String wenTiLeiBieMingCheng;


}
