package org.jeecg.modules.system.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysPositionService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.util.FindsDepartsChildrenUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * ????????? ???????????????
 * <p>
 * 
 * @Author: Steve @Since??? 2019-01-22
 */
@RestController
@RequestMapping("/sys/sysDepart")
@Slf4j
public class SysDepartController {

	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysUserDepartService sysUserDepartService;
	/**
	 * ???????????? ??????????????????,??????????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryMyDeptTreeList", method = RequestMethod.GET)
	public Result<List<SysDepartTreeModel>> queryMyDeptTreeList() {
		Result<List<SysDepartTreeModel>> result = new Result<>();
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		try {
			if(oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals( CommonConstant.USER_IDENTITY_2 )){
				List<SysDepartTreeModel> list = sysDepartService.queryMyDeptTreeList(user.getDepartIds());
				result.setResult(list);
				result.setMessage(CommonConstant.USER_IDENTITY_2.toString());
				result.setSuccess(true);
			}else{
				result.setMessage(CommonConstant.USER_IDENTITY_1.toString());
				result.setSuccess(true);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * ???????????? ??????????????????,??????????????????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryTreeList", method = RequestMethod.GET)
	public Result<List<SysDepartTreeModel>> queryTreeList() {
		Result<List<SysDepartTreeModel>> result = new Result<>();
		try {
			// ??????????????????
			List<SysDepartTreeModel> list = sysDepartService.queryTreeList();
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * ??????????????? ???????????????????????????????????????,?????????????????????
	 * 
	 * @param sysDepart
	 * @return
	 */
	/**@RequiresRoles ({"admin"})*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysDepart> add(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
		Result<SysDepart> result = new Result<SysDepart>();
		String username = JwtUtil.getUserNameByToken(request);
		try {
			sysDepart.setCreateBy(username);
			sysDepartService.saveDepartData(sysDepart, username);
			//?????????????????????
			result.success("???????????????");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("????????????");
		}
		return result;
	}

	/**
	 * ???????????? ???????????????????????????,?????????????????????
	 * 
	 * @param sysDepart
	 * @return
	 */
	/**@RequiresRoles ({"admin"})*/
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysDepart> edit(@RequestBody SysDepart sysDepart, HttpServletRequest request) {
		String username = JwtUtil.getUserNameByToken(request);
		sysDepart.setUpdateBy(username);
		Result<SysDepart> result = new Result<SysDepart>();
		SysDepart sysDepartEntity = sysDepartService.getById(sysDepart.getId());
		if (sysDepartEntity == null) {
			result.error500("?????????????????????");
		} else {
			boolean ok = sysDepartService.updateDepartDataById(sysDepart, username);
			// TODO ??????false???????????????
			if (ok) {
				//?????????????????????
				result.success("????????????!");
			}
		}
		return result;
	}
	
	 /**
     *   ??????id??????
    * @param id
    * @return
    */
	/**@RequiresRoles ({"admin"})*/
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
   public Result<SysDepart> delete(@RequestParam(name="id",required=true) String id) {

       Result<SysDepart> result = new Result<SysDepart>();
       SysDepart sysDepart = sysDepartService.getById(id);
       if(sysDepart==null) {
           result.error500("?????????????????????");
       }else {
           boolean ok = sysDepartService.delete(id);
           if(ok) {
	            //?????????????????????
               result.success("????????????!");
           }
       }
       return result;
   }


	/**
	 * ???????????? ???????????????????????????ID,???????????????????????????????????????????????????
	 * 
	 * @param ids
	 * @return
	 */
	/**@RequiresRoles ({"admin"})*/
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
	public Result<SysDepart> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {

		Result<SysDepart> result = new Result<SysDepart>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("??????????????????");
		} else {
			this.sysDepartService.deleteBatchWithChildren(Arrays.asList(ids.split(",")));
			result.success("????????????!");
		}
		return result;
	}

	/**
	 * ???????????? ?????????????????????????????????????????????,?????????????????????????????????????????????,?????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryIdTree", method = RequestMethod.GET)
	public Result<List<DepartIdModel>> queryIdTree() {
		Result<List<DepartIdModel>> result = new Result<>();
		try {
			List<DepartIdModel> list = sysDepartService.queryDepartIdTreeList();
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	 
	/**
	 * <p>
	 * ????????????????????????,???????????????????????????????????????
	 * </p>
	 * 
	 * @param keyWord
	 * @return
	 */
	@RequestMapping(value = "/searchBy", method = RequestMethod.GET)
	public Result<List<SysDepartTreeModel>> searchBy(@RequestParam(name = "keyWord", required = true) String keyWord,@RequestParam(name = "myDeptSearch", required = false) String myDeptSearch) {
		Result<List<SysDepartTreeModel>> result = new Result<List<SysDepartTreeModel>>();
		//???????????????myDeptSearch???1?????????????????????????????????????????????????????????????????????????????????
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String departIds = null;
		if(oConvertUtils.isNotEmpty(user.getUserIdentity()) && user.getUserIdentity().equals( CommonConstant.USER_IDENTITY_2 )){
			departIds = user.getDepartIds();
		}
		List<SysDepartTreeModel> treeList = this.sysDepartService.searhBy(keyWord,myDeptSearch,departIds);
		if (treeList == null || treeList.size() == 0) {
			result.setSuccess(false);
			result.setMessage("????????????????????????");
			return result;
		}
		result.setResult(treeList);
		return result;
	}


	/**
     * ??????excel
     *
     * @param request
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(SysDepart sysDepart,HttpServletRequest request) {
        // Step.1 ??????????????????
        QueryWrapper<SysDepart> queryWrapper = QueryGenerator.initQueryWrapper(sysDepart, request.getParameterMap());
        //Step.2 AutoPoi ??????Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        List<SysDepart> pageList = sysDepartService.list(queryWrapper);
        //???????????????
        Collections.sort(pageList, new Comparator<SysDepart>() {
            @Override
			public int compare(SysDepart arg0, SysDepart arg1) {
            	return arg0.getOrgCode().compareTo(arg1.getOrgCode());
            }
        });
        //??????????????????
        mv.addObject(NormalExcelConstants.FILE_NAME, "????????????");
        mv.addObject(NormalExcelConstants.CLASS, SysDepart.class);
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:"+user.getRealname(), "????????????"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
    }

    /**
     * ??????excel????????????
     *
     * @param request
     * @param response
     * @return
     */
    /**@RequiresRoles ({"admin"})*/
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@CacheEvict(value= {CacheConstant.SYS_DEPARTS_CACHE,CacheConstant.SYS_DEPART_IDS_CACHE}, allEntries=true)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<String> errorMessageList = new ArrayList<>();
		List<SysDepart> listSysDeparts = null;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
	        // ????????????????????????
            MultipartFile file = entity.getValue();
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
            	// orgCode????????????
            	int codeLength = 3;
                listSysDeparts = ExcelImportUtil.importExcel(file.getInputStream(), SysDepart.class, params);
                //???????????????
                Collections.sort(listSysDeparts, new Comparator<SysDepart>() {
                    @Override
					public int compare(SysDepart arg0, SysDepart arg1) {
                    	return arg0.getOrgCode().length() - arg1.getOrgCode().length();
                    }
                });

                int num = 0;
                for (SysDepart sysDepart : listSysDeparts) {
                	String orgCode = sysDepart.getOrgCode();
                	if(orgCode.length() > codeLength) {
                		String parentCode = orgCode.substring(0, orgCode.length()-codeLength);
                		QueryWrapper<SysDepart> queryWrapper = new QueryWrapper<SysDepart>();
                		queryWrapper.eq("org_code", parentCode);
                		try {
                		SysDepart parentDept = sysDepartService.getOne(queryWrapper);
                		if(!parentDept.equals(null)) {
							sysDepart.setParentId(parentDept.getId());
						} else {
							sysDepart.setParentId("");
						}
                		}catch (Exception e) {
                			//???????????????parentDept
                		}
                	}else{
                		sysDepart.setParentId("");
					}
					sysDepart.setDelFlag(CommonConstant.DEL_FLAG_0.toString());
					ImportExcelUtil.importDateSaveOne(sysDepart, ISysDepartService.class, errorMessageList, num, CommonConstant.SQL_INDEX_UNIQ_DEPART_ORG_CODE);
					num++;
                }
				//??????????????????
				Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
				Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
				redisTemplate.delete(keys3);
				redisTemplate.delete(keys4);
				return ImportExcelUtil.imporReturnRes(errorMessageList.size(), listSysDeparts.size() - errorMessageList.size(), errorMessageList);
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return Result.error("??????????????????:"+e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.error("?????????????????????");
    }


	/**
	 * ????????????????????????
	 * @return
	 */
	@GetMapping("listAll")
	public Result<List<SysDepart>> listAll(@RequestParam(name = "id", required = false) String id) {
		Result<List<SysDepart>> result = new Result<>();
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<SysDepart>();
		query.orderByAsc(SysDepart::getOrgCode);
		if(oConvertUtils.isNotEmpty(id)){
			String[] arr = id.split(",");
			query.in(SysDepart::getId,arr);
		}
		List<SysDepart> ls = this.sysDepartService.list(query);
		result.setSuccess(true);
		result.setResult(ls);
		return result;
	}
	/**
	 * ???????????? ??????????????????,??????????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryTreeByKeyWord", method = RequestMethod.GET)
	public Result<Map<String,Object>> queryTreeByKeyWord(@RequestParam(name = "keyWord", required = false) String keyWord) {
		Result<Map<String,Object>> result = new Result<>();
		try {
			Map<String,Object> map= new HashMap<>(2000);
			List<SysDepartTreeModel> list = sysDepartService.queryTreeByKeyWord(keyWord);
			//??????keyWord??????????????????
			LambdaQueryWrapper<SysUser> queryUser = new LambdaQueryWrapper<SysUser>();
			queryUser.eq(SysUser::getDelFlag,CommonConstant.DEL_FLAG_0);
			queryUser.and(i -> i.like(SysUser::getUsername, keyWord).or().like(SysUser::getRealname, keyWord));
			List<SysUser> sysUsers = this.sysUserService.list(queryUser);
			map.put("userList",sysUsers);
			map.put("departList",list);
			result.setResult(map);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}

	/**
	 * ????????????????????????????????????
	 *
	 * @param orgCode
	 * @return
	 */
	@GetMapping("/getDepartName")
	public Result<SysDepart> getDepartName(@RequestParam(name = "orgCode") String orgCode) {
		Result<SysDepart> result = new Result<>();
		LambdaQueryWrapper<SysDepart> query = new LambdaQueryWrapper<>();
		query.eq(SysDepart::getOrgCode, orgCode);
		SysDepart sysDepart = sysDepartService.getOne(query);
		result.setSuccess(true);
		result.setResult(sysDepart);
		return result;
	}

	/**
	 * ????????????id??????????????????
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/getUsersByDepartId")
	public Result<List<SysUser>> getUsersByDepartId(@RequestParam(name = "id") String id) {
		Result<List<SysUser>> result = new Result<>();
		List<SysUser> sysUsers = sysUserDepartService.queryUserByDepId(id);
		result.setSuccess(true);
		result.setResult(sysUsers);
		return result;
	}
}
