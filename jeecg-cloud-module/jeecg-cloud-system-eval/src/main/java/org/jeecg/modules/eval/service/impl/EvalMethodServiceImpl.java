package org.jeecg.modules.eval.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.EvalMethod;
import org.jeecg.modules.eval.entity.EvalMethodInfo;
import org.jeecg.modules.eval.mapper.EvalMethodInfoMapper;
import org.jeecg.modules.eval.mapper.EvalMethodMapper;
import org.jeecg.modules.eval.service.IEvalMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: eval_method
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Service
public class EvalMethodServiceImpl extends ServiceImpl<EvalMethodMapper, EvalMethod> implements IEvalMethodService {

    @Autowired
    private EvalMethodMapper evalMethodMapper;
    @Autowired
    private EvalMethodInfoMapper evalMethodInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(EvalMethod evalMethod, List<EvalMethodInfo> evalMethodInfoList) {
        evalMethodMapper.insert(evalMethod);
        if (evalMethodInfoList != null && evalMethodInfoList.size() > 0) {
            for (EvalMethodInfo entity : evalMethodInfoList) {
                //外键设置
                entity.setMethodId(evalMethod.getId());
                evalMethodInfoMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(EvalMethod evalMethod, List<EvalMethodInfo> evalMethodInfoList) {
        evalMethodMapper.updateById(evalMethod);

        //1.先删除子表数据
        evalMethodInfoMapper.deleteByMainId(evalMethod.getId());

        //2.子表数据重新插入
        if (evalMethodInfoList != null && evalMethodInfoList.size() > 0) {
            for (EvalMethodInfo entity : evalMethodInfoList) {
                //外键设置
                entity.setMethodId(evalMethod.getId());
                //更新者默认为创建者
                entity.setUpdateBy(evalMethod.getCreateBy());
                //更新时间默认为当前时间戳
                entity.setUpdateTime(DateUtil.date());
                evalMethodInfoMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        evalMethodInfoMapper.deleteByMainId(id);
        evalMethodMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for (Serializable id : idList) {
            evalMethodInfoMapper.deleteByMainId(id.toString());
            evalMethodMapper.deleteById(id);
        }
    }

}
