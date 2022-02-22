package org.jeecg.modules.cloudtools.runex.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.cloudtools.Cases;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 记录xrun的用例
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Data
@TableName("run_case")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="xrun_case对象", description="记录xrun的用例")
public class RunCase extends Cases implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**用例名*/
	@Excel(name = "用例名", width = 15)
    @ApiModelProperty(value = "用例名")
    private String caseName;
	/**用例标识*/
	@Excel(name = "用例标识", width = 15)
    @ApiModelProperty(value = "用例标识")
    private String caseSign;
	/**用例内容*/
	@Excel(name = "用例内容", width = 15)
    @ApiModelProperty(value = "用例内容")
    private String params;
	/**任务id*/
	@Excel(name = "任务id", width = 15, dictTable = "running_task", dicText = "task_name", dicCode = "id")
	@Dict(dictTable = "running_task", dicText = "task_name", dicCode = "id")
    @ApiModelProperty(value = "任务id")
    private String taskId;
	/**任务名*/
	@Excel(name = "任务名", width = 15)
    @ApiModelProperty(value = "任务名")
    private String taskName;
	/**项目id*/
	@Excel(name = "项目id", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "id")
	@Dict(dictTable = "running_project", dicText = "project_name", dicCode = "id")
    @ApiModelProperty(value = "项目id")
    private String projectId;
	/**项目名*/
	@Excel(name = "项目名", width = 15)
    @ApiModelProperty(value = "项目名")
    private String projName;
	
	private String sourceFile;
	
	@TableField(exist = false)
	private JSONObject paramsDetail;
	@TableField(exist = false)
	private List<String> ipStore;
	@Dict(dicCode = "klocStatus")
	private String status;
	
	private String clientIp;
	
	private String caseType;
	
	@TableField(exist = false)
	private String url;
	
	public RunCase stparamsDetail(String prefix) {
		this.formUrl(prefix);
		this.setParamsDetail(JSONObject.parseObject(this.getParams()));
		return this;
	}
	
	public RunCase formUrl(String prefix) {
		this.setUrl("2".equals(this.status)?String.format("%s/testBed/result/%s.zip", prefix,this.getCaseName()):null);
		return this;
	}
	
	/**优先级*/
	@Excel(name = "优先级", width = 15)
    @Dict(dicCode = "priority")
    @ApiModelProperty(value = "优先级")
    private String priorityLevel;

	@ApiModelProperty(value = "超时时间")
	private String timeOut;
	
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date startTime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date endTime;
    
    /**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    
    @Dict(dicCode = "toolStep")
    private String execStep;

	@ApiModelProperty(value = "对应的测试项ID")
	private String caseId;
	
}
