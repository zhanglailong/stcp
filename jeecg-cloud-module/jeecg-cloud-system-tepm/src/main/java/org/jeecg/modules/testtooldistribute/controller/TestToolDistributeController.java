package org.jeecg.modules.testtooldistribute.controller;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.testtooldistribute.entity.DistributeResult;
import org.jeecg.modules.testtooldistribute.entity.EnvironmentOptions;
import org.jeecg.modules.testtooldistribute.entity.TestToolDistribute;
import org.jeecg.modules.testtooldistribute.entity.TestToolOptions;
import org.jeecg.modules.testtooldistribute.service.ITestToolDistributeService;
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
@Api(tags="测试工具分发")
@RestController
@RequestMapping("/testToolDistribute")
@Slf4j
@AllArgsConstructor
public class TestToolDistributeController extends JeecgController<TestToolDistribute, ITestToolDistributeService> {

	private final ITestToolDistributeService testToolDistributeService;

	/**
	 * 分页列表查询
	 *
	 * @param testToolDistribute testToolDistribute
	 * @param pageNo             pageNo
	 * @param pageSize           pageSize
	 * @param req                req
	 * @return 分页集合
	 */
	@AutoLog(value = "测试工具分发-分页列表查询")
	@ApiOperation(value = "测试工具分发-分页列表查询", notes = "测试工具分发-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestToolDistribute testToolDistribute,
								   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
								   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestToolDistribute> queryWrapper = QueryGenerator.initQueryWrapper(testToolDistribute, req.getParameterMap());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
//		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
//      queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
		Page<TestToolDistribute> page = new Page<>(pageNo, pageSize);
		IPage<TestToolDistribute> pageList = testToolDistributeService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 * 添加
	 *
	 * @param testToolDistribute testToolDistribute
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-添加")
	@ApiOperation(value = "测试工具分发-添加", notes = "测试工具分发-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestToolDistribute testToolDistribute) {
		try {
			if (testToolDistribute == null) {
				return Result.error("对象不能为空!");
			}
			testToolDistribute.setIdel(CommonConstant.DATA_INT_IDEL_0);
			testToolDistributeService.add(testToolDistribute);
			return Result.OK("添加成功！");
		} catch (Exception e) {
			log.error("测试工具分发-添加异常:" + e.getMessage());
			return Result.error("添加异常:" + e.getMessage());
		}
	}

	/**
	 * 编辑
	 *
	 * @param testToolDistribute testToolDistribute
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-编辑")
	@ApiOperation(value = "测试工具分发-编辑", notes = "测试工具分发-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestToolDistribute testToolDistribute) {
		try {
			if (StringUtils.isEmpty(testToolDistribute.getId())){
				return Result.error("缺少id");
			}
			testToolDistributeService.edit(testToolDistribute);
			return Result.OK("编辑成功!");
		} catch (Exception e) {
			log.error("编辑失败"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 通过id删除
	 *
	 * @param id id
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-通过id删除")
	@ApiOperation(value = "测试工具分发-通过id删除", notes = "测试工具分发-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name = "id") String id) {
		try {
			TestToolDistribute testToolDistribute = testToolDistributeService.getById(id);
			if(testToolDistributeService.removeById(testToolDistribute)){
				return Result.OK("删除成功!");
			}
			return Result.error("删除失败!");
		} catch (Exception e) {
			log.error("删除异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 批量删除
	 *
	 * @param ids ids
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-批量删除")
	@ApiOperation(value = "测试工具分发-批量删除", notes = "测试工具分发-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name = "ids") String ids) {
		try {
			List<String> stringList = Arrays.asList(ids.split(","));
			if (testToolDistributeService.removeByIds(stringList)) {
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
	 * @return TestToolDistribute
	 */
	@AutoLog(value = "测试工具分发-通过id查询")
	@ApiOperation(value = "测试工具分发-通过id查询", notes = "测试工具分发-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name = "id") String id) {
		try {
			if (StringUtils.isEmpty(id)){
				return Result.error("缺少id");
			}
			TestToolDistribute testToolDistribute = testToolDistributeService.getById(id);
			if(testToolDistribute==null) {
				return Result.error("未找到对应数据");
			}
			return Result.OK(testToolDistribute);
		} catch (Exception e) {
			log.error("通过id查询异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 导出excel
	 *
	 * @param request request
	 * @param testToolDistribute testToolDistribute
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, TestToolDistribute testToolDistribute) {
		return super.exportXls(request, testToolDistribute, TestToolDistribute.class, "测试工具分发");
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request request
	 * @param response response
	 * @return 成功或者失败
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, TestToolDistribute.class);
	}

	/**
	 * 环境虚拟机测试工具2层级联下拉
	 *
	 * @return  List<EnvironmentOptions>
	 */
	@AutoLog(value = " 环境虚拟机测试工具二层级联下拉")
	@ApiOperation(value=" 环境虚拟机测试工具二层级联下拉", notes=" 环境虚拟机测试工具二层级联下拉")
	@GetMapping(value = "/envVmToolOptions")
	public Result<?> envVmToolOptions() {
		//todo 目前测试工具查的running_tools_list表
		try {
			List<EnvironmentOptions> envVmToolOptions = testToolDistributeService.envVmToolOptions();
			if(envVmToolOptions==null) {
				return Result.error("未找到对应数据");
			}
			return Result.OK(envVmToolOptions);
		} catch (Exception e) {
			log.error("环境虚拟机测试工具三层级联下拉异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 测试工具下拉
	 *
	 * @return  List<TestToolOptions>
	 */
	@AutoLog(value = " 测试工具下拉")
	@ApiOperation(value=" 测试工具下拉", notes=" 测试工具下拉")
	@GetMapping(value = "/testToolOptions")
	public Result<?> testToolOptions() {
		//todo 目前测试工具查的running_tools_list表
		try {
			List<TestToolOptions> testToolOptions = testToolDistributeService.testToolOptions();
			if(testToolOptions==null) {
				return Result.error("未找到对应数据");
			}
			return Result.OK(testToolOptions);
		} catch (Exception e) {
			log.error("测试工具下拉异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}



	/**
	 * 通过id分发
	 *
	 * @param id id
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-通过id删除")
	@ApiOperation(value = "测试工具分发-通过id分发", notes = "测试工具分发-通过id分发")
	@PutMapping(value = "/distribute")
	public Result<?> distribute(@RequestParam(name = "id") String id) {
		try {
			TestToolDistribute testToolDistribute = testToolDistributeService.getById(id);
			DistributeResult distributeResult = testToolDistributeService.distributeById(testToolDistribute);
			if(distributeResult.isSucceed()){
				return Result.OK("分发成功!");
			}
			return Result.error("分发失败："+distributeResult.getMessage());
		} catch (Exception e) {
			log.error("分发异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	/**
	 * 批量分发
	 *
	 * @param ids ids
	 * @return 成功或者异常
	 */
	@AutoLog(value = "测试工具分发-批量分发")
	@ApiOperation(value = "测试工具分发-批量分发", notes = "测试工具分发-批量分发")
	@PutMapping(value = "/distributeBatch")
	public Result<?> distributeBatch(@RequestParam(name = "ids") String ids) {
		try {
			List<String> stringList = Arrays.asList(ids.split(","));
			testToolDistributeService.distributeByIds(stringList);
		} catch (Exception e) {
			log.error("批量分发异常"+e.getMessage());
			return Result.error(e.getMessage());
		}
		return Result.OK("批量分发成功!");
	}


}
