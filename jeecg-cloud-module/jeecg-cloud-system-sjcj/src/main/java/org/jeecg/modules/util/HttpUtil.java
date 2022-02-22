package org.jeecg.modules.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * @Author: test
 */
public class HttpUtil {

	/**
	 * 执行get请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @param header 请求头
	 * @return 返回结果
	 */
	public static String doGet(String url, Map<String, Object> params, Map<String, Object> header) {
		// 获取连接客户端工具
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String entityStr = null;
		CloseableHttpResponse response = null;
		try {
			/*
			 * 由于GET请求的参数都是拼装在URL地址后方，所以我们要构建一个URL，带参数
			 */
			URIBuilder uriBuilder = new URIBuilder(url);
			List<NameValuePair> list = new LinkedList<>();
			if (params != null) {
				for (String key : params.keySet()) {
					BasicNameValuePair param = new BasicNameValuePair(key, params.get(key).toString());
					list.add(param);
				}
			}
			uriBuilder.setParameters(list);
			// 根据带参数的URI对象构建GET请求对象
			HttpGet httpGet = new HttpGet(uriBuilder.build());
			/*
			 * 添加请求头信息
			 */
			if (header != null) {
				for (String key : header.keySet()) {
					httpGet.addHeader(key, header.get(key).toString());
				}
			}

			// 执行请求
			response = httpClient.execute(httpGet);
			// 获得响应的实体对象
			HttpEntity entity = response.getEntity();
			// 使用Apache提供的工具类进行转换成字符串
			entityStr = EntityUtils.toString(entity, "UTF-8");
		} catch (ClientProtocolException e) {
			System.err.println("Http协议出现问题");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("解析错误");
			e.printStackTrace();
		} catch (URISyntaxException e) {
			System.err.println("URI解析异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO异常");
			e.printStackTrace();
		} finally {
			// 释放连接
			if (null != response) {
				try {
					response.close();
					httpClient.close();
				} catch (IOException e) {
					System.err.println("释放连接出错");
					e.printStackTrace();
				}
			}
		}
		// 打印响应内容
		return entityStr;
	}

	/**
	 * 执行post请求
	 *
	 * @param url    请求地址
	 * @param params 请求参数
	 * @param header 请求头
	 * @return 请求返回信息
	 */
	public static String doPost(String url, Map<String, Object> params, Map<String, Object> header) {
		// 获取连接客户端工具
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String entityStr = null;
		CloseableHttpResponse response = null;
		try {
			// 创建POST请求对象
			HttpPost httpPost = new HttpPost(url);
			/*
			 * 添加请求参数
			 */
			// 创建请求参数
			List<NameValuePair> list = new LinkedList<>();
			if (params != null && params.size() > 0) {
				for (String key : params.keySet()) {
					BasicNameValuePair param = new BasicNameValuePair(key, params.get(key).toString());
					list.add(param);
				}
			}
			// 使用URL实体转换工具
			UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(list, "UTF-8");
			httpPost.setEntity(entityParam);
			/*
			 * 添加请求头信息
			 */
			if (header != null && header.size() > 0) {
				for (String key : header.keySet()) {
					httpPost.addHeader(key, header.get(key).toString());
				}
			}
			// 执行请求
			response = httpClient.execute(httpPost);
			// 获得响应的实体对象
			HttpEntity entity = response.getEntity();
			// 使用Apache提供的工具类进行转换成字符串
			entityStr = EntityUtils.toString(entity, "UTF-8");
		} catch (ClientProtocolException e) {
			System.err.println("Http协议出现问题");
			e.printStackTrace();
		} catch (ParseException e) {
			System.err.println("解析错误");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO异常");
			e.printStackTrace();
		} finally {
			// 释放连接
			if (null != response) {
				try {
					response.close();
					httpClient.close();
				} catch (IOException e) {
					System.err.println("释放连接出错");
					e.printStackTrace();
				}
			}
		}

		// 返回响应内容
		return entityStr;
	}
}
