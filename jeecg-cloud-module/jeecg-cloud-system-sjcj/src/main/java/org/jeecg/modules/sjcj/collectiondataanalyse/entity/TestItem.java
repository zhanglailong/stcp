package org.jeecg.modules.sjcj.collectiondataanalyse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 29项测试项
 * @Author: jeecg-boot
 * @Date:   2021-01-22
 * @Version: V1.0
 */
@Data
@TableName("test_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="test_item对象", description="29项测试项")
public class TestItem implements Serializable {
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
	/**英文名*/
	@Excel(name = "英文名", width = 15)
    @ApiModelProperty(value = "英文名")
    private String englishName;
	/**中文名*/
	@Excel(name = "中文名", width = 15)
    @ApiModelProperty(value = "中文名")
    private String chineseName;
	/**测试工具id*/
	@Excel(name = "测试工具id", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
	@Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    @ApiModelProperty(value = "测试工具id")
    private String toolsId;
	/**缩写*/
	@Excel(name = "缩写", width = 15)
    @ApiModelProperty(value = "缩写")
    private String abbr;
}
