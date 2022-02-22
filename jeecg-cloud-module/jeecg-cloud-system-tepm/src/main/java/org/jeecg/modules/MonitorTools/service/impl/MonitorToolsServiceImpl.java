package org.jeecg.modules.MonitorTools.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.MonitorTools.entity.MonitorTools;
import org.jeecg.modules.MonitorTools.mapper.MonitorToolsMapper;
import org.jeecg.modules.MonitorTools.service.IMonitorToolsService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 测试工具主服务表
 * @Author: jeecg-boot
 * @Date: 2021-02-01
 * @Version: V1.0
 */
@Service
@Slf4j
public class MonitorToolsServiceImpl extends ServiceImpl<MonitorToolsMapper, MonitorTools> implements IMonitorToolsService {

    @Override
    @Async
    public void startTools(MonitorTools monitorTools) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
            map.add("jmx", "jmx");
            String resultData = restTemplate.postForObject("http://" + monitorTools.getDomain() + ":9050/socket/client/tools/start", map, String.class);
            JSONObject resultJSONObj = JSONObject.parseObject(resultData);
            log.info("jmeter工具日志:" + resultData);
            if (resultJSONObj.get("result").toString().equals("500")) {
                monitorTools.setResultStatus("3");
            } else {
                monitorTools.setResultStatus("2");
            }
            monitorTools.setToolLog(resultJSONObj.get("result").toString());
            this.updateById(monitorTools);
        } catch (Exception e) {
            log.info("启动工具异常:" + e.getMessage());
            monitorTools.setResultStatus("3");
            monitorTools.setToolLog("启动工具异常:" + e.getMessage());
            this.updateById(monitorTools);
        }
    }
}
