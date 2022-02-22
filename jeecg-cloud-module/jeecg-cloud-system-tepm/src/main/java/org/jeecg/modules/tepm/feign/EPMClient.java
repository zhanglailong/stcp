//package org.jeecg.modules.tepm.feign;
//
//import org.jeecg.common.api.vo.Result;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @author zlf
// * @version V1.0
// * @date 2020/12/24
// * @Description: (用一句话描述该文件做什么)
// */
//public interface EPMClient {
//
//    @GetMapping(value = "/test/taskSd/list")
//    Result<?> queryPageList(TaskSd taskSd,
//                            @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                            HttpServletRequest req);
//}
