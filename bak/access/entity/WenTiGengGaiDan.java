package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题更改单
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("问题更改单")
public class WenTiGengGaiDan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("问题ID")
    private String wenTiID;

    @TableField("解决人")
    private String jieJueRen;

    @TableField("解决时间")
    private Date jieJueShiJian;

    @TableField("问题确认")
    private String wenTiQueRen;

    @TableField("成因分析")
    private String chengYinFenXi;

    @TableField("更改方案")
    private String gengGaiFangAn;

    @TableField("更改前")
    private String gengGaiQian;

    @TableField("更改后")
    private String gengGaiHou;

    @TableField("更改影响域分析")
    private String gengGaiYingXiangYuFenXi;

    @TableField("不更改的原因")
    private String buGengGaiDeYuanYin;

    @TableField("解决意见备注")
    private String jieJueYiJianBeiZhu;

    @TableField("解决意见")
    private String jieJueYiJian;

    @TableField("确认人")
    private String queRenRen;

    @TableField("确认时间")
    private Date queRenShiJian;

    @TableField("更改前版本")
    private String gengGaiQianBanBen;

    @TableField("更改后版本")
    private String gengGaiHouBanBen;

    @TableField("问题的解决状态")
    private String wenTiDeJieJueZhuangTai;

    @TableField("解决状态备注")
    private String jieJueZhuangTaiBeiZhu;

    @TableField("验证人")
    private String yanZhengRen;

    @TableField("验证时间")
    private Date yanZhengShiJian;

    @TableField("所属被测对象")
    private String suoShuBeiCeDuiXiang;

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


}
