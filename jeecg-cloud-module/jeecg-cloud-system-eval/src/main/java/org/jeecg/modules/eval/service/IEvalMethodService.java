package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.EvalMethod;
import org.jeecg.modules.eval.entity.EvalMethodInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: eval_method
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface IEvalMethodService extends IService<EvalMethod> {

    /**
     * 添加一对多
     * @param evalMethodInfoList true
     * @return 无返回值
     */
    void saveMain(EvalMethod evalMethod, List<EvalMethodInfo> evalMethodInfoList);

    /**
     * 修改一对多
     * @param evalMethodInfoList true
     * @return 无返回值
     */
    void updateMain(EvalMethod evalMethod, List<EvalMethodInfo> evalMethodInfoList);

    /**
     * 删除一对多
     * @param id true
     * @return 无返回值
     */
    void delMain(String id);

    /**
     * 批量删除一对多
     * @param idList true
     * @return 无返回值
     */
    void delBatchMain(Collection<? extends Serializable> idList);

}
