package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 引用文件表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("引用文件表")
public class YinYongWenJianBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("序号")
    private Integer xuHao;

    @TableField("文档名称")
    private String wenDangMingCheng;

    @TableField("引用文件文档号")
    private String yinYongWenJianWenDangHao;

    @TableField("引用文件标题")
    private String yinYongWenJianBiaoTi;

    @TableField("编写单位及作者")
    private String bianXieDanWeiJiZuoZhe;

    @TableField("出版日期")
    private Date chuBanRiQi;

    @TableField("轮次ID")
    private String lunCiID;

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

    @TableField("接收日期")
    private Date jieShouRiQi;

    @TableField("备注")
    private String beiZhu;


}
