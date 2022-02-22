package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 项目附加信息表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("项目附加信息表")
public class XiangMuFuJiaXinXiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private Integer id;

    @TableField("备用1")
    private String beiYong1;

    @TableField("备用2")
    private String beiYong2;

    @TableField("备用3")
    private String beiYong3;

    @TableField("备用4")
    private String beiYong4;

    @TableField("备用5")
    private String beiYong5;

    @TableField("备用6")
    private String beiYong6;

    @TableField("备用7")
    private String beiYong7;

    @TableField("备用8")
    private String beiYong8;

    @TableField("备用9")
    private String beiYong9;

    @TableField("备用10")
    private String beiYong10;


}
