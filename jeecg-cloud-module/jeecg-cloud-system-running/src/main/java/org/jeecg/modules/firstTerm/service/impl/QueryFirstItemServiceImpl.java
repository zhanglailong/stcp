package org.jeecg.modules.firstTerm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.firstTerm.service.QueryFirstItemService;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.mapper.RunningProjectMapper;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.mapper.RunningTaskMapper;
import org.jeecg.modules.running.uut.entity.RunningUutManager;
import org.jeecg.modules.running.uut.mapper.RunningUutManagerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
/**
 * @Author: test
 * */
public class QueryFirstItemServiceImpl extends ServiceImpl<RunningUutManagerMapper, RunningUutManager> implements QueryFirstItemService {

    @Autowired
    private RunningTaskMapper runningTaskMapper;

    @Autowired
    private RunningProjectMapper runningProjectMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询 - 查询我的审批 admin账号下  查询全部
     * @return
     */
    @Override
    public List<RunningUutManager> getMyApproval() {
        // 创建queryWrapper 实体对象
        QueryWrapper<RunningUutManager> queryWrapper = new QueryWrapper<>();
        // 状态是1
        queryWrapper.eq("status",0);
        // 查询全部
        List<RunningUutManager> list = this.getBaseMapper().selectList(queryWrapper);
        // 获取当前时间
        Long now = System.currentTimeMillis();
        // 循环
        for(RunningUutManager runUutManager : list){
            if("start".equals(runUutManager.getCatalog())){
                long crt = runUutManager.getCreateTime().getTime();
                long du = (now - crt) / 1000;
                String ddd = intForString(du);
                runUutManager.setDuration(ddd);
            } else {
                String instanceId = runUutManager.getInstanceId();
                QueryWrapper<RunningUutManager> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("instance_id", instanceId);
                queryWrapper2.eq("catalog", "start");
                List<RunningUutManager> list2 = this.getBaseMapper().selectList(queryWrapper2);
                if (list2 != null && list2.size() > 0){

                    for (RunningUutManager runningUutManager: list2) {
                        long crt1 = runningUutManager.getCreateTime().getTime();
                        long du = (now - crt1) / 1000;
                        String ddd = intForString(du);
                        runUutManager.setDuration(ddd);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查询 - 查询我的审批 其他账号 查询当前登录人
     * @return
     */
    @Override
    public List<RunningUutManager> getOtherMyApproval(String userId) {
        // 创建queryWrapper 实体对象
        QueryWrapper<RunningUutManager> queryWrapper = new QueryWrapper<>();
        // 当前登录人
        queryWrapper.eq("assignee",userId);
        // 状态是0
        queryWrapper.eq("status",0);
        // 查询全部
        List<RunningUutManager> runUutManagerList = this.getBaseMapper().selectList(queryWrapper);

        // 获取当前时间
        Long now = System.currentTimeMillis();
        // 循环
        for(RunningUutManager runUutManager : runUutManagerList){
            if("start".equals(runUutManager.getCatalog())){
                long crt = runUutManager.getCreateTime().getTime();
                long du = (now - crt)/1000;
                String ddd = intForString(du);
                runUutManager.setDuration(ddd);
            } else {
                String instanceId = runUutManager.getInstanceId();
                QueryWrapper<RunningUutManager> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("instance_id", instanceId);
                queryWrapper2.eq("catalog", "start");
                List<RunningUutManager> list2 = this.getBaseMapper().selectList(queryWrapper2);
                if (list2 != null && list2.size() > 0){
                    for (RunningUutManager uutManager: list2) {
                        long crt1 = uutManager.getCreateTime().getTime();
                        long du = (now - crt1)/1000;
                        String ddd = intForString(du);
                        runUutManager.setDuration(ddd);
                    }
                }
            }
        }
        return runUutManagerList;
    }

    /**
     * 查询 - 查询项目统计 admin账号 查询全部
     * @return
     */
    @Override
    public List<RunningProject> getProjectNums() {
        // 创建 实体 对象
        QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
        // 状态 未完成
        queryWrapper.eq("finish_status",0);
        // 项目标记 存在的
        queryWrapper.eq("del_flag",0);
        // 查询全部
        List<RunningProject> list = runningProjectMapper.selectList(queryWrapper);
        // 循环 查lis结果
        for (RunningProject runningProject: list) {
            // 获取 创建人
            String cb = runningProject.getCreateBy();
            // 获取 user realname
            SysUser sysUser = sysUserMapper.getUserByName(cb);
            // 添加 realname
            runningProject.setCreateBy(sysUser.getRealname());
        }

        return list;
    }

    /**
     * 查询 - 查询项目统计 其余账号 查询当前登录人
     * @return
     */
    @Override
    public List<RunningProject> getOtherProjectNums(String userId) {
        // 创建 实体对象
        QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
        // 状态 未完成
        queryWrapper.eq("finish_status",0);
        // 项目标记 存在的
        queryWrapper.eq("del_flag",0);
        List<RunningProject> list = runningProjectMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 查询 - 查询我的任务  admin账号 查询全部
     * @return
     */
    @Override
    public List<RunningTask> getMyTaskNums() {
        // 创建 queryWrapper
        QueryWrapper<RunningTask> queryWrapper = new QueryWrapper<>();
        // 任务状态存在
        queryWrapper.eq("task_status","0");
        // 查询全部
        List<RunningTask> list = runningTaskMapper.selectList(queryWrapper);

        return list;
    }

    /**
     * 查询 - 查询我的任务 其他账号 - 查询当前登录人
     * @return
     */
    @Override
    public List<RunningTask> getOtherMyTaskNums(String userId) {
        // 创建 queryWrapper 对象
        QueryWrapper<RunningTask> queryWrapper = new QueryWrapper<>();
        // 当前登录人 id
        queryWrapper.eq("assignee",userId);
        // 任务状态存在
        queryWrapper.eq("taskStatus","0");
        // 查询全部
        List<RunningTask> list = runningTaskMapper.selectList(queryWrapper);

        return list;
    }

    private String intForString (Long du) {
        Integer num = 0;
        int second=3600;
        int code1=31536000;
        int hour=24;
        int day=30;
        if (du >= 0 && du <= second) {
            return "小于1小时";
        } else if (du > second && du <= second*hour) {
            num = Math.round(du / 3600);
            return num + "小时之前";
        } else if (du > second*hour && du <= second*hour*day) {
            num = Math.round(du /  (second*hour));
            return num + "天之前";
        } else if (du > second*hour*day && du <= code1) {
            num = Math.round(du / (second*hour*day));
            return num + "月之前";
        } else if (du > code1) {
            num = Math.round(du / 31536000);
            return num + "年之前";
        } else {
            return "";
        }
    }
}
