package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 参与组织
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("参与组织")
public class CanYuZuZhi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("参与组织ID")
    private String canYuZuZhiID;

    @TableField("序号")
    private Integer xuHao;

    @TableField("参与组织的名称")
    private String canYuZuZhiDeMingCheng;

    @TableField("角色与职责")
    private String jiaoSeYuZhiZe;

    @TableField("备注")
    private String beiZhu;

    @TableField("轮次ID")
    private String lunCiID;

    @TableField("测试场所ID")
    private String ceShiChangSuoID;


}
