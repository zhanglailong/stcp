package org.jeecg.modules.firstTerm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.project.entity.RunningProject;

import java.util.Map;
/**
 * @Author: test
 * */
public interface ProManageService extends IService<RunningProject> {

    /**
     * 查询 - 查询项目数量、已过期数
     *
     * @param
     * @return
     */
    Map<String,Object> getRelatedCount();

    /**
     * 查询 - 未关闭项目数、已过期数
     *
     * @param
     * @return
     */
    Map<String,Object> getProjectNotClose();

    /**
     * 查询 - 待审批项目数量、已过期数  admin账号
     *
     * @param
     * @return
     */
    Map<String,Object> getApprovalProNums();

    /**
     * 查询 - 待审批项目数量、已过期数  其余账号
     * @param userId
     * @return
     */
    Map<String,Object> getOtherCode(String userId);

    /**
     * 查询 - 任务数量、已过期数
     *
     * @param
     * @return
     */
    Map<String,Object> getTaskNums();

}
