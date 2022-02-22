package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 质量特性
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("质量特性")
public class ZhiLiangTeXing implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("质量特性ID")
    private String zhiLiangTeXingID;

    @TableField("特性名称")
    private String teXingMingCheng;

    @TableField("简写码")
    private String jianXieMa;

    @TableField("简要说明")
    private String jianYaoShuoMing;

    @TableField("重要性等级")
    private String zhongYaoXingDengJi;

    @TableField("采用技术")
    private String caiYongJiShu;


}
