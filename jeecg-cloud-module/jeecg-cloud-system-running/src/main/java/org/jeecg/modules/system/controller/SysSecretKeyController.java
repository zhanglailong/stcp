package org.jeecg.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.system.entity.SysSecretKey;
import org.jeecg.modules.system.service.ISysSecretKeyService;
import org.jeecg.modules.system.util.RandomStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 系统密钥分发表
 * @Author: jeecg-boot
 * @Date: 2021-01-29
 * @Version: V1.0
 */
@Api(tags = "系统密钥分发表")
@RestController
@RequestMapping("/system/sysSecretKey")
@Slf4j
public class SysSecretKeyController extends JeecgController<SysSecretKey, ISysSecretKeyService> {
    @Autowired
    private ISysSecretKeyService sysSecretKeyService;

    /**
     * 分页列表查询
     *
     * @param sysSecretKey
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "系统密钥分发表-分页列表查询")
    @ApiOperation(value = "系统密钥分发表-分页列表查询", notes = "系统密钥分发表-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysSecretKey sysSecretKey,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysSecretKey> queryWrapper = QueryGenerator.initQueryWrapper(sysSecretKey, req.getParameterMap());
        Page<SysSecretKey> page = new Page<SysSecretKey>(pageNo, pageSize);
        IPage<SysSecretKey> pageList = sysSecretKeyService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param sysSecretKey
     * @return
     */
    @AutoLog(value = "系统密钥分发表-添加")
    @ApiOperation(value = "系统密钥分发表-添加", notes = "系统密钥分发表-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysSecretKey sysSecretKey) {
        sysSecretKey.setToken(RandomStringUtil.getToken());
        sysSecretKey.setSecretKey(RandomStringUtil.getSecretKey());
        sysSecretKey.setPublicKey(RandomStringUtil.getPublicKey());
        sysSecretKeyService.save(sysSecretKey);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param sysSecretKey
     * @return
     */
    @AutoLog(value = "系统密钥分发表-编辑")
    @ApiOperation(value = "系统密钥分发表-编辑", notes = "系统密钥分发表-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody SysSecretKey sysSecretKey) {
        sysSecretKeyService.updateById(sysSecretKey);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统密钥分发表-通过id删除")
    @ApiOperation(value = "系统密钥分发表-通过id删除", notes = "系统密钥分发表-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysSecretKeyService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "系统密钥分发表-批量删除")
    @ApiOperation(value = "系统密钥分发表-批量删除", notes = "系统密钥分发表-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysSecretKeyService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "系统密钥分发表-通过id查询")
    @ApiOperation(value = "系统密钥分发表-通过id查询", notes = "系统密钥分发表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysSecretKey sysSecretKey = sysSecretKeyService.getById(id);
        if (sysSecretKey == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(sysSecretKey);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysSecretKey
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysSecretKey sysSecretKey) {
        return super.exportXls(request, sysSecretKey, SysSecretKey.class, "系统密钥分发表");
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
        return super.importExcel(request, response, SysSecretKey.class);
    }

}
