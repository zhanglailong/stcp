package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 术语表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("术语表")
public class ShuYuBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("术语和缩略语名")
    private String shuYuHeSuoLveYuMing;

    @TableField("确切定义")
    private String queQieDingYi;

    @TableField("轮次ID")
    private String lunCiID;


}
