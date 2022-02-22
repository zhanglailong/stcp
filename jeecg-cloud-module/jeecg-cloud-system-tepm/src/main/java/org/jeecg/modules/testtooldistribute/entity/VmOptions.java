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
@ApiModel(value="虚拟机及其子测试工具集合", description="虚拟机及其子测试工具集合")
public class VmOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "虚拟机id")
    String id;
    @ApiModelProperty(value = "虚拟机名称")
    String name;
    @ApiModelProperty(value = "测试工具集合")
    List<TestToolOptions> testToolOptions;

    public VmOptions(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
