package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文档修改页
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("文档修改页")
public class WenDangXiuGaiYe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("序号")
    private Integer xuHao;

    @TableField("版本号")
    private String banBenHao;

    @TableField("日期")
    private String riQi;

    @TableField("所修改章节")
    private String suoXiuGaiZhangJie;

    @TableField("所修改页")
    private String suoXiuGaiYe;

    @TableField("备注")
    private String beiZhu;

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


}
