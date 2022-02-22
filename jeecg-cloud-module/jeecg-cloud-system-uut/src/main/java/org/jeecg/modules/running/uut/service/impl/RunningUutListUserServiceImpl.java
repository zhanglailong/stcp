package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.running.uut.entity.RunningUutListUser;
import org.jeecg.modules.running.uut.mapper.RunningUutListUserMapper;
import org.jeecg.modules.running.uut.service.IRunningUutListUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description: 被测对象出库
 * @Author: jeecg-boot
 * @Date:   2020-12-17
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutListUserServiceImpl extends ServiceImpl<RunningUutListUserMapper, RunningUutListUser> implements IRunningUutListUserService {

    @Autowired
    private RunningUutListUserMapper runningUutListUserMapper;

    @Override
    public List<RunningUutListUser> getUutUserById(String id) {
        return runningUutListUserMapper.getUutUserById(id);
    }

}
