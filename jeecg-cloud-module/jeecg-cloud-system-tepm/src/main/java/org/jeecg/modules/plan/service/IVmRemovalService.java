package org.jeecg.modules.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openstack.entity.Hypervisors;
import org.jeecg.modules.plan.entity.VmRemoval;


/**
 * @Description: 虚拟机迁移
 * @Author: jeecg-boot
 * @Date: 2021-07-19
 * @Version: V1.0
 */
public interface IVmRemovalService extends IService<VmRemoval> {
    /**
     * 虚拟机迁移
     * @param id 虚拟机表主键id
     * @param vmId 虚拟机id
     * @param force 是否强制迁移
     * @param targetHostName 目标物理机名称
     * @param description 备注
     * @param type 0 热迁移  1 冷迁移
     * @return true
     */
    Boolean getVmRemoval(String id, String vmId, Boolean force, String targetHostName,String description,Integer type);

    /**
     * 物理机列表
     * @return json
     */
    Hypervisors getHomeList();
}
