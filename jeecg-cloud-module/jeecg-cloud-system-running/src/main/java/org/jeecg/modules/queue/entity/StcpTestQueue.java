package org.jeecg.modules.queue.entity;

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
 * @Description: 云化工具分时使用列表
 * @Author: jeecg-boot
 * @Date:   2020-12-25
 * @Version: V1.0
 */
@Data
@TableName("stcp_test_queue")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="stcp_test_queue对象", description="云化工具分时使用列表")
public class StcpTestQueue implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键")
    private Long id;
	/**任务id*/
	@Excel(name = "任务id", width = 15)
    @ApiModelProperty(value = "任务id")
    private String taskId;
	/**任务归属人*/
	@Excel(name = "任务归属人", width = 15)
    @ApiModelProperty(value = "任务归属人")
    private String taskUser;
	/**执行客户端ip*/
	@Excel(name = "执行客户端ip", width = 15)
    @ApiModelProperty(value = "执行客户端ip")
    private String clientIp;
	/**client的id*/
	@Excel(name = "client的id", width = 15)
    @ApiModelProperty(value = "client的id")
    private String clientId;
	/**测试类型*/
	@Excel(name = "测试类型", width = 15)
    @ApiModelProperty(value = "测试类型")
    private String taskType;
	/**测试状态*/
	@Excel(name = "测试状态", width = 15)
    @ApiModelProperty(value = "测试状态")
    private Integer status;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
    @ApiModelProperty(value = "被测件地址")
	private String taskUrl;
    
    @Excel(name = "任务名", width = 15)
    @ApiModelProperty(value = "任务名")
    private String taskName;
    @Excel(name = "任务标识", width = 15)
    @ApiModelProperty(value = "任务标识")
    private String taskCode;
    /**测试状态*/
    @Excel(name = "执行顺序", width = 15)
    @ApiModelProperty(value = "执行顺序")
    private Integer testOrder;
    /**测试状态*/
    @Excel(name = "优先级", width = 15)
    @ApiModelProperty(value = "优先级")
    private Integer priority;
}
