package org.jeecg.modules.testtooldistribute.entity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.List;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="环境以及其子虚拟机测试工具集合", description="环境以及其子虚拟机测试工具集合")
public class EnvironmentOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "环境id")
    String id;
    @ApiModelProperty(value = "环境名称")
    String name;
    @ApiModelProperty(value = "虚拟机集合")
    List<VmOptions> vmOptionsList;
}
