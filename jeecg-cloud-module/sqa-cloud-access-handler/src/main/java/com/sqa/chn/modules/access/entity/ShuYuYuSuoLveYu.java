package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 术语与缩略语
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("术语与缩略语")
public class ShuYuYuSuoLveYu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("术语和缩略语名")
    private String shuYuHeSuoLveYuMing;

    @TableField("摘要说明")
    private String zhaiYaoShuoMing;

    @TableField("备注")
    private String beiZhu;

    @TableField("序号")
    private Integer xuHao;


}
