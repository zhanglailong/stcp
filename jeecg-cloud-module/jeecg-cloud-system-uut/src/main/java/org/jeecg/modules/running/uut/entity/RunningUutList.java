package org.jeecg.modules.running.uut.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2021-01-13
 * @Version: V1.0
 */
@Data
@TableName("running_uut_list")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="running_uut_list对象", description="被测对象列表")
public class RunningUutList implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
    /**被测对象创建方式*/
    @Dict(dicCode ="chooseType")
    @Excel(name = "被测对象创建方式", width = 15, dicCode = "chooseType")
    @ApiModelProperty(value = "被测对象创建方式")
    private String uutChooseType;
	/**被测对象名称*/
	@Excel(name = "被测对象名称", width = 15)
    @ApiModelProperty(value = "被测对象名称")
    private String uutName;
	/**被测对象类型*/
	@Excel(name = "被测对象类型", width = 15)
    @ApiModelProperty(value = "被测对象类型")
    private String uutType;
	/**被测对象标识*/
	@Excel(name = "被测对象标识", width = 15)
    @ApiModelProperty(value = "被测对象标识")
    private String uutCode;
	/**被测对象版本*/
	@Excel(name = "被测对象版本", width = 15)
    @ApiModelProperty(value = "被测对象版本")
    private String uutVersion;
	/**选择资产库*/
	@Excel(name = "选择资产库", width = 15, dicCode = "getAssetsList")
	@Dict(dicCode = "getAssetsList")
    @ApiModelProperty(value = "选择资产库")
    private String uutAssetsId;
	/**资产库详情*/
	@Excel(name = "资产库详情", width = 15)
    @ApiModelProperty(value = "资产库详情")
    private String uutAssetsDetail;
	/**被测件*/
	@Excel(name = "被测件", width = 15)
    @ApiModelProperty(value = "被测件")
    private String uutFile;
    /**简写码*/
    @Excel(name = "简写码", width = 15)
    @ApiModelProperty(value = "简写码")
    private String abbreviatedCode;
    /**规模*/
    @Excel(name = "规模", width = 15)
    @ApiModelProperty(value = "规模")
    private String scale;
    /**编程语言*/
    @Excel(name = "编程语言", width = 15)
    @ApiModelProperty(value = "编程语言")
    private String language;
    /**开发环境*/
    @Excel(name = "开发环境", width = 15)
    @ApiModelProperty(value = "开发环境")
    private String environment;
    /**重要程度*/
    @Excel(name = "重要程度", width = 15)
    @ApiModelProperty(value = "重要程度")
    private String importance;
    /**研制单位*/
    @Excel(name = "研制单位", width = 15)
    @ApiModelProperty(value = "研制单位")
    private String company;
    /**测试开始日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "测试开始日期", width = 15)
    @ApiModelProperty(value = "测试开始日期")
    private Date startTime;
    /**测试结束日期*/
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @Excel(name = "测试结束日期", width = 15)
    @ApiModelProperty(value = "测试结束日期")
    private Date endTime;
    /**测试执行情况*/
    @Excel(name = "测试执行情况", width = 15)
    @ApiModelProperty(value = "测试执行情况")
    private String execute;
    /**测试执行情况补充*/
    @Excel(name = "测试执行情况补充", width = 15)
    @ApiModelProperty(value = "测试执行情况补充")
    private String executePlus;
    /**质量评估*/
    @Excel(name = "质量评估", width = 15)
    @ApiModelProperty(value = "质量评估")
    private String qualityAssessment;
    /**改进建议*/
    @Excel(name = "改进建议", width = 15)
    @ApiModelProperty(value = "改进建议")
    private String suggestion;
    /**测试进度*/
    @Excel(name = "测试进度", width = 15)
    @ApiModelProperty(value = "测试进度")
    private String rate;
    /**硬件准备*/
    @Excel(name = "硬件准备", width = 15)
    @ApiModelProperty(value = "硬件准备")
    private String hardware;
    /**其他测试准备*/
    @Excel(name = "其他测试准备", width = 15)
    @ApiModelProperty(value = "其他测试准备")
    private String prepare;
    /**测试级别*/
    @Excel(name = "测试级别", width = 15)
    @ApiModelProperty(value = "测试级别")
    private String level;
    /**预计工作时间*/
    @Excel(name = "预计工作时间", width = 15)
    @ApiModelProperty(value = "预计工作时间")
    private Date workTime;
    /**前向id*/
    @Excel(name = "前向id", width = 15)
    @ApiModelProperty(value = "前向id")
    private Date forwardId;
	/**创建人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "id")
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
	@Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**所属部门*/
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**状态*/
	@Dict(dicCode = "uutListStatus")
	@Excel(name = "状态", width = 15, dicCode = "uutListStatus")
    @ApiModelProperty(value = "状态")
    private String uutStatus;
	/**分析状态*/
	@Dict(dicCode = "analyzeStatus")
	@Excel(name = "分析状态", width = 15, dicCode = "analyzeStatus")
    @ApiModelProperty(value = "分析状态")
    private String analyzeStatus;
	/**测试模板*/
	@Excel(name = "测试模板", width = 15 )
    @ApiModelProperty(value = "测试模板")
    private String testTemplate;
	/**需求规格说明文档*/
	@Excel(name = "需求规格说明文档", width = 15)
    @ApiModelProperty(value = "需求规格说明文档")
    private String uutStandardFile;
	/**测试依据*/
	@Excel(name = "测试依据", width = 15)
    @ApiModelProperty(value = "测试依据")
    private String uutBasisFile;
	/**测试说明*/
	@Excel(name = "测试说明", width = 15)
    @ApiModelProperty(value = "测试说明")
    private String uutExplainFile;
	/**历史测试用例设计单*/
	@Excel(name = "历史测试用例设计单", width = 15)
    @ApiModelProperty(value = "历史测试用例设计单")
    private String uutHteFile;
	/**执行单*/
	@Excel(name = "执行单", width = 15)
    @ApiModelProperty(value = "执行单")
    private String uutExecuteFile;
	/**报告单*/
	@Excel(name = "报告单", width = 15)
    @ApiModelProperty(value = "报告单")
    private String uutReportFile;
	/**回归用例*/
	@Excel(name = "回归用例", width = 15)
    @ApiModelProperty(value = "回归用例")
    private String uutReFile;
	/**其他*/
	@Excel(name = "其他", width = 15)
    @ApiModelProperty(value = "其他")
    private String uutOtherFile;
    /**版本状态*/
    @Dict(dicCode = "versionStatus")
    @Excel(name = "版本状态", width = 15, dicCode = "versionStatus")
    @ApiModelProperty(value = "状态")
    private String versionStatus;
    /**逻辑删除标识*/
    @ApiModelProperty(value = "删除标记 0存在1删除")
    private Integer deleteFlag;
    /**人员范围*/
    @Excel(name = "人员范围", width = 15)
    @ApiModelProperty(value = "人员范围")
    private String uutUser;

    @TableField(exist = false)
    /**编辑操作用到这个节点，保存修改过的字段名称和字段值*/
    private List<RunningUutListHistory> runningUutListHistory;

    @TableField(exist = false)
    private List<RunningUutListUser> runningUutListUser;
}
