package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 回归测试影响域
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("回归测试影响域")
public class HuiGuiCeShiYingXiangYu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("初次分析标志")
    private String chuCiFenXiBiaoZhi;

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

    @TableField("测试问题涉及的测试用例")
    private String ceShiWenTiSheJiDeCeShiYongLi;

    @TableField("涉及到的测试内容")
    private String sheJiDaoDeCeShiNeiRong;

    @TableField("涉及到的测试内容种类")
    private String sheJiDaoDeCeShiNeiRongZhongLei;

    @TableField("快捷方式的父节点")
    private String kuaiJieFangShiDeFuJieDian;

    @TableField("更动项")
    private String gengDongXiang;

    @TableField("更动报告单号")
    private String gengDongBaoGaoDanHao;

    @TableField("软件更动单ID")
    private String ruanJianGengDongDanID;


}
