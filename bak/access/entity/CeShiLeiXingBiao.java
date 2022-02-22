package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试类型表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试类型表")
public class CeShiLeiXingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试类型ID")
    private String ceShiLeiXingID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试类型名称")
    private String ceShiLeiXingMingCheng;

    @TableField("简写码")
    private String jianXieMa;

    @TableField("所属被测对象ID")
    private String suoShuBeiCeDuiXiangID;

    @TableField("父测试类型ID")
    private String fuCeShiLeiXingID;

    @TableField("子节点类型")
    private Integer ziJieDianLeiXing;

    @TableField("预计工作时间")
    private Integer yuJiGongZuoShiJian;

    @TableField("总体要求")
    private byte[] zongTiYaoQiu;

    @TableField("前向ID")
    private String qianXiangID;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("设计人员")
    private String sheJiRenYuan;

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
