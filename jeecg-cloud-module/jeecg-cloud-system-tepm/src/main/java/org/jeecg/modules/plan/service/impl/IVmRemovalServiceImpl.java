package org.jeecg.modules.plan.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.jeecg.modules.openstack.entity.Hypervisors;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.ext.BasedInterfaceServiceExt;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.entity.VmRemoval;
import org.jeecg.modules.plan.mapper.VmRemovalMapper;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.plan.service.IVmRemovalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
  * @author hxs
  * 虚拟机迁移
  * 2021-07-19
  * V1.0
 */
@Slf4j
@Service
public class IVmRemovalServiceImpl extends ServiceImpl<VmRemovalMapper, VmRemoval> implements IVmRemovalService {
    @Resource
    private IVmRemovalService iVmRemovalService;
    @Resource
    private BasedInterfaceServiceExt basedInterfaceServiceExt;
    @Resource
    private IVmDesignService vmDesignService;
    @Resource
    private IStackQueueService iStackQueueService;
    /**
     * 虚拟机迁移
     * @param id 虚拟机表主键 id
     * @param vmId 虚拟机id
     * @param force 是否强制迁移
     * @param targetHostName 目标物理机名称
     * @param description 备注
     * @param type 0 热迁移  1 冷迁移
     * @return true
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Boolean getVmRemoval(String id,String vmId, Boolean force, String targetHostName,String description,Integer type) {
        try {
            //查询虚拟机当前的状态
            VmDesign vmDesigns = vmDesignService.getById(id);
            VmRemoval vmRemoval = new VmRemoval();
            LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
            //冷迁移 type:1   前后物理机名称不能一样
            if (!vmDesigns.getStatus().equals(ServerStatusEnum.MIGRATING.getStatus()) || !vmDesigns.getStatus().equals(ServerStatusEnum.ERROR.getStatus())){
            if (type.equals(CommonConstant.DATA_INT_1) && !vmDesigns.getHostName().equals(targetHostName)){
                //调用迁移接口
                if (basedInterfaceServiceExt.getVmRemoval(vmId,force,targetHostName,type)){
                    //虚拟机迁移表新增数据
                        vmRemoval.setCreateBy(sysUser.getUsername());
                        vmRemoval.setIdel(CommonConstant.DATA_INT_IDEL_0);
                        vmRemoval.setCreateTime(new Date());
                        //原物理机名称
                        vmRemoval.setPrimevalHostName(vmDesigns.getHostName());
                        //迁移后物理机名称
                        vmRemoval.setTargetHostName(targetHostName);
                        vmRemoval.setVmId(vmId);
                        vmRemoval.setStatus(ServerStatusEnum.MIGRATING);
                        vmRemoval.setDescription(description);
                        vmRemoval.setManner(CommonConstant.DATA_INT_1);
                        iVmRemovalService.save(vmRemoval);
                        //修改虚拟机表数据
                        vmDesigns.setId(id);
                        vmDesigns.setHostName(targetHostName);
                        vmDesigns.setStatus(ServerStatusEnum.MIGRATING);
                        vmDesignService.updateById(vmDesigns);
                    if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_1, vmRemoval.getId(), vmRemoval.getVmId(), ServerStatusEnum.MIGRATING.toString())){
                        return true;
                    }
                }
            }else {
                //选择的物理机和原始的不能一样  并且虚拟机不能是 关机的状态
                if (!vmDesigns.getHostName().equals(targetHostName) && !ServerStatusEnum.SHUTOFF.equals(vmDesigns.getStatus())){
                    if (basedInterfaceServiceExt.getVmRemoval(vmId,force,targetHostName,type)){
                            //虚拟机迁移表新增数据
                            vmRemoval.setCreateBy(sysUser.getUsername());
                            vmRemoval.setIdel(CommonConstant.DATA_INT_IDEL_0);
                            vmRemoval.setCreateTime(new Date());
                            //原物理机名称
                            vmRemoval.setPrimevalHostName(vmDesigns.getHostName());
                            //迁移后物理机名称
                            vmRemoval.setTargetHostName(targetHostName);
                            vmRemoval.setVmId(vmId);
                            vmRemoval.setStatus(ServerStatusEnum.MIGRATING);
                            vmRemoval.setManner(CommonConstant.DATA_INT_0);
                            vmRemoval.setDescription(description);
                            iVmRemovalService.save(vmRemoval);
                            //修改虚拟机表数据
                            vmDesigns.setId(id);
                            vmDesigns.setHostName(targetHostName);
                            vmDesigns.setStatus(ServerStatusEnum.MIGRATING);
                            vmDesignService.updateById(vmDesigns);
                        if (iStackQueueService.saveOne(null, CommonConstant.DATA_INT_1, vmRemoval.getId(), vmRemoval.getVmId(), ServerStatusEnum.MIGRATING.toString())) {
                            return true;
                        }
                    }
                }
            }
            }
        } catch (Exception e) {
            log.error("虚拟机迁移异常,原因:"+e.getMessage());
            return false;
        }
        return null;
    }

    @Override
    public Hypervisors getHomeList() {
        return basedInterfaceServiceExt.getHomeList();
    }
}
