package org.jeecg.modules.vmserportexdevrelation.service.impl;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.CreateSerialPortResult;
import org.jeecg.modules.openstack.entity.TestSerialResult;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.entity.*;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.service.IEnvCustomizedService;
import org.jeecg.modules.vmserportexdevrelation.entity.VmSerportExdevRelation;
import org.jeecg.modules.vmserportexdevrelation.mapper.VmSerportExdevRelationMapper;
import org.jeecg.modules.vmserportexdevrelation.service.IVmSerportExdevRelationService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author yeyl
 */
@Service
public class VmSerportExdevRelationServiceImpl extends ServiceImpl<VmSerportExdevRelationMapper, VmSerportExdevRelation> implements IVmSerportExdevRelationService {
    @Resource
    IVmDesignService iVmDesignService;
    @Resource
    IEnvCustomizedService iEnvCustomizedService;

    @Resource
    OpenStackServiceExt openStackServiceExt;


    @Override
    public void createPortAndUsb(String createBy, EnvPlan envPlan, String handleId, String name) {
        //解析需要更新的虚拟机信息
        EnvPlanJson envPlanJson = JSON.parseObject(envPlan.getPlanJson(), EnvPlanJson.class);
        Map<String, List<EnvPlanNodes>> nodes = new HashMap<>(6);
        if (envPlanJson != null) {
            //获取type不同分类集合
            envPlanJson.getNodeList().forEach(n -> {
                   List<EnvPlanNodes> plans = nodes.get(n.getType());
                    if (plans == null || plans.isEmpty()) {
                        plans = new ArrayList<>();
                    }
                    plans.add(n);
                    nodes.put(n.getType(), plans);

            });
            List<EnvPlanLines> lineList = envPlanJson.getLineList();

            List<EnvPlanNodes> virtualList = nodes.get(CommonConstant.PLAN_TYPE_VIRTUAL);
            //port 串口
            List<EnvPlanNodes> portList = nodes.get(CommonConstant.PLAN_TYPE_PORT);
            //外部工装
            List<EnvPlanNodes> externalDevicesList = nodes.get(CommonConstant.PLAN_TYPE_EXTERNALDEVICES);
            //串口与外部工装要创建的集合
            List<VmSerportExdevRelation> vmSerPortExdevRelations = new ArrayList<>();

            virtualList.forEach(n -> {
                //判断虚拟机数量
                int vnNum = StringUtils.isNotEmpty(n.getVmNum()) ? Integer.parseInt(n.getVmNum()) : CommonConstant.DATA_INT_1;
                for (int i = 0; i < vnNum; i++) {
                    String vmCode =  n.getId() + (i + 1);
                    //查找这个虚拟机 存在 创建该虚拟机连得串口 和外部工装
                    QueryWrapper<VmDesign> vmDesignQueryWrapper = new QueryWrapper<>();
                    vmDesignQueryWrapper.eq(CommonConstant.VM_CODE, vmCode);
                    vmDesignQueryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
                    VmDesign one = iVmDesignService.getOne(vmDesignQueryWrapper);
                    lineList.forEach(m -> {
                        if (n.getId().equals(m.getFrom())) {
                            //串口
                            portList.forEach(p -> {
                                if (p.getId().equals(m.getTo())) {
                                    VmSerportExdevRelation vmSerportExdevRelation = getVmSerPortExdevRelation( createBy,envPlan, one, p);
                                    //设备类型 0串口 1 外部工装
                                    vmSerportExdevRelation.setEquipmentType(CommonConstant.DATA_STR_0);
                                    vmSerPortExdevRelations.add(vmSerportExdevRelation);

                                }
                            });
                            //外部工装
                            externalDevicesList.forEach(ex -> {
                                if (ex.getId().equals(m.getTo())) {
                                    VmSerportExdevRelation vmSerportExdevRelation = getVmSerPortExdevRelation(createBy,envPlan, one, ex);
                                    //设备类型 0串口 1 外部工装
                                    vmSerportExdevRelation.setEquipmentType(CommonConstant.DATA_STR_1);
                                    vmSerPortExdevRelations.add(vmSerportExdevRelation);
                                }
                            });
                        }
                    });
                }
            });
            if (vmSerPortExdevRelations.size() > 0) {
                this.saveBatch(vmSerPortExdevRelations);
            }
        }
    }

    @Override
    public boolean testSerIalPort(Integer port, String host, String id) {
        try{
            VmSerportExdevRelation vmSerportExdevRelation = this.getById(id);
            if (vmSerportExdevRelation==null
                    ||StringUtils.isBlank(vmSerportExdevRelation.getVmServerId())
                    ||StringUtils.isBlank(vmSerportExdevRelation.getServiceMode())

            ){
                return false;
            }
            boolean serverMode=false;
            if (CommonConstant.DATA_STR_1.equals(vmSerportExdevRelation.getServiceMode())){
                //服务端
                serverMode=true;
            }
            String testId = openStackServiceExt.testSerial(vmSerportExdevRelation.getVmServerId(), serverMode, port, host);
            if (StringUtils.isNotBlank(testId)){
                TestSerialResult testSerialResult = openStackServiceExt.getTestSerialResult(testId);
                if (testSerialResult!=null&&testSerialResult.isSuccess()){
                    return true;
                }
            }
        }catch (Exception e){
            log.error("调用测试串口接口异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean bindSerial(Integer port, String host, String id) {
        try{
            VmSerportExdevRelation vmSerportExdevRelation = this.getById(id);
            if (vmSerportExdevRelation==null
                    ||StringUtils.isBlank(vmSerportExdevRelation.getServiceMode())
                    ||StringUtils.isBlank(vmSerportExdevRelation.getVmServerId())

            ){
                return false;
            }
            boolean serverMode=false;
            if (CommonConstant.DATA_STR_1.equals(vmSerportExdevRelation.getServiceMode())){
                //服务端
                serverMode=true;
            }
            CreateSerialPortResult createSerialPortResult = openStackServiceExt.createSerialPort(vmSerportExdevRelation.getVmServerId(), serverMode, port, host);
            if (createSerialPortResult!=null){
               //修改串口
                vmSerportExdevRelation.setPortNumber(port);
                vmSerportExdevRelation.setDesAddress(host);
                //绑定
                vmSerportExdevRelation.setStatus(CommonConstant.DATA_STR_0);
                vmSerportExdevRelation.setOpenstackDeviceId(createSerialPortResult.getId());
                return this.updateById(vmSerportExdevRelation);
            }
        }catch (Exception e){
            log.error("调用创建串口接口异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean unbindSerial(String id) {
        try{
            VmSerportExdevRelation vmSerportExdevRelation = this.getById(id);
            if (vmSerportExdevRelation==null||StringUtils.isBlank(vmSerportExdevRelation.getOpenstackDeviceId())){
                return false;
            }
            Boolean isUnbind = openStackServiceExt.deleteSerial(vmSerportExdevRelation.getOpenstackDeviceId());
            if (isUnbind){
                //未绑定
                vmSerportExdevRelation.setStatus(CommonConstant.DATA_STR_1);
                vmSerportExdevRelation.setOpenstackDeviceId(null);
                return this.updateById(vmSerportExdevRelation);
            }
        }catch (Exception e){
            log.error("调用删除串口接口异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean bindUsb(String openstackHost,String openstackHostId, String openstackDeviceId, String openstackBoundUsb, String id) {
        try{
            VmSerportExdevRelation vmSerportExdevRelation = this.getById(id);
            if (vmSerportExdevRelation==null||StringUtils.isBlank(vmSerportExdevRelation.getVmServerId())){
                return false;
            }
            //校验usb 一个usb这个时间只能绑定一个虚拟机  一个虚拟机可以绑定多个usb
            QueryWrapper<VmSerportExdevRelation> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq(CommonConstant.DATA_STRING_IDEL, CommonConstant.DATA_INT_IDEL_0);
            //状态 已绑定
            queryWrapper.eq(CommonConstant.DATA_STRING_SERVER_STATUS, CommonConstant.DATA_STR_0);
            //外部工装
            queryWrapper.eq(CommonConstant.DATA_STRING_EQUIPMENT_TYPE, CommonConstant.DATA_STR_1);
            queryWrapper.eq(CommonConstant.DATA_STRING_OPENSTACK_DEVICE_ID,openstackDeviceId);
            List<VmSerportExdevRelation> vmSerportExdevRelations = this.list(queryWrapper);
            //其他虚拟机解绑
             if (vmSerportExdevRelations.size()>0){

//                vmSerportExdevRelations.forEach(re -> {
//                    re.setStatus(CommonConstant.DATA_STR_1);
//                    re.setOpenstackDeviceId(null);
//                });

                 log.error("请解绑其他虚拟机上usb");
                 return false;
            }
            //解绑
//            if(openStackServiceExt.unbindukey(openstackDeviceId)) {
//                this.updateBatchById(vmSerportExdevRelations);
//            }else {
//                log.error("设备解绑异常：" +openstackDeviceId);
//                return  false;
//            }

            //绑定
            boolean isSucceed = openStackServiceExt.bindukey(vmSerportExdevRelation.getVmServerId(), openstackDeviceId);
            if (isSucceed){
                //修改usb
                vmSerportExdevRelation.setOpenstackDeviceId(openstackDeviceId);
                vmSerportExdevRelation.setOpenstackBoundUsb(openstackBoundUsb);
                vmSerportExdevRelation.setOpenstackHost(openstackHost);
                vmSerportExdevRelation.setOpenstackHostId(openstackHostId);
                //绑定
                vmSerportExdevRelation.setStatus(CommonConstant.DATA_STR_0);
                return this.updateById(vmSerportExdevRelation);
            }
        }catch (Exception e){
            log.error("调用绑定usb接口异常,原因:" + e.getMessage());
            return false;
        }
        log.error("绑定usb接口返回失败");
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public boolean unbindUsb(String id) {
        try{
            VmSerportExdevRelation vmSerportExdevRelation = this.getById(id);
            if (vmSerportExdevRelation==null||StringUtils.isBlank(vmSerportExdevRelation.getOpenstackDeviceId())){
                return false;
            }
            boolean isSuccess = openStackServiceExt.unbindukey(vmSerportExdevRelation.getOpenstackDeviceId());
            if (isSuccess){
                //解绑成功改成未绑定
                vmSerportExdevRelation.setStatus(CommonConstant.DATA_STR_1);
                vmSerportExdevRelation.setOpenstackDeviceId(null);
                return this.updateById(vmSerportExdevRelation);
            }
        }catch (Exception e){
            log.error("调用解绑USB接口异常,原因:" + e.getMessage());
            return false;
        }
        return false;
    }

    private VmSerportExdevRelation getVmSerPortExdevRelation(String createBy ,EnvPlan envPlan, VmDesign one, EnvPlanNodes p) {
        VmSerportExdevRelation vmSerportExdevRelation = new VmSerportExdevRelation();
        //环境名称
        vmSerportExdevRelation.setEnvId(one.getPlanId());
        vmSerportExdevRelation.setEnvName(one.getPlanName());
        EnvCustomized env = iEnvCustomizedService.getById(one.getPlanId());
        vmSerportExdevRelation.setProjectId(env.getProjectId());
        vmSerportExdevRelation.setProjectName(env.getProjectNames());
        //规划名称
        vmSerportExdevRelation.setPlanId(envPlan.getId());
        vmSerportExdevRelation.setPlanName(envPlan.getName());
        vmSerportExdevRelation.setVmId(one.getId());
        vmSerportExdevRelation.setVmName(one.getVmName());
        vmSerportExdevRelation.setVmServerId(one.getServerId());
        //服务模式0 客户端 1 服务端
        vmSerportExdevRelation.setServiceMode(CommonConstant.DATA_STR_0);
        //设备名称
        vmSerportExdevRelation.setEquipmentName(p.getName());
        vmSerportExdevRelation.setIdel(CommonConstant.DATA_INT_IDEL_0);
        //状态0 绑定 1 未绑定
        vmSerportExdevRelation.setStatus(CommonConstant.DATA_STR_1);
        vmSerportExdevRelation.setCreateBy(createBy);
        return vmSerportExdevRelation;
    }
}
