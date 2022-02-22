package org.jeecg.modules.project.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.project.entity.ProjectUserAssociation;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.entity.RunningProjectHistory;
import org.jeecg.modules.project.service.IRunningProjectHistoryService;
import org.jeecg.modules.project.service.IRunningProjectService;
import org.jeecg.modules.project.service.ProjectUserAssociationService;
import org.jeecg.modules.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 项目用户关联
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Api(tags="项目用户关联")
@RestController
@RequestMapping("/project/userAssociation")
@Slf4j
public class ProjectUserAssociationController extends JeecgController<RunningProject, IRunningProjectService> {
	@Autowired
	private ProjectUserAssociationService projectUserAssociationService;
	@Autowired
	private IRunningProjectHistoryService historyService;
	@Autowired
	private IRunningProjectService projectService;
	@Autowired
	private ISysUserService sysUserService;

	/**
	 * 通过项目id查询该项目下的人员
	 *
	 * @param projectId
	 * @return projectId、userId
	 */
	@AutoLog(value = "项目管理-通过id查询")
	@ApiOperation(value="项目管理-通过id查询", notes="项目管理-通过id查询")
	@GetMapping(value = "/getProjectPerson")
	public Result<?> getProjectPerson(@RequestParam(name="projectId",required=true) String projectId) {
		
		//该方法为最终返回的数据结构
		ProjectUserAssociation data = new ProjectUserAssociation();
		//定义存放同一个项目下 人 主键 集合
		List<String> userIdList = new ArrayList<>();
		//定义存放同一个项目下 人 username集合
		List<String> userNameList = new ArrayList<>();
		
		//查询单个项目下的人
		List<ProjectUserAssociation> personIdList =projectUserAssociationService.getInfoByProjectId(projectId);
		
		if(personIdList.isEmpty()) {
			return Result.error("未找到对应数据");
		}else {
			//获取当前项目下人的主键信息
			for(int i=0;i<personIdList.size();i++) {
				if(i<personIdList.size()) {
					data.setId(personIdList.get(i).getId());
					data.setProjectId(personIdList.get(i).getProjectId());
					userIdList.add(personIdList.get(i).getProjectMemberIds());
				}
			}
			//获取当前项目下人的username(主要用于页面人名称的显示)
			for(int j=0;j<userIdList.size();j++) {
				//用户表ID
				String id =userIdList.get(j);
				String username = sysUserService.getUserNameById(id);
				userNameList.add(username);
			}
			
			//项目下的人的主键
			String projectMemberIds= StringUtils.join(userIdList.toArray(),",");
			//项目下的人的username
			String projectMembers=StringUtils.join(userNameList.toArray(),",");
			
			data.setProjectMemberIds(projectMemberIds);
			data.setProjectMembers(projectMembers);
		}
		return Result.ok(data);
	}
	
	
	/**
	 *   添加
	 *
	 * @param projectUserAssociation
	 * @return
	 */
	@AutoLog(value = "项目管理-添加")
	@ApiOperation(value="项目管理-添加", notes="项目管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ProjectUserAssociation projectUserAssociation) {
		//将人ID 字符  转换成 人ID集合
	    List<String> projectMemberIdList = Arrays.asList(projectUserAssociation.getProjectMemberIds().split(","));
	    // dq add start,项目人员新增,需要在项目操作历史表中插入操作数据
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String projectId = projectUserAssociation.getProjectId();
		String proMemberIds = projectUserAssociation.getProjectMemberIds();
		if(proMemberIds != null)
		{
			RunningProject project = projectService.getById(projectId);
			if(project != null)
			{
				RunningProjectHistory runningProjectHistory = new RunningProjectHistory();
				BeanUtils.copyProperties(project,runningProjectHistory);
				runningProjectHistory.setId(null);
				runningProjectHistory.setOpType(1);
				runningProjectHistory.setMainId(projectId);
				runningProjectHistory.setCreateTime(new Date());
				runningProjectHistory.setCreateBy(sysUser.getId());
				runningProjectHistory.setModifyField("projectMembers");
				runningProjectHistory.setModifyFieldValue(proMemberIds);
				historyService.save(runningProjectHistory);
			}
		}
		// dq add end
	    //循环添加项目下人员信息
	    for(String projectMemberId : projectMemberIdList) {
	    	ProjectUserAssociation data = new ProjectUserAssociation();
	    	data.setProjectId(projectUserAssociation.getProjectId());
	    	data.setProjectMemberIds(projectMemberId);
	    	projectUserAssociationService.save(data);
	    }
		return Result.ok("添加成功！");
	}
	
	
	/**
	 *  编辑
	 *
	 * @param projectUserAssociation
	 * @return
	 */
	@AutoLog(value = "项目管理-编辑")
	@ApiOperation(value="项目管理-编辑", notes="项目管理-编辑")
	@PostMapping(value = "/edit")
	public Result<?> edit(@RequestBody ProjectUserAssociation projectUserAssociation) {
		//项目ID
		String projectId=projectUserAssociation.getProjectId();
		//先批量删除当前项目下所有人的信息，在进行更新后的人员插入，实现更新效果
		projectUserAssociationService.deleteDataByProjectId(projectId);
		
		//将人ID 字符  转换成 人ID集合
	    List<String> projectMemberIdList = Arrays.asList(projectUserAssociation.getProjectMemberIds().split(","));
	    //循环添加项目下人员信息
	    for(String projectMemberId : projectMemberIdList) {
	    	ProjectUserAssociation data = new ProjectUserAssociation();
	    	data.setProjectId(projectUserAssociation.getProjectId());
	    	data.setProjectMemberIds(projectMemberId);
	    	projectUserAssociationService.save(data);
	    }
	    // dq add start,增加历史记录
		RunningProject project = projectService.getById(projectId);
	    if(project != null)
		{
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			RunningProjectHistory runningProjectHistory = new RunningProjectHistory();
			BeanUtils.copyProperties(project,runningProjectHistory);
			runningProjectHistory.setId(null);
			runningProjectHistory.setOpType(1);
			runningProjectHistory.setMainId(projectId);
			runningProjectHistory.setSort(getMaxSortByMainId(projectId));
			runningProjectHistory.setCreateTime(new Date());
			runningProjectHistory.setCreateBy(sysUser.getId());
			runningProjectHistory.setModifyField("projectMembers");
			runningProjectHistory.setModifyFieldValue(projectUserAssociation.getProjectMemberIds());
			runningProjectHistory.setModifyFieldOldValue(projectUserAssociation.getOriginMemberIds());
			if(projectUserAssociation.getIsUserModify())
			{
				historyService.save(runningProjectHistory);
			}
		}
		// dq add end
		return Result.ok("编辑成功!");
	}

	 /** dq add
	  * 向操作历史表中插入操作记录
	  * 参数:
	  * 	RunningProject originData: 新增和编辑操作时，会把最新数据存起来
	  * 	Integer opTye: 操作类型,0:新增  1:编辑 2:删除
	  * 	String mainId: 主表id
	  */
	 public void insertHistory(RunningProject originData, Integer opType, String mainId)
	 {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 RunningProjectHistory runningProjectHistory = new RunningProjectHistory();
		 int delete=2;
		 if(opType == 0)
		 {
			 // 新增时把源数据在历史表中备份一个
			 BeanUtils.copyProperties(originData,runningProjectHistory);
			 runningProjectHistory.setSort(getMaxSortByMainId(mainId));
		 }
		 if(opType == delete)
		 {
			 // 删除
			 runningProjectHistory.setSort(getMaxSortByMainId(mainId));
			 runningProjectHistory.setDelFlag(1);
		 }
		 if(opType == 0 || opType == delete)
		 {
			 runningProjectHistory.setId(null);
			 runningProjectHistory.setCreateBy(sysUser.getId());
			 runningProjectHistory.setOpType(opType);
			 runningProjectHistory.setMainId(mainId);
			 historyService.save(runningProjectHistory);
		 }
		 if(opType == 1)
		 {
			 // 编辑操作单独处理,改过东西才会保存
			 List<RunningProjectHistory> historyList = originData.getModifiedList();
			 if(historyList != null && historyList.size() > 0)
			 {
				 // 当前插入的sort
				 Long insertSort = getMaxSortByMainId(mainId);
				 for(RunningProjectHistory record : historyList)
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
		 QueryWrapper<RunningProjectHistory> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("main_id",mainId);
		 queryWrapper.orderByDesc("create_time");
		 List<RunningProjectHistory> list = historyService.list(queryWrapper);
		 if(list == null || list.size() == 0)
		 {
			 return 0L;
		 }
		 else
		 {
			 Long currentSort = list.get(0).getSort() == null ? 0 : list.get(0).getSort();
			 return currentSort + 1;
		 }
	 }
	 
	 
	 

}
