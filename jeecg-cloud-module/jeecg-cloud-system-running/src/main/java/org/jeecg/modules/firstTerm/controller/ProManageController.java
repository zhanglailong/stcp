package org.jeecg.modules.firstTerm.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.firstTerm.service.ProManageService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 首页-项目管理
 */
@Slf4j
@RestController
@RequestMapping("/project")
/**
 * @Author: test
 * */
public class ProManageController {

    @Autowired
    private ProManageService proManageService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询 - 项目数量
     * 根据项目id查询项目数量
     *
     * @return
     */
    @GetMapping(value = "/queryProjectNums")
    public Result<?> queryProNum() {
        Map<String, Object> resMap = proManageService.getRelatedCount();
        String res = JSON.toJSONString(resMap);
        return Result.ok(res);
    }


    /**
     * 查询 - 未关闭的项目
     *
     * @param
     * @return
     */
    @GetMapping(value = "/queryNotCloseProject")
    public Result<?> queryNotCloseProject() {
        Map<String, Object> resMap = proManageService.getProjectNotClose();
        String res = JSON.toJSONString(resMap);
        return Result.ok(res);
    }


    /**
     * 查询 - 待审批数量
     *
     * @return
     */
    @GetMapping(value = "/queryApprovalNums")
    public Result<?> queryNotApproval() {
        String res = null;
        // 获取当前用户信息
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        //当前用户角色
        List<String> roles = sysUserService.getRole(sysUser.getUsername());
        Map<String, Object> map = proManageService.getOtherCode(sysUser.getId());
        res = JSON.toJSONString(map);
        return Result.ok(res);
    }

    /**
     * 查询 - 任务数量
     *
     * @param queryParams
     * @return
     */
    @GetMapping(value = "/queryTaskNums")
    public Result<?> queryTaskNumber(@RequestBody(required=false) Map<String, Object> queryParams) {
        Map<String, Object> map = proManageService.getTaskNums();
        String res = JSON.toJSONString(map);
        return Result.ok(res);
    }
}
