package org.jeecg.modules.plan.controller;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.ImageManage;
import org.jeecg.modules.plan.service.IImageManageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;


/**
 * @author yeyl
 */
@Api(tags = "镜像管理")
@RestController
@RequestMapping("/plan/imageManage")
@Slf4j
public class ImageManageController extends JeecgController<ImageManage, IImageManageService> {
    @Resource
    private IImageManageService imageManageService;

    /**
     * 分页列表查询
     *
     * @param imageManage imageManage
     * @param pageNo pageNo
     * @param pageSize pageSize
     * @param req req
     * @return list
     */
    @AutoLog(value = "镜像管理-分页列表查询")
    @ApiOperation(value = "镜像管理-分页列表查询", notes = "镜像管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ImageManage imageManage,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ImageManage> queryWrapper = QueryGenerator.initQueryWrapper(imageManage, req.getParameterMap());
        queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
        Page<ImageManage> page = new Page<>(pageNo, pageSize);
        IPage<ImageManage> pageList = imageManageService.page(page, queryWrapper);
        return Result.OK(pageList);
    }




    /**
     *根据其他软件，测试工具，系统版本获取镜像列表 表里面获取
     * @return 获取镜像列表
     */
    @AutoLog(value = "根据其他软件，测试工具，系统版本获取镜像列表")
    @ApiOperation(value="根据其他软件，测试工具，系统版本获取镜像列表", notes="根据其他软件，测试工具，系统版本获取镜像列表")
    @GetMapping(value = "/filterByOther")
    public Result<?> getImageListByOther(  @RequestParam(name = "otherSoftware") String otherSoftware,
                                           @RequestParam(name = "testTools") String testTools,
                                           @RequestParam(name = "systemVersion") String systemVersion,
                                           @RequestParam(name = "sysType") String sysType
    ) {
        try {
            //过滤
            List<ImageManage>   images =  imageManageService.getImageManagesByOther(otherSoftware,testTools,systemVersion,sysType);
            return Result.OK(images);
        }catch (Exception e){
            log.error("获取镜像列表controller异常,原因:"+e.getMessage());
            return Result.error("获取失败异常,请联系管理员！");
        }
    }


    /**
     *通过镜像查 测试工具 其他软件 版本信息
     * @return 获取镜像列表
     */
    @AutoLog(value = "镜像管理-通过镜像id查询")
    @ApiOperation(value = "镜像管理-通过镜像id查询", notes = "镜像管理-通过镜像id查询")
    @GetMapping(value = "/queryByOpenstackId")
    public Result<?> queryByOpenstackId(@RequestParam(name = "openstackId") String openstackId) {
        ImageManage imageManage = imageManageService.getByOpenstackId(openstackId);
        if (imageManage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(imageManage);
    }



    /**
     * 添加
     *
     * @param imageManage imageManage
     * @return 成功或者异常
     */
    @AutoLog(value = "镜像管理-添加")
    @ApiOperation(value = "镜像管理-添加", notes = "镜像管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ImageManage imageManage) {
        imageManageService.save(imageManage);
        return Result.OK("添加成功！");
    }
    /**
     * 编辑
     *
     * @param imageManage imageManage
     * @return 成功或者异常
     */
    @AutoLog(value = "镜像管理-编辑")
    @ApiOperation(value = "镜像管理-编辑", notes = "镜像管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ImageManage imageManage) {
        try {
            if (StringUtils.isBlank(imageManage.getId())){
                return Result.error("缺少id");
            }
            imageManageService.edit(imageManage);
            return Result.OK("编辑成功!");
        } catch (Exception e) {
            log.error("编辑失败"+e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 通过id删除
     * @param id id
     * @return 成功或者异常
     */
    @AutoLog(value = "镜像管理-通过id删除")
    @ApiOperation(value = "镜像管理-通过id删除", notes = "镜像管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id") String id) {
        imageManageService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids  ids
     * @return 成功或者异常
     */
    @AutoLog(value = "镜像管理-批量删除")
    @ApiOperation(value = "镜像管理-批量删除", notes = "镜像管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
        try {
            List<String> stringList = Arrays.asList(ids.split(","));
            if (imageManageService.deleteBatch(stringList)) {
                return Result.OK("批量删除成功!");
            }
            return Result.error("批量删除失败!");
        } catch (Exception e) {
            log.error("批量删除异常"+e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    /**
     * 通过id查询
     *
     * @param id id
     * @return ImageManage
     */
    @AutoLog(value = "镜像管理-通过id查询")
    @ApiOperation(value = "镜像管理-通过id查询", notes = "镜像管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ImageManage imageManage = imageManageService.getById(id);
        if (imageManage == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(imageManage);
    }

    /**
     * 导出excel
     *
     * @param request request
     * @param imageManage imageManage
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ImageManage imageManage) {
        return super.exportXls(request, imageManage, ImageManage.class, "镜像管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request request
     * @param response response
     * @return 成功或者异常
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ImageManage.class);
    }


    @AutoLog(value = "镜像-同步")
    @ApiOperation(value = "镜像-同步", notes = "镜像-同步")
    @PostMapping(value = "/sync")
    public Result<?> sync() {
        try {
            imageManageService.sync();
            return Result.OK("同步成功!");
        } catch (Exception e) {
            log.error("同步失败"+e.getMessage());
            return Result.error(e.getMessage());
        }
    }

}
