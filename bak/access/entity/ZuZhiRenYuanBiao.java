package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 组织人员表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("组织人员表")
public class ZuZhiRenYuanBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("姓名")
    private String xingMing;

    @TableField("登录名")
    private String dengLuMing;

    @TableField("职称")
    private String zhiCheng;

    @TableField("密码")
    private byte[] miMa;

    @TableField("登录备注")
    private String dengLuBeiZhu;

    @TableField("主要职责")
    private String zhuYaoZhiZe;

    @TableField("最近修改时间")
    private String zuiJinXiuGaiShiJian;

    @TableField("用户角色")
    private String yongHuJiaoSe;


}
