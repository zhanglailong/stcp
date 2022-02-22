package org.jeecg.modules.message.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: test
 * */
public class HttpUtil {
	
	public static byte[] getBytes(String url) {
		// 创建restTemplate对象
		RestTemplate restTemplate = new RestTemplate();
		/**
		 * 请求地址 请求方式 请求实体类 请求返回类型
		 */
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url,
				HttpMethod.GET, null, byte[].class);
		// 获取entity中的数据
		return responseEntity.getBody();
	}

	/**
	 * 字符串参数
	 * 
	 * @param
	 * @return 返回请求结果
	 * @throws IOException 
	 */
	public static boolean getFileFromServer(String url, String filepath) throws IOException {
		// 创建restTemplate对象
		RestTemplate restTemplate = new RestTemplate();
		/**
		 * 请求地址 请求方式 请求实体类 请求返回类型
		 */
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
		// 获取entity中的数据
		byte[] body = responseEntity.getBody();
		// 创建输出流 输出到本地
		File file = new File(filepath);
		file.getParentFile().mkdirs();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(body);
		// 关闭流
		fileOutputStream.close();
		return true;
	}
	
	public static JSONObject getFromUrl(String url) {
		
		// 创建restTemplate对象
		RestTemplate restTemplate = new RestTemplate();
		/**
		 * 请求地址 请求方式 请求实体类 请求返回类型
		 */
		ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, byte[].class);
		// 获取entity中的数据
		byte[] body = responseEntity.getBody();
		
		
		
		return null;
	}

}
