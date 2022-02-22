package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 引用文档表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("引用文档表")
public class YinYongWenDangBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("文档标识")
    private String wenDangBiaoShi;

    @TableField("发布日期")
    private Date faBuRiQi;

    @TableField("发布单位")
    private String faBuDanWei;

    @TableField("备注")
    private String beiZhu;

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

    @TableField("接收日期")
    private Date jieShouRiQi;

    @TableField("序号")
    private Integer xuHao;


}
