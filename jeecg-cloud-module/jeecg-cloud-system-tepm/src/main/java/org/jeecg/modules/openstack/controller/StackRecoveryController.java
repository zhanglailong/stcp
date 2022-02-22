package org.jeecg.modules.openstack.controller;

import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.EnvStackStatusEnum;
import org.jeecg.modules.openstack.entity.StackRecovery;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.IStackRecoveryService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * 环境的恢复
 * @author zlf
  * x2021-05-12
 * V1.0
 */
@Api(tags="环境的恢复")
@RestController
@RequestMapping("/openstack/recovery")
@Slf4j
public class StackRecoveryController extends JeecgController<StackRecovery, IStackRecoveryService> {
	@Resource
	private IStackRecoveryService stackRecoveryService;
	@Resource
    private IStackQueueService iStackQueueService;

	 @Resource
	 private IEnvCustomizedService envCustomizedService;
	/**
	 * 分页列表查询
	 *
	 * @param stackRecovery 环境恢复
	 * @param pageNo 页
	 * @param pageSize 数量
	 * @param req req
	 * @return 分页数据
	 */
	@AutoLog(value = "环境的恢复-分页列表查询")
	@ApiOperation(value="环境的恢复-分页列表查询", notes="环境的恢复-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(StackRecovery stackRecovery,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StackRecovery> queryWrapper = QueryGenerator.initQueryWrapper(stackRecovery, req.getParameterMap());
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		queryWrapper.eq(CommonConstant.DATA_STRING_CREATE_BY, sysUser.getUsername());
		queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0).orderByDesc(CommonConstant.DATA_CREATE_TIME);
		Page<StackRecovery> page = new Page<>(pageNo, pageSize);
		IPage<StackRecovery> pageList = stackRecoveryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  *   添加
	  *
	  * @param stackRecovery 环境恢复
	  * @return 添加状态
	  */
	 @AutoLog(value = "环境的恢复-添加")
	 @ApiOperation(value="环境的恢复-添加", notes="环境的恢复-添加")
	 @PostMapping(value = "/add")
	 public Result<?> add(@RequestBody StackRecovery stackRecovery) {
		 stackRecoveryService.save(stackRecovery);
		 return Result.OK("添加成功！");
	 }

	 /**
	  * 创建环境快照
	  * @param stackId openstack栈环境id
	  * @param envId 定制环境id
	  * @param name 快照/备份名称
	  * @param type 0快照/1备份
	  * @return 状态
	  */
	 @AutoLog(value = "测试环境定制-创建快照/备份")
	 @ApiOperation(value="测试环境定制-创建快照/备份", notes="测试环境定制-创建快照/备份")
	 @PostMapping(value = "/snapshot")
	 public Result<?> snapshot(@RequestParam(name="stackId") String stackId,
							   @RequestParam(name="envId") String envId,
							   @RequestParam(name="name") String name,
							   @RequestParam(name="type") Integer type) {
		 String typeString="快照";
		 if (type==1){
			 typeString="备份";
		 }
		 try {

			 EnvCustomized envCustomized = envCustomizedService.getById(envId);
			 EnvStackStatusEnum state = envCustomized.getState();
			 if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state)||
			 EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
					 || EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
			 )
			 {
				 return Result.error("状态："+state.getDesc()+"不允许进行操作");
			 }
			 if (iStackQueueService.snapshot(stackId,envId,name,type ,typeString)){
				 return Result.OK("创建环境"+typeString+"成功！");
			 }
		 } catch (Exception e) {
			 log.error("创建环境"+typeString+"异常,原因:"+e.getMessage());
			 return Result.error("创建环境"+typeString+"异常,请联系管理员！");
		 }
		 return Result.error("创建环境"+typeString+"失败!");
	 }

	 /**
	  * 删除单个快照
	  * 环境的恢复-快照/备份销毁
	  * @param id 快照/备份id
	  * @param stackId openstack栈环境id
	  * @param snapshotId 环境快照/备份id
	  * @return 状态
	  */
	@AutoLog(value = "环境的恢复-快照/备份销毁")
	@ApiOperation(value="环境的恢复-快照/备份销毁", notes="环境的恢复-快照/备份销毁")
	@PostMapping(value = "/destroy")
	public Result<?> destroy(@RequestParam(name="id") String id,
							 @RequestParam(name="stackId") String stackId,
							 @RequestParam(name="snapshotId") String snapshotId) {
		try {
			StackRecovery stackRecovery = stackRecoveryService.getById(id);
			EnvCustomized envCustomized = envCustomizedService.getById(stackRecovery.getEnvId());
			EnvStackStatusEnum state = envCustomized.getState();
			if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state)||
					EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
					|| EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
			)
			{
				return Result.error("状态："+state.getDesc()+"不允许进行操作");
			}

			//删除环境快照
			if (iStackQueueService.deleteDestroy(id,stackId,snapshotId)){
				return Result.OK("销毁成功!");
			}
		} catch (Exception e) {
			log.error("环境的恢复-快照/备份销毁:"+e.getMessage());
			return Result.error("环境的恢复-快照/备份销毁:"+e.getMessage());
		}
		return Result.error("环境的恢复-快照/备份销毁失败!");
	}
	 /**
	  * 环境的恢复-快照/备份恢复
	  * @param envId 定制环境id
	  * @param stackId openstack栈环境id
	  * @param snapshotId 环境快照/备份id
	  * @return 状态
	  */
	@AutoLog(value = "环境的恢复-快照/备份恢复")
	@ApiOperation(value="环境的恢复-快照/备份恢复", notes="环境的恢复-快照/备份恢复")
	@PostMapping(value = "/restore")
	public Result<?> restore(@RequestParam(name="envId") String envId,
							 @RequestParam(name="stackId") String stackId,
							 @RequestParam(name="snapshotId") String snapshotId) {
		EnvCustomized envCustomized = envCustomizedService.getById(envId);
		EnvStackStatusEnum state = envCustomized.getState();
		if (EnvStackStatusEnum.CREATE_IN_PROGRESS.equals(state)||
				EnvStackStatusEnum.BACKUP_RESTORE_IN_PROGRESS.equals(state)
				|| EnvStackStatusEnum.RESTORE_IN_PROGRESS.equals(state)
		)
		{
			return Result.error("状态："+state.getDesc()+"不允许进行操作");
		}
		try {
			Boolean code = iStackQueueService.restore(envId,stackId,snapshotId);
			if (code){
				return Result.OK("环境的恢复-快照/备份恢复成功!");
			}
		} catch (Exception e) {
			log.error("环境的恢复-快照/备份恢复异常,原因:"+e.getMessage());
			return Result.error("环境的恢复-快照/备份恢复异常,请联系管理员！");
		}
		return Result.error("环境的恢复-快照/备份恢复Controller层失败!");
	}

	/**
	 *  编辑
	 *
	 * @param stackRecovery 环境恢复
	 * @return 编辑状态
	 */
	@AutoLog(value = "环境的恢复-编辑")
	@ApiOperation(value="环境的恢复-编辑", notes="环境的恢复-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody StackRecovery stackRecovery) {
		stackRecoveryService.updateById(stackRecovery);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id id
	 * @return 删除状态
	 */
	@AutoLog(value = "环境的恢复-通过id删除")
	@ApiOperation(value="环境的恢复-通过id删除", notes="环境的恢复-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id") String id) {
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids id集合
	 * @return 删除状态
	 */
	@AutoLog(value = "环境的恢复-批量删除")
	@ApiOperation(value="环境的恢复-批量删除", notes="环境的恢复-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids") String ids) {
		this.stackRecoveryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id id
	 * @return 环境恢复信息
	 */
	@AutoLog(value = "环境的恢复-通过id查询")
	@ApiOperation(value="环境的恢复-通过id查询", notes="环境的恢复-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id") String id) {
		StackRecovery stackRecovery = stackRecoveryService.getById(id);
		if(stackRecovery==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(stackRecovery);
	}

	 /**
	  * 导出excel
	  * @param request request
	  * @param stackRecovery 恢复环境信息
	  * @return 导出excel
	  */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StackRecovery stackRecovery) {
        return super.exportXls(request, stackRecovery, StackRecovery.class, "环境的恢复");
    }

    /**
    * 通过excel导入数据
    *
    * @param request request
    * @param response response
    * @return 通过excel导入数据
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StackRecovery.class);
    }

}
