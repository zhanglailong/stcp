package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试用例与测试项关系表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试用例与测试项关系表")
public class CeShiYongLiYuCeShiXiangGuanXiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试用例ID")
    private String ceShiYongLiID;

    @TableField("测试项ID")
    private String ceShiXiangID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("直接所属标志")
    private String zhiJieSuoShuBiaoZhi;

    @TableField("旧标识")
    private String jiuBiaoShi;

    @TableField("轮次ID")
    private String lunCiID;

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

    @TableField("覆盖的测试要求")
    private String fuGaiDeCeShiYaoQiu;

    @TableField("覆盖的测试子项")
    private String fuGaiDeCeShiZiXiang;


}
