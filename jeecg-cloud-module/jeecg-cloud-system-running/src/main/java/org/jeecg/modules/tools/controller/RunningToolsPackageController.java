package org.jeecg.modules.tools.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import groovy.util.logging.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.oss.service.IRunningUploadFormService;
import org.jeecg.modules.tools.entity.RunningToolsHistory;
import org.jeecg.modules.tools.entity.RunningToolsList;
import org.jeecg.modules.tools.entity.RunningToolsPackage;
import org.jeecg.modules.tools.service.IRunningToolsHistoryService;
import org.jeecg.modules.tools.service.IRunningToolsListService;
import org.jeecg.modules.tools.service.IRunningToolsPackageService;
import org.jeecg.modules.util.FtpUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/tools/runningToolsPackage")
public class RunningToolsPackageController {

    @Autowired
    private IRunningToolsPackageService runningToolsPackageService;

    @Autowired
    private FtpUtil ftpUtil;

    @Autowired
    private IRunningToolsListService runningToolsListService;

    @Autowired
    private IRunningToolsHistoryService runningToolsHistoryService;

    @Autowired
    private IRunningUploadFormService runningUploadFormService;

    private String[] unit = new String[]{"bytes","KB","MB","GB"};

    private Integer siz = 1024;

    @GetMapping("/list")
    public Result<?> list(RunningToolsPackage toolsPackage,
                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                          HttpServletRequest req){

        QueryWrapper<RunningToolsPackage> wrapper = QueryGenerator.initQueryWrapper(toolsPackage, req.getParameterMap());
        Result<IPage<RunningToolsPackage>> result = Result.ok();
        wrapper.ne("del_flag",1);
        Page<RunningToolsPackage> page = new Page<RunningToolsPackage>(pageNo, pageSize);
        IPage<RunningToolsPackage> pageList = runningToolsPackageService.page(page, wrapper);

        pageList.getRecords().stream().forEach(item->{
            item.setName(getNameFromAddress(item.getAddress()));
            item.setSizeStr(formatSize(item.getSize()));
        });

        result.setResult(pageList);
        return result;
    }

    @PostMapping("/forbi")
    public Result<?> forbi(@RequestBody RunningToolsPackage toolsPackage){

        toolsPackage.setDisabled(1);
        runningToolsPackageService.updateById(toolsPackage);

        return Result.ok();
    }

    @PostMapping("/remove")
    public Result<?> remove(@RequestBody RunningToolsPackage toolsPackage){

        toolsPackage = runningToolsPackageService.getById(toolsPackage.getId());
        toolsPackage.setDelFlag(1);
        runningToolsPackageService.updateById(toolsPackage);

        RunningToolsList runningToolsList = runningToolsListService.getById(toolsPackage.getToolId());

        saveRunningToolsHistory(runningToolsList, CommonConstant.OPERATE_TYPE_7);

        if(toolsPackage.getDef() == 1){
                runningToolsList.setToolsFileFtpLocations("");
            runningToolsListService.updateById(runningToolsList);
        }

        return Result.ok();
    }

    @RequestMapping(value = "/ftpDownload", method = RequestMethod.GET)
    public void ftpDownload(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        try {
            ftpUtil.downloadFileChop(fileName,response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNameFromAddress(String address){

        Pattern pat = Pattern.compile("_[0-9]+");
        Matcher mat = pat.matcher(address);
        return mat.replaceFirst("");

    }

    private String formatSize(Long size){

        if(size==null){
            return "-";
        }

        String ut = unit[0];

        Double newSize = size.doubleValue();
        //获取大小以及单位
        for(int i=0;i< unit.length;i++){
            if((i+1< unit.length)&&newSize/siz>1){
                newSize = newSize/siz;
                ut = unit[i+1];
            }else{
                ut = unit[i];
                break;
            }
        }

        //保留2位小数
        return String.valueOf(((Double)Math.ceil(newSize*100)/100)).concat(ut);
    }

    private void clearDef(RunningToolsPackage runningToolsPackage){

        UpdateWrapper<RunningToolsPackage> wrapper = new UpdateWrapper<>();
        wrapper.set("def",-1).eq("tool_id",runningToolsPackage.getToolId());
        runningToolsPackageService.update(wrapper);

        RunningToolsList runningToolsList = new RunningToolsList();
        runningToolsList.setId(runningToolsPackage.getToolId());
        runningToolsList.setToolsFileFtpLocations(runningToolsPackage.getAddress());
        runningToolsListService.updateById(runningToolsList);

    }


    @PostMapping("/add")
    public Result<?> add(@RequestBody  RunningToolsPackage runningToolsPackage){

        if(runningToolsPackage.getDef() == 1){
            clearDef(runningToolsPackage);
        }

        AtomicLong size = new AtomicLong(0L);
        Arrays.stream(runningToolsPackage.getAddress().split(",")).forEach(item->{
            try {
                size.addAndGet(ftpUtil.getFileSize(runningUploadFormService.getById(item).getUrl()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        runningToolsPackage.setSize(size.get());
        runningToolsPackage.setDelFlag(0);
        runningToolsPackage.setDisabled(0);
        if(runningToolsPackageService.save(runningToolsPackage)){
            RunningToolsList runningToolsList = runningToolsListService.getById(runningToolsPackage.getToolId());
            saveRunningToolsHistory(runningToolsList, CommonConstant.OPERATE_TYPE_7);
            return Result.ok();
        }else{
            return Result.error("");
        }

    }

    @GetMapping("/check")
    public Result<?> check(RunningToolsPackage runningToolsPackage,HttpServletRequest req){
        Result result = Result.ok();
        QueryWrapper<RunningToolsPackage> wrapper = new QueryWrapper<>();

        wrapper.eq("version",runningToolsPackage.getVersion()).ne("del_flag",1).eq("tool_id",runningToolsPackage.getToolId());
        if(!StringUtils.isEmpty(runningToolsPackage.getId())){
            wrapper.ne("id",runningToolsPackage.getId());
        }

        result.setResult(runningToolsPackageService.count(wrapper));
        return result;
    }

    @PutMapping("edit")
    public Result<?> edit(@RequestBody  RunningToolsPackage runningToolsPackage){

        //获取原本是否为默认
        String id = runningToolsPackage.getId();
        Integer def = runningToolsPackageService.getById(id).getDef();
        RunningToolsList runningToolsList = runningToolsListService.getById(runningToolsPackage.getToolId());

        if(runningToolsPackage.getDef() == 1){
            clearDef(runningToolsPackage);
        }else if(def == 1){
            runningToolsList.setToolsFileFtpLocations("");
            runningToolsListService.updateById(runningToolsList);
        }


        if(runningToolsPackageService.updateById(runningToolsPackage)){
            saveRunningToolsHistory(runningToolsList, CommonConstant.OPERATE_TYPE_8);
            return Result.ok();
        }else{
            return Result.error("");
        }
    }


    /**
     * 保存工具操作历史对象
     */
    private void saveRunningToolsHistory(RunningToolsList runningToolsList,Integer operation){
        RunningToolsHistory runningToolsHistory = new RunningToolsHistory();
        BeanUtils.copyProperties(runningToolsList,runningToolsHistory);
        runningToolsHistory.setId(null);
        runningToolsHistory.setOperationType(operation);
        runningToolsHistory.setRunningToolsId(runningToolsList.getId());
        runningToolsHistory.setCreateTime(new Date());
        runningToolsHistory.setUpdateTime(new Date());
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        runningToolsHistory.setUpdateBy(user.getId());
        runningToolsHistoryService.save(runningToolsHistory);
    }

}
