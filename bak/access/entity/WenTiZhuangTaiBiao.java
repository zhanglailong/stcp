package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题状态表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("问题状态表")
public class WenTiZhuangTaiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("问题状态ID")
    private String wenTiZhuangTaiID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("问题状态内部属性")
    private String wenTiZhuangTaiNeiBuShuXing;

    @TableField("问题状态名称")
    private String wenTiZhuangTaiMingCheng;

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


}
