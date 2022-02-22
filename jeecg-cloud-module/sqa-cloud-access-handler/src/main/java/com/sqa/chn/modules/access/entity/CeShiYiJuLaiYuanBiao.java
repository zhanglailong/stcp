package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试依据来源表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试依据来源表")
public class CeShiYiJuLaiYuanBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试依据来源表ID")
    private String ceShiYiJuLaiYuanBiaoID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("标识")
    private String biaoShi;

    @TableField("版本")
    private String banBen;

    @TableField("编写日期")
    private Date bianXieRiQi;

    @TableField("接收日期")
    private Date jieShouRiQi;

    @TableField("编制单位")
    private String bianZhiDanWei;

    @TableField("备注")
    private String beiZhu;

    @TableField("预留字段1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    private String yuLiuZiDuan3;


}
