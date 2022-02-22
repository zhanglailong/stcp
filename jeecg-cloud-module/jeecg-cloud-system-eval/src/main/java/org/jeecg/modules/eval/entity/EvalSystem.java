package org.jeecg.modules.eval.entity;

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
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: eval_system
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Data
@TableName("eval_system")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="eval_system对象", description="eval_system")
public class EvalSystem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**评价体系名称*/
	@Excel(name = "评价体系名称", width = 15)
    @ApiModelProperty(value = "评价体系名称")
    private String systemName;
	/**体系模型*/
	@Excel(name = "体系模型", width = 15)
    @ApiModelProperty(value = "体系模型")
    private String systemType;
	/**权重分配状态 0：未分配； 1：已分配*/
	@Excel(name = "权重分配状态 0：未分配； 1：已分配", width = 15)
    @ApiModelProperty(value = "权重分配状态 0：未分配； 1：已分配")
    private Integer weightDone;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
    /**评价方法id*/
    @ApiModelProperty(value = "评价方法id")
    private String methodId;
    /**备注*/
    @ApiModelProperty(value = "备注")
    private String comment;
    //被测对象列表
    @TableField(exist = false)
	private List<RunningUutList> uutList;
    //度量树列表
    @TableField(exist = false)
    private List<CaseTreeIdModel> treeList;
    //选中的度量id
    @TableField(exist = false)
    private List<String> selectedRowKeys;
}
