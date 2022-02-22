package org.jeecg.modules.number.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.number.entity.NumberRuleInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Mapper
public interface NumberRuleInfoMapper extends BaseMapper<NumberRuleInfo> {

}
