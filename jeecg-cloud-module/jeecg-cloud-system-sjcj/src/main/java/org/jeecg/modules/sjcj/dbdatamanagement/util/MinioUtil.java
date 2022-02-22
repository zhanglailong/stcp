package org.jeecg.modules.sjcj.dbdatamanagement.util;

import java.io.File;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.Data;

/**
 * minio文件上传工具类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jeecg")
/**
 * @Author: test
 * */
public class MinioUtil {

	private MinioClient minioClient;
	private Map<String, String> minio;
	private String dbDataPath;
	private String bucketName="bucketName";

	/**
	 * 将文件上传至桶内
	 * 
	 * @param fileFullPath, 桶内存放路径
	 * @param realPath,     被上传文件本地路径
	 */
	public void uploadToBucket(String fileFullPath, String realPath) {
		try {
			initMinio(minio.get("minio_url"), minio.get("minio_name"), minio.get("minio_pass"));
			if (!minioClient.bucketExists(minio.get(bucketName))) {
                minioClient.makeBucket(minio.get(bucketName));
            }
			minioClient.putObject(minio.get("bucketName"), fileFullPath, realPath,
					this.getContentType(new File(realPath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private MinioClient initMinio(String minioUrl, String minioName, String minioPass) {
		if (null == minioClient) {
			try {
				minioClient = new MinioClient(minioUrl, minioName, minioPass);
			} catch (InvalidEndpointException | InvalidPortException e) {
				e.printStackTrace();
			}
		}
		return minioClient;
	}

	private String png=".png";
	private String js=".js";
	private String css=".css";
	/**获取文件的上传类型，图片格式为image/png,image/jpg等。非图片为application/octet-stream*/
	public String getContentType(File f) throws Exception {
		String contentType = new MimetypesFileTypeMap().getContentType(f);

		if (f.getName().lastIndexOf(png) != -1) {
			contentType = "image/png";
		}
		if (f.getName().lastIndexOf(js) != -1) {
			contentType = "text/javascript";
		}
		if (f.getName().lastIndexOf(css) != -1) {
			contentType = "text/css";
		}
		if (f.getName().lastIndexOf(png) != -1) {
            contentType = "image/png";
        }
		if (f.getName().lastIndexOf(js) != -1) {
            contentType = "text/javascript";
        }
		if (f.getName().lastIndexOf(css) != -1) {
            contentType = "text/css";
        }

		return contentType;
	}
}
