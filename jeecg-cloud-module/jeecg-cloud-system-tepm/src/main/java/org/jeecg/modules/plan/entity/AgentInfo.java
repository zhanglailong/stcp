package org.jeecg.modules.plan.entity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.common.enums.ServerStatusEnum;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AgentInfo {
    /**
     * 客户端ip
     */
    @ApiModelProperty(value = "客户端ip")
    private String agentIp;

    /**
     * 环境Id
     */
    @ApiModelProperty(value = "环境Id")
    private String envirId;


    /**
     * 虚拟机名称
     */
    @ApiModelProperty(value = "虚拟机名称")
    private String vmName;

    /**
     * 测试工具Id,以逗号分割
     */
    @ApiModelProperty(value = "测试工具Id")
    private String testToolId;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private String projectId;


    /**
     * ACTIVE  运行BUILD
     * 创建RESIZE 调整大小或迁移SHUTOFF
     * 关机PAUSED 暂停SUSPENDED
     * 挂起ERROR
     * 错误SUSPEND_IN_PROGRESS
     * 挂起中RESUME_IN_PROGRESS
     * 恢复挂起中
     */
    @ApiModelProperty(value = "虚拟机状态")
    private String status;

}
