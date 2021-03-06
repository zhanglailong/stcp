package org.jeecg.modules.eval.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.eval.entity.EvalMethodInfo;
import org.jeecg.modules.eval.entity.EvalMethod;
import org.jeecg.modules.eval.vo.EvalMethodPage;
import org.jeecg.modules.eval.service.IEvalMethodService;
import org.jeecg.modules.eval.service.IEvalMethodInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: eval_method
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
@Api(tags = "eval_method")
@RestController
@RequestMapping("/eval/evalMethod")
@Slf4j
public class EvalMethodController {
    @Autowired
    private IEvalMethodService evalMethodService;
    @Autowired
    private IEvalMethodInfoService evalMethodInfoService;

    /**
     * ??????????????????
     *
     * @param evalMethod
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "eval_method-??????????????????")
    @ApiOperation(value = "eval_method-??????????????????", notes = "eval_method-??????????????????")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EvalMethod evalMethod,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<EvalMethod> queryWrapper = QueryGenerator.initQueryWrapper(evalMethod, req.getParameterMap());
        Page<EvalMethod> page = new Page<EvalMethod>(pageNo, pageSize);
        IPage<EvalMethod> pageList = evalMethodService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * ??????
     *
     * @param evalMethodPage
     * @return
     */
    @AutoLog(value = "eval_method-??????")
    @ApiOperation(value = "eval_method-??????", notes = "eval_method-??????")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EvalMethodPage evalMethodPage) {
        EvalMethod evalMethod = new EvalMethod();
        BeanUtils.copyProperties(evalMethodPage, evalMethod);
        evalMethodService.saveMain(evalMethod, evalMethodPage.getEvalMethodInfoList());
        return Result.ok("???????????????");
    }

    /**
     * ??????
     *
     * @param evalMethodPage
     * @return
     */
    @AutoLog(value = "eval_method-??????")
    @ApiOperation(value = "eval_method-??????", notes = "eval_method-??????")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EvalMethodPage evalMethodPage) {
        EvalMethod evalMethod = new EvalMethod();
        BeanUtils.copyProperties(evalMethodPage, evalMethod);
        EvalMethod evalMethodEntity = evalMethodService.getById(evalMethod.getId());
        if (evalMethodEntity == null) {
            return Result.error("?????????????????????");
        }
        evalMethodService.updateMain(evalMethod, evalMethodPage.getEvalMethodInfoList());
        return Result.ok("????????????!");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method-??????id??????")
    @ApiOperation(value = "eval_method-??????id??????", notes = "eval_method-??????id??????")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        evalMethodService.delMain(id);
        return Result.ok("????????????!");
    }

    /**
     * ????????????
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "eval_method-????????????")
    @ApiOperation(value = "eval_method-????????????", notes = "eval_method-????????????")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.evalMethodService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("?????????????????????");
    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method-??????id??????")
    @ApiOperation(value = "eval_method-??????id??????", notes = "eval_method-??????id??????")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EvalMethod evalMethod = evalMethodService.getById(id);
        if (evalMethod == null) {
            return Result.error("?????????????????????");
        }
        return Result.ok(evalMethod);

    }

    /**
     * ??????id??????
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method_info????????????ID??????")
    @ApiOperation(value = "eval_method_info??????ID??????", notes = "eval_method_info-?????????ID??????")
    @GetMapping(value = "/queryEvalMethodInfoByMainId")
    public Result<?> queryEvalMethodInfoListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<EvalMethodInfo> evalMethodInfoList = evalMethodInfoService.selectByMainId(id);
        return Result.ok(evalMethodInfoList);
    }

    /**
     * ??????excel
     *
     * @param request
     * @param evalMethod
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EvalMethod evalMethod) {
        // Step.1 ??????????????????????????????
        QueryWrapper<EvalMethod> queryWrapper = QueryGenerator.initQueryWrapper(evalMethod, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 ??????????????????
        List<EvalMethod> queryList = evalMethodService.list(queryWrapper);
        // ??????????????????
        String selections = request.getParameter("selections");
        List<EvalMethod> evalMethodList = new ArrayList<EvalMethod>();
        if (oConvertUtils.isEmpty(selections)) {
            evalMethodList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            evalMethodList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 ??????pageList
        List<EvalMethodPage> pageList = new ArrayList<EvalMethodPage>();
        for (EvalMethod main : evalMethodList) {
            EvalMethodPage vo = new EvalMethodPage();
            BeanUtils.copyProperties(main, vo);
            List<EvalMethodInfo> evalMethodInfoList = evalMethodInfoService.selectByMainId(main.getId());
            vo.setEvalMethodInfoList(evalMethodInfoList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "eval_method??????");
        mv.addObject(NormalExcelConstants.CLASS, EvalMethodPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("eval_method??????", "?????????:" + sysUser.getRealname(), "eval_method"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // ????????????????????????
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<EvalMethodPage> list = ExcelImportUtil.importExcel(file.getInputStream(), EvalMethodPage.class, params);
                for (EvalMethodPage page : list) {
                    EvalMethod po = new EvalMethod();
                    BeanUtils.copyProperties(page, po);
                    evalMethodService.saveMain(po, page.getEvalMethodInfoList());
                }
                return Result.ok("?????????????????????????????????:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("??????????????????:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("?????????????????????");
    }

}
