package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试环境准备与被测软件安装计划表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试环境准备与被测软件安装计划表")
public class CeShiHuanJingZhunBeiYuBeiCeRuanJianAnZhuangJiHuaBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试环境准备与被测软件安装计划表ID")
    private String ceShiHuanJingZhunBeiYuBeiCeRuanJianAnZhuangJiHuaBiaoID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("工作项目")
    private String gongZuoXiangMu;

    @TableField("计划开始时间")
    private Date jiHuaKaiShiShiJian;

    @TableField("计划完成时间")
    private Date jiHuaWanChengShiJian;

    @TableField("责任人")
    private String zeRenRen;

    @TableField("备注")
    private String beiZhu;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("测试场所ID")
    private String ceShiChangSuoID;


}
