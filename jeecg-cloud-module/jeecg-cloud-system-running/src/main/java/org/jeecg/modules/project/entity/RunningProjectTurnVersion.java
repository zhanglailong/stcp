package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class RunningProjectTurnVersion {
    /**id*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String id;
    /**轮次*/
    @Excel(name = "turnId", width = 15)
    @ApiModelProperty(value = "轮次")
    private String turnId;

    /**项目版本*/
    @ApiModelProperty(value = "项目版本")
    private String versionId;

}
