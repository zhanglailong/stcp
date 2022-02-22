package org.jeecg.modules.system.entity;

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
 * @Description: 项目权限表
 * @Author: jeecg-boot
 * @Date:   2021-07-04
 * @Version: V1.0
 */
@Data
@TableName("sys_permission_project")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="sys_permission_project对象", description="项目权限表")
public class SysPermissionProject implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**sys_permission表id*/
	@Excel(name = "sys_permission表id", width = 15)
    @ApiModelProperty(value = "sys_permission表id")
    private String permissionId;
	/**sys_role表id*/
	@Excel(name = "sys_role表id", width = 15)
    @ApiModelProperty(value = "sys_role表id")
    private String roleId;
	/**sys_role表id*/
	@Excel(name = "running_project表id", width = 15)
    @ApiModelProperty(value = "running_project表id")
    private String projectId;
	/**running_project表id*/
	@Excel(name = "操作ip", width = 15)
    @ApiModelProperty(value = "操作ip")
    private String operateIp;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "操作日期")
    private Date operateDate;
	
	public SysPermissionProject() {
   	}
       
   	public SysPermissionProject(String roleId, String permissionId, String projectId) {
   		this.roleId = roleId;
   		this.permissionId = permissionId;
   		this.projectId = projectId;
   	}
}
