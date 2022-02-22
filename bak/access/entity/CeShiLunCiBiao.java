package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试轮次表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试轮次表")
public class CeShiLunCiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("轮次标识")
    private String lunCiBiaoShi;

    @TableField("序号")
    private Integer xuHao;

    @TableField("轮次名称")
    private String lunCiMingCheng;

    @TableField("轮次特点")
    private String lunCiTeDian;

    @TableField("轮次简要描述")
    private String lunCiJianYaoMiaoShu;

    @TableField("是否冻结")
    private String shiFouDongJie;

    @TableField("测试进度")
    private byte[] ceShiJinDu;

    @TableField("软件准备")
    private byte[] ruanJianZhunBei;

    @TableField("硬件准备")
    private byte[] yingJianZhunBei;

    @TableField("其它测试准备")
    private byte[] qiTaCeShiZhunBei;

    @TableField("设计人员")
    private String sheJiRenYuan;

    @TableField("预留字段3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    private String yuLiuZiDuan5;

    @TableField("轮次来源")
    private String lunCiLaiYuan;

    @TableField("系统标签")
    private String xiTongBiaoQian;

    @TableField("预留字段1")
    private byte[] yuLiuZiDuan1;

    @TableField("改进建议")
    private byte[] gaiJinJianYi;

    @TableField("预留字段2")
    private byte[] yuLiuZiDuan2;


}
