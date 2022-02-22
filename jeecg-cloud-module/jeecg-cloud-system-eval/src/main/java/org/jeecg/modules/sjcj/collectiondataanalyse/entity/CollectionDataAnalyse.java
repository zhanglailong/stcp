package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

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
 * @Description: 采集数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Data
@TableName("collection_data_loadrunner_analyse")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="collection_data_analyse对象", description="采集数据分析")
public class CollectionDataAnalyse implements Serializable {
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
	/**数据结果类型*/
	@Excel(name = "数据结果类型", width = 15)
    @ApiModelProperty(value = "数据结果类型")
    @TableField(value = "result_data")
    private String resultData;

    /**ip*/
    @ApiModelProperty(value = "ip")
    private String ip;

    /**数据结果类型*/
    @Excel(name = "数据类型", width = 15)
    @ApiModelProperty(value = "数据类型")
    @TableField(value = "data_type")
    private String dataType;

    /**数据结果类型*/
    @Excel(name = "测试工具id", width = 15)
    @ApiModelProperty(value = "测试工具id")
    @TableField(value = "tools_id")
    private String toolsId;

}
