package org.jeecg.modules.sjcj.collectiondataanalyse.controller;

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
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.TestItem;
import org.jeecg.modules.sjcj.collectiondataanalyse.service.ITestItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 29项测试项
 * @Author: jeecg-boot
 * @Date: 2021-01-22
 * @Version: V1.0
 */
@Api(tags = "29项测试项")
@RestController
@RequestMapping("/testitem/testItem")
@Slf4j
public class TestItemController extends JeecgController<TestItem, ITestItemService> {
    @Autowired
    private ITestItemService testItemService;

    /**
     * 分页列表查询
     *
     * @param testItem
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "29项测试项-分页列表查询")
    @ApiOperation(value = "29项测试项-分页列表查询", notes = "29项测试项-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(TestItem testItem,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<TestItem> queryWrapper = QueryGenerator.initQueryWrapper(testItem, req.getParameterMap());
        Page<TestItem> page = new Page<TestItem>(pageNo, pageSize);
        IPage<TestItem> pageList = testItemService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param testItem
     * @return
     */
    @AutoLog(value = "29项测试项-添加")
    @ApiOperation(value = "29项测试项-添加", notes = "29项测试项-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody TestItem testItem) {
        testItemService.save(testItem);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param testItem
     * @return
     */
    @AutoLog(value = "29项测试项-编辑")
    @ApiOperation(value = "29项测试项-编辑", notes = "29项测试项-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody TestItem testItem) {
        testItemService.updateById(testItem);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "29项测试项-通过id删除")
    @ApiOperation(value = "29项测试项-通过id删除", notes = "29项测试项-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        testItemService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "29项测试项-批量删除")
    @ApiOperation(value = "29项测试项-批量删除", notes = "29项测试项-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.testItemService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "29项测试项-通过id查询")
    @ApiOperation(value = "29项测试项-通过id查询", notes = "29项测试项-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        TestItem testItem = testItemService.getById(id);
        if (testItem == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(testItem);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param testItem
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestItem testItem) {
        return super.exportXls(request, testItem, TestItem.class, "29项测试项");
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
        return super.importExcel(request, response, TestItem.class);
    }

}
