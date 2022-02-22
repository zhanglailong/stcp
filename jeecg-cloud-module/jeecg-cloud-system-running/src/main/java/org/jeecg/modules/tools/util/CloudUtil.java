package org.jeecg.modules.tools.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jodd.util.StringUtil;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jeecg.modules.message.util.HttpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Data
public class CloudUtil {

    private static String reqToken;

    private static String name="bjtest";

    private static String pass="bjtest";

    private static String token;

    private static final String TOKENURL = "http://221.226.112.238:6005/api/identity/token";

    private static final String LOGURL = "http://221.226.112.238:6005/login";

    private static final String UKEY = "http://221.226.112.238:6005/api/device/ukey?all_projects=true";

    public static JSONArray keyArr = new JSONArray();



    //获取cookie
    private static void getCookies() throws IOException {

        CookieStore httpCookieStore = new BasicCookieStore();

        HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore).build();
        // 创建Post请求
        HttpGet httpGet = new HttpGet(LOGURL);

        // 从响应模型中获取响应实体
        CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(httpGet);
        HttpEntity responseEntity = response.getEntity();


        Header[] headers = response.getHeaders("Set-Cookie");

        List<org.apache.http.cookie.Cookie> cookies = httpCookieStore.getCookies();

        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            System.out.println("cookies: key= "+ name + "  value= " + value);
        }

    }

    private static JSONObject post(String url, Map<String,String> param){
        JSONObject res = null;
        // 响应模型
        CloseableHttpResponse response = null;
        HttpClient clint = HttpClientBuilder.create().build();
        try {

            // 创建Post请求
            HttpPost httpPost = new HttpPost(url);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            for(String key :param.keySet()){
                builder.addTextBody(key,param.get(key));
            }

            HttpEntity entity = builder.build();
            if(!StringUtil.isEmpty(token))
                httpPost.setHeader("Authorization",token);
            httpPost.setEntity(entity);

            // 由客户端执行(发送)Post请求
            response = (CloseableHttpResponse) clint.execute(httpPost);

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                res = JSONObject.parseObject( EntityUtils.toString(responseEntity));
//                token = res.getString("access_token");
//                System.out.println(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private static JSONObject get(String url){
        JSONObject res = null;
        // 响应模型
        CloseableHttpResponse response = null;
        HttpClient clint = HttpClientBuilder.create().build();
        try {

            // 创建Post请求
            HttpGet httpGet = new HttpGet(url);
            if(!StringUtil.isEmpty(token))
                httpGet.setHeader("Authorization",token);
            // 由客户端执行(发送)Post请求
            response = (CloseableHttpResponse) clint.execute(httpGet);

            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                res = JSONObject.parseObject( EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    //获取token
    public static void setToken(){
        if(StringUtil.isEmpty(token)){
            Map<String,String> map = new HashMap<>();
            map.put("username",name);
            map.put("password",pass);
            JSONObject json = post(TOKENURL,map);
            token = String.format("%s %s",json.getString("token_type"),json.getString("access_token"));
        }
    }

    public static void getUkey(){

        if(StringUtil.isEmpty(token)){
            setToken();
        }

        String url=null;
        url = UKEY;
        JSONObject json = get(url);
        keyArr = JSONArray.parseArray(JSONObject.parseObject(json.getString("data")).getString("ukeys"));
        if(keyArr == null){
            keyArr = new JSONArray();
        }
    }

    public static void main(String[] args) throws IOException {
        setToken();
        getUkey();
        //getCookies();
    }



}
