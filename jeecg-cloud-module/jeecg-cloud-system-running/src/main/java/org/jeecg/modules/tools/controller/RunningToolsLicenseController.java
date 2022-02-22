package org.jeecg.modules.tools.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.minio.MinioClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.tools.entity.RunningToolsLicense;
import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import org.jeecg.modules.tools.entity.RunningToolsLicenseuse;
import org.jeecg.modules.tools.service.IRunningToolsLicenseService;
import org.jeecg.modules.tools.service.IRunningToolsLicenselimitService;
import org.jeecg.modules.tools.service.IRunningToolsLicensemonitorService;
import org.jeecg.modules.tools.service.IRunningToolsLicenseuseService;
import org.jeecg.modules.tools.vo.LicenseInfoVO;
import org.jeecg.modules.tools.vo.RunningToolsLicensePage;
import org.jeecg.modules.tools.vo.RunningToolsLicenselimitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: License云平台适配
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Api(tags = "License云平台适配")
@RestController
@RequestMapping("/tools/runningToolsLicense")
@Slf4j
public class RunningToolsLicenseController {
    @Autowired
    private IRunningToolsLicenseService runningToolsLicenseService;
    @Autowired
    private IRunningToolsLicensemonitorService runningToolsLicensemonitorService;
    @Autowired
    private IRunningToolsLicenselimitService runningToolsLicenselimitService;
    @Autowired
    private IRunningToolsLicenseuseService runningToolsLicenseuseService;
    @Value(value = "${jeecg.minio.minio-url}")
    private String minioUrl;
    @Value(value = "${jeecg.minio.minio-name}")
    private String minioName;
    @Value(value = "${jeecg.minio.minio-pwd}")
    private String minioPass;
    @Value(value = "${jeecg.minio.bucket-system}")
    private String bucketNameSystem;

    private static AtomicInteger index = new AtomicInteger(0);
    //@Autowired
    //private RabbitMqCli rabbitMqClient;

    /**
     * 分页列表查询
     *
     * @param runningToolsLicense
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "License云平台适配-分页列表查询")
    @ApiOperation(value = "License云平台适配-分页列表查询", notes = "License云平台适配-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(RunningToolsLicense runningToolsLicense,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<RunningToolsLicense> queryWrapper = QueryGenerator.initQueryWrapper(runningToolsLicense, req.getParameterMap());
        queryWrapper.eq("del_flag", CommonConstant.DEL_FLAG_0);
        Page<RunningToolsLicense> page = new Page<RunningToolsLicense>(pageNo, pageSize);
        IPage<RunningToolsLicense> pageList = runningToolsLicenseService.page(page, queryWrapper);
        //处理license信息显示
       /* List<RunningToolsLicense> records = pageList.getRecords();
        for (RunningToolsLicense record : records) {
            String licenseInfo = record.getToolsLicenseInfo();
            List<LicenseInfoVO> icenseInfoVolList = JSONObject.parseArray(licenseInfo, LicenseInfoVO.class);
            StringBuilder builder = new StringBuilder();
            int count = 1;
            for (LicenseInfoVO licenseInfoVO : icenseInfoVolList) {
                StringBuilder append = builder.append(licenseInfoVO.getName()).append("：").append(licenseInfoVO.getValue());
                if (count < icenseInfoVolList.size()) {
                    append.append("</br>");
                }
                count++;
            }
            record.setToolsLicenseInfoFormat(builder.toString());
            record.setCreateBy(null);
            record.setCreateTime(null);
        }*/
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param runningToolsLicensePage
     * @return
     */
    @AutoLog(value = "License云平台适配-添加")
    @ApiOperation(value = "License云平台适配-添加", notes = "License云平台适配-添加")

    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody RunningToolsLicensePage runningToolsLicensePage) {
        List<LicenseInfoVO> licenseInfoVOList = runningToolsLicensePage.getToolsLicenseInfo();
        RunningToolsLicense runningToolsLicense = new RunningToolsLicense();
        BeanUtils.copyProperties(runningToolsLicensePage, runningToolsLicense);
        //list转json
        String licenseInfo = JSON.toJSONString(licenseInfoVOList);
        runningToolsLicense.setToolsLicenseInfo(licenseInfo);
        runningToolsLicense.setDelFlag(CommonConstant.DEL_FLAG_0);
        runningToolsLicenseService.save(runningToolsLicense);

        //创建队列,队列名称=license+id后4位
        /*String licenseQueueName = getLicenseQueueName(runningToolsLicense.getId());
        Queue licenseQueue = new Queue(licenseQueueName);
        rabbitMqClient.addQueue(licenseQueue);
        log.info("成功添加队列，队列名称，{}",licenseQueueName);*/
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param runningToolsLicensePage
     * @return
     */
    @AutoLog(value = "License云平台适配-编辑")
    @ApiOperation(value = "License云平台适配-编辑", notes = "License云平台适配-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody RunningToolsLicense runningToolsLicense) {
//        System.out.println(12345);
//        List<LicenseInfoVO> toolsLicenseInfo = runningToolsLicensePage.getToolsLicenseInfo();
//        String licenseInfo = JSON.toJSONString(toolsLicenseInfo);
//        RunningToolsLicense runningToolsLicense = new RunningToolsLicense();
//        BeanUtils.copyProperties(runningToolsLicensePage, runningToolsLicense);
//        runningToolsLicense.setToolsLicenseInfo(licenseInfo);
//        RunningToolsLicense runningToolsLicenseEntity = runningToolsLicenseService.getById(runningToolsLicense.getId());
//        if (runningToolsLicenseEntity == null) {
//            return Result.error("未找到对应数据");
//        }
        runningToolsLicenseService.updateById(runningToolsLicense);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "License云平台适配-通过id删除")
    @ApiOperation(value = "License云平台适配-通过id删除", notes = "License云平台适配-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        runningToolsLicenseService.lambdaUpdate()
                .eq(RunningToolsLicense::getId, id)
                .set(RunningToolsLicense::getDelFlag, CommonConstant.DEL_FLAG_1)
                .update();
        //删除该lisenceId对应的黑名单
        LambdaQueryWrapper<RunningToolsLicenselimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RunningToolsLicenselimit::getLicenseId, id);
        runningToolsLicenselimitService.remove(wrapper);

        //删除队列
       /* String licenseQueueName = getLicenseQueueName(id);
        rabbitMqClient.deleteQueue(licenseQueueName);*/
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "License云平台适配-批量删除")
    @ApiOperation(value = "License云平台适配-批量删除", notes = "License云平台适配-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        runningToolsLicenseService.lambdaUpdate()
                .set(RunningToolsLicense::getDelFlag, CommonConstant.DEL_FLAG_1)
                .in(RunningToolsLicense::getId, Arrays.asList(ids.split(",")))
                .update();
        //删除该lisenceId对应的黑名单
        LambdaQueryWrapper<RunningToolsLicenselimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(RunningToolsLicenselimit::getLicenseId, Arrays.asList(ids.split(",")));
        runningToolsLicenselimitService.remove(wrapper);
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "License云平台适配-通过id查询")
    @ApiOperation(value = "License云平台适配-通过id查询", notes = "License云平台适配-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        RunningToolsLicense runningToolsLicense = runningToolsLicenseService.getById(id);
        if (runningToolsLicense == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(runningToolsLicense);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "License监控表通过主表ID查询")
    @ApiOperation(value = "License监控表主表ID查询", notes = "License监控表-通主表ID查询")
    @GetMapping(value = "/queryRunningToolsLicensemonitorByMainId")
    public Result<?> queryRunningToolsLicensemonitorListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<RunningToolsLicensemonitor> runningToolsLicensemonitorList = runningToolsLicensemonitorService.selectByMainId(id);
        return Result.ok(runningToolsLicensemonitorList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "License访问限制表通过主表ID查询")
    @ApiOperation(value = "License访问限制表主表ID查询", notes = "License访问限制表-通主表ID查询")
    @GetMapping(value = "/queryRunningToolsLicenselimitByMainId")
    public Result<?> queryRunningToolsLicenselimitListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<RunningToolsLicenselimit> runningToolsLicenselimitList = runningToolsLicenselimitService.selectByMainId(id);
        return Result.ok(runningToolsLicenselimitList);
    }

    /**
     * 黑名单
     *
     * @param runningToolsLicenselimitVO
     * @return
     */
    @AutoLog(value = "License云平台适配-黑名单")
    @ApiOperation(value = "License云平台适配-黑名单", notes = "License云平台适配-黑名单")
    @PostMapping(value = "/addBlack")
    public Result<?> addBlack(@RequestBody RunningToolsLicenselimitVO runningToolsLicenselimitVO) {
        String licenseId = runningToolsLicenselimitVO.getLicenseId();
        if (StringUtils.isEmpty(licenseId)) {
            return Result.error("LicenseId不能为空！");
        }
        List<RunningToolsLicenselimit> runningToolsLicenselimitList = runningToolsLicenselimitVO.getRunningToolsLicenselimitList();
        if (runningToolsLicenselimitList.isEmpty()) {
            return Result.error("参数不能为空！");
        }
        LambdaQueryWrapper<RunningToolsLicenselimit> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RunningToolsLicenselimit::getLicenseId, licenseId);
        runningToolsLicenselimitService.remove(wrapper);
        for (RunningToolsLicenselimit entity : runningToolsLicenselimitList) {
            //外键设置
            entity.setLicenseId(licenseId);
            runningToolsLicenselimitService.save(entity);
        }
        return Result.ok("添加成功！");
    }

    /**
     * 下载license
     *
     * @param toolId
     * @return
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(String toolId, HttpServletResponse response) throws Exception {
//        String toolsId = "1341647180284678146";//testbed
//        String toolsId = "1342658577659195394";//自动化功能测试工具
//        String toolsId = "1342658900054372353";//代码质量测试工具
//        String toolsId = "1342659301919027201";//性能测试工具LoadRunner
//        String toolsId = "1342659460316917762";//软件测试用例辅助测试工具
        List<RunningToolsLicense> runningToolsLicenseList = runningToolsLicenseService.lambdaQuery()
                .eq(RunningToolsLicense::getToolsId, toolId).list();
        //一个工具不可能单机版和浮动版license同时存在
        String toolsLicenseType = runningToolsLicenseList.get(0).getToolsLicenseType();
        //单机版license
        if (CommonConstant.SINGLE.equals(toolsLicenseType)) {
            for (RunningToolsLicense runningToolsLicense : runningToolsLicenseList) {
                String licenseId = runningToolsLicense.getId();
                RunningToolsLicenseuse toolsLicenseuse = runningToolsLicenseuseService.lambdaQuery()
                        .eq(RunningToolsLicenseuse::getLicenseId, licenseId).one();
                //没有使用记录
                if (toolsLicenseuse == null) {
                    downloadLicense(runningToolsLicense, response);
                } else {
                    continue;
                }
            }
            //如果有使用记录
            throw new Exception("没有可用的license");
        }
        //浮动版license(只存在有限制license，不存在无限制license)
        if (CommonConstant.FLOAT.equals(toolsLicenseType)) {
            for (int i = 0; i < runningToolsLicenseList.size(); i++) {
                if (index.get() >= runningToolsLicenseList.size()) {
                    index = new AtomicInteger(0);
                }
                RunningToolsLicense runningToolsLicense = runningToolsLicenseList.get(index.get());
                Integer toolsLicenseAuthNum = runningToolsLicense.getToolsLicenseAuthNum();
                String licenseId = runningToolsLicense.getId();
                List<RunningToolsLicenseuse> list = runningToolsLicenseuseService.lambdaQuery()
                        .eq(RunningToolsLicenseuse::getLicenseId, licenseId).list();
                if (toolsLicenseAuthNum > list.size()) {
                    downloadLicense(runningToolsLicense, response);
                    return;
                } else {
                    index.incrementAndGet();
                    continue;
                }
            }
            throw new Exception("没有可用的license");
        }
    }

    /**
     * 下载
     *
     * @param runningToolsLicense
     * @param response
     */
    private Boolean downloadLicense(RunningToolsLicense runningToolsLicense, HttpServletResponse response) {
        InputStream fileInputStream = null;
        try {
            MinioClient minioClient = new MinioClient(minioUrl, minioName, minioPass);
            String toolsLicenseLocation = runningToolsLicense.getToolsLicenseLocation();
            //截取对象名和文件名
            int bucketName = toolsLicenseLocation.indexOf(this.bucketNameSystem);
            String objectName = toolsLicenseLocation.substring(bucketName + 7);
            String fileName = objectName.substring(objectName.lastIndexOf("/") + 1);
            fileInputStream = minioClient.getObject(this.bucketNameSystem, objectName);
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "iso-8859-1"));
            response.setHeader("licenseId", runningToolsLicense.getId());
            response.setContentType("application/force-download");
            IOUtils.copy(fileInputStream, response.getOutputStream());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("文件下载失败" + e.getMessage());
            return false;
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * license配置成功回调
     *
     * @param jsonObject
     */
    @AutoLog(value = "License云平台适配-license配置成功回调")
    @ApiOperation(value = "License云平台适配-license配置成功回调", notes = "License云平台适配-license配置成功回调")
    @PostMapping(value = "/licenseCallBack")
    public void licenseCallBack(@RequestBody JSONObject jsonObject) {
        String licenseId = jsonObject.getString("licenseId");
        RunningToolsLicenseuse runningToolsLicenseuse = new RunningToolsLicenseuse();
        runningToolsLicenseuse.setLicenseId(licenseId);
        runningToolsLicenseuse.setCreateTime(new Date());
        runningToolsLicenseuseService.save(runningToolsLicenseuse);
        index.incrementAndGet();
    }

    /**
     * 删除license使用记录
     *
     * @param licenseId
     */
    @AutoLog(value = "License云平台适配-删除license使用记录")
    @ApiOperation(value = "License云平台适配-删除license使用记录", notes = "License云平台适配-删除license使用记录")
    @DeleteMapping(value = "/deleteLicenseUse")
    public void deleteLicenseUse(@RequestParam(name="licenseId",required=true) String licenseId) {
        RunningToolsLicense toolsLicense = runningToolsLicenseService.getById(licenseId);
        String toolsLicenseType = toolsLicense.getToolsLicenseType();
        //单机license
        if (CommonConstant.SINGLE.equals(toolsLicenseType)) {
            RunningToolsLicenseuse licenseuse = runningToolsLicenseuseService.lambdaQuery()
                    .eq(RunningToolsLicenseuse::getLicenseId, toolsLicense.getId()).one();
            runningToolsLicenseuseService.removeById(licenseuse.getId());
        }
        //浮动license
        if (CommonConstant.FLOAT.equals(toolsLicenseType)) {
            List<RunningToolsLicenseuse> list = runningToolsLicenseuseService.lambdaQuery()
                    .eq(RunningToolsLicenseuse::getLicenseId, licenseId).list();
            String id = list.get(0).getId();
            runningToolsLicenseuseService.removeById(id);
        }
    }

    /**
     * 获取licensequeue名称
     * @param licenseId
     * @return
     */
    public String getLicenseQueueName(String licenseId){
        Integer start = Integer.valueOf(licenseId.length()-4);
        Integer end = Integer.valueOf(licenseId.length());
        String licenseQueueName = "license" + licenseId.substring(start,end);
        return licenseQueueName;
    }
}
