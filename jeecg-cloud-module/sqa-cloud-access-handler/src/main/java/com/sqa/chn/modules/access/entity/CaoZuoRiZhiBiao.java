package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("操作日志表")
public class CaoZuoRiZhiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Integer id;

    @TableField("事件名")
    private String shiJianMing;

    @TableField("发生时间")
    private String faShengShiJian;

    @TableField("所操作机器IP")
    private String suoCaoZuoJiQiIP;

    @TableField("事件类型")
    private String shiJianLeiXing;

    @TableField("事件主题")
    private String shiJianZhuTi;

    @TableField("事件结果")
    private String shiJianJieGuo;

    @TableField("操作人员")
    private String caoZuoRenYuan;


}
