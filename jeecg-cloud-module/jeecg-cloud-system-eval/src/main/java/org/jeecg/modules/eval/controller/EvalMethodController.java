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
     * 分页列表查询
     *
     * @param evalMethod
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "eval_method-分页列表查询")
    @ApiOperation(value = "eval_method-分页列表查询", notes = "eval_method-分页列表查询")
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
     * 添加
     *
     * @param evalMethodPage
     * @return
     */
    @AutoLog(value = "eval_method-添加")
    @ApiOperation(value = "eval_method-添加", notes = "eval_method-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EvalMethodPage evalMethodPage) {
        EvalMethod evalMethod = new EvalMethod();
        BeanUtils.copyProperties(evalMethodPage, evalMethod);
        evalMethodService.saveMain(evalMethod, evalMethodPage.getEvalMethodInfoList());
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param evalMethodPage
     * @return
     */
    @AutoLog(value = "eval_method-编辑")
    @ApiOperation(value = "eval_method-编辑", notes = "eval_method-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EvalMethodPage evalMethodPage) {
        EvalMethod evalMethod = new EvalMethod();
        BeanUtils.copyProperties(evalMethodPage, evalMethod);
        EvalMethod evalMethodEntity = evalMethodService.getById(evalMethod.getId());
        if (evalMethodEntity == null) {
            return Result.error("未找到对应数据");
        }
        evalMethodService.updateMain(evalMethod, evalMethodPage.getEvalMethodInfoList());
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method-通过id删除")
    @ApiOperation(value = "eval_method-通过id删除", notes = "eval_method-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        evalMethodService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "eval_method-批量删除")
    @ApiOperation(value = "eval_method-批量删除", notes = "eval_method-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.evalMethodService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method-通过id查询")
    @ApiOperation(value = "eval_method-通过id查询", notes = "eval_method-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        EvalMethod evalMethod = evalMethodService.getById(id);
        if (evalMethod == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(evalMethod);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "eval_method_info通过主表ID查询")
    @ApiOperation(value = "eval_method_info主表ID查询", notes = "eval_method_info-通主表ID查询")
    @GetMapping(value = "/queryEvalMethodInfoByMainId")
    public Result<?> queryEvalMethodInfoListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<EvalMethodInfo> evalMethodInfoList = evalMethodInfoService.selectByMainId(id);
        return Result.ok(evalMethodInfoList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param evalMethod
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EvalMethod evalMethod) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<EvalMethod> queryWrapper = QueryGenerator.initQueryWrapper(evalMethod, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<EvalMethod> queryList = evalMethodService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<EvalMethod> evalMethodList = new ArrayList<EvalMethod>();
        if (oConvertUtils.isEmpty(selections)) {
            evalMethodList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            evalMethodList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<EvalMethodPage> pageList = new ArrayList<EvalMethodPage>();
        for (EvalMethod main : evalMethodList) {
            EvalMethodPage vo = new EvalMethodPage();
            BeanUtils.copyProperties(main, vo);
            List<EvalMethodInfo> evalMethodInfoList = evalMethodInfoService.selectByMainId(main.getId());
            vo.setEvalMethodInfoList(evalMethodInfoList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "eval_method列表");
        mv.addObject(NormalExcelConstants.CLASS, EvalMethodPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("eval_method数据", "导出人:" + sysUser.getRealname(), "eval_method"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
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
                return Result.ok("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.ok("文件导入失败！");
    }

}
