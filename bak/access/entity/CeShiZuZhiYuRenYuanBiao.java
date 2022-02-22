package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试组织与人员表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试组织与人员表")
public class CeShiZuZhiYuRenYuanBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("组织人员ID")
    private String zuZhiRenYuanID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("角色")
    private String jiaoSe;

    @TableField("职称")
    private String zhiCheng;

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

    @TableField("主要职责")
    private String zhuYaoZhiZe;

    @TableField("时间需求")
    private String shiJianXuQiu;

    @TableField("培训需求")
    private String peiXunXuQiu;

    @TableField("测试场所ID")
    private String ceShiChangSuoID;


}
