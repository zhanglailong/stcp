package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试级别定义
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试级别定义")
public class CeShiJiBieDingYi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试级别定义ID")
    private String ceShiJiBieDingYiID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试级别名称")
    private String ceShiJiBieMingCheng;

    @TableField("测试级别说明")
    private String ceShiJiBieShuoMing;

    @TableField("备注说明")
    private String beiZhuShuoMing;

    @TableField("轮次ID")
    private String lunCiID;


}
