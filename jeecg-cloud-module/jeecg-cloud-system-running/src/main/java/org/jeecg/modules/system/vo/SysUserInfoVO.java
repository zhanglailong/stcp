package org.jeecg.modules.system.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.util.Date;

/**
 * 用于其他系统同步用户数据
 */
@Data
/**
 * @Author: test
 * */
public class SysUserInfoVO {
    /**
     * 登录账号
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
       /* @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")*/
        /**@DateTimeFormat(pattern = "yyyy-MM-dd")*/
    private Date birthday;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 性别名称
     */
    private String sexName;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 电话
     */
    private String phone;


    /**
     * 状态
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 工号，唯一键
     */
    private String workNo;

    /**
     * 职务，关联职务表
     */
    @Dict(dictTable = "sys_position", dicText = "name", dicCode = "code")
    private String post;

    /**
     * 座机号
     */
    private String telephone;


}
