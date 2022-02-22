package org.jeecg.modules.plan.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.*;
import org.jeecg.modules.plan.service.IEnvPlanService;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zlf
 * 环境设计
 */
@Api(tags = "环境设计")
@RestController
@RequestMapping("/plan/envPlan")
@Slf4j
public class EnvPlanController extends JeecgController<EnvPlan, IEnvPlanService> {
    @Resource
    private IEnvPlanService envPlanService;
    @Resource
    private IVmDesignService vmDesignService;

    /**
     * 分页列表查询
     *
     * @param envPlan  规划
     * @param pageNo   页
     * @param pageSize 数量
     * @param req      req
     * @return 分页数据
     */
    @AutoLog(value = "环境设计-分页列表查询")
    @ApiOperation(value = "环境设计-分页列表查询", notes = "环境设计-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EnvPlan envPlan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        try {
            QueryWrapper<EnvPlan> queryWrapper = QueryGenerator.initQueryWrapper(envPlan, req.getParameterMap());
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
            queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            Page<EnvPlan> page = new Page<>(pageNo, pageSize);
            String[] ids = req.getParameterMap().get(CommonConstant.DATA_STRING_IDS);
            if (ids != null) {
                queryWrapper.in(CommonConstant.DATA_STRING_ID, (Object[]) ids[0].split(","));
            }
            queryWrapper.orderByDesc(CommonConstant.DATA_CREATE_TIME);
            IPage<EnvPlan> pageList = envPlanService.page(page, queryWrapper);
            return Result.OK(pageList);
        } catch (Exception e) {
            log.error("环境设计-分页列表查询异常,原因:" + e.getMessage());
            return Result.error("环境设计-分页列表查询异常,原因:" + e.getMessage());
        }
    }

    /**
     * 添加
     *
     * @param jsonParam 添加环境
     * @return 状态
     */
    @AutoLog(value = "环境设计-添加")
    @ApiOperation(value = "环境设计-添加", notes = "环境设计-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody JSONObject jsonParam) {
        int result = 0;
        try {
            if (jsonParam != null && StringUtils.isNotEmpty(jsonParam.toString())) {
                EnvPlanJson envPlanJson = JSON.parseObject(jsonParam.toString(), EnvPlanJson.class);
                if (envPlanJson != null) {
                    if (envPlanService.getAnalysisEnvPlan(envPlanJson, jsonParam.toString())) {
                        result = 1;
                    }
                }
            }
        } catch (Exception e) {
            log.error("环境规划异常：" + e.getMessage());
            return Result.error("环境规划异常：" + e.getMessage());
        }

        if (result == 0) {
            return Result.error("data值异常，请重新发送");
        } else {
            return Result.OK("添加成功！");
        }
    }

    /**
     * 编辑
     *
     * @param envPlan 环境
     * @return 编辑状态
     */
    @AutoLog(value = "环境设计-编辑")
    @ApiOperation(value = "环境设计-编辑", notes = "环境设计-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EnvPlan envPlan) {
        envPlanService.updateById(envPlan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id id
     * @return 删除状态
     */
    @AutoLog(value = "环境设计-通过id删除")
    @ApiOperation(value = "环境设计-通过id删除", notes = "环境设计-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        envPlanService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids id集合
     * @return 批量删除状态
     */
    @AutoLog(value = "环境设计-批量删除")
    @ApiOperation(value = "环境设计-批量删除", notes = "环境设计-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.envPlanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return 环境设计
     */
    @AutoLog(value = "环境设计-通过id查询")
    @ApiOperation(value = "环境设计-通过id查询", notes = "环境设计-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        EnvPlan envPlan = null;
        VoEnvPlan voEnvPlan = new VoEnvPlan();
        if (StringUtils.isNotEmpty(id) && !id.equals(CommonConstant.DATA_UNDEFINED)) {
            envPlan = envPlanService.getById(id);
            List<VmDesign> vmList = vmDesignService.getVmListByEnvId(envPlan.getId());
            if (vmList == null) {
                vmList = new ArrayList<>();
            }
            BeanUtils.copyProperties(envPlan, voEnvPlan);
            voEnvPlan.setVmList(vmList);
        }
        if (envPlan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(voEnvPlan);
    }

    /**
     * 导出excel
     *
     * @param request request
     * @param envPlan 环境
     * @return 导出excel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EnvPlan envPlan) {
        return super.exportXls(request, envPlan, EnvPlan.class, "环境设计");
    }

    /**
     * 通过excel导入数据
     *
     * @param request  request
     * @param response response
     * @return 通过excel导入数据
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, EnvPlan.class);
    }

}
