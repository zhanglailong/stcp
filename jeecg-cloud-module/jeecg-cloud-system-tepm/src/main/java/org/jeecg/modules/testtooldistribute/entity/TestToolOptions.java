package org.jeecg.modules.testtooldistribute.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author yeyl
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="测试工具集合", description="测试工具集合")
public class TestToolOptions implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "测试工具id")
    String id;
    @ApiModelProperty(value = "测试工具名称")
    String name;

    public TestToolOptions(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
