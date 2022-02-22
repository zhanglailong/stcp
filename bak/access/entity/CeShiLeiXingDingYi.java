package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试类型定义
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试类型定义")
public class CeShiLeiXingDingYi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试类型定义ID")
    private String ceShiLeiXingDingYiID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试类型名称")
    private String ceShiLeiXingMingCheng;

    @TableField("缩略符")
    private String suoLveFu;

    @TableField("测试类型说明")
    private String ceShiLeiXingShuoMing;

    @TableField("备注说明")
    private String beiZhuShuoMing;

    @TableField("轮次ID")
    private String lunCiID;


}
