package org.jeecg.modules.task.vo;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.task.entity.RunningCaseStep;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
/**
 * @Author: test
 * */
public class RunningCaseManageVO implements Serializable {


    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private String testTaskId;
    
	/**任务名称*/
	@Excel(name = "任务名称", width = 15)
    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @Excel(name = "被测对象名称", width = 15)
    @TableField(exist = false)
    private String uutName;

    @Excel(name = "被测对象标识", width = 15)
    @TableField(exist = false)
    private String uutCode;

    @Excel(name = "被测对象类型", width = 15)
    @TableField(exist = false)
    private String uutType;

    @Excel(name = "被测对象版本", width = 15)
    @TableField(exist = false)
    private String uutVersion;

	/**所属项目名称*/
	@Excel(name = "所属项目名称", width = 15)
    @ApiModelProperty(value = "所属项目名称")
    private String projectInfo;
	
	/**测试用例名称*/
	@Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private String testName;
	/**测试用例标识*/
	@Excel(name = "测试用例标识", width = 15)
    @ApiModelProperty(value = "测试用例标识")
    private String testCode;
    /**追踪关系*/
    @Excel(name = "追踪关系" , width = 20)
    @ApiModelProperty(value = "追踪关系")
    private String testRelationship;
	/**测试用例类型*/
	@Excel(name = "测试用例类型", width = 15)
    @ApiModelProperty(value = "测试用例类型")
    private String testType;
	/**测试说明*/
	@Excel(name = "测试说明", width = 15)
    @ApiModelProperty(value = "测试说明")
    private String testInstructions;
	/**测试用例初始化*/
	@Excel(name = "测试用例初始化", width = 15)
    @ApiModelProperty(value = "测试用例初始化")
    private String testInit;
	/**前提与约束*/
	@Excel(name = "前提与约束", width = 15)
    @ApiModelProperty(value = "前提与约束")
    private String testConstraint;
	/**终止过程*/
	@Excel(name = "终止过程", width = 15)
    @ApiModelProperty(value = "终止过程")
    private String testProcess;
    /**测试步骤*/
    @Excel(name = "序号" , width = 10)
    @ApiModelProperty(value = "序号")
    private String stepId;
    /**输入及操作说明*/
    @Excel(name = "输入及操作说明", width = 40)
    @ApiModelProperty(value = "输入及操作说明")
    private String operatingInstructions;
    /**期望测试结果*/
    @Excel(name = "期望测试结果", width = 40)
    @ApiModelProperty(value = "期望测试结果")
    private String expectResult;
    /**终止条件*/
    @Excel(name = "测试用例终止条件", width = 15)
    @ApiModelProperty(value = "测试用例终止条件")
    private String testOver;
    /**测试用例通过准则*/
    @Excel(name = "评估准则", width = 15)
    @ApiModelProperty(value = "评估准则")
    private String testCriterion;
	/**测试人员*/
    @Excel(name = "测试人员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "测试人员")
    private String testPerson;
	/**执行日期*/
	@Excel(name = "执行日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "执行日期")
    private java.util.Date testDate;
	/**执行情况*/
	@Excel(name = "执行情况", width = 15)
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
	@Excel(name = "被测软件版本", width = 15)
    @ApiModelProperty(value = "被测软件版本")
    private String testVersion;
	/**问题标识*/
	@Excel(name = "问题标识", width = 15)
    @ApiModelProperty(value = "问题标识")
    private String testQuestionCode;
	/**用例属性*/
	@Excel(name = "用例属性", width = 15)
    @ApiModelProperty(value = "用例属性")
    private String testAttributes;
	/**实际测试结果*/
	@Excel(name = "实际测试结果", width = 15)
    @ApiModelProperty(value = "实际测试结果")
    private String actualResult;
	/**用例属性*/
	@Excel(name = "用例属性", width = 15)
    @ApiModelProperty(value = "用例属性")
    private String testSx;
	/**测试问题单*/
	@Excel(name = "测试问题单", width = 15)
    @ApiModelProperty(value = "测试问题单")
    private String userOrder;
	/**测试结果 0为通过，1为不通过、2为无法测试*/
	@Excel(name = "测试结果 0为通过，1为不通过、2为无法测试", width = 15)
    @ApiModelProperty(value = "测试结果 0为通过，1为不通过、2为无法测试")
    private String testRealResult;
	
	@ApiModelProperty(value = "删除标记")
	private Integer delFlag;
	
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @TableField(exist = false)
    private List<RunningCaseStep> stepList;

}