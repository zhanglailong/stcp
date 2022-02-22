package org.jeecg.modules.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.entity.EvnRunningVo;

import java.util.List;

/**
 * @Description: 测试环境定制与管理
 * @Author: jeecg-boot
 * @Date: 2020-12-23
 * @Version: V1.0
 */
public interface EnvCustomizedMapper extends BaseMapper<EnvCustomized> {
    /**
     * 项目+环境 关联查询
     * @param envCustomized
     * @return list
     */
    List<EvnRunningVo> findByPage(EvnRunningVo envCustomized);
}
