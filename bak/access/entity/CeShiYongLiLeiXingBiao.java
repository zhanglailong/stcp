package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试用例类型表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试用例类型表")
public class CeShiYongLiLeiXingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试用例类型ID")
    private String ceShiYongLiLeiXingID;

    @TableField("测试用例类型名称")
    private String ceShiYongLiLeiXingMingCheng;

    @TableField("简写码")
    private String jianXieMa;

    @TableField("简要说明")
    private String jianYaoShuoMing;

    @TableField("重要性等级")
    private String zhongYaoXingDengJi;

    @TableField("序号")
    private Integer xuHao;

    @TableField("备注")
    private String beiZhu;

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
