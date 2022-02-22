package org.jeecg.modules.plan.service;

import org.jeecg.modules.plan.entity.EnvPlan;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.plan.entity.EnvPlanJson;

import java.util.List;

/**
 * @author zlf
 * @Description: 环境设计
 * @Date: 2020-12-29
 * @Version: V1.0
 */
public interface IEnvPlanService extends IService<EnvPlan> {
    /**
     * 根据id查找环境规划
     *
     * @param id
     * @return
     */
    EnvPlan selectById(String id);

    /**
     * 根据id集合查找环境集合
     *
     * @param ids
     * @return
     */
    List<EnvPlan> getPlanListByIds(List<String> ids);

    /**
     * 分析规划形成Open Stack json数据
     *
     * @param envPlanJson
     * @param planJson
     * @return
     * @throws Exception
     */
    Boolean getAnalysisEnvPlan(EnvPlanJson envPlanJson, String planJson) throws Exception;
}
