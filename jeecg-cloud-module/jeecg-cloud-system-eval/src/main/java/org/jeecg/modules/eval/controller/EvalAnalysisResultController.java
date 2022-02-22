package org.jeecg.modules.eval.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.eval.entity.EvalAnalysisResult;
import org.jeecg.modules.eval.service.IEvalAnalysisResultService;
import org.jeecg.modules.eval.service.IEvalResultDetailService;
import org.jeecg.modules.eval.vo.EvalAnalysisResultVO;
import org.jeecg.modules.running.uut.service.IRunningUutListService;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @Description: 分析评价结果表
 * @Author: jeecg-boot
 * @Date: 2020-12-30
 * @Version: V1.0
 */
@Api(tags = "分析评价结果表")
@RestController
@RequestMapping("/eval/evalAnalysisResult")
@Slf4j
public class EvalAnalysisResultController extends JeecgController<EvalAnalysisResult, IEvalAnalysisResultService> {
    @Autowired
    private IEvalAnalysisResultService evalAnalysisResultService;
    @Autowired
    private IEvalResultDetailService evalResultDetailService;
    @Autowired
    private IRunningUutListService runningUutListService;

    /**
     * 分页列表查询
     *
     * @param uutName
     * @param evaluate
     * @param pageNo
     * @param pageSize
     * @return
     */
    @AutoLog(value = "分析评价结果表-分页列表查询")
    @ApiOperation(value = "分析评价结果表-分页列表查询", notes = "分析评价结果表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(@RequestParam(name = "uutName", required = false) String uutName,
                                   @RequestParam(name = "evaluate", required = false) String evaluate,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   @RequestParam(name = "projectIdCookie", required = false) String projectIdCookie) {
        Page<EvalAnalysisResultVO> pageList = new Page<EvalAnalysisResultVO>(pageNo, pageSize);
        if(!StringUtils.isEmpty(projectIdCookie)){
            pageList = evalAnalysisResultService.queryPageList(pageList, uutName, evaluate, projectIdCookie);
        }else{
            pageList = new Page<>();
        }
        return Result.ok(pageList);
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "分析评价结果表-通过id查询")
    @ApiOperation(value = "分析评价结果表-通过id查询", notes = "分析评价结果表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        EvalAnalysisResult evalAnalysisResult = evalAnalysisResultService.getById(id);
        if (evalAnalysisResult == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(evalAnalysisResult);
    }
    /**导出exce*/
    @RequestMapping(value = "/exportXls")
    public void exportXls(HttpServletRequest request, HttpServletResponse response, EvalAnalysisResultVO evalMeasureInfo) throws IOException {
            evalMeasureInfo = evalAnalysisResultService.evalResult(evalMeasureInfo.getId());
            exportXls(request, response, evalMeasureInfo, "评价信息表");
    }

    private void exportXls(HttpServletRequest request, HttpServletResponse response, EvalAnalysisResultVO object, String title) throws IOException {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<EvalAnalysisResultVO> list = new ArrayList<>();
        list.add(evalAnalysisResultService.evalResult(object.getId()));

        Workbook wb = ExcelExportUtil.exportExcel(new ExportParams(title.concat("报表"), title), EvalAnalysisResultVO.class,list);
        Sheet sheet = wb.getSheet(title);
        Row row = sheet.createRow(4);
        Cell cell = row.createCell(0);
        cell.setCellValue("名称");

        cell = row.createCell(1);
        cell.setCellValue("权重");

        cell = row.createCell(2);
        cell.setCellValue("得分");

        List<LinkedHashMap<String, String>> listMap = formt(evalResultDetailService.queryTreeList(object.getSystemId(), object.getId()));

        int i = 5;

        for (LinkedHashMap<String, String> m : listMap) {
            row = sheet.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(m.get("table1"));

            cell = row.createCell(1);
            cell.setCellValue(m.get("table2"));

            cell = row.createCell(2);
            cell.setCellValue(m.get("table3"));
            i++;
        }
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
    }

    private String buildPre(int size) {
        String str = "";
        while (size > 0) {
            str = str.concat("-");
            size--;
        }
        return str;
    }

    private void zk(List<LinkedHashMap<String, String>> zk, List<CaseTreeIdModel> list, int size) {
        for (CaseTreeIdModel m : list) {
            LinkedHashMap<String, String> item = new LinkedHashMap<String, String>();
            List<CaseTreeIdModel> ca = m.getChildren();
            item.put("table1", buildPre(size).concat(m.getTitle()));
            if (m.getWeight() == null){
                continue;
            }else {
                item.put("table2", m.getWeight());
            }
            if(m.getScore() == null){
                continue;
            }else {
                item.put("table3", m.getScore().toString());
            }
            zk.add(item);
            if (!oConvertUtils.listIsEmpty(ca)) {
                zk(zk, ca, ++size);
                size--;
            }
        }
    }
    private List<LinkedHashMap<String, String>> formt(List<CaseTreeIdModel> list) {
        List<LinkedHashMap<String, String>> newList = new ArrayList<LinkedHashMap<String, String>>();
        this.zk(newList, list, 0);
        return newList;

    }
    @GetMapping(value = "/chart")
    public List<Map<String,Object>> listmap(@RequestParam (name = "systemId", required = true)String systemId,
                                             @RequestParam (name = "id",required = true)String id) {
           return this.handleData(systemId,id);
    }
    private List<Map<String, Object>> handleData(String systemId,String id){
        int a = 0;
        int b = 0;
        int c = 0;
        List<LinkedHashMap<String, String>> listMap = formt(evalResultDetailService.queryTreeList(systemId, id));
        List<List<LinkedHashMap<String,String>>> list = splitList(listMap, -1);
        List<Map<String, Object>> result = new ArrayList<>();
        for (List<LinkedHashMap<String,String>> list0: list) {
            Map<String, Object> subMap = new HashMap<>();
            List<List<LinkedHashMap<String,String>>> subList0 = splitList(list0, 0);
            subMap.put("name", listMap.get(0).get("table1").replaceAll("-","") + "(" + listMap.get(0).get("table3") + ")");
            subMap.put("value", listMap.get(0).get("table2"));
            //subMap.put("table3", listMap.get(0).get("table3"));
            List<Map<String, Object>> resultListMap = new ArrayList<>();
            for (List<LinkedHashMap<String,String>> list1: subList0) {
                Map<String, Object> mapResult1 = new HashMap<>();
                mapResult1.put("name", list1.get(0).get("table1").replaceAll("-","") + "(" + list1.get(0).get("table3") + ")");
                mapResult1.put("value", list1.get(0).get("table2"));
//                mapResult1.put("table2",list1.get(0).get("table2"));
                //mapResult1.put("table3", list1.get(0).get("table3"));
                List<LinkedHashMap<String,String>> list3 = new ArrayList<>();
                for (LinkedHashMap<String,String> map : list1.subList(1,list1.size())) {
                    LinkedHashMap<String,String> map1 = new LinkedHashMap<>();
                    map1.put("name", map.get("table1").replaceAll("-","") + "(" + map.get("table3") + ")");
                    a = Integer.parseInt(map.get("table2") );
                    b = Integer.parseInt(String.valueOf(mapResult1.get("value")));
                    c = a * b / 100;
                    map1.put("value",String.valueOf(c));
                    mapResult1.put("value", list1.get(0).get("table2"));
                    list3.add(map1);
                }
                mapResult1.put("children", list3);
                resultListMap.add(mapResult1);
            }
            subMap.put("children", resultListMap);
            result.add(subMap);
        }
        return  result;
    }

    private List<List<LinkedHashMap<String,String>>> splitList(List<LinkedHashMap<String,String>> list, int position){
        List<List<LinkedHashMap<String,String>>> result = new ArrayList<>();
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < list.size(); i++){
            if(((String)list.get(i).get("table1")).lastIndexOf("-") == position){
                if(i != 0){
                    index.add(i);
                }
            }
        }
        index.add(list.size());
        int b = 0;
        for (int j : index) {
            if(position == -1){
                result.add(list.subList(b,j));
                b = j;
            }else if(position == 0){
                b = j;
                position = -1;
            }
        }
        return result;
    }

    /**
     * 查询测试用例完成数并加入轮次
     * @param uutId
     * @param systemId
     */
    @AutoLog(value = "eval_system-变更评价体系")
    @ApiOperation(value="eval_system-变更评价体系", notes="eval_system-变更评价体系")
    @GetMapping(value = "/changeSystem")
    public Result<?> changeSystem(@RequestParam(name = "uutId") String uutId,
                                 @RequestParam(name = "systemId") String systemId) {
        evalAnalysisResultService.changeSystem(uutId, systemId);
        runningUutListService.changeSystem(uutId, systemId);
        return Result.ok("变更成功");
    }
}
