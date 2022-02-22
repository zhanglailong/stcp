package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 附件
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("附件")
public class FuJian implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("附件ID")
    private String fuJianID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("附件名称")
    private String fuJianMingCheng;

    @TableField("附件内容")
    private byte[] fuJianNeiRong;

    @TableField("附件类型")
    private String fuJianLeiXing;

    @TableField("备注")
    private String beiZhu;

    @TableField("对应原文件路径")
    private String duiYingYuanWenJianLuJing;

    @TableField("关联数")
    private Integer guanLianShu;

    @TableField("输出与否")
    private String shuChuYuFou;

    @TableField("轮次ID")
    private String lunCiID;


}
