package org.jeecg.modules.access.controller;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.modules.access.service.IAccessDatabaseService;
import org.jeecg.modules.access.service.impl.AccessDatabaseServiceImpl;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("access/test")

public class AccessTestController {

    @Autowired
    private IAccessDatabaseService accessDatabaseService;

    @GetMapping("test")
    public String test(){
        accessDatabaseService.updateAccessDataSource(accessDatabaseService.getAccessDataSourceInstance("D:/0.mdb"));
        accessDatabaseService.test();
        return "test 1";
    }

}
