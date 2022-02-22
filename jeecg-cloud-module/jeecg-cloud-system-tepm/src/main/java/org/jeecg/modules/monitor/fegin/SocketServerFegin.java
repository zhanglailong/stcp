package org.jeecg.modules.monitor.fegin;

import org.jeecg.common.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/1/22
 * @Description: 用一句话描述该文件做什么)
 */
@FeignClient(value = "tepm-agent-server")
@Component
public interface SocketServerFegin {

    @GetMapping(value = "/socket/server/updateCoolecTime")
    Result<?> updateCoolecTime(@RequestParam("userName") String userName, @RequestParam("time") String time);
}
