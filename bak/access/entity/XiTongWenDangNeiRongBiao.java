package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统文档内容表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("系统文档内容表")
public class XiTongWenDangNeiRongBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("文档内容ID")
    private String wenDangNeiRongID;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("内容标题")
    private String neiRongBiaoTi;

    @TableField("内容类型")
    private String neiRongLeiXing;

    @TableField("文档内容")
    private byte[] wenDangNeiRong;

    @TableField("文本内容")
    private String wenBenNeiRong;

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
