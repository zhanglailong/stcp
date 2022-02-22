package org.jeecg.modules.running.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Data
@NoArgsConstructor
@TableName("running_case")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_case对象", description="测试用例表")
public class RunningCase implements Serializable {
    private static final long serialVersionUID = 1L;
	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**任务id*/
    /**@Excel (name = "任务id", width = 15)*/
    @ApiModelProperty(value = "任务id")
    private String testTaskId;
    /**所属项目名称，数据库中没有该字段*/
    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private String projectInfo;
        /*
	 * @Excel(name = "任务名称", width = 15)
	 * 
	 * @ApiModelProperty(value = "任务名称")
	 * 
	 * @TableField(exist = false) private java.lang.String taskName;
	 */
	/**测试用例名称*/
	@Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private String testName;
	/**测试用例标识*/
	@Excel(name = "标识", width = 15)
    @ApiModelProperty(value = "测试用例标识")
    private String testCode;
    /**追踪关系*/
    @Excel(name = "追踪关系", width = 50)
    @ApiModelProperty(value = "追踪关系")
    private String testRelationship;
	/**测试用例类型*/
    /**@Excel (name = "测试用例类型", width = 15)*/
    @ApiModelProperty(value = "测试用例类型")
    @Dict(dicCode="caseType")
    private String testType;
	/**测试用例说明*/
    /**@Excel (name = "测试用例说明", width = 15)*/
    @ApiModelProperty(value = "测试用例")
    private String testInstructions;
	/**用例初始化*/
	@Excel(name = "用例初始化", width = 15)
    @ApiModelProperty(value = "测试用例初始化")
    private String testInit;
	/**前提与约束*/
   /** @Excel (name = "前提与约束", width = 15)*/
    @ApiModelProperty(value = "前提与约束")
    private String testConstraint;
    /**设计方法*/
    /** @Excel (name = "设计方法", width = 15)*/
    @ApiModelProperty(value = "设计方法")
    private String testProcess;
    /*测试步骤*/
   /**
    * 输入及操作说明
    * @ApiModelProperty (value = "序号")
    * private java.lang.String stepId;
    * @Excel (name = "输入及操作说明", width = 15)
    */
    @ApiModelProperty(value = "输入及操作说明")
    private String operatingInstructions;
   /** 期望测试结果
    *  @Excel (name = "期望测试结果", width = 15)
    */
    @ApiModelProperty(value = "期望测试结果")
    private String expectResult;

   /**测试用例终止条件
    * @Excel (name = "测试用例终止条件", width = 15)
    */
    @ApiModelProperty(value = "测试用例终止条件")
    private String testOver;
    /** 测试用例通过准则
     * @Excel (name = "测试用例通过准则", width = 15)
     */
    @ApiModelProperty(value = "测试用例通过准则")
    private String testCriterion;
	/**用例设计人*/
	@Excel(name = "用例设计人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "用例设计人")
    private String testPerson;
	/**执行日期*/
	@Excel(name = "执行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行日期")
    private java.util.Date testDate;
	/**执行情况*/
    @ApiModelProperty(value = "执行情况")
    private String testSituation;
	/**执行结果*/
	@Excel(name = "执行结果", width = 15)
    @ApiModelProperty(value = "执行结果")
    private String testResult;
	/**测试监督人*/
	@Excel(name = "测试监督人", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "测试监督人")
    private String testSupervisor;
	/**被测软件版本*/
    /**@Excel (name = "被测软件版本", width = 15)*/
    @ApiModelProperty(value = "被测软件版本")
    private String testVersion;
	/**问题标识*/
    /**@Excel (name = "问题标识", width = 15)*/
    @ApiModelProperty(value = "问题标识")
    private String testQuestionCode;
	/**用例属性*/
    /**@Excel (name = "用例属性", width = 15)*/
    @ApiModelProperty(value = "用例属性")
    private String testAttributes;
	/**实际测试结果*/
    /**@Excel (name = "实际测试结果", width = 15)*/
    @ApiModelProperty(value = "实际测试结果")
    private String actualResult;
	/**用例属性*/
   /**@Excel (name = "用例属性", width = 15)*/
    @ApiModelProperty(value = "用例属性")
    private String testSx;
	/**测试问题单*/
   /** @Excel (name = "测试问题单", width = 15)*/
    @ApiModelProperty(value = "测试问题单")
    private String userOrder;
	/**测试结果 0为通过，1为不通过、2为无法测试*/
    /**@Excel (name = "测试结果 0为通过，1为不通过、2为无法测试", width = 15)*/
    @ApiModelProperty(value = "测试结果 0为通过，1为不通过、2为无法测试")
    private String testRealResult;
    
    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;
    
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
    /**判断标志*/
    @TableField(exist = false)
    private String opFlag;

    @TableField(exist = false)
    private Boolean isModified;

    @TableField(exist = false)
    private List<RunningCaseHistory> modifiedList;

    @TableField(exist = false)
    private List<RunningCaseStep> stepList;

    public RunningCase(String testTaskId,String testName, String testCode, String testRelationship, String testInstructions, String testInit, String testConstraint, String testProcess, String testOver, String testCriterion, String testPerson, String testVersion) {
        this.setTestTaskId(testTaskId);
        this.setTestName(testName);
        this.setTestCode(testCode);
        this.setTestRelationship(testRelationship);
        this.setTestInstructions(testInstructions);
        this.setTestInit(testInit);
        this.setTestConstraint(testConstraint);
        this.setTestProcess(testProcess);
        this.setTestOver(testOver);
        this.setTestCriterion(testCriterion);
        this.setTestPerson(testPerson);
        this.setTestVersion(testVersion);
    }
//
//    public RunningCase() {
//    }
}
