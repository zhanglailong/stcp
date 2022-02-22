package org.jeecg.modules.plan.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.EnvironmentPlan;
import org.jeecg.modules.plan.service.IEnvironmentPlanService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author zlf
 * @Description: 环境规划
 * @Date: 2020-12-25
 * @Version: V1.0
 */
@Api(tags = "环境规划")
@RestController
@RequestMapping("/plan/environmentPlan")
@Slf4j
public class EnvironmentPlanController extends JeecgController<EnvironmentPlan, IEnvironmentPlanService> {
    @Resource
    private IEnvironmentPlanService environmentPlanService;

    /**
     * 分页列表查询
     *
     * @param environmentPlan
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "环境规划-分页列表查询")
    @ApiOperation(value = "环境规划-分页列表查询", notes = "环境规划-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(EnvironmentPlan environmentPlan,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<EnvironmentPlan> queryWrapper = QueryGenerator.initQueryWrapper(environmentPlan, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
        Page<EnvironmentPlan> page = new Page<>(pageNo, pageSize);
        IPage<EnvironmentPlan> pageList = environmentPlanService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param environmentPlan
     * @return
     */
    @AutoLog(value = "环境规划-添加")
    @ApiOperation(value = "环境规划-添加", notes = "环境规划-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody EnvironmentPlan environmentPlan, @RequestParam String data) {
        System.out.println(environmentPlan.toString());
        System.out.println(data);
        JSONObject jsonObject = JSONObject.parseObject(data);
        System.out.println(jsonObject.get("name"));
        System.out.println(jsonObject.get("remarks"));
        System.out.println(jsonObject.get("purpose"));
        System.out.println(jsonObject.get("nodeList"));
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param environmentPlan
     * @return
     */
    @AutoLog(value = "环境规划-编辑")
    @ApiOperation(value = "环境规划-编辑", notes = "环境规划-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody EnvironmentPlan environmentPlan) {
        environmentPlanService.updateById(environmentPlan);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "环境规划-通过id删除")
    @ApiOperation(value = "环境规划-通过id删除", notes = "环境规划-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        environmentPlanService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "环境规划-批量删除")
    @ApiOperation(value = "环境规划-批量删除", notes = "环境规划-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        this.environmentPlanService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "环境规划-通过id查询")
    @ApiOperation(value = "环境规划-通过id查询", notes = "环境规划-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id") String id) {
        EnvironmentPlan environmentPlan = environmentPlanService.getById(id);
        if (environmentPlan == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(environmentPlan);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param environmentPlan
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, EnvironmentPlan environmentPlan) {
        return super.exportXls(request, environmentPlan, EnvironmentPlan.class, "环境规划");
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
        return super.importExcel(request, response, EnvironmentPlan.class);
    }

}
