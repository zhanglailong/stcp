package org.jeecg.modules.openstack.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.ext.LoadRunnerServiceExt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author hxsi
 * @date 2021年06月03日 14:59
 */
@Api(tags="工具集成")
@RestController
@RequestMapping("/tool/integration")
@Slf4j
public class JmeterController{
    @Resource
    private LoadRunnerServiceExt loadRunnerServiceExt;
    @Resource
    private IStackQueueService iStackQueueService;

    /**
     *
     * @param planName 测试名称
     * @param numThreads 线程数
     * @param loops 循环次数
     * @param url 域名或者ip
     * @param port 端口号
     * @param httpRequest 请求方式
     * @param duration 并发时间
     * @param path 测试用例路径
     * @param request 请求参数
     * @param jmeterPath 测试用例地址
     * @param id 测试工具表主键
     * @return htmlPath
     * @throws IOException
     */
    @AutoLog(value = "jmeter启动")
    @ApiOperation(value="jmeter启动", notes="jmeter启动")
    @PostMapping(value = "/getJmeter")
    public Result<?> getJmeter (@RequestParam(name ="planName")String planName,
                         @RequestParam(name ="numThreads")int numThreads,
                         @RequestParam(name ="loops")String loops,
                         @RequestParam(name ="url")String url,
                         @RequestParam(name ="port")String port,
                         @RequestParam(name ="httpRequest")String httpRequest,
                         @RequestParam(name ="duration")int duration,
                         @RequestParam(name ="path")String path,
                         @RequestParam(name ="request")String request,
                         @RequestParam(name ="jmeterPath")String jmeterPath,
                         @RequestParam(name = "id")String id){
        try {
            Boolean htmlPath = iStackQueueService.getJmeter(planName,numThreads,loops,url,port,httpRequest,duration,path,request,jmeterPath,id);
            if (htmlPath){
                return Result.OK("Jmeter测试成功！");
            }
        } catch (IOException e) {
            log.error("Jmeter测试异常,原因:"+e.getMessage());
            return Result.error("Jmeter测试异常,请联系管理员！");
        }
        return Result.error("Jmeter测试异常,请联系管理员！");
    }

    /**
     *
     * @param lrsPath 测试用例地址
     * @returnlrrPath
     */
    @AutoLog(value = "LoadRunner启动")
    @ApiOperation(value="LoadRunner启动", notes="LoadRunner启动")
    @PostMapping(value = "/getLoadRunner")
    public Result<?> getLoadRunner(@RequestParam(name = "lrsPath") String lrsPath){
        try {
            String lrrPath = loadRunnerServiceExt.getLoadRunner(lrsPath);
            if (StringUtils.isNotEmpty(lrrPath)){
                return Result.OK("LoadRunner测试成功！");
            }
        } catch (IOException e) {
            log.error("LoadRunner测试异常,原因:"+e.getMessage());
            return Result.error("LoadRunner测试异常,请联系管理员！");
        }
        return Result.error("LoadRunner测试异常,请联系管理员！");
    }

    /**
     * AppScan 测试工具
     * @param id monitorTools主键
     * @param path 测试地址
     * @return
     */
    @AutoLog(value = "AppScan启动")
    @ApiOperation(value="AppScan启动", notes="AppScan启动")
    @PostMapping(value = "/getAppScan")
    public Result<?> getAppScan(@RequestParam(name = "id") String id ,@RequestParam(name = "path") String path){
        try {
            Boolean appScanPath = iStackQueueService.getAppScan(id,path);
            if (appScanPath){
                return Result.OK("AppScan测试成功！");
            }
        } catch (Exception e) {
            log.error("AppScan测试异常,原因:"+e.getMessage());
            return Result.error("AppScan测试异常,请联系管理员！");
        }
        return Result.error("AppScan测试异常,请联系管理员！");
    }

    /**
     * UnderStand 测试工具
     * @param id monitorTools主键
     * @param path 测试地址
     * @return
     */
    @AutoLog(value = "UnderStand启动")
    @ApiOperation(value="UnderStand启动", notes="UnderStand启动")
    @PostMapping(value = "/getUnderStand")
    public Result<?> getUnderStand(@RequestParam(name = "id") String id ,@RequestParam(name = "path") String path){
        try {
            Boolean underStandPath = iStackQueueService.getUnderStand(id,path);
            if (underStandPath){
                return Result.OK("AppScan测试成功！");
            }
        } catch (Exception e) {
            log.error("UnderStand测试异常,原因:"+e.getMessage());
            return Result.error("UnderStand测试异常,请联系管理员！");
        }
        return Result.error("UnderStand测试异常,请联系管理员！");
    }
}
