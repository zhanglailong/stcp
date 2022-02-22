package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 被测对象表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("被测对象表")
public class BeiCeDuiXiangBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("被测对象ID")
    private String beiCeDuiXiangID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("被测对象名称")
    private String beiCeDuiXiangMingCheng;

    @TableField("简写码")
    private String jianXieMa;

    @TableField("规模")
    private String guiMo;

    @TableField("编程语言")
    private String bianChengYuYan;

    @TableField("开发环境")
    private String kaiFaHuanJing;

    @TableField("重要程度")
    private String zhongYaoChengDu;

    @TableField("研制单位")
    private String yanZhiDanWei;

    @TableField("测试开始日期")
    private Date ceShiKaiShiRiQi;

    @TableField("测试结束日期")
    private Date ceShiJieShuRiQi;

    @TableField("测试执行情况")
    private String ceShiZhiXingQingKuang;

    @TableField("测试执行情况_补充")
    private byte[] ceShiZhiXingQingKuangBuChong;

    @TableField("质量评估")
    private byte[] zhiLiangPingGu;

    @TableField("改进建议")
    private byte[] gaiJinJianYi;

    @TableField("测试进度")
    private byte[] ceShiJinDu;

    @TableField("硬件准备")
    private byte[] yingJianZhunBei;

    @TableField("软件准备")
    private byte[] ruanJianZhunBei;

    @TableField("其它测试准备")
    private byte[] qiTaCeShiZhunBei;

    @TableField("版本")
    private String banBen;

    @TableField("测试级别")
    private String ceShiJiBie;

    @TableField("预计工作时间")
    private Date yuJiGongZuoShiJian;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("前向ID")
    private String qianXiangID;

    @TableField("设计人员")
    private String sheJiRenYuan;

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
