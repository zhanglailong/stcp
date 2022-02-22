package org.jeecg.modules.Imageotherinfo.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 镜像其他软件，版本信息，测试工具关联表
 * @Author: jeecg-boot
 * @Date:   2021-07-13
 * @Version: V1.0
 */
@Data
@TableName("tepm_image_other_info")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="image_other_info对象", description="镜像其他软件，版本信息，测试工具关联表")
public class ImageOtherInfo implements Serializable {
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
	/**镜像id*/
	@Excel(name = "镜像id", width = 15)
    @ApiModelProperty(value = "镜像id")
    private String openstackId;
	/**镜像名称*/
	@Excel(name = "镜像名称", width = 15)
    @ApiModelProperty(value = "镜像名称")
    private String imageName;
	/**镜像名称，关联镜像表里面name*/
	@Excel(name = "镜像名称，关联镜像表里面name", width = 15)
    @ApiModelProperty(value = "镜像名称，关联镜像表里面name")
    private String imageInnerName;
	/**系统版本*/
	@Excel(name = "系统版本", width = 15)
    @ApiModelProperty(value = "系统版本")
    private String systemVersion;
	/**测试工具id（单个）*/
	@Excel(name = "测试工具id（单个）", width = 15)
    @ApiModelProperty(value = "测试工具id（单个）")
    private String testToolId;
	/**测试工具名称（单个）*/
	@Excel(name = "测试工具名称（单个）", width = 15)
    @ApiModelProperty(value = "测试工具名称（单个）")
    private String testToolName;
	/**其他软件（单个）*/
	@Excel(name = "其他软件（单个）", width = 15)
    @ApiModelProperty(value = "其他软件（单个）")
    private String otherSoftware;

    /**
     * 虚拟删除
     */
    @Excel(name = "虚拟删除", width = 15)
    @ApiModelProperty(value = "虚拟删除")
    @TableLogic(value = "0", delval = "1")
    private Integer idel;
}
