package org.jeecg.modules.firstTerm.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.firstTerm.service.QueryFirstItemService;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.running.uut.entity.RunningUutManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/project")
/**
 * @Author: test
 * */
public class QueryFirstItemController {

    @Autowired
    private QueryFirstItemService queryFirstItemService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询 - 我的审批
     *
     * @return
     */
    @GetMapping(value = "/myApproval")
    public Result<?> queryMyApproval() {
        // 获取当前用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        // 查询我的审批 admin账号
        List<RunningUutManager> list = new ArrayList<>();
        if (roles.contains("admin")) {
            list = queryFirstItemService.getMyApproval();
        } else {
            // 查询我的审批 其余账号  当前登录人账号
            list = queryFirstItemService.getOtherMyApproval(sysUser.getId());
        }
        return Result.ok(JSON.toJSONString(list));
    }

    /**
     * 查询 - 项目统计
     *
     * @return
     */
    @GetMapping(value = "/projectStatistics")
    public Result<?> queryProjectStatistics() {
        // 获取当前用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        List<RunningProject> list = new ArrayList<RunningProject>();
        if (roles.contains("admin")) {
            list = queryFirstItemService.getProjectNums();
        } else {
            list = queryFirstItemService.getOtherProjectNums(sysUser.getId());
        }
        return Result.ok(list);
    }


    /**
     * 查询 - 我的任务
     *
     * @return
     */
    @GetMapping(value = "/myTask")
    public Result<?> queryMyTask() {
        // 获取当前用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        // 查询我的审批 admin账号
        List<RunningTask> list = new ArrayList<>();
        if (roles.contains("admin")) {
            list = queryFirstItemService.getMyTaskNums();
        } else {
            list = queryFirstItemService.getOtherMyTaskNums(sysUser.getId());
        }
        return Result.ok(list);
    }
}
