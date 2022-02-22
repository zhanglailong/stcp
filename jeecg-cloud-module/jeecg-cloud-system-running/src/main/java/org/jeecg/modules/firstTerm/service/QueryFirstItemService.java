package org.jeecg.modules.firstTerm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.running.uut.entity.RunningUutManager;

import java.util.List;
/**
 * @Author: test
 * */
public interface QueryFirstItemService extends IService<RunningUutManager> {


    /**
     * 查询 - 查询我的审批  admin账号
     *
     * @return
     */
    List<RunningUutManager> getMyApproval();

    /**
     * 查询 - 查询我的审批 其他账号 当前登录人
     * @param  userId true
     * @return list集合
     */
    List<RunningUutManager> getOtherMyApproval(String userId);


    /**
     * 查询 - 查询项目统计 admin账号
     *
     * @return list集合
     */
    List<RunningProject> getProjectNums();


    /**
     * 查询 - 查询项目统计 其余账号 当前登录人
     * @param userId true
     * @return list集合
     */
    List<RunningProject> getOtherProjectNums(String userId);


    /**
     * 查询 - 查询我的任务  admin账号 查询全部
     *
     * @return list集合
     */
    List<RunningTask> getMyTaskNums();


    /**
     * 查询 - 查询我的任务  其余账号 当前登录人
     * @param userId true
     * @return list集合
     */
    List<RunningTask> getOtherMyTaskNums(String userId);

}
