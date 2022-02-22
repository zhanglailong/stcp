package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 回归测试测试选项添加原因表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("回归测试测试选项添加原因表")
public class HuiGuiCeShiCeShiXuanXiangTianJiaYuanYinBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("测试ID")
    private String ceShiID;

    @TableField("测试类型")
    private String ceShiLeiXing;

    @TableField("添加原因")
    private String tianJiaYuanYin;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("更动项ID")
    private String gengDongXiangID;

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
