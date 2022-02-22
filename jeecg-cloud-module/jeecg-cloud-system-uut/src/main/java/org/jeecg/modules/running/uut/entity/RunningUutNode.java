package org.jeecg.modules.running.uut.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @Description: 被测对象流程节点表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@ApiModel(value="running_uut_catagory对象", description="被测对象流程分类表")
@Data
@TableName("running_uut_node")
public class RunningUutNode implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "id")
	private String id;
	/**流程分类表id*/
	@ApiModelProperty(value = "流程分类表id")
	private String catagoryId;
	/**节点标识*/
	@Excel(name = "节点标识", width = 15)
	@ApiModelProperty(value = "节点标识")
	private String code;
	/**节点名称*/
	@Excel(name = "节点名称", width = 15)
	@ApiModelProperty(value = "节点名称")
	private String name;
	/**上一个节点id*/
	@Excel(name = "上一个节点id", width = 15)
	@ApiModelProperty(value = "上一个节点id")
	private String preNode;
	/**下一个节点id*/
	@Excel(name = "下一个节点id", width = 15)
	@ApiModelProperty(value = "下一个节点id")
	private String nextNode;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**以下是新增伪列*/
	@TableField(exist = false)
	/**上一节点名称*/
	private String preNodeDictText;

	@TableField(exist = false)
	/**下一节点名称*/
	private String nextNodeDictText;
}
