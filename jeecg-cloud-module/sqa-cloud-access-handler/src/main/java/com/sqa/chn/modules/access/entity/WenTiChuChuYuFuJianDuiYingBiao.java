package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 问题出处与附件对应表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("问题出处与附件对应表")
public class WenTiChuChuYuFuJianDuiYingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("问题出处附件ID")
    private String wenTiChuChuFuJianID;

    @TableField("问题出处ID")
    private String wenTiChuChuID;


}
