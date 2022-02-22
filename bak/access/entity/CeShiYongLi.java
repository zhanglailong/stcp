package org.jeecg.modules.access.entity;

import cn.icesun.util.entityutil.annotation.FieldSync;
import cn.icesun.util.entityutil.handler.impl.ByteArr2StrTranslateHandler;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.sql.Blob;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.modules.task.entity.RunningCase;

/**
 * <p>
 * 测试用例
 * </p>
 *  //TODO 基本完成
 *  test_task_id '任务id' 测试项Code
 * 	test_process '终止过程' 未找到
 * 	test_supervisor '测试监督人'
 * 	test_version '被测软件版本'
 * 	test_question_code '问题标识'
 * 	test_attributes '用例属性'
 * 	operating_instructions '输入及操作说明'
 * 	expect_result '期望测试结果'
 * 	actual_result '实际测试结果'
 * 	test_sx '用例属性'
 * 	user_order '测试问题单'
 * 	del_flag '删除标记0存在1删除'
 *
 * 	update_by '更新人'
 * 	update_time '更新日期'
 * 	sys_org_code '所属部门'
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试用例")
public class CeShiYongLi implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试用例ID")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "id")
    private String ceShiYongLiID;

    @TableField("测试用例名称")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testName")
    private String ceShiYongLiMingCheng;

    @TableField("追踪关系")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testRelationship")
    private String zhuiZongGuanXi;

    @TableField("用例描述")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testInstructions")
    private String yongLiMiaoShu;

    @TableField("用例的初始化")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testInit")
    private String yongLiDeChuShiHua;

    @TableField("测试过程终止条件")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testOver")
    private String ceShiGuoChengZhongZhiTiaoJian;

    @TableField("测试结果评估标准")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testCriterion")
    private String ceShiJieGuoPingGuBiaoZhun;

    @TableField("前提和约束")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testConstraint")
    private String qianTiHeYueShu;

    @TableField("执行与否")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testSituation")
    private String zhiXingYuFou;

    @TableField("测试人员")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testPerson")
    private String ceShiRenYuan;

    @TableField("测试时间")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testDate")
    private Date ceShiShiJian;

    @TableField("测试结论")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testResult")
    private String ceShiJieLun;

    @TableField("通过与否")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testRealResult")
    //TODO 通过 0, 未通过 1, "" 2
    private String tongGuoYuFou;

    @TableField("备注")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "remark")
    private String beiZhu;

    @TableField("测试用例类型ID")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testCaseTypeId")
    private String ceShiYongLiLeiXingID;

    @TableField("设计人员")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "createBy")
    private String sheJiRenYuan;

    @TableField("设计时间")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "createTime")
    private Date sheJiShiJian;

    @TableField("未执行原因")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "unexecuteReason")
    private String weiZhiXingYuanYin;

    @TableField("预计工作时间")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "expectWorkTime")
    private Double yuJiGongZuoShiJian;

    @TableField("实际工作时间")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "realWorkTime")
    private Double shiJiGongZuoShiJian;

    @TableField("扩展")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "extend", typeTranslateHandler = ByteArr2StrTranslateHandler.class)
    private byte[] kuoZhan;

    @TableField("用例类型")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testType", targetTypeClazz = String.class)
    private Integer yongLiLeiXing;

    @TableField("测试用例标识")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testCode")
    private String ceShiYongLiBiaoShi;

    @TableField("是否属于本项目")
    private String shiFouShuYuBenXiangMu = "否";

    @TableField("前向ID")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "frontId")
    private String qianXiangID;

    @TableField("用例设计审核人")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "designReviewer")
    private String yongLiSheJiShenHeRen;

    @TableField("审核结论")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reviewResult")
    private String shenHeJieLun;

    @TableField("审核时间")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reviewTime")
    private String shenHeShiJian;

    @TableField("用例设计审核")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "designReview")
    private String yongLiSheJiShenHe;

    @TableField("用例设计审核说明")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "designReviewExplain")
    private String yongLiSheJiShenHeShuoMing;

    @TableField("用例记录审核人")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "recordReviewer")
    private String yongLiJiLuShenHeRen;

    @TableField("用例记录审核")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "recordReview")
    private String yongLiJiLuShenHe;

    @TableField("用例记录审核说明")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "recordReviewExplain")
    private String yongLiJiLuShenHeShuoMing;

    @TableField("用例不执行原因")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "testCaseUnexecuteReason")
    private String yongLiBuZhiXingYuanYin;

    @TableField("轮次ID")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "turnId")
    private String lunCiID;

    @TableField("预留字段1")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reserveField1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reserveField2")
    private String yuLiuZiDuan2;

    @TableField("预留字段3")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reserveField3")
    private String yuLiuZiDuan3;

    @TableField("预留字段4")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reserveField4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "reserveField5")
    private String yuLiuZiDuan5;

    @TableField("沿用类型")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "continueType")
    private String yanYongLeiXing;

    @TableField("偏差说明")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "deviationDescription")
    private String pianChaShuoMing;

    @TableField("偏差理由")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "deviationReason")
    private String pianChaLiYou;

    @TableField("偏差影响")
    @FieldSync(fieldClazz = RunningCase.class, fieldName = "deviationAffect")
    private String pianChaYingXiang;


}
