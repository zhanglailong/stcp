package org.jeecg.modules.running.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: running_case_history
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("running_case_history")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_case_history对象", description="running_case_history")
public class RunningCaseHistory implements Serializable {
    private static final long serialVersionUID = 1L;


	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private String testTaskId;
	/**测试用例名称*/
	@Excel(name = "测试用例名称", width = 15)
    @ApiModelProperty(value = "测试用例名称")
    private String testName;
	/**测试用例标识*/
	@Excel(name = "测试用例标识", width = 15)
    @ApiModelProperty(value = "测试用例标识")
    private String testCode;

    /**追踪关系*/
    @Excel(name = "追踪关系", width = 50)
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
	/**终止条件*/
	@Excel(name = "终止条件", width = 15)
    @ApiModelProperty(value = "终止条件")
    private String testOver;
	/**终止过程*/
	@Excel(name = "终止过程", width = 15)
    @ApiModelProperty(value = "终止过程")
    private String testProcess;
	/**评估准则*/
	@Excel(name = "评估准则", width = 15)
    @ApiModelProperty(value = "评估准则")
    private String testCriterion;
	/**测试人员*/
	@Excel(name = "测试人员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "id")
	/** @Dict (dictTable = "sys_user", dicText = "realname", dicCode = "id")*/
    private String testPerson;
	/**执行日期*/
	@Excel(name = "执行日期", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "执行日期")
    private Date testDate;
	/**执行情况*/
	@Excel(name = "执行情况", width = 15)
    @ApiModelProperty(value = "执行情况")
    private String testSituation;
	/**执行结果*/
	@Excel(name = "执行结果", width = 15)
    @ApiModelProperty(value = "执行结果")
    private String testResult;
	/**测试监督人*/
	@Excel(name = "测试监督人", width = 15)
    @ApiModelProperty(value = "测试监督人")
    private String testSupervisor;
	/**被测软件版本*/
	@Excel(name = "被测软件版本", width = 15)
    @Dict(dictTable = "running_case_history", dicText = "id", dicCode = "test_version")
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
	/**输入及操作说明*/
	@Excel(name = "输入及操作说明", width = 15)
    @ApiModelProperty(value = "输入及操作说明")
    private String operatingInstructions;
	/**期望测试结果*/
	@Excel(name = "期望测试结果", width = 15)
    @ApiModelProperty(value = "期望测试结果")
    private String expectResult;
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
	/**删除标记0存在1删除*/
	@Excel(name = "删除标记0存在1删除", width = 15)
    @ApiModelProperty(value = "删除标记0存在1删除")
    private Integer delFlag;
	/**操作类型*/
	@Excel(name = "操作类型", width = 15)
    @ApiModelProperty(value = "操作类型")
    private String operationType;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;

    @TableField(exist = false)
    /**查询开始日期*/
    private String startDate;
    @TableField(exist = false)
    /**查询结束日期*/
    private String endDate;

    /**主表id*/
    @ApiModelProperty(value = "主表id")
    private String mainId;
    /**排序*/
    @ApiModelProperty(value = "排序")
    private Integer sort;
    /**修改字段code*/
    @ApiModelProperty(value = "修改字段code")
    private String modifyField;
    /**修改字段当前值*/
    @ApiModelProperty(value = "修改字段当前值")
    private String modifyFieldValue;
    /**修改字段旧值*/
    @ApiModelProperty(value = "修改字段旧值")
    private String modifyFieldOldValue;

    @TableField(exist = false)
    private String modifyFieldName;

    @TableField(exist = false)
    private String taskName;
    /**所属项目*/
    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private String projectId;

    @TableField(exist = false)
    private List<RunningCaseStepHistory> stepHistoryList;
}
