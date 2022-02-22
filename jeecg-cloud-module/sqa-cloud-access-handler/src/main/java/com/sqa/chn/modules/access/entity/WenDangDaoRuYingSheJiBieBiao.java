package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文档导入映射级别表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("文档导入映射级别表")
public class WenDangDaoRuYingSheJiBieBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("序号")
    private Integer xuHao;

    @TableField("标题级别")
    private Integer biaoTiJiBie;

    @TableField("操作对象")
    private String caoZuoDuiXiang;

    @TableField("备注")
    private String beiZhu;


}
