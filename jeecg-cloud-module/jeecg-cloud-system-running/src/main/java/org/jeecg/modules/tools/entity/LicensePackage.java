package org.jeecg.modules.tools.entity;

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
 * @Description: 证书版本表
 * @Author: jeecg-boot
 * @Date:   2021-07-28
 * @Version: V1.0
 */
@Data
@TableName("license_package")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="license_package对象", description="证书版本表")
public class LicensePackage implements Serializable {
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
	/**对应license*/
	@Excel(name = "对应license", width = 15)
    @ApiModelProperty(value = "对应license")
    private String licenseId;
	/**证书地址*/
	@Excel(name = "证书地址", width = 15)
    @ApiModelProperty(value = "证书地址")
    private String licFile;
	/**网段限制*/
	@Excel(name = "网段限制", width = 15)
    @ApiModelProperty(value = "网段限制")
    private String netList;
	/**硬件key*/
	@Excel(name = "硬件key", width = 15)
    @ApiModelProperty(value = "硬件key")
    private String keyList;
	/**物理地址*/
	@Excel(name = "物理地址", width = 15)
    @ApiModelProperty(value = "物理地址")
    private String macList;
	/**证书数量*/
	@Excel(name = "证书数量", width = 15)
    @ApiModelProperty(value = "证书数量")
    private Integer count;

	private String version;

	private Integer def;
}
