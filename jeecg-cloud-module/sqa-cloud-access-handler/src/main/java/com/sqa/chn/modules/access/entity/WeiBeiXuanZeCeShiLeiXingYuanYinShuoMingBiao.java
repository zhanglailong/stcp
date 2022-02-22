package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 未被选择测试类型原因说明表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("未被选择测试类型原因说明表")
public class WeiBeiXuanZeCeShiLeiXingYuanYinShuoMingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("序号")
    private String xuHao;

    @TableField("被测对象名称")
    private String beiCeDuiXiangMingCheng;

    @TableField("测试类型ID")
    private String ceShiLeiXingID;

    @TableField("原因说明")
    private String yuanYinShuoMing;


}
