package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试激励类型表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试激励类型表")
public class CeShiJiLiLeiXingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试激励类型名称")
    private String ceShiJiLiLeiXingMingCheng;

    @TableField("说明")
    private String shuoMing;

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
