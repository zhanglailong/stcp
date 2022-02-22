package org.jeecg.modules.running.record.entity;

import java.io.Serializable;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-03-29
 * @Version: V1.0
 */
@Data
@TableName("running_uut_record")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_record对象", description="被测对象属性表")
public class RunningUutRecord implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**被测对象id*/
	@Excel(name = "被测对象id", width = 15)
    @ApiModelProperty(value = "被测对象id")
    private String uutListId;
	/**状态*/
	@Excel(name = "状态", width = 15)
    @ApiModelProperty(value = "状态")
    private String status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**总功能数*/
	@Excel(name = "总功能数", width = 15)
    @ApiModelProperty(value = "总功能数")
    private String totalFunctionCount;
	/**接口总数*/
	@Excel(name = "接口总数", width = 15)
    @ApiModelProperty(value = "接口总数")
    private String totalApiCount;
	/**功能性的依从性总数*/
	@Excel(name = "功能性的依从性总数", width = 15)
    @ApiModelProperty(value = "功能性的依从性总数")
    private String totalYcxCount;
	/**要求依从性的界面总数*/
	@Excel(name = "要求依从性的界面总数", width = 15)
    @ApiModelProperty(value = "要求依从性的界面总数")
    private String totalInterfaceCount;
	/**在评价中检测缺少的功能数*/
	@Excel(name = "在评价中检测缺少的功能数", width = 15)
    @ApiModelProperty(value = "在评价中检测缺少的功能数")
    private String missingFunctionNumbers;
	/**在评价中检测不能正确实现的功能数*/
	@Excel(name = "在评价中检测不能正确实现的功能数", width = 15)
    @ApiModelProperty(value = "在评价中检测不能正确实现的功能数")
    private String cannotRealizedNumbers;
	/**按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数*/
	@Excel(name = "按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数", width = 15)
    @ApiModelProperty(value = "按照预期合理的结果，用户得到的结果在可允许范围的差别的用例数")
    private String caseNumbers;
	/**能够调用成功的接口数*/
	@Excel(name = "能够调用成功的接口数", width = 15)
    @ApiModelProperty(value = "能够调用成功的接口数")
    private String sucessInterfaceNumbers;
	/**不能与其他软件或系统交换数据的次数*/
	@Excel(name = "不能与其他软件或系统交换数据的次数", width = 15)
    @ApiModelProperty(value = "不能与其他软件或系统交换数据的次数")
    private String cannotExchangedNumbers;
	/**企图交换数据的总次数*/
	@Excel(name = "企图交换数据的总次数", width = 15)
    @ApiModelProperty(value = "企图交换数据的总次数")
    private String totalNumbersExchanges;
	/**测试中还未实现规定的功能性的依从性的项目*/
	@Excel(name = "测试中还未实现规定的功能性的依从性的项目", width = 15)
    @ApiModelProperty(value = "测试中还未实现规定的功能性的依从性的项目")
    private String unrealizedSubject;
	/**按规定正确实现的界面总数*/
	@Excel(name = "按规定正确实现的界面总数", width = 15)
    @ApiModelProperty(value = "按规定正确实现的界面总数")
    private String totalInterface;
	/**预测的潜在风险总数*/
	@Excel(name = "预测的潜在风险总数", width = 15)
    @ApiModelProperty(value = "预测的潜在风险总数")
    private String totalPotentialRisks;
	/**实际检测到的故障总数*/
	@Excel(name = "实际检测到的故障总数", width = 15)
    @ApiModelProperty(value = "实际检测到的故障总数")
    private String totalFailuresNumbers;
	/**代码行数*/
	@Excel(name = "代码行数", width = 15)
    @ApiModelProperty(value = "代码行数")
    private String codeLines;
	/**规定的可靠性的依从性还未完全实现的项数*/
	@Excel(name = "规定的可靠性的依从性还未完全实现的项数", width = 15)
    @ApiModelProperty(value = "规定的可靠性的依从性还未完全实现的项数")
    private String unrealizedProjectsNumbers;
	/**规定的可靠性的依从性项总数*/
	@Excel(name = "规定的可靠性的依从性项总数", width = 15)
    @ApiModelProperty(value = "规定的可靠性的依从性项总数")
    private String reliabilityComplianceSum;
	/**模块总数*/
	@Excel(name = "模块总数", width = 15)
    @ApiModelProperty(value = "模块总数")
    private String modulesTotalNumbers;
	/**模块的平均注释率*/
	@Excel(name = "模块的平均注释率", width = 15)
    @ApiModelProperty(value = "模块的平均注释率")
    private String averageAnnotationRate;
	/**注释率小于20%的模块总数*/
	@Excel(name = "注释率小于20%的模块总数", width = 15)
    @ApiModelProperty(value = "注释率小于20%的模块总数")
    private String annotationRateLess20;
	/**行数超过200的模块数*/
	@Excel(name = "行数超过200的模块数", width = 15)
    @ApiModelProperty(value = "行数超过200的模块数")
    private String modulesLinesThan200;
	/**模块的调用层数*/
	@Excel(name = "模块的调用层数", width = 15)
    @ApiModelProperty(value = "模块的调用层数")
    private String modulesCallLevel;
	/**所需的平均响应时间*/
	@Excel(name = "所需的平均响应时间", width = 15)
    @ApiModelProperty(value = "所需的平均响应时间")
    private String averageResponseTime;
	/**所需的最大响应时间*/
	@Excel(name = "所需的最大响应时间", width = 15)
    @ApiModelProperty(value = "所需的最大响应时间")
    private String maximumResponseTime;
	/**所要求的平均吞吐量*/
	@Excel(name = "所要求的平均吞吐量", width = 15)
    @ApiModelProperty(value = "所要求的平均吞吐量")
    private String averageThroughput;
	/**所需的最大吞吐量*/
	@Excel(name = "所需的最大吞吐量", width = 15)
    @ApiModelProperty(value = "所需的最大吞吐量")
    private String maximumThroughput;
	/**所需的平均周转时间*/
	@Excel(name = "所需的平均周转时间", width = 15)
    @ApiModelProperty(value = "所需的平均周转时间")
    private String averageTurnaroundTime;
	/**所需的最大周转时间*/
	@Excel(name = "所需的最大周转时间", width = 15)
    @ApiModelProperty(value = "所需的最大周转时间")
    private String maximumTurnaroundTime;
    /**所需的最大周转时间*/
    @Excel(name = "等待时间", width = 15)
    @ApiModelProperty(value = "等待时间")
    private String waitTime;
}
