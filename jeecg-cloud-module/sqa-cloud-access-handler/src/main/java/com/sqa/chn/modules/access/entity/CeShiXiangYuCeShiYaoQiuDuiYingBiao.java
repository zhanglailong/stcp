package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试项与测试要求对应表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试项与测试要求对应表")
public class CeShiXiangYuCeShiYaoQiuDuiYingBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("测试要求")
    private String ceShiYaoQiu;

    @TableField("测试项ID")
    private String ceShiXiangID;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("序号")
    private Integer xuHao;


}
