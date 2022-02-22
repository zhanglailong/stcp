package com.sqa.chn.modules.access.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * Access相关全局变量
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("running_access_global")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class RunningAccessGlobal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 键名
     */
    @TableId(value = "`key`", type = IdType.NONE)
    private String key;

    /**
     * 值
     */
    private String value;


}
