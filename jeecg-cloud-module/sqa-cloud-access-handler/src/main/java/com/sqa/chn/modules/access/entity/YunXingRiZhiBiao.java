package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 运行日志表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("运行日志表")
public class YunXingRiZhiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Integer id;

    @TableField("登录人员")
    private String dengLuRenYuan;

    @TableField("登录时间")
    private String dengLuShiJian;

    @TableField("登录IP")
    private String dengLuIP;

    @TableField("登录状态")
    private String dengLuZhuangTai;

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
