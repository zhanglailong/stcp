package org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @Author: test
 * */
public class UploadUtil {
	
	public static String httpClientUploadFile(String url, File file) {
		StringBuffer resultBuffer;
		CloseableHttpClient httpclient=HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 构建请求参数
		BufferedReader br = null;
		try {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			//设置请求的编码格式  
			entityBuilder.setCharset(StandardCharsets.UTF_8);
			entityBuilder.addBinaryBody("file", file);
		    HttpEntity reqEntity =entityBuilder.build();
		    httpPost.setEntity(reqEntity);
		    
			CloseableHttpResponse response = httpclient.execute(httpPost);
			 //从状态行中获取状态码  
		     String responsecode = String.valueOf(response.getStatusLine().getStatusCode());
			// 读取服务器响应数据
			resultBuffer = new StringBuffer();

			br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8));
			String temp;
			while ((temp = br.readLine()) != null) {
				resultBuffer.append(temp);
			}
			if(resultBuffer.length()==0){
				resultBuffer.append("上传文件异常，响应码：").append(responsecode);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignored) {
				}
			}
		}
		return resultBuffer.toString();
	}
}
