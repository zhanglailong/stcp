//package org.jeecg.modules.tepm.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.jeecg.common.api.vo.Result;
//import org.jeecg.modules.tepm.feign.EPMClient;
//import org.jeecg.modules.tepm.feign.TaskSd;
//import org.jeecg.starter.cloud.feign.impl.JeecgFeignService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author zlf
// * @version V1.0
// * @date 2020/12/24
// * @Description: (用一句话描述该文件做什么)
// */
//@RestController
//@RequestMapping("/test/taskSd")
//@Api(tags = "测试环境定制与管理")
//public class EnvironmentPlanMangeFeign {
//
//    @Autowired
//    private JeecgFeignService jeecgFeignService;
//
//    @GetMapping("list")
//    @ApiOperation(value = "测试环境定制与管理获取list集合", notes = "测试环境定制与管理获取list集合")
//    public Result<?> queryPageList(TaskSd taskSd,
//                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                                   HttpServletRequest req) {
//        EPMClient epmClient = jeecgFeignService.newInstance(EPMClient.class, "jeecg-tepm-service");
//        return epmClient.queryPageList(taskSd,pageNo,pageSize,req);
//    }
//}
