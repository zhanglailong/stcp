package org.jeecg.modules.access.entity;

import cn.icesun.util.entityutil.annotation.FieldSync;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jeecg.modules.task.entity.RunningCaseStep;
import org.jeecg.modules.task.entity.RunningQuestion;

/**
 * <p>
 * 问题报告单
 * </p>
 *  //TODO 基本完成
 * case_id 测试用例主键
 * question_code 主键
 * question_version 没法用
 * question_number 暂时不用
 * opinion  无
 * del_flag
 * create_by
 * create_time
 * update_by
 * update_time
 * sys_org_code
 * status 映射
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("问题报告单")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class WenTiBaoGaoDan implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("问题ID")
    //@FieldSync(fieldClazz = RunningQuestion.class, fieldName = "id")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "questionCode")
    private String wenTiID;

    @TableField("问题名称")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionName")
    private String wenTiMingCheng;

    @TableField("问题位置")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionLocation")
    private String wenTiWeiZhi;

    @TableField("问题状态")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionState")
    private String wenTiZhuangTai;

    @TableField("序号")
    private Integer xuHao;

    @TableField("发现日期")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "discoverTime")
    private Date faXianRiQi;

    @TableField("报告日期")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reportDate")
    private Date baoGaoRiQi;

    @TableField("报告人")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reporter")
    private String baoGaoRen;

    @TableField("程序文档名称")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "programFileName")
    private String chengXuWenDangMingCheng;

    @TableField("问题类别")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionType")
    private String wenTiLeiBie;

    @TableField("问题级别")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionLevel")
    private String wenTiJiBie;

    @TableField("问题追踪")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionTrack")
    private String wenTiZhuiZong;

    @TableField("问题描述")
    //二进制部分内容未知, 暂时不进行同步
    private byte[] wenTiMiaoShu;

    @TableField("附注及修改建议")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "annotationsModify")
    private String fuZhuJiXiuGaiJianYi;

    @TableField("一级标识")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "oneLevelIdentification")
    private String yiJiBiaoShi;

    @TableField("二级标识")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "twoLevelIdentification")
    private String erJiBiaoShi;

    @TableField("三级标识")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "threeLevelIdentification")
    private String sanJiBiaoShi;

    @TableField("四级标识")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "fourLevelIdentification")
    private String siJiBiaoShi;

    @TableField("同标识序号")
    private Integer tongBiaoShiXuHao;

    @TableField("所属被测对象")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "uutCode")
    private String suoShuBeiCeDuiXiang;

    @TableField("审核人")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reviewer")
    private String shenHeRen;

    @TableField("审核结论")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reviewResult")
    private String shenHeJieLun;

    @TableField("审核时间")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reviewTime")
    private String shenHeShiJian;

    @TableField("审核状态")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reviewState")
    private String shenHeZhuangTai;

    @TableField("审核备注")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reviewRemark")
    private String shenHeBeiZhu;

    @TableField("轮次ID")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "turnId")
    private String lunCiID;

    @TableField("备用1")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserve1")
    private String beiYong1;

    @TableField("备用2")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserve2")
    private String beiYong2;

    @TableField("预留字段1")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField5")
    private String yuLiuZiDuan5;

    @TableField("问题概要描述")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionDescription")
    private String wenTiGaiYaoMiaoShu;

    @TableField("问题处置情况")
    //TODO 未重置/未处置/未处置。
    private String wenTiChuZhiQingKuang;

    @TableField("新老状态")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "newOldState")
    private String xinLaoZhuangTai;

    @TableField("运行载体")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "operationCarrier")
    private String yunXingZaiTi;

    @TableField("具体失效形式")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "specificFailureForm")
    private String juTiShiXiaoXingShi;

    @TableField("预留字段6")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField6")
    private String yuLiuZiDuan6;

    @TableField("预留字段7")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "reserveField7")
    private String yuLiuZiDuan7;

    @TableField("备注")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "remark")
    private String beiZhu;

    @TableField("问题形式")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionForm")
    private String wenTiXingShi;

    @TableField("测试阶段")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "testStage")
    private String ceShiJieDuan;

    @TableField("问题子类别")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "questionSubtype")
    private String wenTiZiLeiBie;

    @TableField("处置人员")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "handlePerson")
    private String chuZhiRenYuan;

    @TableField("审批领导")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "approveLeader")
    private String shenPiLingDao;

    @TableField("处置日期")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "handleTime")
    private Date chuZhiRiQi;

    @TableField("审批日期")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "approveTime")
    private Date shenPiRiQi;

    @TableField("修改状态")
    @FieldSync(fieldClazz = RunningQuestion.class, fieldName = "modifyState")
    private String xiuGaiZhuangTai;


}
