package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测试过程描述表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试过程描述表")
public class CeShiGuoChengMiaoShuBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试过程描述ID")
    private String ceShiGuoChengMiaoShuID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("过程名称")
    private String guoChengMingCheng;

    @TableField("开始时间")
    private Date kaiShiShiJian;

    @TableField("结束时间")
    private Date jieShuShiJian;

    @TableField("过程概述")
    private String guoChengGaiShu;


}
