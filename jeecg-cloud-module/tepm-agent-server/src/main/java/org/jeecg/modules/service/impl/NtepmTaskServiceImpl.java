package org.jeecg.modules.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.entity.NtepmTask;
import org.jeecg.modules.mapper.NtepmTaskMapper;
import org.jeecg.modules.service.INtepmTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试任务
 * @author zlf
 * @date   2021-05-14
 * @version V1.0
 */
@Service
public class NtepmTaskServiceImpl extends ServiceImpl<NtepmTaskMapper, NtepmTask> implements INtepmTaskService {
    @Resource
    NtepmTaskMapper ntepmTaskMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void delete(String id) {
        int flag = ntepmTaskMapper.deleteById(id);
        if (flag==CommonConstant.DATA_INT_0) {
            throw new UnsupportedOperationException("删除失败!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteBatchIds(List<String> ids) {
        int flag = ntepmTaskMapper.deleteBatchIds(ids);
        if (flag==CommonConstant.DATA_INT_0) {
            throw new UnsupportedOperationException("删除失败!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public List<NtepmTask> getTasksByOrderId(String orderId) {
        QueryWrapper<NtepmTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(CommonConstant.DATA_STRING_ORDER_ID,orderId);
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL,CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void addNtepmTask(NtepmTask ntepmTask) {
        if (!this.save(ntepmTask)) {
            throw new UnsupportedOperationException("添加任务失败!");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void edit(NtepmTask ntepmTask) {
        if (!this.updateById(ntepmTask)) {
            throw new UnsupportedOperationException("修改失败!");
        }
    }

}
