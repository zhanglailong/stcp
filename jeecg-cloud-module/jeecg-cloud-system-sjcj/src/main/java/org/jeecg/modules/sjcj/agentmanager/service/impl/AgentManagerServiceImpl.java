package org.jeecg.modules.sjcj.agentmanager.service.impl;

import org.jeecg.modules.sjcj.agentmanager.entity.AgentManager;
import org.jeecg.modules.sjcj.agentmanager.mapper.AgentManagerMapper;
import org.jeecg.modules.sjcj.agentmanager.service.IAgentManagerService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 记录当前已开启的代理
 * @Author: jeecg-boot
 * @Date:   2021-01-06
 * @Version: V1.0
 */
@Service
public class AgentManagerServiceImpl extends ServiceImpl<AgentManagerMapper, AgentManager> implements IAgentManagerService {

}
