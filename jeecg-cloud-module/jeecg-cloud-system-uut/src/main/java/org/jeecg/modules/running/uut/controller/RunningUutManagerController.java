package org.jeecg.modules.running.uut.controller;

import java.util.*;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.number.service.INumberRuleInfoService;
import org.jeecg.modules.running.uut.entity.*;
import org.jeecg.modules.running.uut.mapper.RunningUutFileListMapper;
import org.jeecg.modules.running.uut.service.*;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.util.BeanMapper;
import org.jeecg.modules.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 被测对象流程任务表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Api(tags="被测对象流程任务表")
@RestController
@RequestMapping("/uut/runningUutManager")
@Slf4j
public class RunningUutManagerController extends JeecgController<RunningUutManager, IRunningUutManagerService> {
	@Autowired
	private IRunningUutManagerService runningUutManagerService;
	@Autowired
	private IRunningUutCatagoryService runningUutCatagoryService;
	@Autowired
	private IRunningUutListService runningUutListService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IRunningUutLinkService runningUutLinkService;
	@Autowired
	private IRunningUutKvService runningUutKvService;
	@Autowired
	private INumberRuleInfoService numberService;
	@Autowired
	private IRunningUutFileListService runningUutFileListService;
	@Autowired
	private RunningUutFileListMapper runningUutFileListMapper;
	@Autowired
	private IRunningUutVersionService runningUutVersionService;
	@Autowired
	private IRunningUutFileListHistoryService runningUutFileListHistoryService;

	final String IN_CODE = "RKSQ_CODE";//入库
	final String OUT_CODE = "CKSQ_CODE";//出库
	final String CHANGE_CODE = "GDSQ_CODE";//更动

	private final String OUT="出库";
	private final String INPUT="入库";
	private final String EDIT="更动";
//	private final String NUM0="0";//未出库
//	private final String NUM1="1";//出库中
//	private final String NUM2="2";//已出库
//	private final String NUM3="3";//未入库
//	private final String NUM4="4";//入库中
//	private final String NUM5="5";//已入库
//	private final String NUM6="6";//更动中
//	private final String NUM7="7";//已更动
	private final String NUM0="0";//新增（手动创建）
	private final String NUM1="1";//新增（从资产库拉取）
	private final String NUM2="2";//出库
	private final String NUM3="3";//入库
	private final String NUM4="4";//更动
	private final String NUM5="5";//审核中
	private final String NUM6="6";//归档

	/**
	 * 分页列表查询
	 *
	 * @param runningUutManager
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-分页列表查询")
	@ApiOperation(value="被测对象流程任务表-分页列表查询", notes="被测对象流程任务表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutManager runningUutManager,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		Map<String, String[]> paramMap = req.getParameterMap();
		Map<String, Object> map = new HashMap<>(2000);
		map.put("uutType" ,
				paramMap.get("uutType") == null ? null : paramMap.get("uutType")[0]);
		map.put("title" ,
				paramMap.get("title") == null ? null : paramMap.get("title")[0]);
		map.put("status" ,
				paramMap.get("status") == null ? null : paramMap.get("status")[0]);
		map.put("pageNo" , pageNo);
		map.put("pageSize" , pageSize);
		Page<RunningUutManager> pageList = new Page<RunningUutManager>(pageNo, pageSize);
		try {
			pageList = runningUutManagerService.getPageList(pageList, map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		//去掉默认排序
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param runningUutManager
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-添加")
	@ApiOperation(value="被测对象流程任务表-添加", notes="被测对象流程任务表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutManager runningUutManager) {
		runningUutManagerService.save(runningUutManager);
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param runningUutManager
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-编辑")
	@ApiOperation(value="被测对象流程任务表-编辑", notes="被测对象流程任务表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutManager runningUutManager) {
		runningUutManagerService.updateById(runningUutManager);
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-通过id删除")
	@ApiOperation(value="被测对象流程任务表-通过id删除", notes="被测对象流程任务表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		runningUutManagerService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-批量删除")
	@ApiOperation(value="被测对象流程任务表-批量删除", notes="被测对象流程任务表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.runningUutManagerService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象流程任务表-通过id查询")
	@ApiOperation(value="被测对象流程任务表-通过id查询", notes="被测对象流程任务表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutManager runningUutManager = runningUutManagerService.getById(id);
		if(runningUutManager==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutManager);
	}

	 /**
	  * 通过id查询
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "被测对象流程任务表-通过id查询相关记录")
	 @ApiOperation(value="被测对象流程任务表-通过id查询相关记录", notes="被测对象流程任务表-通过id查询相关记录")
	 @GetMapping(value = "/queryRecordById")
	 public Result<?> queryRecordById(@RequestParam(name="id",required=true) String id) {
		 Map<String, Object> result = new HashMap<>();
		 //查询记录
		 RunningUutManager runningUutManager = runningUutManagerService.getById(id);
		 if(runningUutManager==null) {
			 return Result.error("未找到对应数据");
		 }else{
			 result.put("runningUutManager", runningUutManager);
			 //查询表单键值
			 RunningUutLink runningUutLink =
					 runningUutLinkService.findUniqueBy("instance_id",runningUutManager.getInstanceId());
			 if(null != runningUutLink){
				 List<Map<String, Object>> records = runningUutKvService.findKvMapByLinkId(runningUutLink.getId());
				 result.put("records", records);
			 }
			 //查询所有实例
			 List<RunningUutManager> list =
					 runningUutManagerService.getList(runningUutManager.getInstanceId());
			 List<RunningUutManager> result1 = new ArrayList<>();
			 for (RunningUutManager rum : list) {
				 String name = sysUserService.getRealNameById(rum.getAssignee());
				 rum.setAssignee(name);
				 result1.add(rum);
			 }
			 result.put("list", result1);
			 return Result.ok(result);
		 }

	 }

    /**
    * 导出excel
    *
    * @param request
    * @param runningUutManager
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutManager runningUutManager) {
        return super.exportXls(request, runningUutManager, RunningUutManager.class, "被测对象流程任务表");
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
        return super.importExcel(request, response, RunningUutManager.class);
    }

	/**
	 * 通过id查询
	 * @return
	 */
	@AutoLog(value = "被测对象出库/入库/更动申请发起")
	@ApiOperation(value="被测对象出库/入库/更动申请发起", notes="被测对象出库/入库/更动申请发起")
	@RequestMapping(value = "/requestForm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<?> requestForm(@RequestBody Map<String, Object> params) {
		try {
			String instanceId = (String) params.get("instanceId");// 申请类型
			String requestType = (String) params.get("requestType");// 申请类型
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();// 获取当前登录用户
			RunningUutManager runningUutManager = null;
			if(StringUtils.isEmpty(instanceId)){//流程发起
				instanceId = UUIDUtil.getUuId();
				runningUutManager=new RunningUutManager(getUutType(requestType),sysUser.getId(),requestType + " - " + sysUser.getRealname() + " - " + sdf.format(new Date()),new Date(),(String) params.get("comment"),sysUser.getUsername(),new Date(),instanceId,"request","完成",(String) params.get("uutListId"),(String) params.get("projectCode"),"start");
			}else{//被驳回的发起人提交
				runningUutManager = runningUutManagerService.getCurrentInstance(instanceId);
				runningUutManager.setFinishTime(new Date());
				runningUutManager.setResultType("完成");
				runningUutManager.setStatus(null);
				runningUutManager.setComment((String)params.get("comment"));
			}

			params.put("instanceId", instanceId);
			log.info("表单提交数据：", params);
			this.storekv(params);// 更新/存入表单数据
			if(StringUtils.isEmpty(instanceId)){//存入发起数据
				}else{//被驳回时修改
			}
			this.runningUutManagerService.saveOrUpdate(runningUutManager);

			// 存入下一个节点数据
			RunningUutNode nextNode = runningUutCatagoryService.findNextStep(getUutType(requestType), runningUutManager.getNodeId());
			RunningUutList runningUutList = runningUutListService.getById((String) params.get("uutListId"));
			if(null != nextNode) {
				// 存在下个节点则生成下个任务
				RunningUutManager nextStep = new RunningUutManager();
				nextStep.setUutType(getUutType(requestType));
				nextStep.setAssignee("role:uutManager");//角色为被测对象审核管理员
				nextStep.setTitle(requestType + " - " + sysUser.getRealname() + " - " + sdf.format(new Date()));
				nextStep.setCreateTime(new Date());
				nextStep.setCreateBy(sysUser.getUsername());
				nextStep.setNodeId(nextNode.getCode());
				nextStep.setCatalog("normal");
				nextStep.setInstanceId(instanceId);
				nextStep.setUutListId((String) params.get("uutListId"));
				nextStep.setProjectCode((String) params.get("projectCode"));
				nextStep.setStatus(0);
				this.runningUutManagerService.save(nextStep);

				if(requestType.equals("出库")){
					// 传过来被测件/其他附件的集合
					String uutFileNames = null;
					String otherFileNames = null;
					List<String> uutFileList = new ArrayList<>();
					List<String> otherFileList = new ArrayList<>();
					Integer totalNums = 0;
					if (params.get("uutFile") != null) {
						uutFileNames = (String) params.get("uutFile");
						uutFileList = Arrays.asList(uutFileNames.split(","));
						// 给传来的被测件在被测件更动表中修改状态（running_uut_file_list）
						for (String uutFileName : uutFileList) {
							List<RunningUutFileList> runningUutFileLists1 = runningUutFileListService.getByFileNameAndFileType(uutFileName, "被测件", (String) params.get("uutListId"));
							if (runningUutFileLists1.size() > 0 && runningUutFileLists1 != null) {
								for (RunningUutFileList runningUutFileList : runningUutFileLists1) {
									runningUutFileList.setStatus(NUM5);
									runningUutFileListService.saveOrUpdate(runningUutFileList);
								}
							}
						}
					}
					if (params.get("uutOtherFile") != null) {
						otherFileNames = (String) params.get("uutOtherFile");
						otherFileList = Arrays.asList(otherFileNames.split(","));
						// 给传来的其他附件在被测件更动表中修改状态（running_uut_file_list）
						for (String otherFileName : otherFileList) {
							List<RunningUutFileList> runningUutFileLists1 = runningUutFileListService.getByFileNameAndFileType(otherFileName, "其他", (String) params.get("uutListId"));
							if (runningUutFileLists1.size() > 0 && runningUutFileLists1 != null) {
								for (RunningUutFileList runningUutFileList : runningUutFileLists1) {
									runningUutFileList.setStatus(NUM5);
									runningUutFileListService.saveOrUpdate(runningUutFileList);
								}
							}
						}
					}
				}else {
					// 改变被测对象状态
					runningUutList.setUutStatus(NUM5);
				}

			}else {//不存在则流程结束
//				int totalNums = 0;
//				List<RunningUutFileList> runningUutFileLists = runningUutFileListService.getFileListByUutListId((String) params.get("uutListId"));
//				// 判断是否该被测对象下所有附件均为出库状态
//				for (RunningUutFileList runningUutFileList : runningUutFileLists) {
//					if (runningUutFileList.getStatus().equals("2")) {
//						totalNums += 1;
//					};
//				}
				// 改变被测对象状态
//				if(OUT.equals(requestType) && totalNums == runningUutFileLists.size()) { runningUutList.setUutStatus(NUM2);
				if(OUT.equals(requestType)) { runningUutList.setUutStatus(NUM2);
				}else if(INPUT.equals(requestType)) { runningUutList.setUutStatus(NUM3);
				}else if(EDIT.equals(requestType)) { runningUutList.setUutStatus(NUM4); }
			}
			runningUutListService.saveOrUpdate(runningUutList);
			return Result.ok("success");
		} catch (Exception e) {
			return Result.error("error");
		}
	}

	 /**
	  * 查找办理历史
	  * @return
	  */
	 @AutoLog(value = "办理历史-通过id查询")
	 @ApiOperation(value ="办理历史-通过id查询",notes = "办理历史-通过id查询")
	 @GetMapping(value = "/queryStepHistory")
	 public Result<?> queryStepHistory(@RequestParam(name="instanceId",required=true) String instanceId) {
		 List<RunningUutManager> list = runningUutManagerService.getList(instanceId);
		 List<RunningUutManager> result1 = new ArrayList<>();
		 for (RunningUutManager rum : list) {
			 String name = sysUserService.getRealNameById(rum.getAssignee());
			 rum.setAssignee(name);
			 result1.add(rum);
		 }
		 return Result.ok(result1);
	 }

	private String getUutType(String type) {
		String uutType = null;
		switch (type) {
		case "出库":
			uutType = "uutOut";
			break;
		case "入库":
			uutType = "uutIn";
			break;
		case "更动":
			uutType = "uutChange";
			break;
		default:
			break;
		}
		return uutType;
	}

	/**更新/存入表单数据*/
	private boolean storekv(Map<String, Object> params) {
		// dq add start,入库/出库/更动 申请序号单独赋值
		String requestType = (String) params.get("requestType");
		String requestCode = null;
		if(!org.springframework.util.StringUtils.isEmpty(requestType)) {
			if(INPUT.equals(requestType)) {
				requestCode = this.IN_CODE;
			}
			if(OUT.equals(requestType)) {
				requestCode = this.OUT_CODE;
			}
			if(EDIT.equals(requestType)) {
				requestCode = this.CHANGE_CODE;
			}
			params.put("uutRequestCode",numberService.generateNumberStrByNumberCode(requestCode));
		}
		// dq add end
		if(null == params.get("startDate") && "" == params.get("startDate")
				&& ((String)params.get("startDate")).length() <= 11){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			params.put("startDate", sdf.format(new Date()));
		}
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		String instanceId = (String) params.get("instanceId");
		Set<String> keys = params.keySet();
		if(!StringUtils.isEmpty(instanceId)) {
			RunningUutLink runningUutLink = runningUutLinkService.findUniqueBy("instance_id", instanceId);
			// 存在数据，则遍历
			if(null != runningUutLink) {
				List<RunningUutKv> kvList = runningUutKvService.findKvListByLinkId(runningUutLink.getInstanceId());
				List<RunningUutKv> result = new ArrayList<RunningUutKv>();
				for (RunningUutKv runningUutKv : kvList) {
					// 包含该条数据，则重新赋值
					if(keys.contains(runningUutKv.getPropCode())) {
						try {
							runningUutKv.setPropValue(params.get(runningUutKv.getPropCode()).toString());
							result.add(runningUutKv);
						} catch (Exception e) {
							log.error("数据存储错误", params.get(runningUutKv.getPropCode()));
						}
					}else {
						result.add(runningUutKv);
					}
				}
				runningUutKvService.saveBatch(result);
			}else {
				runningUutLink = new RunningUutLink();
				runningUutLink.setCreateBy(sysUser.getUsername());
				runningUutLink.setCreateTime(new Date());
				runningUutLink.setInstanceId(instanceId);
				runningUutLink.setStatus("0");
				runningUutLinkService.save(runningUutLink);

				for (String key : keys) {
					RunningUutKv runningUutKv = new RunningUutKv();
					runningUutKv.setCreateTime(new Date());
					runningUutKv.setLinkId(runningUutLink.getId());
					runningUutKv.setPropCode(key);
					runningUutKv.setPropStatus("0");
					if(null != params.get(key)){
						runningUutKv.setPropValue(params.get(key).toString());
					}
					runningUutKvService.save(runningUutKv);
				}
			}
			// 不存在数据，则插入
		}else {
			RunningUutLink runningUutLink = new RunningUutLink();
			runningUutLink.setCreateBy(sysUser.getUsername());
			runningUutLink.setCreateTime(new Date());
			runningUutLink.setInstanceId(instanceId);
			runningUutLink.setStatus("0");
			runningUutLinkService.save(runningUutLink);

			for (String key : keys) {
				RunningUutKv runningUutKv = new RunningUutKv();
				runningUutKv.setCreateTime(new Date());
				runningUutKv.setLinkId(runningUutLink.getId());
				runningUutKv.setPropCode(key);
				runningUutKv.setPropStatus("0");
				if(null != params.get(key)){
					runningUutKv.setPropValue(params.get(key).toString());
				}
				runningUutKvService.save(runningUutKv);
			}
		}
		return true;
	}

	/**
	 * 通过id查询
	 * @return
	 */
	@AutoLog(value = "项目查询")
	@ApiOperation(value="项目查询", notes="项目查询")
	@RequestMapping(value = "/getKvMap", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<?> getKvMap(@RequestBody Map<String, Object> params) {
		try {
			Map<String, Object> resultMap = new HashMap<>(2000);
			String instanceId="instanceId";
			if(null != params.get(instanceId)) {
				RunningUutLink runningUutLink =
					runningUutLinkService.findUniqueBy("instance_id", (String) params.get("instanceId"));
				log.info("runningUutLink : " + runningUutLink);
				if(null != runningUutLink) {
					List<Map<String, Object>> runningUutKvMap =
							runningUutKvService.findKvMapByLinkId(runningUutLink.getId());
					List<RunningUutManager> runningUutManager=runningUutManagerService.getList((String) params.get("instanceId"));
					List<RunningUutManager> result1 = new ArrayList<>();
					for (RunningUutManager rum : runningUutManager) {
						String name = sysUserService.getRealNameById(rum.getAssignee());
						rum.setAssignee(name);
						result1.add(rum);
					}
					Map<String, Object> result = new HashMap<>(2000);
					for (Map<String, Object> map : runningUutKvMap) {
						result.put((String) map.get("key"), map.get("value"));
					}

					log.info("runningUutKvMap : " + result);
					result.put("runningManager",result1);
					resultMap.put("runningUutKv", result);
				}else {
					resultMap.put("runningUutKv", "");
//					resultMap.put("runningManager","");
				}
			}
			return Result.ok(resultMap);
		} catch (Exception e) {
			return Result.error("error");
		}
	}

	/**
	 * 将多账号名转化为“,”拼接的真实姓名
	 * @param usernames
	 * @return
	 */
	public String convertProjectMembers(String usernames) {
		StringBuffer result = new StringBuffer("");
		String info=",";
		if(!StringUtils.isEmpty(usernames)) {
			for (String username : usernames.split(info)) {
				SysUser sysUser = sysUserService.getUserByName(username);
				if(!StringUtils.isEmpty(result.toString())) {
					result.append(",");
				}
				result.append(sysUser.getRealname());
			}
		}
		return result.toString();
	}

	/**
	 * 通过id查询
	 * @return
	 */
	@AutoLog(value = "被测对象出库/入库/更动审批")
	@ApiOperation(value="被测对象出库/入库/更动审批", notes="被测对象出库/入库/更动审批")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/submitForm", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Result<?> submitForm(@RequestBody Map<String, Object> params) {
		try {
			log.info("表单提交数据：", params);
			// 更新/存入表单数据
			this.storekv(params);
			// 申请类型
			String requestType = (String) params.get("requestType");
			// 处理结果
			String uutStatus = (String) params.get("uutStatus");
			// 处理结果
			String instanceId = (String) params.get("instanceId");
			// 表单参数
			Map<String, Object> parameterMap = (Map<String, Object>) params.get("values");
			//当前用户
			LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
			String disagree="disagree";
			// 审批结束
			String num2="2";
			if(num2.equals(uutStatus)) {
				// 改变被测对象审批表状态
				RunningUutManager runningUutManager = runningUutManagerService.getCurrentInstance(instanceId);
				if(null != runningUutManager) {
					runningUutManager.setFinishTime(new Date());
					runningUutManager.setComment((String) parameterMap.get("comment"));

					runningUutManager.setResultType("同意");
					runningUutManager.setStatus(1);
					runningUutManager.setAssignee(sysUser.getId());
					runningUutManagerService.saveOrUpdate(runningUutManager);
				}

				// 改变被测对象状态
				RunningUutList runningUutList =
						runningUutListService.getById((String) parameterMap.get("uutListId"));

				if(INPUT.equals(requestType)) {
					uutStatus = NUM3;

					runningUutList.setUutStatus(uutStatus);
					// 改变被测件附件修改表状态
					List<RunningUutFileList> runningUutFileLists =
							runningUutFileListService.getFileListByUutListId((String) parameterMap.get("uutListId"));
					if(runningUutFileLists.size() > 0 && runningUutFileLists != null){
						for(RunningUutFileList runningUutFileList : runningUutFileLists ){
							runningUutFileList.setStatus(uutStatus);
							runningUutFileListService.saveOrUpdate(runningUutFileList);
						}
					}
				}
				if(EDIT.equals(requestType)){
					uutStatus = NUM4;

					runningUutList.setUutStatus(uutStatus);
					// 改变被测件附件修改表状态
					List<RunningUutFileList> runningUutFileLists =
							runningUutFileListService.getFileListByUutListId((String) parameterMap.get("uutListId"));
					if(runningUutFileLists.size() > 0 && runningUutFileLists != null){
						for(RunningUutFileList runningUutFileList : runningUutFileLists ){
							runningUutFileList.setStatus(uutStatus);
							runningUutFileListService.saveOrUpdate(runningUutFileList);
						}
					}
				}

				// 改变被测件附件修改表状态
				List<RunningUutFileList> runningUutFileLists =
						runningUutFileListService.getFileListByUutListId((String) parameterMap.get("uutListId"));
				if(runningUutFileLists.size() > 0 && runningUutFileLists != null) {
					// 传过来被测件/其他附件的集合
					String uutFileNames = null;
					String otherFileNames = null;
					List<String> uutFileList = new ArrayList<>();
					List<String> otherFileList = new ArrayList<>();
					int totalNums = 0;
					if (parameterMap.get("uutFile") != null) {
						uutFileNames = (String) parameterMap.get("uutFile");
						uutFileList = Arrays.asList(uutFileNames.split(","));
						// 给传来的被测件在被测件更动表中修改状态（running_uut_file_list）
						for (String uutFileName : uutFileList) {
							List<RunningUutFileList> runningUutFileLists1 = runningUutFileListService.getByFileNameAndFileType(uutFileName, "被测件", (String) parameterMap.get("uutListId"));
							if (runningUutFileLists1.size() > 0 && runningUutFileLists1 != null) {
								for (RunningUutFileList runningUutFileList : runningUutFileLists1) {
									runningUutFileList.setStatus(uutStatus);
									runningUutFileListService.saveOrUpdate(runningUutFileList);
								}
							}
						}
					}
					if (parameterMap.get("uutOtherFile") != null) {
						otherFileNames = (String) parameterMap.get("uutOtherFile");
						otherFileList = Arrays.asList(otherFileNames.split(","));
						// 给传来的其他附件在被测件更动表中修改状态（running_uut_file_list）
						for (String otherFileName : otherFileList) {
							List<RunningUutFileList> runningUutFileLists1 = runningUutFileListService.getByFileNameAndFileType(otherFileName, "其他", (String) parameterMap.get("uutListId"));
							if (runningUutFileLists1.size() > 0 && runningUutFileLists1 != null) {
								for (RunningUutFileList runningUutFileList : runningUutFileLists1) {
									runningUutFileList.setStatus(uutStatus);
									runningUutFileListService.saveOrUpdate(runningUutFileList);
								}
							}
						}
					}

					// 判断是否该被测对象下所有附件均为出库状态
					List<RunningUutFileList> runningUutFileLists1 = runningUutFileListService.getFileListByUutListId((String) parameterMap.get("uutListId"));
					for (RunningUutFileList runningUutFileList : runningUutFileLists1) {
						if (runningUutFileList.getStatus().equals("2")) {
							totalNums += 1;
						};
					}
					// 若是则将被测对象表中该被测对象状态改为出库
					if (totalNums == runningUutFileLists.size()) {
						runningUutList.setUutStatus(uutStatus);
					}
				}

				// 更动结束，修改被测对象信息
				if(EDIT.equals(requestType)) {

					runningUutList.setUpdateBy(runningUutManager.getCreateBy());
					runningUutList.setUpdateTime(new Date());
					runningUutList.setUutAssetsDetail((String) parameterMap.get("uutAssetsDetail"));
					runningUutList.setUutAssetsId((String) parameterMap.get("uutAssetsId"));
					runningUutList.setUutCode((String) parameterMap.get("uutCode"));
					runningUutList.setUutFile((String) parameterMap.get("uutFile"));
					runningUutList.setUutOtherFile((String) parameterMap.get("uutOtherFile"));
					runningUutList.setUutType((String) parameterMap.get("uutType"));
					runningUutList.setUutVersion((String) parameterMap.get("uutVersion"));

					// 将更动后的版本号插入被测对象版本表(running_uut_version表)
					if(!((String) parameterMap.get("uutVersion")).isEmpty() && ((String) parameterMap.get("uutVersion")) != ""){
						RunningUutVersion runningUutVersion = new RunningUutVersion();
						runningUutVersion.setUutListId((String) parameterMap.get("uutListId"));
						runningUutVersion.setVersion((String) parameterMap.get("uutVersion"));
						runningUutVersionService.save(runningUutVersion);
					}

					// 若被测件、其他附件不为空，则将被测件附件更动表(running_uut_file_list)对应被测对象id历史数据先清空
					runningUutFileListService.deleteByUutListId((String) parameterMap.get("uutListId"));
					// 再将其新数据插入其中
					String uutVersionId = runningUutVersionService.getUutVersionId((String) parameterMap.get("uutListId"),(String) parameterMap.get("uutVersion"));
					if(!((String) parameterMap.get("uutFile")).isEmpty()){
						for(String uutFileName : ((String) parameterMap.get("uutFile")).split(",")){
							RunningUutFileList runningUutFileList = new RunningUutFileList();
							runningUutFileList.setFileName(uutFileName);
							runningUutFileList.setFileType("被测件");
							runningUutFileList.setStatus("4");
							runningUutFileList.setUutListId((String) parameterMap.get("uutListId"));
							runningUutFileList.setCreateBy(sysUser.getId());
							runningUutFileList.setCreateTime(new Date());
							runningUutFileList.setUpdateBy(sysUser.getUsername());
							runningUutFileList.setUpdateTime(new Date());
							runningUutFileListService.save(runningUutFileList);
							RunningUutFileListHistory runningUutFileListHistory = new RunningUutFileListHistory();
							runningUutFileListHistory.setFileName(uutFileName);
							runningUutFileListHistory.setFileType("被测件");
							runningUutFileListHistory.setUutListId((String) parameterMap.get("uutListId"));
							runningUutFileListHistory.setUutVersionId(uutVersionId);
							runningUutFileListHistory.setCreateBy(sysUser.getId());
							runningUutFileListHistory.setCreateTime(new Date());
							runningUutFileListHistoryService.save(runningUutFileListHistory);
						}
					}
					if(!((String) parameterMap.get("uutOtherFile")).isEmpty()){
						for(String uutOtherFileName : ((String) parameterMap.get("uutOtherFile")).split(",")){
							RunningUutFileList runningUutFileList = new RunningUutFileList();
							runningUutFileList.setFileName(uutOtherFileName);
							runningUutFileList.setFileType("其他");
							runningUutFileList.setStatus("4");
							runningUutFileList.setUutListId((String) parameterMap.get("uutListId"));
							runningUutFileList.setCreateBy(sysUser.getId());
							runningUutFileList.setCreateTime(new Date());
							runningUutFileList.setUpdateBy(sysUser.getUsername());
							runningUutFileList.setUpdateTime(new Date());
							runningUutFileListService.save(runningUutFileList);
							RunningUutFileListHistory runningUutFileListHistory = new RunningUutFileListHistory();
							runningUutFileListHistory.setFileName(uutOtherFileName);
							runningUutFileListHistory.setFileType("其他");
							runningUutFileListHistory.setUutListId((String) parameterMap.get("uutListId"));
							runningUutFileListHistory.setUutVersionId(uutVersionId);
							runningUutFileListHistory.setCreateBy(sysUser.getId());
							runningUutFileListHistory.setCreateTime(new Date());
							runningUutFileListHistoryService.save(runningUutFileListHistory);
						}
					}
//					if(runningUutFileLists.size() > 0 && runningUutFileLists != null){
//						for(RunningUutFileList runningUutFileList : runningUutFileLists ){
//							runningUutFileList.setUpdateBy(runningUutManager.getCreateBy());
//							runningUutFileList.setUpdateTime(new Date());
//							runningUutFileListService.saveOrUpdate(runningUutFileList);
//						}
//					}
				}
				runningUutListService.saveOrUpdate(runningUutList);
				// 驳回
			}else if(disagree.equals(uutStatus)) {
				// 改变被测对象审批表状态
				RunningUutManager runningUutManager = runningUutManagerService.getCurrentInstance(instanceId);
				if(null != runningUutManager) {
					runningUutManager.setFinishTime(new Date());
					runningUutManager.setComment((String) parameterMap.get("comment"));
					runningUutManager.setResultType("不同意");
					runningUutManager.setAssignee(sysUser.getId());
					runningUutManagerService.saveOrUpdate(runningUutManager);

				}
				// 改变被测对象审批表状态
				RunningUutManager startNode = runningUutManagerService.getStartInstance(instanceId);
				if(null != startNode) {
					// 生成下个任务
					RunningUutManager nextStep = new RunningUutManager();
					BeanMapper.copyBean(startNode, nextStep);
					nextStep.setId(null);
					nextStep.setCreateTime(new Date());
					nextStep.setFinishTime(null);
					nextStep.setResultType("");
					nextStep.setCatalog("back");
					nextStep.setComment("");

					nextStep.setStatus(0);
					this.runningUutManagerService.save(nextStep);
				}
			}
			return Result.ok("success");
		} catch (Exception e) {
			return Result.error("error");
		}
	}
}
