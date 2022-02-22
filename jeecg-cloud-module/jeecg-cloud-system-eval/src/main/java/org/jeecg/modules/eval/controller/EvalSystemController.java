package org.jeecg.modules.eval.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.utils.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;
import org.jeecg.modules.eval.entity.EvalSystem;
import org.jeecg.modules.eval.service.IEvalAnalysisResultService;
import org.jeecg.modules.eval.service.IEvalMeasureInfoService;
import org.jeecg.modules.eval.service.IEvalMeasureWeightService;
import org.jeecg.modules.eval.service.IEvalSystemService;
import org.jeecg.modules.running.project.entity.RunningProject;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.jeecg.modules.running.uut.mapper.RunningUutListMapper;
import org.jeecg.modules.running.uut.service.IRunningUutListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
* @Description: eval_system
* @Author: jeecg-boot
* @Date:   2021-02-24
* @Version: V1.0
*/
@Api(tags="eval_system")
@RestController
@RequestMapping("/evalSystem/evalSystem")
@Slf4j
public class EvalSystemController extends JeecgController<EvalSystem, IEvalSystemService> {
    @Autowired
    private IEvalSystemService evalSystemService;

    @Autowired
    private RunningUutListMapper runningUutListMapper;

    @Autowired
    private IRunningUutListService runningUutListService;

    @Autowired
    private IEvalMeasureWeightService evalMeasureWeightService;

    @Autowired
    private IEvalAnalysisResultService evalAnalysisResultService;

    @Autowired
    private IEvalMeasureInfoService evalMeasureInfoService;

   /**
    * 分页列表查询
    *
    * @param evalSystem
    * @param pageNo
    * @param pageSize
    * @param req
    * @return
    */
   @AutoLog(value = "eval_system-分页列表查询")
   @ApiOperation(value="eval_system-分页列表查询", notes="eval_system-分页列表查询")
   @GetMapping(value = "/list")
   public Result<?> queryPageList(EvalSystem evalSystem,
                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                  HttpServletRequest req) {
       QueryWrapper<EvalSystem> queryWrapper = QueryGenerator.initQueryWrapper(evalSystem, req.getParameterMap());
       Page<EvalSystem> page = new Page<EvalSystem>(pageNo, pageSize);
       IPage<EvalSystem> pageList = evalSystemService.page(page, queryWrapper);
       //查询度量树列表
       List<EvalSystem> resultList = new ArrayList<>();
       for (EvalSystem es : pageList.getRecords()) {
           es.setTreeList(evalMeasureWeightService.queryTreeList(es.getId()));
           resultList.add(es);
       }
       pageList.setRecords(resultList);
       //遍历查询被测对象
//       List<EvalSystem> list = pageList.getRecords();
//       List<EvalSystem> resultList = new ArrayList<>();
//       for (EvalSystem es : list ) {
//           es.setUutList(evalSystemService.getUutList(es.getId()));
//           resultList.add(es);
//       }
       return Result.ok(pageList);
   }





   /**
    *   添加
    *
    * @param evalSystem
    * @return
    */
   @AutoLog(value = "eval_system-添加")
   @ApiOperation(value="eval_system-添加", notes="eval_system-添加")
   @PostMapping(value = "/add")
   public Result<?> add(@RequestBody EvalSystem evalSystem) {
       evalSystemService.save(evalSystem);
       //保存度量信息
       saveTreeData(evalSystem);
       //保存度量树
       evalMeasureWeightService.saveList(evalSystem.getTreeList(), evalSystem.getId());
       return Result.ok("添加成功！");
   }

    public Result<?> saveTreeData(EvalSystem evalSystem) {
        List<String> idList = evalSystem.getSelectedRowKeys();
        String systemId = evalSystem.getId();
        String methodId = evalSystem.getMethodId();
        if (idList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("system_id", systemId);
            evalMeasureInfoService.removeByMap(map);
            for (String temp : idList) {
                EvalMeasureStructureVo evalMeasureStructureVo = evalMeasureInfoService.getEvalMeasuerStructureVo(temp);
                if(null != evalMeasureStructureVo){
                    EvalMeasureInfo evalMeasureInfo = new EvalMeasureInfo();
                    evalMeasureInfo.setSid(evalMeasureStructureVo.getId());
                    evalMeasureInfo.setName(evalMeasureStructureVo.getName());
                    evalMeasureInfo.setParentId(evalMeasureStructureVo.getParentId());
                    evalMeasureInfo.setGrandId(evalMeasureStructureVo.getGrandId());
                    evalMeasureInfo.setFormula(evalMeasureStructureVo.getFormula());
                    evalMeasureInfo.setSystemId(systemId);
                    evalMeasureInfo.setMethodId(methodId);
                    evalMeasureInfoService.save(evalMeasureInfo);
                }
            }
        }
        return Result.ok();
    }

   /**
    *  编辑
    *
    * @param evalSystem
    * @return
    */
   @AutoLog(value = "eval_system-编辑")
   @ApiOperation(value="eval_system-编辑", notes="eval_system-编辑")
   @PutMapping(value = "/edit")
   public Result<?> edit(@RequestBody EvalSystem evalSystem) {
       evalSystemService.updateById(evalSystem);
       //保存度量树
       Map<String, Object> map = new HashMap<>();
       map.put("system_id", evalSystem.getId());
       //度量信息
       evalMeasureInfoService.removeByMap(map);
       saveTreeData(evalSystem);
       //权重
       evalMeasureWeightService.removeByMap(map);
       evalMeasureWeightService.saveList(evalSystem.getTreeList(), evalSystem.getId());
       return Result.ok("编辑成功!");
   }

   /**
    *   通过id删除
    *
    * @param id
    * @return
    */
   @AutoLog(value = "eval_system-通过id删除")
   @ApiOperation(value="eval_system-通过id删除", notes="eval_system-通过id删除")
   @DeleteMapping(value = "/delete")
   public Result<?> delete(@RequestParam(name="id",required=true) String id) {
       evalSystemService.deleteMeasureAndWeightInfo(id);
       return Result.ok("删除成功!");
   }

   /**
    *  批量删除
    *
    * @param ids
    * @return
    */
   @AutoLog(value = "eval_system-批量删除")
   @ApiOperation(value="eval_system-批量删除", notes="eval_system-批量删除")
   @DeleteMapping(value = "/deleteBatch")
   public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
       this.evalSystemService.removeByIds(Arrays.asList(ids.split(",")));
       return Result.ok("批量删除成功!");
   }

   /**
    * 通过id查询
    *
    * @param id
    * @return
    */
   @AutoLog(value = "eval_system-通过id查询")
   @ApiOperation(value="eval_system-通过id查询", notes="eval_system-通过id查询")
   @GetMapping(value = "/queryById")
   public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
       EvalSystem evalSystem = evalSystemService.getById(id);
       if(evalSystem==null) {
           return Result.error("未找到对应数据");
       }
       return Result.ok(evalSystem);
   }

   /**
   * 导出excel
   *
   * @param request
   * @param evalSystem
   */
   @RequestMapping(value = "/exportXls")
   public ModelAndView exportXls(HttpServletRequest request, EvalSystem evalSystem) {
       return super.exportXls(request, evalSystem, EvalSystem.class, "eval_system");
   }

   /**
     * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
   @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
   public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
       return super.importExcel(request, response, EvalSystem.class);
   }

    /**
     * 通过id查询
     *
     * @param projectId
     * @return
     */
    @AutoLog(value = "eval_system-通过项目id查询项目相关信息")
    @ApiOperation(value="eval_system-通过项目id查询项目相关信息", notes="eval_system-通过项目id查询项目相关信息")
    @GetMapping(value = "/getUutListByProjectId")
    public Result<?> getUutListByProjectId(@RequestParam(name="projectId",required=false) String projectId) {
        //被测对象信息
        Map<String, String> result = new HashMap<>();
        if(!StringUtils.isEmpty(projectId)){
            RunningProject runningProject = evalSystemService.findUniqueBy("id",projectId);
            RunningUutList runningUutList = runningUutListMapper.selectById(runningProject.getUutListId());
            if(Optional.ofNullable(runningUutList).isPresent()){
                result.put("projectName", runningProject.getProjectName());
                result.put("uutName", runningUutList.getUutName());
            }
            //查询最终评价
            result.put("evaluate", evalAnalysisResultService.getEvaluation(projectId));
        }else {
            return Result.error("未找到对应数据");
        }
        //轮次数
        result.put("turnNum", evalSystemService.getTurnNum(projectId)+"");
        //测试项
        List<String> taskIds = evalSystemService.getTaskIds(projectId,"", 0);
        result.put("taskNum", taskIds.size() + "");
        //测试用例
        List<String> caseIds = evalSystemService.getCaseIds(taskIds, "");
        int caseNum = caseIds.size();
        result.put("caseNum", caseNum + "");
        //通过的测试用例数
        int accessNum = evalSystemService.getAccessCaseIds(taskIds, "").size();
        result.put("accessCaseNum", accessNum + "");
        //未通过的测试用例数
        result.put("notAccessCaseNum", caseNum - accessNum + "");
        //问题单数
        int questionNum = evalSystemService.getQuestionIds(caseIds, "");
        result.put("questionNum", questionNum + "");
        //已解决的问题单数
        int solvedQuestionNum = evalSystemService.getSolvedQuestionIds(caseIds, "");
        result.put("solvedQuestionNum", solvedQuestionNum + "");
        //未解决的问题单数
        result.put("unSolvedQuestionNum", questionNum - solvedQuestionNum + "");
        //已执行的测试用例数
        int executedCaseCount = evalSystemService.getExecutedCaseCount(taskIds, "");
        result.put("executedCaseCount", executedCaseCount + "");
        //未执行的测试用例数
        result.put("unExecutedCaseCount", caseNum - executedCaseCount + "");
        return Result.ok(result);
    }

    /**
     * 通过id查询
     *
     * @param projectId
     * @return
     */
    @AutoLog(value = "eval_system-通过项目id按轮次查询项目相关信息")
    @ApiOperation(value="eval_system-通过项目id按轮次查询项目相关信息", notes="eval_system-通过项目id按轮次查询项目相关信息")
    @GetMapping(value = "/getTurnByProjectId")
    public Result<?> getTurnByProjectId(@RequestParam(name="projectId",required=false) String projectId) {
        //被测对象信息
        Map<String, Map<String, String>> result = new HashMap<>();
        List<String> turnList = evalSystemService.getTurnList(projectId);
        for (String turnId: turnList) {
            Map<String, String> map = new HashMap<>();
            //测试项
            List<String> taskIds = evalSystemService.getTaskIds(projectId, turnId, 0);
            //map.put("taskNum", taskIds.size() + "");
            //测试用例
            List<String> caseIds = evalSystemService.getCaseIds(taskIds, turnId);
            int caseNum = caseIds.size();
            map.put("caseNum", caseNum + "");
            //通过的测试用例数
            int accessNum = evalSystemService.getAccessCaseIds(taskIds, turnId).size();
            map.put("accessCaseNum", accessNum + "");
            //未通过的测试用例数
            map.put("notAccessCaseNum", caseNum - accessNum + "");
            //问题单数
            int questionNum = evalSystemService.getQuestionIds(caseIds, turnId);
            map.put("questionNum", questionNum + "");
            //已解决的问题单数

            int solvedQuestionNum = evalSystemService.getSolvedQuestionIds(caseIds, turnId);
            map.put("solvedQuestionNum", solvedQuestionNum + "");
            //未解决的问题单数
            map.put("unSolvedQuestionNum", questionNum - solvedQuestionNum + "");
            //已执行的测试用例数
            int executedCaseCount = evalSystemService.getExecutedCaseCount(taskIds, turnId);
            map.put("executedCaseCount", executedCaseCount + "");
            //未执行的测试用例数
            map.put("unExecutedCaseCount", caseNum - executedCaseCount + "");
            result.put(evalSystemService.getTurnName(turnId), map);
        }
        return Result.ok(result);
    }

    /**
     *   通过systemId获取被测对象列表
     * @param systemId
     * @return
     */
    @AutoLog(value = "eval_system-通过systemId获取被测对象列表")
    @ApiOperation(value="eval_system-通过systemId获取被测对象列表", notes="eval_system-通过systemId获取被测对象列表")
    @GetMapping(value = "/getUutBySystemId")
    public Result<?> getUutBySystemId(RunningUutList runningUutList,
                                      @RequestParam(name="systemId",required=true) String systemId,
                                      @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                      @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                      HttpServletRequest req) {
        QueryWrapper<RunningUutList> queryWrapper = QueryGenerator.initQueryWrapper(runningUutList, req.getParameterMap());
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("test_template", systemId);
        Page<RunningUutList> page = new Page<>(pageNo, pageSize);
        IPage<RunningUutList> pageList = runningUutListService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     *   被测对象列表查询
     * @return Result<RunningUutList>
     */
    @AutoLog(value = "eval_system-被测对象列表查询")
    @ApiOperation(value="eval_system-被测对象列表查询", notes="eval_system-被测对象列表查询")
    @GetMapping(value = "/getUutList")
    public Result<?> getUutList(RunningUutList runningUutList,
                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                HttpServletRequest req) {
        QueryWrapper<RunningUutList> queryWrapper = QueryGenerator.initQueryWrapper(runningUutList, req.getParameterMap());
        queryWrapper.eq("delete_flag",0);
        Page<RunningUutList> page = new Page<>(pageNo, pageSize);
        IPage<RunningUutList> pageList = runningUutListService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_system-通过id删除")
    @ApiOperation(value="eval_system-通过id删除", notes="eval_system-通过id删除")
    @GetMapping(value = "/deleteUut")
    public Result<?> deleteUut(@RequestParam(name="id",required=true) String id) {
        runningUutListMapper.setValue(id, "test_template", "");
        return Result.ok("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "eval_system-批量删除")
    @ApiOperation(value="eval_system-批量删除", notes="eval_system-批量删除")
    @GetMapping(value = "/deleteUutBatch")
    public Result<?> deleteUutBatch(@RequestParam(name="ids",required=true) String ids) {
        for (String id : ids.split(",") ) {
            runningUutListMapper.setValue(id, "test_template", "");
        }
        return Result.ok("批量删除成功!");
    }

    /**
     *   通过systemId获取被测对象列表
     * @param uutIds
     * @param systemId
     */
    @AutoLog(value = "eval_system-批量为被测对象设置评价体系")
    @ApiOperation(value="eval_system-批量为被测对象设置评价体系", notes="eval_system-批量为被测对象设置评价体系")
    @GetMapping(value = "/batchSetSystemId")
    public Result<?> batchSetSystemId(@RequestParam(name = "uutIds") String uutIds,
                                      @RequestParam(name = "systemId") String systemId) {
        for (String uutId : uutIds.split(",")) {
            runningUutListMapper.setValue(uutId, "test_template", systemId);
        }
        return Result.ok("设置成功");
    }

    /**
     * 查询测试项完成数并加入轮次
     * @param projectId
     */
    @AutoLog(value = "eval_system-查询测试项完成数并加入轮次")
    @ApiOperation(value="eval_system-查询测试项完成数并加入轮次", notes="eval_system-查询测试项完成数并加入轮次")
    @GetMapping(value = "/getTurnTask")
    public Result<?> getTurnTask(@RequestParam(name = "projectId") String projectId) {
        //未完成
        Map<String, Object> unFinished = evalSystemService.getTurnTask(projectId, "0");
        //已完成
        Map<String, Object> finished = evalSystemService.getTurnTask(projectId, "1");
        Map<String, Map<String, Object>> result = new HashMap<>();
        result.put("unfinishedTask", unFinished.size() > 0 ? unFinished : null);
        result.put("finishedTask", finished.size() > 0 ? finished : null);
        return Result.ok(result);
    }

    /**
     * 查询测试用例完成数并加入轮次
     * @param projectId
     */
    @AutoLog(value = "eval_system-查询测试用例通过数并加入轮次")
    @ApiOperation(value="eval_system-查询测试用例通过数并加入轮次", notes="eval_system-查询测试用例通过数并加入轮次")
    @GetMapping(value = "/getTurnCase")
    public Result<?> getTurnCase(@RequestParam(name = "projectId") String projectId) {
        //未通过
        List<Map<String, Object>> access = evalSystemService.getTurnCase(projectId, "0");
        //已通过
        List<Map<String, Object>> unAccess = evalSystemService.getTurnCase(projectId, "1");
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("unAccessCaseNum", unAccess.size() > 0 ? unAccess : null);
        result.put("accessCaseNum", access.size() > 0 ? access : null);
        return Result.ok(result);
    }

    /**
     * 查询测试用例完成数并加入轮次
     * @param projectId
     */
    @AutoLog(value = "eval_system-查询测试用例执行数并加入轮次")
    @ApiOperation(value="eval_system-查询测试用例执行数并加入轮次", notes="eval_system-查询测试用例执行数并加入轮次")
    @GetMapping(value = "/getTurnCaseExecute")
    public Result<?> getTurnCaseExecute(@RequestParam(name = "projectId") String projectId) {
        //未通过
        List<Map<String, Object>> executed = evalSystemService.getTurnCaseExecute(projectId, "0");
        //已通过
        List<Map<String, Object>> unExecuted = evalSystemService.getTurnCaseExecute(projectId, "1");
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("unExecutedCaseCount", unExecuted.size() > 0 ? unExecuted : null);
        result.put("executedCaseCount", executed.size() > 0 ? executed : null);
        return Result.ok(result);
    }

    /**
     * 查询测试用例完成数并加入轮次
     * @param projectId
     */
    @AutoLog(value = "eval_system-查询问题单解决数并加入轮次")
    @ApiOperation(value="eval_system-查询测试用例执行数并加入轮次", notes="eval_system-查询测试用例执行数并加入轮次")
    @GetMapping(value = "/getTurnQuestionSolved")
    public Result<?> getTurnQuestionSolved(@RequestParam(name = "projectId") String projectId) {
        //未通过
        List<Map<String, Object>> unSolved = evalSystemService.getTurnQuestionSolved(projectId, "0");
        //已通过
        List<Map<String, Object>> solved = evalSystemService.getTurnQuestionSolved(projectId, "1");
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("unSolvedQuestionNum", unSolved.size() > 0 ? unSolved : null);
        result.put("solvedQuestionNum", solved.size() > 0 ? solved : null);
        return Result.ok(result);
    }

    /**
     * 查询测试用例完成数并加入轮次
     * @param sid
     */
    @AutoLog(value = "eval_system-查询公式参数")
    @ApiOperation(value="eval_system-查询公式参数", notes="eval_system-查询公式参数")
    @GetMapping(value = "/getParamsBySid")
    public Result<?> getParamsBySid(@RequestParam(name = "sid") String sid) {
        List<Map<String, Object>> result = evalSystemService.getParamsBySid(sid);
        return Result.ok(result);
    }
}
