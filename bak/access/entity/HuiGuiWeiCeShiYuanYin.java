package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 回归_未测试原因
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("回归_未测试原因")
public class HuiGuiWeiCeShiYuanYin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("测试项ID")
    private String ceShiXiangID;

    @TableField("测试项名称")
    private String ceShiXiangMingCheng;

    @TableField("测试用例ID")
    private String ceShiYongLiID;

    @TableField("测试用例名称")
    private String ceShiYongLiMingCheng;

    @TableField("测试用例标识")
    private String ceShiYongLiBiaoShi;

    @TableField("未测试原因")
    private String weiCeShiYuanYin;


}
