package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 被测对象与测试依据关系表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("被测对象与测试依据关系表")
public class BeiCeDuiXiangYuCeShiYiJuGuanXiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("被测对象ID")
    private String beiCeDuiXiangID;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("测试依据ID")
    private String ceShiYiJuID;

    @TableField("符合与否说明")
    private String fuHeYuFouShuoMing;

    @TableField("依据符合与否")
    private String yiJuFuHeYuFou;


}
