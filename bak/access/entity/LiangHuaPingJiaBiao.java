package org.jeecg.modules.access.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 量化评价表
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("量化评价表")
public class LiangHuaPingJiaBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("ID")
    private String id;

    @TableField("已提供合格功能数")
    private String yiTiGongHeGeGongNengShu;

    @TableField("应提供合格功能数")
    private String yingTiGongHeGeGongNengShu;

    @TableField("功能正确实现率")
    private String gongNengZhengQueShiXianLv;

    @TableField("问题总数")
    private String wenTiZongShu;

    @TableField("代码行数")
    private String daiMaXingShu;

    @TableField("千行代码缺陷率")
    private String qianXingDaiMaQueXianLv;

    @TableField("实占内存空间")
    private String shiZhanNeiCunKongJian;

    @TableField("可用内存空间")
    private String keYongNeiCunKongJian;

    @TableField("内存效率")
    private String neiCunXiaoLv;

    @TableField("实际占用时间")
    private String shiJiZhanYongShiJian;

    @TableField("分配占用时间")
    private String fenPeiZhanYongShiJian;

    @TableField("时间效率")
    private String shiJianXiaoLv;

    @TableField("注释行总数")
    private String zhuShiXingZongShu;

    @TableField("代码总行数")
    private String daiMaZongXingShu;

    @TableField("程序注释率")
    private String chengXuZhuShiLv;

    @TableField("被测对象ID")
    private String beiCeDuiXiangID;

    @TableField("轮次ID")
    private String lunCiID;


}
