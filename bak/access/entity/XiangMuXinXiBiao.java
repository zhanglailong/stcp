package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 项目信息表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("项目信息表")
public class XiangMuXinXiBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("库版本")
    private String kuBanBen;

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

    @TableField("库密码")
    private byte[] kuMiMa;

    @TableField("测试结果总结")
    private byte[] ceShiJieGuoZongJie;

    @TableField("改进建议")
    private byte[] gaiJinJianYi;

    @TableField("指标测试结果")
    private byte[] zhiBiaoCeShiJieGuo;


}
