package org.jeecg.modules.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.jeecg.modules.system.entity.SysSecretKey;
import org.jeecg.modules.system.service.ISysSecretKeyService;
import org.jeecg.modules.system.util.SecurityUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RabbitComponent(value = "deleteUserListener")
@RabbitListener(queues = "deleteUserQueue")
/**
 * @Author: test
 * */
public class DeleteUserListener {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ISysSecretKeyService sysSecretKeyService;

    @RabbitHandler
    public void onMessage(BaseMap baseMap, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        //获取所有系统的修改接口，并向各个接口推送消息
        List<SysSecretKey> sysSecretKeyList = sysSecretKeyService.list();
        String urlMsg = "";
        try {
            for (SysSecretKey sysSecretKey : sysSecretKeyList) {
                String deleteUrl = sysSecretKey.getApiDeleteUrl();
                urlMsg = deleteUrl;
                String secretKey = sysSecretKey.getSecretKey();
                String userInfo = JSON.toJSONString(baseMap);
                String secret = SecurityUtil.jiami(userInfo, secretKey);
                restTemplate.postForObject(deleteUrl, secret, String.class);
            }
        } catch (RestClientException e) {
            log.error("删除用户信息推送失败==>推送地址:{},推送数据:{}", urlMsg, baseMap);
            e.printStackTrace();
        }
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            log.error("删除用户消息确认失败==>推送地址:{},推送数据:{}", urlMsg, baseMap);
            e.printStackTrace();
        }
    }
}
