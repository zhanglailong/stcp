package org.jeecg.modules.tepm.feign;

import org.jeecg.common.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author yeyl
 * @version V1.0
 * @date 2021/8/4
 * 客户端信息(虚拟机)
 */
@FeignClient(value = "jeecg-system-tepm")
@Component
public interface AgentClient {

    /**
     * 通过客户端ip 查询 项目id,工具id,环境id,客户端名称，客户端状态。
     *
     * @param agentIp agentIp
     * @return 虚拟机信息
     */
    @GetMapping(value = "/plan/vmDesign/queryByAgentIp")
    Result<?> queryByAgentIp(@RequestParam(name = "agentIp") String agentIp);


    /**
     * 更改客户端状态
     * @param agentIpList 客户端IP列表
     * @param environmentIdList 环境列表
     * @param agentStatus 客户端状态
     * @return 成功或者失败
     */
    @PostMapping(value = "/agent/sjcjAgent/changeAgentStatus")
    Result<?> changeAgentStatus(
            @RequestParam(name = "agentIpList") List<String> agentIpList,
            @RequestParam(name = "environmentIdList") List<String> environmentIdList,
            @RequestParam(name = "agentStatus")           String agentStatus
    );


}
