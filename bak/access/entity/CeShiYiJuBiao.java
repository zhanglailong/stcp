package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试依据表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试依据表")
public class CeShiYiJuBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试依据")
    private String ceShiYiJu;

    @TableField("测试依据说明")
    private String ceShiYiJuShuoMing;

    @TableField("父节点ID")
    private String fuJieDianID;

    @TableField("章节号")
    private String zhangJieHao;

    @TableField("是否追踪")
    private String shiFouZhuiZong;

    @TableField("原因说明")
    private String yuanYinShuoMing;

    @TableField("使用者")
    private String shiYongZhe;

    @TableField("标识")
    private String biaoShi;

    @TableField("符合与否说明")
    private String fuHeYuFouShuoMing;

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
