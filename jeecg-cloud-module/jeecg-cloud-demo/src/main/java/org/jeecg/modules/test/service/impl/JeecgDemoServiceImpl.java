package org.jeecg.modules.test.service.impl;

import org.jeecg.modules.test.service.JeecgDemoService;
import org.jeecg.common.api.vo.Result;
import org.springframework.stereotype.Service;

@Service
public class JeecgDemoServiceImpl implements JeecgDemoService {
    @Override
    public Result<Object> getMessage(String name) {
        return Result.ok("Hello");
    }
}
