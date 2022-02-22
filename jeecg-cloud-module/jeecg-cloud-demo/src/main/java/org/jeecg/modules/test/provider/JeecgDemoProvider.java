package org.jeecg.modules.test.provider;

import org.jeecg.modules.test.service.JeecgDemoService;
import org.jeecg.common.api.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * feign服务端接口
 */
@RestController
@RequestMapping("/test")
public class JeecgDemoProvider {

    @Resource
    private JeecgDemoService jeecgDemoService;

    @GetMapping("/getMessage")
    public Result<Object> getMessage(@RequestParam String name) {
        return jeecgDemoService.getMessage(name);
    }

}
