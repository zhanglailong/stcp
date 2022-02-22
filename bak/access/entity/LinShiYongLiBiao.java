package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 临时用例表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("临时用例表")
public class LinShiYongLiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("问题单ID")
    private String wenTiDanID;

    @TableField("测试用例名称")
    private String ceShiYongLiMingCheng;

    @TableField("测试用例标识")
    private String ceShiYongLiBiaoShi;

    @TableField("测试用例章节号")
    private String ceShiYongLiZhangJieHao;


}
