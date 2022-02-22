package org.jeecg.modules.sjcj.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.sjcj.collectiondatamanagement.entity.CollectionDataManagement;
import org.jeecg.modules.sjcj.collectiondatamanagement.service.ICollectionDataManagementService;
import org.jeecg.modules.sjcj.file.entity.FileView;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;
import org.jeecg.modules.sjcj.resultdataanalysis.service.IResultDataAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidExpiresRangeException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.messages.Item;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feilijin
 *
 */
@Api(tags = "记录当前已开启的代理")
@Controller
@RequestMapping("/fileView")
@Slf4j
@Configuration
public class FileViewManager {
	
	@Autowired
	private IResultDataAnalysisService resultDataAnalysisService;

	@Autowired
	private ICollectionDataManagementService collectionDataManagementService;

	@Value(value = "${jeecg.minio.minio-url}")
	private String minioUrl;
	@Value(value = "${jeecg.minio.minio-name}")
	private String minioName;
	@Value(value = "${jeecg.minio.minio-pwd}")
	private String minioPass;
	@Value(value = "${jeecg.minio.bucket-sjcj}")
	private String bucketName;

	private static final String JMETERID = "1342659707554361346";

	private static final String LRID = "1342659301919027201";

	private static final int SIZE=1024;

	private MinioClient minioClient = null;

	public static final String SEPARATOR = "/";

	private ArrayList<String> idArray = new ArrayList<>(Arrays.asList("1341647180284678146","1342658577659195394",
			"1342658900054372353","1342659460316917762","1342659955639054337","1342660167866642433","1342660509505286145"));

	/**
	 * 获取路径下文件列表
	 * 
	 * @param path
	 * @return
	 */
	@GetMapping(value = "/list")
	@ResponseBody
	public Result<?> getFileViewList(@RequestParam(name = "path", required = true) String path) {
		String objectName = getObjName(path);
		if (objectName.isEmpty()) {
			Result.error("请求路径有误");
		}
		List<FileView> items = new ArrayList<FileView>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			MinioClient minioClient = getMinioClient();
			Iterable<io.minio.Result<Item>> listObjects = minioClient.listObjects(bucketName, objectName);
			Iterator<io.minio.Result<Item>> iterator = listObjects.iterator();

			while (iterator.hasNext()) {
				Item item = iterator.next().get();
				FileView fileView = new FileView();
				String objName = item.objectName();
				objName = objName.replace(objectName, "");
				if (objName.indexOf(SEPARATOR) >= 0) {
					String[] filenames = objName.split(SEPARATOR);
					if (filenames.length >= 1) {
						boolean isCunZai = false;
						for (FileView fileView2 : items) {
							if (filenames[0].equals(fileView2.getFileName())) {
								isCunZai = true;
							}
						}
						if (!isCunZai) {
							fileView.setFileCreateTime("");
							fileView.setFileName(filenames[0]);
							fileView.setFilePath(minioUrl + SEPARATOR + "minio" + SEPARATOR + bucketName + SEPARATOR
									+ objectName + filenames[0] + SEPARATOR);
							fileView.setFileSize("");
							fileView.setFileType("1");
							items.add(fileView);
						}
					}
				} else {
					fileView.setFileName(objName);
					fileView.setFileType("2");
					fileView.setFileCreateTime(format.format(item.lastModified()));
					fileView.setFileSize(formatFileSize(item.objectSize()));
					fileView.setFilePath(
							minioUrl + SEPARATOR + "minio" + SEPARATOR + bucketName + "/" + objectName + objName);
					items.add(fileView);
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidBucketNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InsufficientDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ErrorResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InternalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Result.ok(items);

//		List<FileView> fileList = new ArrayList<FileView>();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		File file = new File(path);
//
//		if (file.isDirectory()) {
//			File[] listFiles = file.listFiles();
//
//			for (File file2 : listFiles) {
//
//				if (file2.isDirectory()) {
//					FileView fileView = new FileView();
//					fileView.setFileName(file2.getName());
//					fileView.setFilePath(file2.getAbsolutePath());
//					fileView.setFileType("1");
//					fileList.add(fileView);
//				} else {
//					FileView fileView = new FileView();
//					fileView.setFileName(file2.getName());
//					fileView.setFilePath(file2.getAbsolutePath());
//					fileView.setFileSize((file2.length() / 1024) + "KB");
//					fileView.setFileCreateTime(format.format(file2.lastModified()));
//					fileView.setFileType("2");
//					fileList.add(fileView);
//				}
//			}
//		} else {
//			FileView fileView = new FileView();
//			fileView.setFileName(file.getName());
//			fileView.setFilePath(file.getAbsolutePath());
//			fileView.setFileSize((file.length() / 1024) + "KB");
//			fileView.setFileCreateTime(format.format(file.lastModified()));
//			fileView.setFileType("2");
//			fileList.add(fileView);
//		}
//
//		return Result.ok(fileList);
	}

	/**
	 * 删除文件
	 *
	 * @param deletePath
	 * @return
	 */
	@GetMapping(value = "/delete")
	@ResponseBody
	public Result<?> deleteFile(@RequestParam(name = "deletePath", required = true) String deletePath) {
		File file = new File(deletePath);
		if (file.exists()) {
			deleteFile(file);
		}
		return Result.ok();
	}

	/**
	 * 上传文件
	 *
	 * @param multipartFile
	 * @return
	 */
	@PostMapping("/upload")
	@ResponseBody
	public Result<?> upload(@RequestParam("file") MultipartFile multipartFile,
			@RequestParam("savePath") String savePath) {
		Result<String> result = new Result<String>();
		if (savePath.isEmpty()) {
			return result.error500("保存路径不存在");
		} else {
			try {
				String originalFilename = multipartFile.getOriginalFilename();
				multipartFile.transferTo(new File(savePath + File.separator + originalFilename));
				result.success("上传成功！");
			} catch (Exception ex) {
				log.info(ex.getMessage(), ex);
				result.error500("上传失败");
			}
			return result;
		}

	}

	@RequestMapping(value = "/downFile", method = { RequestMethod.GET, RequestMethod.POST })
	public void downFile(HttpServletResponse response, @RequestParam("filePath") String filePath) {
		Result<String> result = new Result<String>();

		File file = new File(filePath);
		String fileName = file.getName();
		FileInputStream bis = null;
		OutputStream os = null;
		try {
			response.setHeader("content-type", "application/octet-stream");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));

			byte[] buff = new byte[1024];

			os = response.getOutputStream();
			bis = new FileInputStream(file);

			int readTmp = 0;
			while ((readTmp = bis.read(buff)) != -1) {
				os.write(buff, 0, readTmp);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.error500("文件下载失败");
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					result.error500("文件下载失败");
				}
			}
		}

//		return result.success("文件下载成功");
	}

	/**
	 * 通过文件路径获取minio文件外部链接
	 *
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/getObjectURL", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Result<?> getObjectUrl(@RequestParam("filePath") String filePath) {
		Result<String> result = new Result<String>();
		String url = "";
		String objectName = getObjName(filePath);
		// 文件不是空
		if (!objectName.isEmpty() || objectName.endsWith(SEPARATOR)) {
			MinioClient minioClient = getMinioClient();
			try {
				url = minioClient.presignedGetObject(bucketName, objectName, 432000);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (InvalidBucketNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (InsufficientDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (InternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (InvalidExpiresRangeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.error500("获取外部链接失败");
				return result;
			}
		} else {
			result.error500("获取Minio文件名称失败");
			return result;
		}

		result.setResult(url);
		return result;
	}

	/**
	 * 通过文件路径获取minio文件输入流
	 *
	 * @param filePath
	 * @return
	 */
	@RequestMapping(value = "/getFileStream", method = { RequestMethod.GET, RequestMethod.POST })
	public InputStream getFileStream(String filePath) {
		InputStream stream = null;
		String objectName = getObjName(filePath);
		// 文件不是空
		if (!objectName.isEmpty()) {
			MinioClient minioClient;
			try {
				minioClient = getMinioClient();
				stream = minioClient.getObject(bucketName, objectName);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBucketNameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InsufficientDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ErrorResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InternalException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return stream;
	}

	@GetMapping(value = "/analysisUrl")
	@ResponseBody
	public Result<?> analysisUrl(@RequestParam(name = "path") String path, @RequestParam(name = "toolId") String toolId,
								 @RequestParam(name = "agentIp") String agentIp, @RequestParam(name = "projectId") String projectId,
								 @RequestParam(name = "taskId") String taskId, @RequestParam(name = "caseId") String caseId,
								 @RequestParam(name = "collectionDataManagementId") String collectionDataManagementId) throws Exception {
		Result<String> result = null;
		ResultDataAnalysis resultDataAnalysis;
		CollectionDataManagement entity = new CollectionDataManagement();
		entity.setId(collectionDataManagementId);
		// 更具工具处理路径
		if (idArray.contains(toolId)) {
			// LDRA TESTBED
			result=new Result<>(true,"分析数据成功",path);
		}else if (JMETERID.equals(toolId)) {
			// JMeter
			// result的目录
			String fileUrl = path.replace("/minio", "").concat("result").concat(SEPARATOR);
			// 调用接口将路径和工具ID传入service方法
			resultDataAnalysis=new ResultDataAnalysis(fileUrl,agentIp,projectId,toolId);
			try {
				resultDataAnalysisService.add(resultDataAnalysis);
				entity.setAbnormalInformation("1");
				collectionDataManagementService.updateById(entity);
				result=new Result<>(true,"分析数据成功",resultDataAnalysis.getId());
			} catch (IOException e) {
				entity.setAbnormalInformation("2");
				collectionDataManagementService.updateById(entity);
				return result.error500("分析数据时失败");
			}
		} else if (LRID.equals(toolId)) {
			// LoadRunner
			// 找result下一级目录名
			String objectName = getObjName(path);
			objectName = objectName.concat("result").concat(SEPARATOR);
			List<String> fileUrls = new ArrayList<String>();
			MinioClient minioClient = getMinioClient();
			Iterable<io.minio.Result<Item>> listObjects;
			try {
				listObjects = minioClient.listObjects(bucketName, objectName);
				Iterator<io.minio.Result<Item>> iterator = listObjects.iterator();
				while (iterator.hasNext()) {
					Item item = iterator.next().get();
					String fileName = item.objectName().replace(objectName, "");
					if (fileName.indexOf(SEPARATOR) >= 0) {
						String[] filenames = fileName.split(SEPARATOR);
						if (filenames.length >= 1) {
							boolean isCunZai = false;
							for (String fileUrl : fileUrls) {
								if (fileUrl.indexOf(filenames[0]) >= 0) { isCunZai = true; }
							}
							if (!isCunZai) { fileUrls.add(path.replace("/minio", "") + "result" + SEPARATOR + filenames[0] + SEPARATOR); }
						}
					}
				}
			} catch (XmlPullParserException | InvalidKeyException | InvalidBucketNameException
					| NoSuchAlgorithmException | InsufficientDataException | NoResponseException
					| ErrorResponseException | InternalException | IOException e) {
				log.info(e.getMessage());
				return result.error500("分析失败");
			}
			if (fileUrls.size() <= 0) {
				return result.error500("未找到指定采集结果文件");
			}
			// 调用接口将路径和工具ID传入service方法
			resultDataAnalysis=new ResultDataAnalysis(fileUrls.get(0),agentIp,projectId,toolId);
			try {
				resultDataAnalysisService.add(resultDataAnalysis);
				entity.setAbnormalInformation("1");
				collectionDataManagementService.updateById(entity);
				result=new Result<>(true,"分析数据成功",resultDataAnalysis.getId());
			} catch (IOException e) {
				entity.setAbnormalInformation("2");
				collectionDataManagementService.updateById(entity);
				return result.error500("分析数据时失败");
			}
		} else {
			return result.error500("未找到解析工具");
		}
		return result;
	}

	/**
	 * 删除文件
	 *
	 * @param file
	 */
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFile(files[i]);
			}
		}
		file.delete();
	}

	/**
	 * 获取文件夹路径
	 *
	 * @param url
	 * @return
	 */
	private String getObjName(String url) {
		String objName = "";
		String beforUrl = minioUrl + SEPARATOR + "minio" + SEPARATOR + bucketName + SEPARATOR;
		if (url.indexOf(beforUrl) >= 0) {
			objName = url.replace(beforUrl, "");
		}
		return objName;
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	private String formatFileSize(long fileS) {
		String fileSizeString = "0B";
		if (fileS == 0) {
			return fileSizeString;
		}
		if (fileS < SIZE) {
			fileSizeString = (fileS) + "B";
		} else if (fileS < SIZE*SIZE) {
			fileSizeString = (fileS / 1024) + "KB";
		} else if (fileS < SIZE*SIZE*SIZE) {
			fileSizeString = (fileS / 1048576) + "MB";
		} else {
			fileSizeString = (fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 获取minioClient
	 * 
	 * @return
	 */
	private MinioClient getMinioClient() {
		if (minioClient == null) {
			try {
				minioClient = new MinioClient(minioUrl, minioName, minioPass);
			} catch (InvalidEndpointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return minioClient;
	}

}
