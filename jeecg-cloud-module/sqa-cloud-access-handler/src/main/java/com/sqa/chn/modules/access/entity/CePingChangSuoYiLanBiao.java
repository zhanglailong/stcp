package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 测评场所一览表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测评场所一览表")
public class CePingChangSuoYiLanBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测评场所名称")
    private String cePingChangSuoMingCheng;

    @TableField("配置和符合性说明")
    private String peiZhiHeFuHeXingShuoMing;

    @TableField("主要用途")
    private String zhuYaoYongTu;

    @TableField("轮次ID")
    private String lunCiID;


}
