package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;

import java.util.List;

/**
 * @Description: eval_measure_weight
 * @Author: jeecg-boot
 * @Date: 2021-02-25
 * @Version: V1.0
 */
public interface IEvalMeasureWeightService extends IService<EvalMeasureWeight> {
    /**
     * 树状分页
     * @param systemId true
     * @return 无返回值
     * */
    List<CaseTreeIdModel> queryTreeList(String systemId);
    /**
     * 存储
     * @param systemId true
     * @param list true
     * @return 无返回值
     * */
    boolean saveList(List<CaseTreeIdModel> list,String systemId);
    /**
     * 树状分页
     * @param systemId true
     * @param caseTreeIdModel true
     * @return 无返回值
     * */
    boolean deleteWeight(String systemId,CaseTreeIdModel caseTreeIdModel);

    /**
     * 根据systemId 查询所有实体
     * @param systemId
     * @return
     */
    public List<EvalMeasureWeight> getListBySystemId(String systemId);
}
