package org.jeecg.modules.scheduled;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 虚拟机 添加物理机名称
 * @author hxs
 * @version V1.0
 * @date 2021/7/19
 */
@Slf4j
@Component
public class VmScheduled {

    private boolean running;
    @Resource
    IVmDesignService vmDesignService;
    @Resource
    private OpenStackServiceExt openStackServiceExt;

    /**
     * 定时处理队列消息
     */
//    @Scheduled(cron = "0/30 * * * * ?")
//    public synchronized void handleVm() {
//        log.info("Timed processing of queue messages");
//        if (running) {
//            return;
//        }
//        running = true;
//        List<VmDesign> vmDesignList = vmDesignService.getHandleVm();
//        List<VmDesign> vmDesigns = new ArrayList<>();
//        if (vmDesignList != null && vmDesignList.size() > 0){
//            vmDesignList.forEach(vmDesign -> {
//                //调用统一接口查询虚拟机的信息，取出物理机名称 存入虚拟机表
//                JSONObject resultJson = openStackServiceExt.getVirIpAndStatus(vmDesign.getOpenVmId());
//                if (resultJson != null) {
//                    //获取物理机名称
//                    String hostName = resultJson.get(CommonConstant.OS_EXT_SRV_ATTR_HOST).toString();
//                    if (StringUtils.isNotBlank(hostName)){
//                        vmDesign.setHostName(hostName);
//                    }
//                }
//                vmDesigns.add(vmDesign);
//            });
//            try {
//                if (vmDesigns.size()!=0) {
//                    vmDesignService.updateBatchById(vmDesigns);
//                }
//            } catch (Exception e) {
//                log.error("虚拟机物理机名称保存异常:" + e.getMessage());
//            }
//        }
//        running = false;
//    }
}
