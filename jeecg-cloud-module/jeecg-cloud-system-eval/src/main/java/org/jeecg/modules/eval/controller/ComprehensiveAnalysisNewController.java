package org.jeecg.modules.eval.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.eval.service.IComprehensiveAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: 综合分析
 * @Author: jeecg-boot
 * @Date:   2021-03-31
 * @Version: V1.0
 */
@Api(tags="综合分析")
@RestController
@RequestMapping("/eval/analysis")
@Slf4j
public class ComprehensiveAnalysisNewController {

    @Autowired
    IComprehensiveAnalysisService iComprehensiveAnalysisService;

    /**
     * 综合分析
     *
     * @param analyzeResultId 分析结果表id
     * @return Result  结果
     */
    @AutoLog(value = "综合分析 分析处理")
    @ApiOperation(value="分析处理", notes="分析处理")
    @GetMapping(value = "/analysis")
    @ResponseBody
    public Result<?> analysis(@RequestParam(name = "analyzeResultId") String analyzeResultId) throws Exception {
        return iComprehensiveAnalysisService.analysisProcess(analyzeResultId);
    }
}
