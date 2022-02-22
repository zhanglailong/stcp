package org.jeecg.modules.vmserportexdevrelation.service;
import org.jeecg.modules.plan.entity.EnvPlan;
import org.jeecg.modules.vmserportexdevrelation.entity.VmSerportExdevRelation;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author yeyl
 */
public interface IVmSerportExdevRelationService extends IService<VmSerportExdevRelation> {


    /**
     * 创建串口与外部工装
     * @param createBy 创建人
     * @param envPlan envPlan 规划
     * @param handleId  环境id
     * @param name 环境名称
     */
    void createPortAndUsb(String createBy, EnvPlan envPlan, String handleId, String name);

    /**
     * 测试串口
     * @param port  端口
     * @param host 目标地址
     * @param id 串口id
     * @return boolean
     */
    boolean testSerIalPort(Integer port, String host, String id);
    /**
     * 绑定串口
     * @param port  端口
     * @param host 目标地址
     * @param id 串口id
     * @return boolean
     */
    boolean bindSerial(Integer port, String host, String id);

    /**
     * 解绑串口
     * @param id 表id
     * @return boolean
     */
    boolean unbindSerial(String id);

    /**
     * 绑定Usb
     * @param openstackBoundUsb usb名称
     * @param openstackHost usb主机
     * @param openstackDeviceId usb设备id
     * @param openstackHostId 主机Id
     * @param id tepm_vm_serport_exdev_relation表id
     * @return boolean
     */
    boolean bindUsb(String openstackHost,String openstackHostId, String openstackDeviceId, String openstackBoundUsb, String id);

    /**
     * 解绑USB
     * @param id  表id
     * @return boolean
     */
    boolean unbindUsb(String id);

}
