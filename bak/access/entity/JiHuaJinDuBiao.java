package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 计划进度表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("计划进度表")
public class JiHuaJinDuBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("预计开始时间")
    private Date yuJiKaiShiShiJian;

    @TableField("预计完成时间")
    private Date yuJiWanChengShiJian;

    @TableField("组织人员ID")
    private String zuZhiRenYuanID;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("工作内容说明")
    private String gongZuoNeiRongShuoMing;

    @TableField("备注")
    private String beiZhu;

    @TableField("活动名称")
    private String huoDongMingCheng;

    @TableField("工作产品")
    private String gongZuoChanPin;

    @TableField("工作量估计")
    private String gongZuoLiangGuJi;

    @TableField("预计工作量")
    private String yuJiGongZuoLiang;

    @TableField("实际工作量")
    private String shiJiGongZuoLiang;


}
