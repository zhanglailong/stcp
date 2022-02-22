package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.EvalMethodInfo;

import java.util.List;

/**
 * @Description: eval_method_info
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface IEvalMethodInfoService extends IService<EvalMethodInfo> {
    /**
     * 通过主ID查询
     * @param mainId true
     * @return 无返回值
     * */
    List<EvalMethodInfo> selectByMainId(String mainId);

    /**
     * 查询评价
     * @param methodId true
     * @param score true
     * @return String
     * */
    String getEvaluate(String methodId, Double score);
}
