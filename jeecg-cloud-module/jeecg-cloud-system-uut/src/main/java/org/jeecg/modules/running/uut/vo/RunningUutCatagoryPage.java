package org.jeecg.modules.running.uut.vo;

import java.util.List;

import org.jeecg.modules.running.uut.entity.RunningUutNode;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 被测对象流程分类表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Data
@ApiModel(value="running_uut_catagoryPage对象", description="被测对象流程分类表")
public class RunningUutCatagoryPage {

	/**id*/
	@ApiModelProperty(value = "id")
	private String id;
	/**流程标识*/
	@Excel(name = "流程标识", width = 15)
	@ApiModelProperty(value = "流程标识")
	private String code;
	/**流程名称*/
	@Excel(name = "流程名称", width = 15)
	@ApiModelProperty(value = "流程名称")
	private String name;
	/**版本号*/
	@Excel(name = "版本号", width = 15)
	@ApiModelProperty(value = "版本号")
	private String version;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	/**排序*/
	@Excel(name = "排序", width = 15)
	@ApiModelProperty(value = "排序")
	private String priority;
	/**所属部门id*/
	@Excel(name = "所属部门id", width = 15)
	@ApiModelProperty(value = "所属部门id")
	private String departmentId;

	@ExcelCollection(name="被测对象流程节点表")
	@ApiModelProperty(value = "被测对象流程节点表")
	private List<RunningUutNode> runningUutNodeList;

}
