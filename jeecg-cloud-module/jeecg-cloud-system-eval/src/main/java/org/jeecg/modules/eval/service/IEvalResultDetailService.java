package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalResultDetail;

import java.util.List;

/**
 * @Description: 分析结果详情表
 * @Author: jeecg-boot
 * @Date:   2020-12-31
 * @Version: V1.0
 */
public interface IEvalResultDetailService extends IService<EvalResultDetail> {
    /**
     * 获取树状列表
     * @param systemId true
     * @param resultId true
     * @return 无返回值
     */
    List<CaseTreeIdModel> queryTreeList(String systemId, String resultId);
}
