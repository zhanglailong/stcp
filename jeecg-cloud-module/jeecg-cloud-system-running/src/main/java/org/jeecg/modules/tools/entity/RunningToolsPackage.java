package org.jeecg.modules.tools.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import javax.persistence.Entity;
import java.util.Date;

@Data
@TableName("running_tools_package")
public class RunningToolsPackage {

    private String id;

    private String toolId;

    private String version;

    @Dict(dicCode="toolsPackageStatus")
    private Integer disabled;
    @Dict(dictTable = "running_upload_form", dicText = "name", dicCode = "id")
    private String address;

    private Date createTime;

    private Date updateTime;

    private String createBy;

    @Dict(dicCode = "toolsPackageDel")
    private Integer delFlag;

    private Long size;

    @TableField(exist = false)
    private String name;
    @TableField(exist = false)
    private String sizeStr;

    private Integer def;

    @TableField(exist = false)
    private String num;



}
