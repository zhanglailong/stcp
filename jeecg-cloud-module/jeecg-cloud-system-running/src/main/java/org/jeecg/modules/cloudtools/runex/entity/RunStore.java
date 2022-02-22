package org.jeecg.modules.cloudtools.runex.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: xrun工具使用ip库
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Data
@TableName("run_store")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="run_store对象", description="工具使用库")
public class RunStore implements Serializable {
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
	/**ip库名*/
	@Excel(name = "ip库名", width = 15)
    @ApiModelProperty(value = "ip库名")
    private String name;
	/**ip库版本*/
	@Excel(name = "ip库版本", width = 15)
    @ApiModelProperty(value = "ip库版本")
    private Integer version;
	/**库类型（1共有，2专属）*/
	@Excel(name = "库类型（1共有，2专属）", width = 15)
    @ApiModelProperty(value = "库类型（1共有，2专属）")
    private Integer type;
	/**地址*/
	@Excel(name = "地址", width = 15)
    @ApiModelProperty(value = "地址")
    private String url;
	/**所属项目id*/
	@Excel(name = "父id", width = 15)
    @ApiModelProperty(value = "父id")
    private String parentId;
	/**所属项目名*/
	@Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private Integer storeOrder;
	/**描述*/
	@Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private String des;
	@TableField(exist = false)
	private List<RunStore> children;
	@TableField(exist = false)
	private boolean isLeaf;

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}
