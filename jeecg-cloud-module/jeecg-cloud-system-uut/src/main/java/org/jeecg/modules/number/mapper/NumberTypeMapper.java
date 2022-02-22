package org.jeecg.modules.number.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.number.entity.NumberType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 编号管理表
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Mapper
public interface NumberTypeMapper extends BaseMapper<NumberType> {

}
