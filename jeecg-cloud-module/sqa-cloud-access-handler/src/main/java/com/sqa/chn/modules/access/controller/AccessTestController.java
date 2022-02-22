package com.sqa.chn.modules.access.controller;

import com.sqa.chn.modules.access.service.IAccessDatabaseService;
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
