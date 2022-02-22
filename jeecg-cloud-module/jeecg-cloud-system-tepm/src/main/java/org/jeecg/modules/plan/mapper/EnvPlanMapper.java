package org.jeecg.modules.plan.mapper;

import java.util.List;

import org.jeecg.modules.plan.entity.EnvPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * @author zlf
 * @Description: 环境设计
 * @Date: 2020-12-29
 * @Version: V1.0
 */
@Component
public interface EnvPlanMapper extends BaseMapper<EnvPlan> {

    /**
     * 根据id集合查找环境规划集合
     *
     * @param ids
     * @return
     */
    List<EnvPlan> getPlanListByIds(List<String> ids);
}
