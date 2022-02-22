package com.sqa.chn.modules.access.entity;

import cn.icesun.util.entityutil.annotation.FieldSync;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.sqa.chn.modules.task.entity.RunningCaseStep;

/**
 * <p>
 * 测试过程
 * </p>
 *  //TODO 基本完成
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试过程")
public class CeShiGuoCheng implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试过程ID")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "id")
    private String ceShiGuoChengID;

    @TableField("测试用例ID")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "caseId")
    private String ceShiYongLiID;

    @TableField("测试激励类型ID")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "impelTypeId")
    private String ceShiJiLiLeiXingID;

    @TableField("序号")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "stepId", targetTypeClazz = String.class)
    private Integer xuHao;

    @TableField("输入及操作")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "operatingInstructions")
    private String shuRuJiCaoZuo;

    @TableField("期望结果")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "wishResult")
    private String qiWangJieGuo;

    @TableField("评估标准")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "evaluationCriteria")
    private String pingGuBiaoZhun;

    @TableField("实测结果")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "realTestResult")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "testResult")
    private String shiCeJieGuo;

    @TableField("轮次ID")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "turnId")
    private String lunCiID;

    @TableField("预留字段1")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "reserveField1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "reserveField2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "reserveField3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "reserveField4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "reserveField5")
    private String yuLiuZiDuan5;

    @TableField("问题报告单ID")
    @FieldSync(fieldClazz = RunningCaseStep.class, fieldName = "questionId")
    private String wenTiBaoGaoDanID;


}
