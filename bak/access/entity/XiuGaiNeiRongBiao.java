package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 修改内容表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("修改内容表")
public class XiuGaiNeiRongBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("修改项ID")
    private String xiuGaiXiangID;

    @TableField("修改项内容")
    private String xiuGaiXiangNeiRong;

    @TableField("类别")
    private Integer leiBie;

    @TableField("比较数据库")
    private String biJiaoShuJuKu;

    @TableField("预留字段1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    private String yuLiuZiDuan5;


}
