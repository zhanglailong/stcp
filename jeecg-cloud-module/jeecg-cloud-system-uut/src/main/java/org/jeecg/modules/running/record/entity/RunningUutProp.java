package org.jeecg.modules.running.record.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 被测对象kv支撑表
 * @Author: jeecg-boot
 * @Date:   2021-02-05
 * @Version: V1.0
 */
@ApiModel(value="running_uut_record对象", description="被测对象属性表")
@Data
@TableName("running_uut_prop")
public class RunningUutProp implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "id")
	private String id;
	/**审核记录表id*/
	@ApiModelProperty(value = "审核记录表id")
	private String recordId;
	/**标识id*/
	@Excel(name = "标识id", width = 15)
	@ApiModelProperty(value = "标识id")
	private String propCode;
	/**标识名*/
	@Excel(name = "标识名", width = 15)
	@ApiModelProperty(value = "标识名")
	private String propName;
	/**值*/
	@Excel(name = "值", width = 15)
	@ApiModelProperty(value = "值")
	private String propValue;
	/**类型*/
	@Excel(name = "类型", width = 15)
	@ApiModelProperty(value = "类型")
	private String propType;
	/**状态*/
	@Excel(name = "状态", width = 15)
	@ApiModelProperty(value = "状态")
	private String propStatus;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
}
