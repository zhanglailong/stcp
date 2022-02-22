package org.jeecg.modules.sjcj.collectiondatamanagement.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
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
 * @Description: 采集数据管理
 * @Author: jeecg-boot
 * @Date:   2021-01-11
 * @Version: V1.0
 */
@Data
@TableName("collection_data_management")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="collection_data_management对象", description="采集数据管理")
public class CollectionDataManagement implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**工具名称*/
	@Excel(name = "工具名称", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
	@Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    @ApiModelProperty(value = "工具名称")
    private String toolId;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "id")
	@Dict(dictTable = "running_project", dicText = "project_name", dicCode = "id")
    @ApiModelProperty(value = "项目ID")
    private String projectId;
    /**项目名称*/
    @TableField(exist = false)
    private String projectName;
    /**任务ID*/
	@Excel(name = "任务ID", width = 15, dictTable = "running_task", dicText = "task_name", dicCode = "id")
	@Dict(dictTable = "running_task", dicText = "task_name", dicCode = "id")
    @ApiModelProperty(value = "任务ID")
    private String taskId;
	/**测试用例ID*/
	@Excel(name = "测试用例ID", width = 15, dictTable = "running_case", dicText = "test_name", dicCode = "id")
	@Dict(dictTable = "running_case", dicText = "test_name", dicCode = "id")
    @ApiModelProperty(value = "测试用例ID")
    private String caseId;
    /**任务名称*/
    @TableField(exist = false)
    private String taskName;
	/**Agent地址*/
	@Excel(name = "Agent地址", width = 15)
    @ApiModelProperty(value = "Agent地址")
    private String agentIp;
	/**数据采集结果*/
	@Excel(name = "数据采集结果", width = 15)
    @ApiModelProperty(value = "数据采集结果")
    private String gatherResult;
	/**被测软件名称*/
	@Excel(name = "被测软件名称", width = 15)
    @ApiModelProperty(value = "被测软件名称")
    private String testedSoftwareName;
	@Excel(name = "数据采集描述", width = 15)
    @ApiModelProperty(value = "数据采集描述")
    private String description;
	/**数据文件路径*/
	@Excel(name = "数据文件路径", width = 15)
    @ApiModelProperty(value = "数据文件路径")
    private String sjwjurl;
	/**数据采集时间*/
	@Excel(name = "数据采集时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "数据采集时间")
    private Date sjcjTime;
	/**异常信息*/
	@Excel(name = "异常信息", width = 15)
    @ApiModelProperty(value = "异常信息")
    private String abnormalInformation;
	/**删除标识*/
	@Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    private String deleteFalg;
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
    /**AgentManagerId*/
    @ApiModelProperty(value = "AgentManagerId")
    private String agentManagerId;
    /**采集类型*/
	@Excel(name = "采集类型", width = 15, dicCode = "typeOfCollection")
	@Dict(dicCode = "typeOfCollection")
    @ApiModelProperty(value = "采集类型")
    private String typeOfCollection;
}
