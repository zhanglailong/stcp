package org.jeecg.modules.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.project.entity.RunningRole;
import org.jeecg.modules.project.entity.RunningRoleHistory;
import org.jeecg.modules.project.service.IRunningRoleHistoryService;
import org.jeecg.modules.project.service.IRunningRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 项目角色表
 * @Author: jeecg-boot
 * @Date:   2021-03-19
 * @Version: V1.0
 */
@Api(tags="项目角色表")
@RestController
@RequestMapping("/project/runningRole")
@Slf4j
public class RunningRoleController extends JeecgController<RunningRole, IRunningRoleService> {
	@Autowired
	private IRunningRoleService runningRoleService;
	
	@Autowired
	private IRunningRoleHistoryService historyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param runningRole
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "项目角色表-分页列表查询")
	@ApiOperation(value="项目角色表-分页列表查询", notes="项目角色表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(@RequestParam(name="roleId", required = false) String roleId,
			   @RequestParam(name="createTime", required = false) String createTime,
			   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
			   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
			   HttpServletRequest req) {
		Page<RunningRole> pageList = new Page<RunningRole>(pageNo, pageSize);
		pageList = runningRoleService.queryPageList(pageList,roleId,createTime);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		return Result.ok(pageList);
	}

	
	/**
	 *   添加
	 *
	 * @param runningRole
	 * @return
	 */
	@AutoLog(value = "项目角色表-添加")
	@ApiOperation(value="项目角色表-添加", notes="项目角色表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningRole runningRole) {
		//生成UUID
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString(); 
        String uuidStr=str.replace("-", "");
        runningRole.setRoleCode(uuidStr);
		runningRoleService.save(runningRole);
		
		// dq add 往操作历史表中插入记录
		insertHistory(runningRole,0,runningRole.getId());
		
		
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param runningRole
	 * @return
	 */
	@AutoLog(value = "项目角色表-编辑")
	@ApiOperation(value="项目角色表-编辑", notes="项目角色表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningRole runningRole) {
		runningRoleService.updateById(runningRole);
		// dq add 往操作历史表中插入记录
		insertHistory(runningRole,1,runningRole.getId());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目角色表-通过id删除")
	@ApiOperation(value="项目角色表-通过id删除", notes="项目角色表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		//逻辑删除角色，物理删除项目角色关联表拥有该角色的数据，物理删除角色用户表中拥有该角色的数据
		//runningRoleService.removeRoleInfo(id);
		// dq add 往操作历史表中插入记录
		insertHistory(null,2,id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "项目角色表-批量删除")
	@ApiOperation(value="项目角色表-批量删除", notes="项目角色表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningRoleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "项目角色表-通过id查询")
	@ApiOperation(value="项目角色表-通过id查询", notes="项目角色表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningRole runningRole = runningRoleService.getById(id);
		if(runningRole==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningRole);
	}
	
	/**
	 * 角色名称下拉框数据查询方法
	 */
	 @GetMapping(value = "/getRoleData")
	 public Result<?> getRoleData()
	 {
		 //存放最终返回的数据结构集合
		 List<Map<String,Object>> resultList= new ArrayList<>();
		 //查询出所有基础角色的名称和id
		 List <RunningRole> list = runningRoleService.getRoleData();
		 if(list.isEmpty()) {
			 return Result.error("未找到对应数据");
		 }
		 //拼装 角色名称下拉框所需数据结构
		 for(int i=0;i<list.size();i++) {
			 Map <String,Object> map = new HashMap<>(2000);
			 String id = list.get(i).getId();
			 String roleName= list.get(i).getRoleName();
			 map.put("value", id);
			 map.put("label", roleName);
			 resultList.add(map);
		 }
		 return Result.ok(resultList);
	 }
	 

	 /**
	  * 角色管理操作历史记录查询
	  *
	  */
	 @AutoLog(value = "角色管理操作历史查询")
	 @ApiOperation(value="角色管理操作历史查询", notes="角色管理操作历史查询")
	 @GetMapping(value = "/historyList")
	 public Result<?> queryPageList(RunningRoleHistory runningRoleHistory,
									@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									HttpServletRequest req) {
		 Page page = new Page(pageNo, pageSize);
		 return Result.ok(runningRoleService.getOperationHistoryList(page,runningRoleHistory));
	 }
	 
	 /** dq add
	  * 向操作历史表中插入操作记录
	  * 参数:
	  * 	RunningRole originData: 新增和编辑操作时，会把最新数据存起来
	  * 	Integer opTye: 操作类型,0:新增  1:编辑 2:删除
	  * 	String mainId: 主表id
	  */
	 public void insertHistory(RunningRole originData, Integer opType, String mainId)
	 {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 RunningRoleHistory runningRoleHistory = new RunningRoleHistory();
		 int sign2=2;
		 if(opType == 0)
		 {
			 // 新增时把源数据在历史表中备份一个
			 BeanUtils.copyProperties(originData,runningRoleHistory);
			 runningRoleHistory.setSort(getMaxSortByMainId(mainId));
		 }
		 if(opType == sign2)
		 {
			 // 删除
			 runningRoleHistory.setSort(getMaxSortByMainId(mainId));
			 runningRoleHistory.setDelFlag(1);
		 }
		 if(opType == 0 || opType == sign2)
		 {
			 runningRoleHistory.setId(null);
			 runningRoleHistory.setCreateBy(sysUser.getId());
			 runningRoleHistory.setOpType(opType);
			 runningRoleHistory.setMainId(mainId);
			 historyService.save(runningRoleHistory);
		 }
		 if(opType == 1)
		 {
			 // 编辑操作单独处理,改过东西才会保存
			 List<RunningRoleHistory> historyList = originData.getModifiedList();
			 if(historyList != null && historyList.size() > 0)
			 {
				 // 当前插入的sort
				 Long insertSort = getMaxSortByMainId(mainId);
				 for(RunningRoleHistory record : historyList)
				 {
					 // 备份最新数据
					 BeanUtils.copyProperties(originData,record);
					 record.setId(null);
					 record.setMainId(mainId);
					 record.setCreateBy(sysUser.getId());
					 record.setSort(insertSort);
					 record.setCreateTime(new Date());
					 record.setOpType(1);
				 }
				 historyService.saveBatch(historyList);
			 }
		 }
	 }
	 
	 /** dq add
	  * 返回本次插入历史表中的sort值,同次操作sort值相同
	  */
	 public Long getMaxSortByMainId(String mainId)
	 {
		 QueryWrapper<RunningRoleHistory> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("main_id",mainId);
		 queryWrapper.orderByDesc("create_time");
		 List<RunningRoleHistory> list = historyService.list(queryWrapper);
		 if(list == null || list.size() == 0)
		 {
			 return 0L;
		 }
		 else
		 {
			 Long currentSort = list.get(0).getSort();
			 return currentSort + 1;
		 }
	 }
	 
	 /** dq add
	  * 通过id查询角色详情
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "操作记录-通过id查询")
	 @ApiOperation(value="操作记录-通过id查询", notes="操作记录-通过id查询")
	 @GetMapping(value = "/queryRoleHistoryById")
	 public Result<?> queryHistoryById(@RequestParam(name="id",required=true) String id) {
		 RunningRoleHistory runningRoleHistory = historyService.getById(id);
		 if(runningRoleHistory==null) {
			 return Result.error("未找到对应数据");
		 }
		 return Result.ok(runningRoleHistory);
	 }
	 
	
	

    /**
    * 导出excel
    *
    * @param request
    * @param runningRole
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningRole runningRole) {
        return super.exportXls(request, runningRole, RunningRole.class, "项目角色");
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
        return super.importExcel(request, response, RunningRole.class);
    }

}
