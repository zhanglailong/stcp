package org.jeecg.modules.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.test.entity.RunningProject;
import org.jeecg.modules.test.mapper.RunningProjectMapper;
import org.jeecg.modules.test.service.IRunningProjectServer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shx
 * 项目管理
 * 2021-07-06
 * V1.0
 */
@Service
@Slf4j
public class RunningProjectServerImpl extends ServiceImpl<RunningProjectMapper, RunningProject> implements IRunningProjectServer {
    /**
     * 查询项目列表
     * @return list
     */
    @Override
    public List<RunningProject> getProjectList() {
        QueryWrapper<RunningProject> queryWrapper = new QueryWrapper<>();
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        queryWrapper.eq(CommonConstant.DEL_FLAG, CommonConstant.DATA_INT_IDEL_0);
        return baseMapper.selectList(queryWrapper);
    }
}
