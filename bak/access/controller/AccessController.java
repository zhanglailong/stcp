package org.jeecg.modules.access.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.javatuples.Triplet;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.access.dto.SyncResultVo;
import org.jeecg.modules.access.service.IAccessDatabaseService;
import org.jeecg.modules.access.service.IRunningAccessGlobalService;
import org.jeecg.modules.task.entity.RunningCase;
import org.jeecg.modules.task.entity.RunningCaseStep;
import org.jeecg.modules.task.entity.RunningTask;
import org.jeecg.modules.task.service.IRunningCaseService;
import org.jeecg.modules.task.service.IRunningCaseStepService;
import org.jeecg.modules.task.service.IRunningTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("access")
public class AccessController {

    @Autowired
    private IAccessDatabaseService accessDatabaseService;

    @Autowired
    private IRunningCaseService runningCaseService;

    @Autowired
    private IRunningCaseStepService runningCaseStepService;

    @Autowired
    private IRunningTaskService runningTaskService;

    @Autowired
    private IRunningAccessGlobalService runningAccessGlobalService;


    /**
     * 获取测试项和其测试用例子项的分页列表方法
     * @param likeFullName 模糊查询全称
     * @param pageNo 分页Index
     * @param pageSize 单分页大小
     * @return 测试项和其测试用例子项的分页列表
     */
    @GetMapping("list/task2case")
    public Result<?> listTask2Case(
            @RequestParam("likeFullName")String likeFullName,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){

        try {
            if (likeFullName == null || likeFullName.trim().length() <= 0){
                likeFullName = "";
            }
            accessDatabaseService.updateAccessDataSource(accessDatabaseService.getAccessDataSourceInstance(runningAccessGlobalService.getValue4DataSourceFilePath()));
            return Result.ok("操作成功", accessDatabaseService.pageCeShiXiang4CeShiYongLiVo4LikeCeShiXiangFullName(likeFullName, new Page<>(pageNo, pageSize)));
        } catch (Exception e){
            return Result.error(e.getLocalizedMessage());
        }


    }

    /**
     * 从Access数据源同步数据到MySQL数据源
     * @param map JSON对象Map, 其中含有
     *            key: [ids], value: [测试用例ID列表字符串 ","分割 String]
     *            key: [projectId], value: [项目ID, String]
     *            key: [turnId], value: [轮次ID String]
     *            key: [turnVerId], value: [轮次版本ID String]
     *
     * @return 成功同步的测试用例ID列表
     */
    @PostMapping("sync")
    public Result<?> syncData2MySQL(@RequestBody(required = false) Map<String, ?> map){
        try {
            if (map == null){
                throw new RuntimeException("同步测试用例ID列表, 项目ID, 轮次ID, 轮次版本ID 不能为空");
            }

            String ids = (String)map.get("ids");
            if (ids == null || ids.length() <= 0){
                throw new RuntimeException("同步测试用例ID列表不能为空");
            }

            String projectId = (String)map.get("projectId");
            if (projectId == null || projectId.length() <= 0){
                throw new RuntimeException("项目ID不能为空");
            }

            String turnId = (String)map.get("turnId");
            if (turnId == null || turnId.length() <= 0){
                throw new RuntimeException("轮次ID不能为空");
            }
            String turnVerId = (String)map.get("turnVerId");
            if (turnVerId == null || turnVerId.length() <= 0){
                throw new RuntimeException("轮次版本ID不能为空");
            }

            accessDatabaseService.updateAccessDataSource(accessDatabaseService.getAccessDataSourceInstance(runningAccessGlobalService.getValue4DataSourceFilePath()));

            List<String> idArr = Arrays.asList(ids.split(","));
            Triplet<List<RunningCase>, List<RunningTask>, List<RunningCaseStep>> triplet = accessDatabaseService.getNeedSyncData2MySQL(idArr, projectId, turnId, turnVerId);
            List<String> caseIds = triplet.getValue0().stream().map(i->i.getId()).collect(Collectors.toList());
            List<String> taskIds = triplet.getValue1().stream().map(i->i.getId()).collect(Collectors.toList());
            List<String> caseStepIds = triplet.getValue2().stream().map(i->i.getId()).collect(Collectors.toList());

            List<RunningCase> existWithNeedRemoveRunningCaseList = caseIds.size() > 0 ? runningCaseService.list(Wrappers.<RunningCase>query().lambda().in(RunningCase::getId, caseIds)) : new ArrayList<>();
            List<RunningTask> existWithNeedRemoveRunningTaskList = taskIds.size() > 0 ? runningTaskService.list(Wrappers.<RunningTask>query().lambda().in(RunningTask::getId, taskIds)) : new ArrayList<>();
            List<RunningCaseStep> existWithNeedRemoveRunningCaseStepList = caseStepIds.size() > 0 ? runningCaseStepService.list(Wrappers.<RunningCaseStep>query().lambda().in(RunningCaseStep::getId, caseStepIds)) : new ArrayList<>();
            if (existWithNeedRemoveRunningCaseList == null){
                existWithNeedRemoveRunningCaseList = new ArrayList<>();
            }
            if (existWithNeedRemoveRunningTaskList == null){
                existWithNeedRemoveRunningTaskList = new ArrayList<>();
            }
            if (existWithNeedRemoveRunningCaseStepList == null){
                existWithNeedRemoveRunningCaseStepList = new ArrayList<>();
            }


            List<String> existRunningCaseIds = existWithNeedRemoveRunningCaseList.stream().filter(i->i.getDelFlag().equals(0)).map(i->i.getId()).collect(Collectors.toList());
            List<String> removeRunningCaseIds = existWithNeedRemoveRunningCaseList.stream().filter(i->!i.getDelFlag().equals(0)).map(i->i.getId()).collect(Collectors.toList());
            List<String> existRunningTaskIds = existWithNeedRemoveRunningTaskList.stream().filter(i->i.getDelFlag().equals(0)).map(i->i.getId()).collect(Collectors.toList());
            List<String> removeRunningTaskIds = existWithNeedRemoveRunningTaskList.stream().filter(i->!i.getDelFlag().equals(0)).map(i->i.getId()).collect(Collectors.toList());
            List<String> existRunningCaseStepIds = existWithNeedRemoveRunningCaseStepList.stream().filter(i->!removeRunningCaseIds.contains(i.getCaseId())).map(i->i.getId()).collect(Collectors.toList());
            List<String> removeRunningCaseStepIds = existWithNeedRemoveRunningCaseStepList.stream().filter(i->removeRunningCaseIds.contains(i.getCaseId())).map(i->i.getId()).collect(Collectors.toList());

            List<String> saveRunningCaseIds = caseIds.stream().filter(i->!existRunningCaseIds.contains(i)).collect(Collectors.toList());
            List<String> saveRunningTaskIds = taskIds.stream().filter(i->!existRunningTaskIds.contains(i)).collect(Collectors.toList());
            List<String> saveRunningCaseStepIds = caseStepIds.stream().filter(i->!existRunningCaseStepIds.contains(i)).collect(Collectors.toList());

            if (removeRunningCaseStepIds.size() > 0){
                runningCaseStepService.removeByIds(removeRunningCaseStepIds);
            }
            if (removeRunningCaseIds.size() > 0){
                runningCaseService.removeByIds(removeRunningCaseIds);
            }
            if (removeRunningTaskIds.size() > 0){
                runningTaskService.removeByIds(removeRunningTaskIds);
            }

            if (saveRunningCaseIds.size() > 0){
                runningCaseService.saveBatch(triplet.getValue0().stream().filter(i->saveRunningCaseIds.contains(i.getId())).collect(Collectors.toList()));
            }
            if (saveRunningTaskIds.size() > 0){
                runningTaskService.saveBatch(triplet.getValue1().stream().filter(i->saveRunningTaskIds.contains(i.getId())).collect(Collectors.toList()));
            }
            if (saveRunningCaseStepIds.size() > 0){
                runningCaseStepService.saveBatch(triplet.getValue2().stream().filter(i->saveRunningCaseStepIds.contains(i.getId())).collect(Collectors.toList()));
            }

            return Result.ok("操作成功", SyncResultVo
                    .builder()
                    .skipCaseCount(existRunningCaseIds.size())
                    .skipCaseStepCount(existRunningCaseStepIds.size())
                    .skipTaskCount(existRunningTaskIds.size())
                    .syncCaseCount(saveRunningCaseIds.size())
                    .syncTaskCount(saveRunningTaskIds.size())
                    .syncCaseStepCount(saveRunningCaseStepIds.size())
                    .syncCaseIds(saveRunningCaseIds)
                    .syncCaseStepIds(saveRunningCaseStepIds)
                    .syncTaskIds(saveRunningTaskIds)
                    .build()
            );
        }
        catch (Exception e){
            return Result.error("同步失败, 原因: {result}".replace("{result}", e.getLocalizedMessage()));
        }
    }

    /**
     * 更新数据源文件路径
     * @param map JSON对象Map, 其中含
     *            key: [path], value: [文件路径 String]
     * @return
     */
    @PostMapping("update/data-source/path")
    public Result<?> updateAccessDataSource(@RequestBody(required = false) Map<String, ?> map){
        try {
            if (map == null || !map.containsKey("path") || map.get("path") == null || ((String)map.get("path")).trim().length() <= 0){
                throw new RuntimeException("数据源文件路径 不能为空");
            }
            String path = ((String) map.get("path")).trim();
            File file = new File(path);
            if (!file.exists() || !file.isFile()){
                throw new RuntimeException("数据源文件不存在");
            }


            accessDatabaseService.updateAccessDataSource(accessDatabaseService.getAccessDataSourceInstance(path));
            runningAccessGlobalService.setValue4DataSourceFilePath(path);
            return Result.ok("更新数据源成功");
        }
        catch (Exception e){
            return Result.error("更新数据源失败, 原因: {result}".replace("{result}", e.getLocalizedMessage()));
        }

    }

    /**
     * 获取存储的Access数据源文件路径
     * @return Access数据源文件路径
     */
    @GetMapping("get/data-source/path")
    public Result<?> getAccessDataSource(){
        return Result.ok("操作成功", runningAccessGlobalService.getValue4DataSourceFilePath());
    }


    /**
     * 检查该Access数据源文件是否存在
     * @param path Access数据源文件路径(eg. D:\0.mdb)
     * @return 是否存在
     */
    @GetMapping("check/data-source/path")
    public Result<?> checkAccessDateSource(@RequestParam("path")String path){

        try {
            if (path == null || path.trim().length() <= 0) {
                throw new RuntimeException("文件路径 不能为空");
            }
            File file = new File(path);
            if (!file.exists() || !file.isFile()){
                throw new RuntimeException("数据源文件不存在");
            }
            else {
                return Result.ok("数据源文件存在", true);
            }
        }
        catch (Exception e){
            return Result.error("检查数据源文件是否存在 失败, 原因: {result}".replace("{result}", e.getLocalizedMessage()));
        }



    }
}
