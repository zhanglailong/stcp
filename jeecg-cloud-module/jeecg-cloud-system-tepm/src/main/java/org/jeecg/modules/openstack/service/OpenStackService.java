package org.jeecg.modules.openstack.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口转发
 *
 * @author zlf
 * @version V1.0
 * @date 2021/4/14
 */
@Service
@Slf4j
public class OpenStackService {
    @Resource
    private RestTemplate restTemplate;
    private static final Map<String, String> T_MAP = new HashMap<>();

    /**
     * token
     */
    @Value(value = "${openstack.token}")
    public String token;
    @Value(value = "${openstack.userName}")
    public String userName;
    @Value(value = "${openstack.password}")
    public String password;

    /**
     * 获取token
     *
     * @return 布尔值
     */
    public boolean getToken() {
        HttpHeaders headers = getHeaders(CommonConstant.DATA_INT_0);
        MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
        pMap.add("grant_type", "password");
        pMap.add("username", userName);
        pMap.add("password", password);
        String resultData = postHeadersT(token, headers, pMap, HttpMethod.POST);
        try {
            if (StringUtils.isNotBlank(resultData)) {
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty()) {
                    if (StringUtils.isNotBlank(parseObject.get(CommonConstant.REST_TEMPLATE_ACCESS_TOKEN).toString())
                            && StringUtils.isNotBlank(parseObject.get("expires_at").toString())) {
                        T_MAP.clear();
                        T_MAP.put("access_token", parseObject.get("access_token").toString());
                        T_MAP.put("expires_at", parseObject.get("expires_at").toString());
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            log.error("json返回数据异常:" + e.getMessage());
            return true;
        }
        return true;
    }

    /**
     * 获取Headers
     *
     * @param type 0为token 1为其他
     * @return HttpHeaders 头部信息
     */
    public HttpHeaders getHeaders(int type) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(mediaType);
        //0为token 1为其他
        if (CommonConstant.DATA_INT_1 == type) {
            if (!StringUtils.isNotBlank(T_MAP.get(CommonConstant.REST_TEMPLATE_ACCESS_TOKEN)) || computeData()) {
                if (getToken()) {
                    return null;
                }
            }
            headers.set("Authorization", String.format("%s %s", "Bearer", T_MAP.get(CommonConstant.REST_TEMPLATE_ACCESS_TOKEN)));
        } else {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        }
        headers.add("accept", "application/json");
        return headers;
    }

    /**
     * 计算token有效期
     *
     * @return 布尔值
     */
    public boolean computeData() {
        if (StringUtils.isNotEmpty(T_MAP.get(CommonConstant.REST_TEMPLATE_EXPIRES_AT))) {
            DateTime exTime = new DateTime(T_MAP.get(CommonConstant.REST_TEMPLATE_EXPIRES_AT), DatePattern.NORM_DATETIME_FORMAT);
            DateTime newDate = DateUtil.offsetHour(new Date(), CommonConstant.DATA_INT_12);
            if (exTime.dayOfMonth() > newDate.dayOfMonth()) {
                log.info("token有效期正常");
                return false;
            }
            if (exTime.dayOfMonth() == newDate.dayOfMonth() && exTime.hour(true) > newDate.hour(true)) {
                log.info("token有效期正常");
                return false;
            }
        }
        return true;
    }

    /**
     * 发送数据
     *
     * @param url     请求接口地址
     * @param headers 请求头
     * @param t       请求体参数
     * @param type    请求方式
     * @return 返回值
     */
    public String postHeadersT(String url, HttpHeaders headers, Object t, HttpMethod type) {
        try {
            log.info("restTemplate接口,url:" + url);
            HttpEntity<Object> entity;
            if (t != null) {
                entity = new HttpEntity<>(t, headers);
            } else {
                entity = new HttpEntity<>(headers);
            }
            ResponseEntity<String> responseEntity;
            //将请求头部和参数合成一个请求
            responseEntity = restTemplate.exchange(url, type, entity, String.class);
            //执行HTTP请求，将返回的结构使用ResultVO类格式化
            String resultData = responseEntity.getBody();
            log.info("返回数据：" + JSON.toJSONString(resultData));
            if (StringUtils.isNotBlank(resultData)) {
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty()) {
                    Object codeObj = parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE);
                    if (codeObj != null && codeObj.toString().equals(CommonConstant.DATA_STR_0)) {
                        return resultData;
                    }
                    //token 放行
                    Object accessTokenObj = parseObject.get(CommonConstant.REST_TEMPLATE_ACCESS_TOKEN);
                    if (accessTokenObj != null && StringUtils.isNotBlank(accessTokenObj.toString())){
                        return resultData;
                    }
                }
            }
            getToken();
            return null;
        } catch (Exception e) {
            log.error("发送数据异常,url为" + url + "异常原因为:" + e);
            return null;
        }
    }
}
