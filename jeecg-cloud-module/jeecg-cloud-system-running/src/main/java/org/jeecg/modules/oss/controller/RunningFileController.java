package org.jeecg.modules.oss.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.RestUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.RunningUploadForm;
import org.jeecg.modules.oss.service.IRunningUploadFormService;
import org.jeecg.modules.util.FtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;


/**
 * @author Administrator
 *
 * */
@Slf4j
@RestController
@RequestMapping("/running/oss/file")
public class RunningFileController {

	@Autowired
	private IRunningUploadFormService ossService;

	@Autowired
	private FtpUtil ftpUtil;

	@Autowired
	private ISysBaseAPI sysBaseApi;

	@Value(value = "${jeecg.path.upload}")
	private String uploadpath;

	/**
	 * 本地：local minio：minio 阿里：alioss
	 */
	@Value(value="${jeecg.uploadType}")
	private String uploadType;

	/**
	 * 系统桶名
	 */
	@Value(value="${jeecg.minio.bucket-system}")
	private String customBucket;

	@GetMapping("/getFileInfos")
	public Result<?> getFileInfos(String id){

		return Result.ok(ossService.getById(id));
	}


	@RequestMapping(value = "/ftpDownloadById", method = RequestMethod.GET)
	public void ftpDownload(HttpServletRequest request, HttpServletResponse response, String id) throws IOException {
		String fileName = ossService.getById(id).getUrl();
		try {
			ftpUtil.downloadFileChop(fileName,response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件上传统一方法(仅支持minio/ftp)
	 * @param request
	 * @return
	 */
	@PostMapping(value = "/upload")
	public Result<?> upload(HttpServletRequest request) {
		Result result = new Result<>();
		String savePath = "";
		String bizPath = request.getParameter("biz");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		// 获取上传文件对象
		MultipartFile file = multipartRequest.getFile("file");
		//dq add start 增加上传到ftp服务器的逻辑,只有测试工具可执行文件会上传到ftp,其它的保持原来逻辑
		//是否上传到ftp
		Boolean isUploadToFtp = Boolean.valueOf(request.getParameter("isUploadToFtp"));

		if(isUploadToFtp){
			savePath = uploadFtp(file);
		}else{
			savePath = CommonUtils.upload(file, bizPath, CommonConstant.UPLOAD_TYPE_MINIO,customBucket);
		}

		if(oConvertUtils.isNotEmpty(savePath)){

			RunningUploadForm uploadForm = new RunningUploadForm(null,savePath,isUploadToFtp?3:2,file.getOriginalFilename(),file.getSize(),new Date(),
					JwtUtil.getUserNameByToken(request));
			ossService.save(uploadForm);

			return Result.ok(uploadForm);
//			result.setMessage(savePath);
//			result.setResult(new String[]{savePath,file.getOriginalFilename()});
//			result.setSuccess(true);
			//result.setResult(file.getSize());
		}else {
			result.setMessage("上传失败！");
			result.setSuccess(false);
			//result.setResult(file.getSize());
		}
		return result;
	}

	private String uploadFtp(MultipartFile file){
		try{
			String savePath = "";

			//文件原名
			String originalFilename = file.getOriginalFilename();
			//上传到ftp服务器的文件名
			//String ftpFileName = originalFilename.substring(0, originalFilename.lastIndexOf(".")) + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.indexOf("."));

			savePath = String.format("%s_%s",System.currentTimeMillis(),originalFilename);
			boolean isUploadOk = ftpUtil.uploadToFtp(file.getInputStream(), savePath, false);

			if(isUploadOk){
				return savePath;
			}
			else
			{
				return null;
			}
		}catch (Exception e){
			return null;
		}
	}



}
