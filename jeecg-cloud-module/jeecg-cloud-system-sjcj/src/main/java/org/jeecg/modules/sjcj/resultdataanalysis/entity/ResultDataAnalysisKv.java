package org.jeecg.modules.sjcj.resultdataanalysis.entity;

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

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 结果数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
@Data
@TableName("result_data_analysis_kv")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="result_data_analysis_kv对象", description="结果数据分析键值对")
public class ResultDataAnalysisKv implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**结果分析数据*/
	@Excel(name = "结果数据分析表ID")
    @ApiModelProperty(value = "结果数据分析表ID")
    private String resultId;
	/**键*/
	@Excel(name = "键")
    @ApiModelProperty(value = "键")
    private String code;
    /**键*/
    @Excel(name = "值")
    @ApiModelProperty(value = "值")
    private String value;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    public ResultDataAnalysisKv(String resultId,String code,String value,Date createTime) {
        this.resultId = resultId;
        this.code = code;
        this.value = value;
        this.createTime = createTime;
    }
}
