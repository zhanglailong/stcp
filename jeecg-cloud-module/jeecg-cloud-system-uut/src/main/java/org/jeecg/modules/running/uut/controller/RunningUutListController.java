package org.jeecg.modules.running.uut.controller;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.running.uut.entity.*;
import org.jeecg.modules.running.uut.mapper.RunningUutVersionMapper;
import org.jeecg.modules.running.uut.service.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.running.uut.vo.RunningUutListVo;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 被测对象列表
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Api(tags="被测对象列表")
@RestController
@RequestMapping("/uut/runningUutList")
@Slf4j
public class RunningUutListController extends JeecgController<RunningUutList, IRunningUutListService> {
	@Autowired
	private IRunningUutListService runningUutListService;
	@Autowired
	private IRunningUutListUserService runningUutListUserService;
	@Autowired
	private IRunningUutListHistoryService runningUutListHistoryService;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private IRunningUutFileListService runningUutFileListService;
	@Autowired
	private IRunningUutVersionService runningUutVersionService;
	@Autowired
	private IRunningUutFileListHistoryService runningUutFileListHistoryService;

	/**
	 * 分页列表查询
	 *
	 * @param runningUutList
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "被测对象列表-分页列表查询")
	@ApiOperation(value="被测对象列表-分页列表查询", notes="被测对象列表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(RunningUutList runningUutList,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<RunningUutList> queryWrapper = QueryGenerator.initQueryWrapper(runningUutList, req.getParameterMap());
		queryWrapper.eq("delete_flag",0);
		Page<RunningUutList> page = new Page<>(pageNo, pageSize);
		IPage<RunningUutList> pageList = runningUutListService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 *   添加
	 *
	 * @param runningUutList
	 * @return
	 */
	@AutoLog(value = "被测对象列表-添加")
	@ApiOperation(value="被测对象列表-添加", notes="被测对象列表-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody RunningUutList runningUutList) {
		final String sign0="0";//如果创建方式为“手动创建”（0），则状态为“新增”（0）
		if(sign0.equals(runningUutList.getUutChooseType())){
			runningUutList.setUutStatus("0");
		}else{//从资产库选取
			runningUutList.setUutStatus("1");
		}
		runningUutList.setAnalyzeStatus("0");
		//获取当前登录用户
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningUutList.setCreateBy(sysUser.getId());
		runningUutList.setUpdateBy(sysUser.getUsername());
		runningUutList.setCreateTime(new Date());
		runningUutListService.save(runningUutList);
		//新增时将版本插入被测对象版本表(running_uut_version表)
		RunningUutVersion runningUutVersion = new RunningUutVersion();
		runningUutVersion.setUutListId(runningUutList.getId());
		runningUutVersion.setVersion(runningUutList.getUutVersion());
		runningUutVersionService.save(runningUutVersion);


		//通过uutListId和version获取versionId
		String versionId = runningUutVersionService.getUutVersionId(runningUutList.getId(),runningUutList.getUutVersion());
		if(runningUutList.getUutFile() != null){
			for (String uutFileName : runningUutList.getUutFile().split(",")){
				//将被测件、附件存入被测件附件更动表(running_uut_file_list表)
				RunningUutFileList runningUutFileList = new RunningUutFileList();
				runningUutFileList.setFileName(uutFileName);
				runningUutFileList.setFileType("被测件");
				runningUutFileList.setStatus("0");
				runningUutFileList.setUutListId(runningUutList.getId());
				runningUutFileList.setCreateBy(sysUser.getId());
				runningUutFileList.setCreateTime(new Date());
				runningUutFileListService.save(runningUutFileList);
				//将被测件、附件存入被测件附件更动履历表(running_uut_file_list_history)
				RunningUutFileListHistory runningUutFileListHistory = new RunningUutFileListHistory();
				runningUutFileListHistory.setFileName(uutFileName);
				runningUutFileListHistory.setFileType("被测件");
				runningUutFileListHistory.setUutListId(runningUutList.getId());
				runningUutFileListHistory.setUutVersionId(versionId);
				runningUutFileListHistory.setCreateBy(sysUser.getId());
				runningUutFileListHistory.setCreateTime(new Date());
				runningUutFileListHistoryService.save(runningUutFileListHistory);
			}
		}
		if(runningUutList.getUutOtherFile() != null){
			for(String uutOtherFileName : runningUutList.getUutOtherFile().split(",")){
				//将被测件、附件存入被测件附件更动表(running_uut_file_list表)
				RunningUutFileList runningUutFileList = new RunningUutFileList();
				runningUutFileList.setFileName(uutOtherFileName);
				runningUutFileList.setFileType("其他");
				runningUutFileList.setStatus("0");
				runningUutFileList.setUutListId(runningUutList.getId());
				runningUutFileList.setCreateBy(sysUser.getId());
				runningUutFileList.setCreateTime(new Date());
				runningUutFileListService.save(runningUutFileList);
				//将被测件、附件存入被测附件更动履历表(running_uut_file_list_history)
				RunningUutFileListHistory runningUutFileListHistory = new RunningUutFileListHistory();
				runningUutFileListHistory.setFileName(uutOtherFileName);
				runningUutFileListHistory.setFileType("其他");
				runningUutFileListHistory.setUutListId(runningUutList.getId());
				runningUutFileListHistory.setUutVersionId(versionId);
				runningUutFileListHistory.setCreateBy(sysUser.getId());
				runningUutFileListHistory.setCreateTime(new Date());
				runningUutFileListHistoryService.save(runningUutFileListHistory);
			}
		}

		//将人员范围存入人员表
		List<RunningUutListUser> userList = new ArrayList<>();
		boolean isContainCurrentUser = false;
		for (String username : runningUutList.getUutUser().split(",")) {
			if(username.equals(sysUser.getUsername())){
				isContainCurrentUser = true;
			}
			SysUser user = sysUserService.getUserByName(username);
			userList.add(new RunningUutListUser(null, runningUutList.getId(), user.getId()));
		}
		if(!isContainCurrentUser){
			userList.add(new RunningUutListUser(null, runningUutList.getId(), sysUser.getId()));
		}
		runningUutListUserService.saveBatch(userList);
		// dq add 往操作历史表中插入记录
		insertHistory(runningUutList,0,runningUutList.getId());
		return Result.ok("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param runningUutList
	 * @return
	 */
	@AutoLog(value = "被测对象列表-编辑")
	@ApiOperation(value="被测对象列表-编辑", notes="被测对象列表-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody RunningUutList runningUutList) {
		//获取当前登录用户
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningUutList.setUpdateBy(sysUser.getUsername());
		runningUutList.setUpdateTime(new Date());
		runningUutListService.updateById(runningUutList);
		// 操作历史表中插入记录
        insertHistory(runningUutList,1,runningUutList.getId());
		return Result.ok("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象列表-通过id删除")
	@ApiOperation(value="被测对象列表-通过id删除", notes="被测对象列表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		// runningUutListService.removeById(id); 以下改为逻辑删除
		RunningUutList record = runningUutListService.getById(id);
		record.setDeleteFlag(1);
		runningUutListService.updateById(record);
		// dq add 往操作历史表中插入记录
		insertHistory(record,2,record.getId());
		return Result.ok("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "被测对象列表-批量删除")
	@ApiOperation(value="被测对象列表-批量删除", notes="被测对象列表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		//this.runningUutListService.removeByIds(Arrays.asList(ids.split(","))); 以下改完逻辑删除
		List<RunningUutList> runningUutList = runningUutListService.listByIds(Arrays.asList(ids.split(",")));
		for(RunningUutList record : runningUutList)
		{
			record.setDeleteFlag(1);
			// dq add 增加操作记录
			insertHistory(record,2,record.getId());
		}
		runningUutListService.updateBatchById(runningUutList);
		return Result.ok("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象列表-通过id查询")
	@ApiOperation(value="被测对象列表-通过id查询", notes="被测对象列表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		RunningUutListVo runningUutListVo = runningUutListService.findUniqueVoBy("id", id);
		if(runningUutListVo==null) {
			return Result.error("未找到对应数据");
		}
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		runningUutListVo.setApplierDictText(sysUser.getRealname());
		runningUutListVo.setApplier(sysUser.getUsername());
		runningUutListVo.setRunningManager(null);
		return Result.ok(runningUutListVo);
	}

	/**
	 * 接口--添加评价体系id
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "被测对象列表-通过id查询")
	@ApiOperation(value="被测对象列表-通过id查询", notes="被测对象列表-通过id查询")
	@GetMapping(value = "/saveTestMassTemplateById")
	public Result<?> saveTestMassTemplate(
			@RequestParam(name="id",required=true) String id,
			@RequestParam(name="testTemplate") String testTemplate) {
		RunningUutList runningUutList = runningUutListService.getById(id);
		if(runningUutList==null) {
			return Result.error("未找到对应数据");
		}
		runningUutList.setTestTemplate(testTemplate);
		runningUutListService.updateById(runningUutList);
		return Result.ok(runningUutList);
	}


    /**
    * 导出excel
    *
    * @param request
    * @param runningUutList
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, RunningUutList runningUutList) {
        return super.exportXls(request, runningUutList, RunningUutList.class, "被测对象列表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
//    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
//    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
//        return super.importExcel(request, response, RunningUutList.class);
//    }

	 /**
	  * 通过excel自定义导入数据
	  *
	  * @param request
	  * @param response
	  * @return
	  */
	 @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	 public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		 MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		 Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		 for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			 MultipartFile file = entity.getValue();// 获取上传文件对象
			 ImportParams params = new ImportParams();
			 params.setTitleRows(2);
			 params.setHeadRows(1);
			 params.setNeedSave(true);
			 try {
			 	 // 欲导入数据集合
				 List<RunningUutList> list = ExcelImportUtil.importExcel(file.getInputStream(), RunningUutList.class, params);
				 //update-begin-author:taoyan date:20190528 for:批量插入数据
				 long start = System.currentTimeMillis();
				 List<RunningUutList> runningUutLists = runningUutListService.list();
				 // 获取数据库中已存在被测对象标识集合
				 List<String> uutCodes = runningUutLists.stream().map(i -> i.getUutCode()).collect(Collectors.toList());
				 // 被测对象标识已存在集合
				 List<String> existUutCodes = new ArrayList<>();
				 List<Integer> indexs = new ArrayList<>();
				 // 判断欲导入数据中被测对象标识是否已存在，若存在则移除。
				for(int i = 0; i < list.size(); i++){
					int number = 0;
					for(int j = 0; j < uutCodes.size() ; j++ ){
						if(list.get(i).getUutCode().equals(uutCodes.get(j))){
							existUutCodes.add(list.get(i).getUutCode());
							// TODO  若欲导入仅为一条数据 且该数据已存在 先记录 别移除
							indexs.add(number);
						}
					}
					number+=1;
				}

				if(!indexs.isEmpty() && indexs.size() > 0){
					int size = list.size();
					for (int i : indexs){
						if(i == 0){
							list.remove(i);
							if(list.isEmpty() && list.size() == 0){
								return Result.error("文件导入失败:被测对象标识均已存在。若有改动请点击更多-编辑。");
							}
						}else if(size == list.size()){
							list.remove(i);
						}else {
							list.remove(i-1);
						}
					}
					if(!list.isEmpty() && list.size() > 0){
						runningUutListService.saveBatch(list);
						// 若欲导入数据中有已存在被测对象标识，且还可正常导入的情况，算导入成功，并将发生冲突被测对象标识展示提醒。
						if(!existUutCodes.isEmpty() && existUutCodes.size() > 0){
							return Result.ok("文件导入成功！数据行数：" + list.size() + "。但有"+ existUutCodes.size()+ "条数据,被测对象标识已存在。若有改动请点编辑。为:" + existUutCodes.toString());
						}
					}
				}else{
					runningUutListService.saveBatch(list);
					// 若欲导入数据中有已存在被测对象标识，且还可正常导入的情况，算导入成功，并将发生冲突被测对象标识展示提醒。
					return Result.ok("文件导入成功！数据行数：" + list.size());
				}

				 //400条 saveBatch消耗时间1592毫秒  循环插入消耗时间1947毫秒
				 //1200条  saveBatch消耗时间3687毫秒 循环插入消耗时间5212毫秒
				 log.info("消耗时间" + (System.currentTimeMillis() - start) + "毫秒");
				 //update-end-author:taoyan date:20190528 for:批量插入数据
			 } catch (Exception e) {
				 log.error(e.getMessage(), e);
				 return Result.error("文件导入失败:" + e.getMessage());
			 } finally {
				 try {
					 file.getInputStream().close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
			 }
		 }
		 return Result.error("文件导入失败！");
	 }



	 /** dq add
	  * 向操作历史表中插入操作记录
	  * 参数:
	  * 	RunningUutList originData: 新增和编辑操作时，会把最新数据存起来
	  * 	Integer opTye: 操作类型,0:新增  1:编辑 2:删除
	  * 	String mainId: running_uut_list表id
	  */
	public void insertHistory(RunningUutList originData,Integer opType,String mainId)
	{
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		RunningUutListHistory runningUutListHistory = new RunningUutListHistory();
		int sign2=2;
		if(opType == 0)
		{
			// 新增时把源数据在历史表中备份一个
			BeanUtils.copyProperties(originData,runningUutListHistory);
			runningUutListHistory.setSort(getMaxSortByMainId(mainId));
		}
		if(opType == sign2)
		{
			// 删除
			BeanUtils.copyProperties(originData,runningUutListHistory);
			runningUutListHistory.setSort(getMaxSortByMainId(mainId));
			runningUutListHistory.setDelFlag(1);
		}
		if(opType == 0 || opType == sign2)
        {
            runningUutListHistory.setId(null);
            runningUutListHistory.setUpdateBy(sysUser.getUsername());
            runningUutListHistory.setOpType(opType);
            runningUutListHistory.setRunningUutListId(mainId);
            runningUutListHistoryService.save(runningUutListHistory);
        }
		if(opType == 1)
        {
            // 编辑操作单独处理,改过东西才会保存
            List<RunningUutListHistory> historyList = originData.getRunningUutListHistory();
            if(historyList != null && historyList.size() > 0)
            {
				Integer insertSort = getMaxSortByMainId(mainId); // 当前插入的sort
				RunningUutListHistory newEdit=new RunningUutListHistory();
				BeanUtils.copyProperties(originData,newEdit);
				newEdit.setId(null);
				newEdit.setRunningUutListId(mainId);
				newEdit.setUpdateBy(sysUser.getUsername());
				newEdit.setSort(insertSort);
				newEdit.setCreateTime(new Date());
				newEdit.setOpType(1);
				runningUutListHistoryService.save(newEdit);//数据库存入当前编辑的记录值

				//暂不存 每个字段的编辑记录的保存到数据库
//				for(RunningUutListHistory record : historyList){
//                  BeanUtils.copyProperties(originData,record); // 备份最新数据
//					record.setId(null);
//                	record.setRunningUutListId(mainId);
//                    record.setCreateBy(sysUser.getId());
//                    record.setSort(insertSort);
//                    record.setCreateTime(new Date());
//                    record.setOpType(1);
//					}
//                	runningUutListHistoryService.saveBatch(historyList);
            }
        }
	}

	 /** dq add
	  * 返回本次插入历史表中的sort值,同次操作sort值相同
	  */
	 public Integer getMaxSortByMainId(String runningUutListId)
	 {
		 QueryWrapper<RunningUutListHistory> queryWrapper = new QueryWrapper<>();
		 queryWrapper.eq("running_uut_list_id",runningUutListId);
		 queryWrapper.orderByDesc("create_time");
		 List<RunningUutListHistory> list = runningUutListHistoryService.list(queryWrapper);
		 if(list == null || list.size() == 0)
		 {
		 	return 0;
		 }
		 else
		 {
		 	Integer currentSort = list.get(0).getSort();
		 	return currentSort + 1;
		 }
	 }
 }
