package org.jeecg.modules.tools.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.tools.entity.*;
import org.jeecg.modules.tools.service.ILicensePackageService;
import org.jeecg.modules.tools.service.IRunningToolsLicenseService;
import org.jeecg.modules.tools.service.IRunningToolsLicensemonitorService;
import org.jeecg.modules.tools.service.IRunningToolsListService;
import org.jeecg.modules.tools.util.CloudUtil;
import org.jeecg.modules.util.FtpUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import reactor.core.publisher.CloudFlux;

/**
 * @Description: ???????????????
 * @Author: jeecg-boot
 * @Date:   2021-07-28
 * @Version: V1.0
 */
@Api(tags="???????????????")
@RestController
@RequestMapping("/tools/licensePackage")
@Slf4j
public class LicensePackageController extends JeecgController<LicensePackage, ILicensePackageService> {
	@Autowired
	private ILicensePackageService licensePackageService;

	@Autowired
	private IRunningToolsLicenseService licenseService;

	@Autowired
	private IRunningToolsListService toolsService;

	@Autowired
	private FtpUtil ftpUtil;

	@Autowired
	private IRunningToolsLicensemonitorService monitorService;

	@GetMapping(value = "/getLicenseStatus")
	public Result<?> getLicenseStatus(){

		QueryWrapper<LicensePackage> wrapper = new QueryWrapper<>();
		wrapper.eq("def","1");
		//??????????????????
		List<LicensePackage> list = licensePackageService.list(wrapper);
		//?????????
		List<RunningToolsLicense> licList = licenseService.list();
		//?????????
		List<RunningToolsList> toolList = toolsService.list();
		//???????????????
		List<RunningToolsLicensemonitor> monList = monitorService.list();
		List<JSONObject> res = licList.stream().map(item->{
			JSONObject json = new JSONObject();
			String name = getTool(item.getToolsId(),toolList).getToolsName();
			List<RunningToolsLicensemonitor> monitor =  getLicInUse(monList,item.getId());

			json.put("key",name);
 			json.put("title",name);
			json.put("id",item.getId());

 			JSONObject count = new JSONObject();

			LicensePackage packages = getPackage(item.getId(),list);
 			Integer total =packages ==null?0:packages.getCount();


			JSONObject used = new JSONObject();
			used.put("title",String.format("???????????????%s/%s",monitor.size(),total));
			used.put("key",String.format("%s-used",name));
			used.put("id",item.getId());
//			used.put("data",monitor);
			JSONArray jarr = new JSONArray();
//			jarr.add(count);
			jarr.add(used);

			json.put("children",jarr);

			return json;
		}).collect(Collectors.toList());

		return Result.ok(res);
	}

	@GetMapping("/check")
	public Result<?> check(LicensePackage licPackage, HttpServletRequest req){
		Result result = Result.ok();
		QueryWrapper<LicensePackage> wrapper = new QueryWrapper<>();

		wrapper.eq("version",licPackage.getVersion()).eq("license_id",licPackage.getLicenseId());
		if(!StringUtils.isEmpty(licPackage.getId())){
			wrapper.ne("id",licPackage.getId());
		}

		result.setResult(licensePackageService.count(wrapper));
		return result;
	}

	private List<RunningToolsLicensemonitor> getLicInUse(List<RunningToolsLicensemonitor> list,String licId){
		List<RunningToolsLicensemonitor> res = list.stream().filter(item->item.getLicenseId().equals(licId)).collect(Collectors.toList());
		if(res == null){
			res = new ArrayList<>();
		}
		return res;
	}

	private RunningToolsList getTool(String toolId,List<RunningToolsList> toolList){

		for (RunningToolsList tool : toolList) {
			if(tool.getId().equals(toolId)){
				return tool;
			}
		}
		return null;

	}

	private LicensePackage getPackage(String licId,List<LicensePackage> packList){

		for (LicensePackage licensePackage : packList) {
			if(licensePackage.getLicenseId().equals(licId)){
				return licensePackage;
			}
		}
		return null;

	}

	@GetMapping("/getUkeys")
	public Result<?> getUkeys(HttpServletResponse response,HttpServletRequest req){
		return Result.ok(CloudUtil.keyArr);
	}

	@RequestMapping("/downLic")
	public void downLic(HttpServletResponse response,String id,String toolId) throws IOException {

		if(StringUtil.isEmpty(id)&&StringUtil.isEmpty(toolId)){
			return;
		}

		if(StringUtil.isEmpty(id)){
			QueryWrapper<RunningToolsLicense> licenseWrapper = new QueryWrapper<>();
			licenseWrapper.eq("tools_id",toolId);
			id = licenseService.getOne(licenseWrapper).getId();
		}


		QueryWrapper<LicensePackage> wrapper = new QueryWrapper<>();
		wrapper.eq("license_id",id).eq("def",1);
		LicensePackage LicPack = licensePackageService.getOne(wrapper);
		//??????????????????
		if(!StringUtil.isEmpty(LicPack.getLicFile())){

			//??????????????????
			String tempDownPath = String.format("%s/downTemp/%s",System.getProperty("user.dir"),LicPack.getVersion());
			File file = new File(tempDownPath);
			file.mkdirs();

			//????????????
			String[] licFiles = LicPack.getLicFile().split(",");
			for (String fil:licFiles){
				ftpUtil.downFileChop(fil,tempDownPath.concat("/").concat(getName(fil)));
			}

			//??????mac?????????????????????readme
			if(!StringUtil.isEmpty(LicPack.getMacList())){

				FileWriter fw = new FileWriter(new File(tempDownPath.concat("/").concat("readMe.txt")));

				JSONArray jArray = JSONArray.parseArray(LicPack.getMacList());
				jArray.stream().forEach(js->{
					try {
						fw.write(String.format("??????:%s",((JSONObject)js).get("key")));
						fw.write("\r\n");
						fw.write(String.format("????????????:%s",((JSONObject)js).get("name")));
						fw.write("\r\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				fw.flush();
				fw.close();

			}

			//???????????????
			String tempZipPath = String.format("%s/downZipTemp/%s.zip",System.getProperty("user.dir"),LicPack.getVersion());
			File zipFile = new File(tempZipPath);
			zipFile.getParentFile().mkdirs();
			try{
				FileOutputStream out = new FileOutputStream(zipFile);
				ftpUtil.toZip(tempDownPath,response.getOutputStream(),true);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			zipFile.delete();
			delAllFile(tempDownPath);

		}

	}

	// ????????????????????????????????????
	// param path ???????????????????????????
	private boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// ?????????????????????????????????
				delFolder(path + "/" + tempList[i]);// ?????????????????????
				flag = true;
			}
		}
		return flag;
	}

	private void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // ???????????????????????????
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // ??????????????????
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private String getName(String str){
		String[] arr = str.split("/");
		return arr[arr.length-1];
	}
	
	/**
	 * ??????????????????
	 *
	 * @param licensePackage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "???????????????-??????????????????")
	@ApiOperation(value="???????????????-??????????????????", notes="???????????????-??????????????????")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(LicensePackage licensePackage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<LicensePackage> queryWrapper = QueryGenerator.initQueryWrapper(licensePackage, req.getParameterMap());
		Page<LicensePackage> page = new Page<LicensePackage>(pageNo, pageSize);
		IPage<LicensePackage> pageList = licensePackageService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	private void flushDef(String id,String version,Integer num){

		UpdateWrapper<LicensePackage> wrapper = new UpdateWrapper();
		wrapper.set("def","-1").eq("license_id",id);
		licensePackageService.update(wrapper);

		UpdateWrapper<RunningToolsLicense> lwrapper = new UpdateWrapper();
		lwrapper.eq("id",id).set("package_in_use",version).set("tools_license_num",num);
		licenseService.update(lwrapper);

	}
	
	/**
	 *   ??????
	 *
	 * @param licensePackage
	 * @return
	 */
	@AutoLog(value = "???????????????-??????")
	@ApiOperation(value="???????????????-??????", notes="???????????????-??????")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody LicensePackage licensePackage) {
		if(licensePackage.getDef()==1){
			flushDef(licensePackage.getLicenseId(),licensePackage.getVersion(),licensePackage.getCount());
		}
		licensePackageService.save(licensePackage);
		return Result.ok("???????????????");
	}
	
	/**
	 *  ??????
	 *
	 * @param licensePackage
	 * @return
	 */
	@AutoLog(value = "???????????????-??????")
	@ApiOperation(value="???????????????-??????", notes="???????????????-??????")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody LicensePackage licensePackage) {

		//???????????????????????????
		String id = licensePackage.getId();
		Integer def = licensePackageService.getById(id).getDef();

		if(licensePackage.getDef()==1){
			flushDef(licensePackage.getLicenseId(),licensePackage.getVersion(),licensePackage.getCount());
		}else if(def == 1){
			UpdateWrapper<RunningToolsLicense> lwrapper = new UpdateWrapper();
			lwrapper.eq("id",id).set("package_in_use","").set("tools_license_num",0);
			licenseService.update(lwrapper);
		}

		licensePackageService.updateById(licensePackage);
		return Result.ok("????????????!");
	}
	
	/**
	 *   ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????-??????id??????")
	@ApiOperation(value="???????????????-??????id??????", notes="???????????????-??????id??????")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		licensePackageService.removeById(id);
		return Result.ok("????????????!");
	}
	
	/**
	 *  ????????????
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "???????????????-????????????")
	@ApiOperation(value="???????????????-????????????", notes="???????????????-????????????")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.licensePackageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("??????????????????!");
	}
	
	/**
	 * ??????id??????
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "???????????????-??????id??????")
	@ApiOperation(value="???????????????-??????id??????", notes="???????????????-??????id??????")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		LicensePackage licensePackage = licensePackageService.getById(id);
		if(licensePackage==null) {
			return Result.error("?????????????????????");
		}
		return Result.ok(licensePackage);
	}

    /**
    * ??????excel
    *
    * @param request
    * @param licensePackage
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LicensePackage licensePackage) {
        return super.exportXls(request, licensePackage, LicensePackage.class, "???????????????");
    }

    /**
      * ??????excel????????????
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, LicensePackage.class);
    }

}
