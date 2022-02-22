package org.jeecg.modules.testtooldistribute.fegin;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.testtooldistribute.fegin.entity.ResultSocket;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author yeyl
 * @version V1.0
 * @date 2021/7/8
 * 测试工具管理
 */
@FeignClient(value = "tepm-agent-server")
@Component
public interface SocketServerFegin {


    /**
     * SocketServer-to发送消息
     *
     * @param ip
     * @param id
     * @param code
     * @param path
     * @param url
     * @param tool
     * @param type
     * @param toolProcessName 测试工具进程名称
     * @return
     */
    @PostMapping(value = "/socket/server/to/sendMsg")
    Result<?> sendMsg(@RequestParam("ip") String ip,
                      @RequestParam("id") String id,
                      @RequestParam("code") Integer code, @RequestParam("path") String path,
                      @RequestParam("url") String url, @RequestParam("tool") String tool,
                      @RequestParam("type") Integer type, @RequestParam("toolProcessName") String toolProcessName,
                      @RequestParam("toolId") String toolId, @RequestParam("toolsPort") String toolsPort,
                      @RequestParam("toolLinuxProcessName") String toolLinuxProcessName);
}
