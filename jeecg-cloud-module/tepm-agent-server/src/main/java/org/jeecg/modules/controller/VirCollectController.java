package org.jeecg.modules.controller;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.entity.VirCollect;
import org.jeecg.modules.service.IVirCollectService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 虚拟机收集信息表
 * @Author: jeecg-boot
 * @Date: 2021-01-14
 * @Version: V1.0
 */
@Api(tags = "虚拟机收集信息表")
@RestController
@RequestMapping("/socket/server/VirCollect/virCollect")
@Slf4j
public class VirCollectController extends JeecgController<VirCollect, IVirCollectService> {
    @Autowired
    private IVirCollectService virCollectService;

    /**
     * 分页列表查询
     *
     * @param virCollect
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-分页列表查询")
    @ApiOperation(value = "虚拟机收集信息表-分页列表查询", notes = "虚拟机收集信息表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(VirCollect virCollect,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<VirCollect> queryWrapper = QueryGenerator.initQueryWrapper(virCollect, req.getParameterMap());
        Page<VirCollect> page = new Page<VirCollect>(pageNo, pageSize);
        IPage<VirCollect> pageList = virCollectService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 根据IP查询
     *
     * @param
     * @return
     */
    @AutoLog(value = "虚拟机信息采集表")
    @ApiOperation(value = "虚拟机信息采集表", notes = "虚拟机信息采集表")
    @GetMapping(value = "/queryByIp")
    public Result<?> queryByIp(@RequestParam(name = "ip", required = true) String ip) {
        QueryWrapper<VirCollect> virCollectQueryWrapper = new QueryWrapper<>();
        virCollectQueryWrapper.eq("ip", ip);
        List<VirCollect> virCollectList = virCollectService.list(virCollectQueryWrapper);
        return Result.OK(virCollectList);
    }

    /**
     * 添加
     *
     * @param virCollect
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-添加")
    @ApiOperation(value = "虚拟机收集信息表-添加", notes = "虚拟机收集信息表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody VirCollect virCollect) {
        virCollectService.save(virCollect);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param virCollect
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-编辑")
    @ApiOperation(value = "虚拟机收集信息表-编辑", notes = "虚拟机收集信息表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody VirCollect virCollect) {
        virCollectService.updateById(virCollect);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-通过id删除")
    @ApiOperation(value = "虚拟机收集信息表-通过id删除", notes = "虚拟机收集信息表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        virCollectService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-批量删除")
    @ApiOperation(value = "虚拟机收集信息表-批量删除", notes = "虚拟机收集信息表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.virCollectService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "虚拟机收集信息表-通过id查询")
    @ApiOperation(value = "虚拟机收集信息表-通过id查询", notes = "虚拟机收集信息表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        VirCollect virCollect = virCollectService.getById(id);
        if (virCollect == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(virCollect);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param virCollect
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VirCollect virCollect) {
        return super.exportXls(request, virCollect, VirCollect.class, "虚拟机收集信息表");
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
        return super.importExcel(request, response, VirCollect.class);
    }

}
