package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试准备表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试准备表")
public class CeShiZhunBeiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试准备名称")
    private String ceShiZhunBeiMingCheng;

    @TableField("测试进度")
    private byte[] ceShiJinDu;

    @TableField("软件准备")
    private byte[] ruanJianZhunBei;

    @TableField("硬件准备")
    private byte[] yingJianZhunBei;

    @TableField("其它测试准备")
    private byte[] qiTaCeShiZhunBei;


}
