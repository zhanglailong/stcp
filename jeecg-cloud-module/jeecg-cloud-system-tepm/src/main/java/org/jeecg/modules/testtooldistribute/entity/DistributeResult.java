package org.jeecg.modules.testtooldistribute.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * 分发结果
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class DistributeResult {
    @ApiModelProperty(value = "是否成功")
    boolean isSucceed;

    @ApiModelProperty(value = "虚拟机id")
    String vmId;
    @ApiModelProperty(value = "虚拟机名称")
    String vmName;
    /**
     * 分发状态
     */
    @Excel(name = "分发状态", width = 15)
    @ApiModelProperty(value = "分发状态")
    private Integer distributeState;

    @ApiModelProperty(value = "失败原因")
    private String message;

}
