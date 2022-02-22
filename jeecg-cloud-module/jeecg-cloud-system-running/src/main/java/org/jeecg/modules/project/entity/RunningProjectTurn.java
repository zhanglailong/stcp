package org.jeecg.modules.project.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class RunningProjectTurn {
    /**id*/
    @Excel(name = "id", width = 15)
    @ApiModelProperty(value = "id")
    private String id;
    /**projectId*/
    @Excel(name = "projectId", width = 15)
    @ApiModelProperty(value = "projectId")
    private String projectId;

    /**轮次*/
    @ApiModelProperty(value = "轮次")
    private String turnNum;

    /**备注*/
    @ApiModelProperty(value = "备注")
    private String comment;

    /**项目版本*/
    @TableField(exist = false)
    List<RunningProjectTurnVersion> uutTurnVersion;

    /**已选版本结果拼接字符串*/
    @TableField(exist = false)
    String versionStr;
}
