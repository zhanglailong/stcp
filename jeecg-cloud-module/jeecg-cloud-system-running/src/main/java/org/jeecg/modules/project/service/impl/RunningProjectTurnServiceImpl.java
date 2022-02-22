package org.jeecg.modules.project.service.impl;

import org.jeecg.modules.project.entity.RunningProjectTurn;
import org.jeecg.modules.project.mapper.RunningProjectTurnMapper;
import org.jeecg.modules.project.service.IRunningProjectTurnService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: running_project_turn
 * @Author: jeecg-boot
 * @Date:   2021-07-21
 * @Version: V1.0
 */
@Service
public class RunningProjectTurnServiceImpl extends ServiceImpl<RunningProjectTurnMapper, RunningProjectTurn> implements IRunningProjectTurnService {

    @Override
    public void deleteByProjectId(String projectId) {
        this.getBaseMapper().deleteByProjectId(projectId);
    }

    @Override
    public List<String> getIdsByProjectId(String projectId) {
        return this.getBaseMapper().getIdsByProjectId(projectId);
    }
}
