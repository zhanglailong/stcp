package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试过程附件
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试过程附件")
public class CeShiGuoChengFuJian implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试用例ID")
    private String ceShiYongLiID;

    @TableField("测试过程序号")
    private Integer ceShiGuoChengXuHao;

    @TableField("附件ID")
    private String fuJianID;

    @TableField("附件所属")
    private String fuJianSuoShu;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试过程附件ID")
    private String ceShiGuoChengFuJianID;

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
