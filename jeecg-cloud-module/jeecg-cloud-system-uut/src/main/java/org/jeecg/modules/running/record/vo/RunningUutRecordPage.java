package org.jeecg.modules.running.record.vo;

import java.util.List;

import org.jeecg.modules.running.record.entity.RunningUutProp;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 被测对象属性表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
@Data
@ApiModel(value="running_uut_recordPage对象", description="被测对象属性表")
public class RunningUutRecordPage {

	/**id*/
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
	private Date createTime;
	/**总功能数*/
	@Excel(name = "总功能数", width = 15)
	@ApiModelProperty(value = "总功能数")
	private Integer totalFunctionCount;
	/**接口总数*/
	@Excel(name = "接口总数", width = 15)
	@ApiModelProperty(value = "接口总数")
	private Integer totalApiCount;
	/**功能性的依从性总数*/
	@Excel(name = "功能性的依从性总数", width = 15)
	@ApiModelProperty(value = "功能性的依从性总数")
	private Integer totalYcxCount;
	/**要求依从性的界面总数*/
	@Excel(name = "要求依从性的界面总数", width = 15)
	@ApiModelProperty(value = "要求依从性的界面总数")
	private Integer totalInterfaceCount;

	@ExcelCollection(name="被测对象kv支撑表")
	@ApiModelProperty(value = "被测对象kv支撑表")
	private List<RunningUutProp> runningUutPropList;

}
