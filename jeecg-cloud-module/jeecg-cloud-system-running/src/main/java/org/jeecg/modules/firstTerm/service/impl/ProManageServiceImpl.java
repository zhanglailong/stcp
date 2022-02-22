package org.jeecg.modules.firstTerm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.firstTerm.service.ProManageService;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.mapper.RunningProjectMapper;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.RunningTaskMapper;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.running.uut.entity.RunningUutManager;
import org.jeecg.modules.running.uut.service.IRunningUutManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
/**
 * @Author: test
 * */
public class ProManageServiceImpl extends ServiceImpl<RunningProjectMapper, RunningProject> implements ProManageService {

    @Autowired
    private IRunningUutManagerService runningUutManagerService;

    @Autowired
    private RunningTaskMapper runningTaskMapper;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询 - 项目数量 已过期数
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getRelatedCount() {
        // 查询项目数量
        Integer projectNums = 0;
        // 当前时间
        Long dateTime = System.currentTimeMillis();
        // 已过期数量
        Integer pastNums = 0;
        QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", "0");
        List<RunningProject> list = this.getBaseMapper().selectList(queryWrapper);
        // 判断是否为null值 或 项目数大于0
        if (list != null && list.size() > 0) {
            // 项目数量
            projectNums = list.size();
            for (RunningProject runProject : list) {
                // 结束时间
                Long finishTime = runProject.getFinishDate().getTime();
                if (dateTime > finishTime && runProject.getFinishStatus()==0) {
                    pastNums++;
                }
            }
        }
        // 创建map集合
        HashMap<String, Object> returnMap = new HashMap<>(2000);
        // 添加项目数量
        returnMap.put("projectNum", projectNums);
        // 添加已过期数
        returnMap.put("pastNums", pastNums);
        return returnMap;
    }


    /**
     * 查询 - 未关闭的项目数、已过期数
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getProjectNotClose() {
        // 查询未关闭数量
        Integer notProjectClose = 0;
        // 查询已过期数
        Integer pastNums = 0;
        // 当前时间
        Long dateTime = System.currentTimeMillis();
        QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        List<RunningProject> list = this.getBaseMapper().selectList(queryWrapper);
        // 判断是否为null值
        if (list != null && list.size() > 0) {
            for (RunningProject runProject : list) {
                // 查询未关闭数量  状态是 1 的情况下
                Integer notProjectNum = runProject.getFinishStatus();
                if (notProjectNum==0) {
                    notProjectClose++;
                }
                // 结束时间
                Long finishTime = runProject.getFinishDate().getTime();
                // 查询已过期数
                if (dateTime > finishTime && runProject.getFinishStatus() == 0) {
                    pastNums++;
                }
            }
        }
        // 创建map集合
        HashMap<String, Object> returnMap = new HashMap<>(2000);
        // 添加未关闭项目数
        returnMap.put("notProjectClose", notProjectClose);
        // 添加已过期数
        returnMap.put("pastNums", pastNums);
        return returnMap;
    }

    /**
     * 查询 - 待审批数量、已过期数量   账号admin
     *
     * @return
     */
    @Override
    public Map<String, Object> getApprovalProNums() {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        // 查询待审批任务数
        QueryWrapper<RunningUutManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("finish_time");
        if(roles.contains("uutManager")){
            queryWrapper.eq("assignee", "role:uutManager");
        }else{
            queryWrapper.eq("assignee", sysUser.getId());
        }
        // 待审批数量
        Integer approvalNums = runningUutManagerService.getBaseMapper().selectCount(queryWrapper);

        // 今日完结数量
        Integer unfinishedNums = 0;


        // 创建HashMap
        HashMap<String, Object> resultMap = new HashMap<>(2000);
        // 添加待审批数量
        resultMap.put("approvalNums", approvalNums);
        // 添加未完结数量
        resultMap.put("unfinishedNums", unfinishedNums);
        return resultMap;
    }

    /**
     * 查询 - 待审批数量、今日已审批   其余账号
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getOtherCode(String userId) {
        //当前用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        // 查询待审批任务数
        QueryWrapper<RunningUutManager> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("finish_time");
        if(roles.contains("uutManager")){
            queryWrapper.eq("assignee", "role:uutManager");
        }else{
            queryWrapper.eq("assignee", sysUser.getId());
        }
        // 待审批数量
        Integer approvalNums = runningUutManagerService.getBaseMapper().selectCount(queryWrapper);

        queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("finish_time").eq("assignee", sysUser.getId());
        //今日审批数
        Integer unfinishedNums = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<RunningUutManager> list = runningUutManagerService.getBaseMapper().selectList(queryWrapper);
        for (RunningUutManager runningUutManager : list) {
            Date finishTime = runningUutManager.getFinishTime();
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(finishTime);
            cal2.setTime(new Date());
            boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
            if(sameDay){
                unfinishedNums++;
            }
        }

        // 创建hashMap
        HashMap<String, Object> resultMap = new HashMap<>(2000);
        // 添加待审批数量
        resultMap.put("approvalNums", approvalNums);
        // 添加已过期数
        resultMap.put("unfinishedNums", unfinishedNums);
        return resultMap;
    }


    /**
     * 查询 - 任务数量数、已过期数
     *
     * @param
     * @return
     */
    @Override
    public Map<String, Object> getTaskNums() {
        // 任务数量
        Integer taskNums = 0;
        // 过期数
        Integer pastNums = 0;
        // 当前时间
        Long dateTime = System.currentTimeMillis();
        // 创建QueryWrapper
        QueryWrapper<RunningTask> queryWrapper = new QueryWrapper<>();
        List<RunningTask> list = runningTaskMapper.selectList(queryWrapper);
        // 判断是否为null值 且 大于0
        if (list != null && list.size() > 0) {
            // 任务数量
            taskNums = list.size();
            for (RunningTask runTask : list) {
                // 结束时间
                Long finishTime = runTask.getFinishDate().getTime();
                if (dateTime > finishTime && "1".equals(runTask.getDelFlag())) {
                    pastNums++;
                }
            }
        }
        // 创建map集合
        HashMap<String, Object> resultMap = new HashMap<>(2000);
        // 添加任务数量
        resultMap.put("taskNums", taskNums);
        // 添加已过期数量
        resultMap.put("pastNums", pastNums);
        return resultMap;
    }

}
