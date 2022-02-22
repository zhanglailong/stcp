package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试资源配置表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试资源配置表")
public class CeShiZiYuanPeiZhiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("名称")
    private String mingCheng;

    @TableField("用途")
    private String yongTu;

    @TableField("数量")
    private String shuLiang;

    @TableField("说明")
    private String shuoMing;

    @TableField("组织人员ID")
    private String zuZhiRenYuanID;

    @TableField("配置")
    private String peiZhi;

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

    @TableField("资源类型")
    private Integer ziYuanLeiXing;

    @TableField("所有者的特性")
    private String suoYouZheDeTeXing;

    @TableField("提交时间或备注说明")
    private String tiJiaoShiJianHuoBeiZhuShuoMing;

    @TableField("需方权利")
    private String xuFangQuanLi;

    @TableField("许可证")
    private String xuKeZheng;

    @TableField("差异说明")
    private String chaYiShuoMing;

    @TableField("差异影响分析")
    private String chaYiYingXiangFenXi;

    @TableField("测试场所ID")
    private String ceShiChangSuoID;

    @TableField("预留字段6")
    private String yuLiuZiDuan6;


}
