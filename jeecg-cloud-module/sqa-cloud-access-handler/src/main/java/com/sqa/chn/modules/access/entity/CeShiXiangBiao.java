package com.sqa.chn.modules.access.entity;

import cn.icesun.util.entityutil.annotation.FieldSync;
import cn.icesun.util.entityutil.handler.impl.ByteArr2StrTranslateHandler;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.sqa.chn.modules.task.entity.RunningTask;

/**
 * <p>
 * 测试项表
 * </p>
 *  //TODO 基本完成
 * task_type
 * project_info
 * task_tool
 * task_soft_name 被测对象表-被测对象名称
 * task_soft_version 被测对象表-版本
 * task_principal
 * task_data
 * task_status  未知对应
 * cu_file  x
 * begin_date  被测对象表-测试开始日期-未知格式不知该如何处理//TODO
 * finish_date  被测对象表-测试结束日期-未知格式不知该如何处理//TODO
 * remark
 * del_flag
 * create_time
 * update_by
 * update_time
 * sys_org_code
 * task_assets_id
 * task_soft_file
 * task_soft_type
 * task_assets_detail
 * test_type
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("测试项表")
public class CeShiXiangBiao implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("测试项ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "projectId")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "id")
    private String ceShiXiangID;

    @TableField("测试项标识")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskCode")
    private String ceShiXiangBiaoShi;

    @TableField("序号")
    private Integer xuHao;

    @TableField("测试项名称")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskName")
    private String ceShiXiangMingCheng;

    @TableField("简写码")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "abbreviatedCode")
    private String jianXieMa;

    @TableField("测试项说明及测试要求")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskExplain")
    private String ceShiXiangShuoMingJiCeShiYaoQiu;

    @TableField("测试方法说明")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskMethod")
    private String ceShiFangFaShuoMing;

    @TableField("追踪关系")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "trackRelationship")
    private String zhuiZongGuanXi;

    @TableField("终止条件")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "terminationConditions")
    private String zhongZhiTiaoJian;

    @TableField("优先级")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "priority")
    private String youXianJi;

    @TableField("下属测试用例说明")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskCaseExplain")
    private String xiaShuCeShiYongLiShuoMing;

    @TableField("所属测试类型ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "testTypeId")
    private String suoShuCeShiLeiXingID;

    @TableField("父节点ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "parentId")
    private String fuJieDianID;

    @TableField("预计工作时间")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "expectWorkTime")
    private Double yuJiGongZuoShiJian;

    @TableField("测试项类型ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "taskTypeId")
    private String ceShiXiangLeiXingID;

    @TableField("前向ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "frontId")
    private String qianXiangID;

    @TableField("审核人")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reviewer")
    private String shenHeRen;

    @TableField("审核结论")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reviewConclusion")
    private String shenHeJieLun;

    @TableField("审核时间")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reviewTime")
    private String shenHeShiJian;

    @TableField("审核状态")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reviewState")
    private String shenHeZhuangTai;

    @TableField("审核备注")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reviewRemark")
    private String shenHeBeiZhu;

    @TableField("设计人员")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "createBy")
    private String sheJiRenYuan;

    @TableField("轮次ID")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "turnId")
    private String lunCiID;

    @TableField("预留字段1")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField1")
    private String yuLiuZiDuan1;

    @TableField("预留字段2")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField2")
    private String yuLiuZiDuan2;

    @TableField("预留字段4")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField4")
    private String yuLiuZiDuan4;

    @TableField("预留字段5")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField5")
    private String yuLiuZiDuan5;

    @TableField("预留字段3")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField3", typeTranslateHandler = ByteArr2StrTranslateHandler.class)
    private byte[] yuLiuZiDuan3;

    @TableField("前提约束")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "premiseConstraint")
    private String qianTiYueShu;

    @TableField("评估准则")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "evaluationCriteria")
    private String pingGuZhunZe;

    @TableField("测试要求")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "testRequirement")
    private String ceShiYaoQiu;

    @TableField("覆盖的测试要求")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "coverTestRequirement")
    private String fuGaiDeCeShiYaoQiu;

    @TableField("预留字段6")
    @FieldSync(fieldClazz = RunningTask.class, fieldName = "reserveField6", typeTranslateHandler = ByteArr2StrTranslateHandler.class)
    private byte[] yuLiuZiDuan6;


}
