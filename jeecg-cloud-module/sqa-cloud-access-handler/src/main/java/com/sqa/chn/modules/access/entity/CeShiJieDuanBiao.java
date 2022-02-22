package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试阶段表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试阶段表")
public class CeShiJieDuanBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试阶段ID")
    private String ceShiJieDuanID;

    @TableField("测试阶段名称")
    private String ceShiJieDuanMingCheng;

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

    @TableField("序号")
    private Integer xuHao;


}
