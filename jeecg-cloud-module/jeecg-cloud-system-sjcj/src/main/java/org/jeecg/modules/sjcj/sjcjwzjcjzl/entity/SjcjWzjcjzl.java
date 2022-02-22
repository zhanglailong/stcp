package org.jeecg.modules.sjcj.sjcjwzjcjzl.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * @Description: 数据采集位置及采集种类
 * @Author: jeecg-boot
 * @Date:   2020-12-29
 * @Version: V1.0
 */
@Data
@TableName("sjcj_wzjcjzl")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sjcj_wzjcjzl对象", description="数据采集位置及采集种类")
public class SjcjWzjcjzl implements Serializable {
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
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String remark;
	/**采集类型*/
	@Excel(name = "采集类型", width = 15, dicCode = "typeOfCollection")
	@Dict(dicCode = "typeOfCollection")
    @ApiModelProperty(value = "采集类型")
    private String typeOfCollection;
	/**工具ID*/
	@Excel(name = "工具ID", width = 15, dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
	@Dict(dictTable = "running_tools_list", dicText = "tools_name", dicCode = "id")
    @ApiModelProperty(value = "工具ID")
    private String toolsId;
	/**配置采集路径/自定义采集路径*/
	@Excel(name = "配置采集路径/自定义采集路径", width = 15)
    @ApiModelProperty(value = "配置采集路径/自定义采集路径")
    private String pzurl;
	/**配置采集文件种类/自定义采集文件种类*/
	@Excel(name = "配置采集文件种类/自定义采集文件种类", width = 15)
    @ApiModelProperty(value = "配置采集文件种类/自定义采集文件种类")
    private String pzcjwjzl;
	/**过程采集路径*/
	@Excel(name = "过程采集路径", width = 15)
    @ApiModelProperty(value = "过程采集路径")
    private String gcurl;
	/**过程采集文件种类*/
	@Excel(name = "过程采集文件种类", width = 15)
    @ApiModelProperty(value = "过程采集文件种类")
    private String gccjwjzl;
	/**结果采集路径*/
	@Excel(name = "结果采集路径", width = 15)
    @ApiModelProperty(value = "结果采集路径")
    private String jgurl;
	/**结果采集文件种类*/
	@Excel(name = "结果采集文件种类", width = 15)
    @ApiModelProperty(value = "结果采集文件种类")
    private String jgcjwjzl;
	/**创建人所属部门*/
	@Excel(name = "创建人所属部门", width = 15, dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
	@Dict(dictTable = "sys_depart", dicText = "depart_name", dicCode = "id")
    @ApiModelProperty(value = "创建人所属部门")
    private String departmentId;
}
