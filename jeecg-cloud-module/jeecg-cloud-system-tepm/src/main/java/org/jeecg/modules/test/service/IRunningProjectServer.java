package org.jeecg.modules.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.test.entity.RunningProject;

import java.util.List;

/**
 * @author shx
 * 项目管理
 * 2021-07-06
 * V1.0
 */
public interface IRunningProjectServer extends IService<RunningProject> {
    /**
     * 获取项目下拉列表
     * @return list
     */
    List<RunningProject> getProjectList();
}
