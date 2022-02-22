package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试用例与设计方法对应表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试用例与设计方法对应表")
public class CeShiYongLiYuSheJiFangFaDuiYingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("测试用例ID")
    private String ceShiYongLiID;

    @TableField("设计方法ID")
    private String sheJiFangFaID;

    @TableField("轮次ID")
    private String lunCiID;


}
