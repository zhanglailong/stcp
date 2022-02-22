package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.EvalMethodInfo;
import org.jeecg.modules.eval.mapper.EvalMethodInfoMapper;
import org.jeecg.modules.eval.service.IEvalMethodInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: eval_method_info
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Service
public class EvalMethodInfoServiceImpl extends ServiceImpl<EvalMethodInfoMapper, EvalMethodInfo> implements IEvalMethodInfoService {

    @Override
    public List<EvalMethodInfo> selectByMainId(String mainId) {
        return this.getBaseMapper().selectByMainId(mainId);
    }

    @Override
    public String getEvaluate(String methodId, Double score) {
        return this.getBaseMapper().getEvaluate(methodId, score);
    }
}
