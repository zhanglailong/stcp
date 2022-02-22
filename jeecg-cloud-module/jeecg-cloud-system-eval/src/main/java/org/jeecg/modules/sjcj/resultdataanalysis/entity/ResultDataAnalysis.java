package org.jeecg.modules.sjcj.resultdataanalysis.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: 结果数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
@Data
@TableName("result_data_analysis")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="result_data_analysis对象", description="结果数据分析")
public class ResultDataAnalysis implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**结果分析数据*/
	@Excel(name = "结果分析数据", width = 15)
    @ApiModelProperty(value = "结果分析数据")
    private String resultData;
	/**Agent地址*/
	@Excel(name = "Agent地址", width = 15)
    @ApiModelProperty(value = "Agent地址")
    private String agentIp;
	/**测试工具*/
	@Excel(name = "测试工具", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
	@Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    @ApiModelProperty(value = "测试工具")
    private String toolId;
	/**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "id")
	@Dict(dictTable = "running_project", dicText = "project_name", dicCode = "id")
    @ApiModelProperty(value = "项目ID")
    private String projectId;
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
    @ApiModelProperty(value = "CollectionDataManagementId")
    private String collectionDataManagementId;

    @TableField(exist = false)
    private String url;

    public ResultDataAnalysis() {
    	
    }
    public ResultDataAnalysis(String url,String agentIp,String projectId,String taskId,String caseId,String toolId) {
        this.setUrl(url);
        this.setAgentIp(agentIp);
        this.setProjectId(projectId);
        this.setToolId(toolId);
    }
}
