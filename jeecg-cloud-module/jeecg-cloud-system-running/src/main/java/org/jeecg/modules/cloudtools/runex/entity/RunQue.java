package org.jeecg.modules.cloudtools.runex.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
 * @Description: 队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Data
@TableName("run_que")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="run_que对象", description="队列表")
public class RunQue implements Serializable {
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
    @Excel(name = "用例名称", width = 15)
    @Dict(dictTable = "run_case", dicText = "case_name", dicCode = "id")
    @ApiModelProperty(value = "用例名称")
    private String caseId;
    private String caseName;
	/**当前执行步骤*/
	@Excel(name = "当前执行步骤", width = 15)
	@Dict(dicCode = "toolstep")
    @ApiModelProperty(value = "当前执行步骤")
    private String execStep;
    /**当前优先级*/
    @Excel(name = "当前优先级", width = 15)
    @ApiModelProperty(value = "当前优先级")
    @Dict(dicCode = "priority")
    private String priorityLevel;
    @Excel(name = "执行客户端IP", width = 15)
    @ApiModelProperty(value = "执行客户端IP")
    private String clientIp;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss")
    private Date startTime;
    
    @TableField(exist = false)
    private String timeSpend;
    
    private String caseType;
    
    /**项目ID*/
	@Excel(name = "项目ID", width = 15, dictTable = "running_project", dicText = "project_name", dicCode = "id")
	@Dict(dictTable = "running_project", dicText = "project_name", dicCode = "id")
    @ApiModelProperty(value = "项目ID")
    private String projectId;
	/**任务ID*/
	@Excel(name = "任务ID", width = 15, dictTable = "running_task", dicText = "task_name", dicCode = "id")
	@Dict(dictTable = "running_task", dicText = "task_name", dicCode = "id")
    @ApiModelProperty(value = "任务ID")
    private String taskId;
    
    public RunQue setStateByClientStatus(Set<String> clientIpList) {
    	if(clientIpList == null || !clientIpList.contains(this.clientIp)) {
    		this.execStep = "-999";
    	}
    	return this;
    }
    
    public RunQue setTimeSpend() {
    	if(this.startTime!=null) {
    		this.timeSpend = String.valueOf((System.currentTimeMillis()-this.startTime.getTime())/1000);
    	}else {
    		this.timeSpend = "-";
    	}
    	return this;
    }
    
}
