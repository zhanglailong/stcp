package org.jeecg.modules.plan.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.plan.entity.*;
import org.jeecg.modules.plan.mapper.EnvPlanMapper;
import org.jeecg.modules.plan.service.IEnvPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zlf
 * 环境设计
 * 2020-12-29
 * V1.0
 */
@Slf4j
@Service
public class EnvPlanServiceImpl extends ServiceImpl<EnvPlanMapper, EnvPlan> implements IEnvPlanService {

    @Resource
    private EnvPlanMapper envPlanMapper;

    @Override
    public EnvPlan selectById(String id) {
        return envPlanMapper.selectById(id);
    }

    @Override
    public List<EnvPlan> getPlanListByIds(List<String> ids) {
        return envPlanMapper.getPlanListByIds(ids);
    }

    @Override
    public Boolean getAnalysisEnvPlan(EnvPlanJson envPlanJson, String planJson) {
        Map<String, List<EnvPlanNodes>> nodes = new HashMap<>(16);
        EnvPlan plan = new EnvPlan();
        BeanUtils.copyProperties(envPlanJson, plan);
        //获取type不同分类集合
        envPlanJson.getNodeList().forEach(n -> {
            List<EnvPlanNodes> plans = nodes.get(n.getType());
            if (plans == null || plans.isEmpty()) {
                plans = new ArrayList<>();
            }
            plans.add(n);
            nodes.put(n.getType(), plans);
        });
        List<EnvPlanNodes> netList = nodes.get(CommonConstant.PLAN_TYPE_NET);
        List<EnvPlanNodes> childNetList = nodes.get(CommonConstant.PLAN_TYPE_CHILDNET);
        List<EnvPlanNodes> virtualList = nodes.get(CommonConstant.PLAN_TYPE_VIRTUAL);
        List<EnvPlanNodes> routeList = nodes.get(CommonConstant.PLAN_TYPE_ROUTE);
        //裸机
        List<EnvPlanNodes> bareVirtualList = nodes.get(CommonConstant.BARE_VIRTUAL);
        //port 串口
        List<EnvPlanNodes> portList = nodes.get(CommonConstant.PLAN_TYPE_PORT);
        //外部工装
        List<EnvPlanNodes> externalDevicesList = nodes.get(CommonConstant.PLAN_TYPE_EXTERNALDEVICES);
        if (netList == null || netList.isEmpty()) {
            netList = Collections.emptyList();
        }
        if (childNetList == null || childNetList.isEmpty()) {
            childNetList = Collections.emptyList();
        }
        if (virtualList == null || virtualList.isEmpty()) {
            virtualList = Collections.emptyList();
        }
        if (routeList == null || routeList.isEmpty()) {
            routeList = Collections.emptyList();
        }
        if (bareVirtualList == null || bareVirtualList.isEmpty()) {
            bareVirtualList = Collections.emptyList();
        }


        if (portList == null || portList.isEmpty()) {
            portList = Collections.emptyList();
        }
        if (externalDevicesList == null || externalDevicesList.isEmpty()) {
            externalDevicesList = Collections.emptyList();
        }
        //统计
        log.info("网络数：" + netList.size());
        log.info("子网数：" + childNetList.size());
        log.info("路由数：" + routeList.size());
        log.info("虚拟机数：" + virtualList.size());
        log.info("裸机数：" + bareVirtualList.size());
        log.info("串口数：" + portList.size());
        log.info("外部工装数：" + externalDevicesList.size());

        plan.setPlanJson(planJson);
        plan.setNetNum(netList.size());
        plan.setChildNetNum(childNetList.size());
        plan.setVirtualNum(virtualList.size()+bareVirtualList.size());
        plan.setRouteNum(routeList.size());
        plan.setPortNum(portList.size());
        plan.setExternaldevicesNum(externalDevicesList.size());
        plan.setState(CommonConstant.DATA_INT_0);
        plan.setNodeList(JSON.toJSONString(envPlanJson.getNodeList()));
        plan.setLineList(JSON.toJSONString(envPlanJson.getLineList()));
        String openstackJson = getOpenstackJson(netList, childNetList, routeList, virtualList,envPlanJson.getLineList(),bareVirtualList);
        log.info("openstackJson:" + openstackJson);
        plan.setOpenstackJson(openstackJson);
        plan.setIdel(CommonConstant.DATA_INT_IDEL_0);
        log.info("plan:" + JSON.toJSONString(plan));
        return this.saveOrUpdate(plan);
    }

    private String getOpenstackJson(List<EnvPlanNodes> nets, List<EnvPlanNodes> childNets, List<EnvPlanNodes> routes, List<EnvPlanNodes> victuals,  List<EnvPlanLines> lineList,List<EnvPlanNodes> bareVirtualList) {
        //子网添加网络名称
        nets.forEach(n -> lineList.forEach(m -> {
            if (n.getId().equals(m.getFrom())) {
                childNets.forEach(k -> {
                    if (k.getId().equals(m.getTo())) {
                        k.setNetName(n.getName());
                    }
                });
            }
        }));
        //虚拟机添加子网名称
        childNets.forEach(n -> lineList.forEach(m -> {
            if (n.getId().equals(m.getFrom())) {
                victuals.forEach(k -> {
                    if (k.getId().equals(m.getTo())) {
                        k.setChildNetName(n.getName());
                        k.setNetName(n.getNetName());
                    }
                });
            }
        }));
        StringBuffer sb = new StringBuffer();
        sb.append("{").append("\"heat_template_version\": \"").append("2013-05-23").append("\"").append(",").append("\"resources\": {");
        //添加网络
        /*nets.forEach(n -> sb.append("\"").append(n.getName()).append("\"").append(":{\"type\":\"OS::Neutron::Net\",\"properties\":{\"port_security_enabled\":\"True\"}},"));*/
        //添加子网
        /*childNets.forEach(n -> {
            sb.append("\"").append(n.getName()).append("\"").append(":{\"type\":\"OS::Neutron::Subnet\",\"properties\":{\"network_id\":{\"get_resource\":\"");
            sb.append(n.getNetName()).append("\"").append("},\"cidr\":\"").append(org.apache.commons.lang3.StringUtils.isEmpty(n.getCidr()) ? "10.0.0.0/24" : n.getCidr()).append("\"");
            sb.append(",\"enable_dhcp\":\"True\"}},");
        });*/
        List<String> floatList = new ArrayList<>();
        //添加路由
        /*routes.forEach(n -> lineList.forEach(m -> {
            sb.append("\"router-").append(n.getId()).append("\":{\"type\":\"OS::Neutron::Router\",\"properties\":{\"name\":\"test-router\",\"external_gateway_info\":{\"network\":\"public-network\"}}},");
            if (n.getId().equals(m.getTo())) {
                for (int i = 0; i < childNets.size(); i++) {
                    if (childNets.get(i).getId().equals(m.getFrom())) {
                        floatList.add(childNets.get(i).getId());
                        sb.append("\"router_interface").append(i + 1).append("\"");
                        sb.append(":{\"type\":\"OS::Neutron::RouterInterface\",\"properties\":{\"router_id\":{\"Ref\":\"router-").append(n.getId()).append("\"},\"subnet_id\":{\"Ref\":\"");
                        sb.append(childNets.get(i).getName()).append("\"}}},");
                    }
                }
            }
        }));*/
        //添加裸机
            bareVirtualList.forEach(n -> {
                //判断虚拟机数量
                int vnNum = StringUtils.isNotEmpty(n.getVmNum())?Integer.parseInt(n.getVmNum()):CommonConstant.DATA_INT_1;
                for (int i = 0; i < vnNum; i++) {
                    sb.append("\"volume-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\": \"OS::Cinder::Volume\",\"properties\": {\"volume_type\": \"__DEFAULT__\",\"size\": \"");
                    sb.append(StringUtils.isEmpty(n.getVirDisk()) ? "100" : n.getVirDisk()).append("\",\"image\": \"").append(n.getMirror()).append("\" }},");
                    sb.append("\"").append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"").append(":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":\"");
                    sb.append("alone-server-network").append("\"").append(",\"security_groups\":[\"default\"]}},");
                    sb.append("\"").append("Server-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\": \"OS::Nova::Server\",\"properties\": {\"block_device_mapping_v2\": [{\"volume_id\": {\"Ref\": \"volume-");
                    sb.append(n.getName()).append(n.getId()).append(i+1).append("\"},\"device_name\": \"vda\",\"boot_index\": 0, \"delete_on_termination\": \"True\"}],");
                    sb.append("\"flavor\": \"").append(org.apache.commons.lang3.StringUtils.isEmpty(n.getVirCpu()) ? "1" : n.getVirCpu()).append("c");
                    sb.append("_").append(StringUtils.isEmpty(n.getVirInner()) ? "1" : n.getVirInner()).append("g").append("_").append("100G");
                    sb.append("\", \"key_name\": \"oskey\", \"networks\": [{\"port\":{\"get_resource\": \"");
                    sb.append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"}}]}},");
                    if (floatList.size() >0){
                        for ( String floatId : floatList ){
                            for (EnvPlanLines envPlanLines : lineList){
                                if (floatId.equals(envPlanLines.getFrom()) && n.getId().equals(envPlanLines.getTo())){
                                    sb.append("\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIP\",\"properties\":{\"floating_network\":\"public-network\"}}");
                                    sb.append(",\"server_floating_ip_assoc-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIPAssociation\",\"properties\":{\"floatingip_id\":{\"get_resource\":\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\"},\"port_id\":{\"get_resource\":\"");
                                    sb.append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"}}},");
                                }
                            }
                        }
                    }
                }
            });
        //添加虚拟机
        victuals.forEach(n -> {
            //判断虚拟机数量
            int vnNum = StringUtils.isNotEmpty(n.getVmNum())?Integer.parseInt(n.getVmNum()):CommonConstant.DATA_INT_1;
            for (int i = 0; i < vnNum; i++) {
                sb.append("\"volume-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\": \"OS::Cinder::Volume\",\"properties\": {\"volume_type\": \"__DEFAULT__\",\"size\": \"");
                sb.append(StringUtils.isEmpty(n.getVirDisk()) ? "100" : n.getVirDisk()).append("\",\"image\": \"").append(n.getMirror()).append("\" }},");
                sb.append("\"").append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"").append(":{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":\"");
                sb.append(n.getNetName()).append("\"").append(",\"security_groups\":[\"default\"]}},");
                sb.append("\"").append("Server-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\": \"OS::Nova::Server\",\"properties\": {\"block_device_mapping_v2\": [{\"volume_id\": {\"Ref\": \"volume-");
                sb.append(n.getName()).append(n.getId()).append(i+1).append("\"},\"device_name\": \"vda\",\"boot_index\": 0, \"delete_on_termination\": \"True\"}],");
                sb.append("\"flavor\": \"").append(org.apache.commons.lang3.StringUtils.isEmpty(n.getVirCpu()) ? "1" : n.getVirCpu()).append("c");
                sb.append("_").append(StringUtils.isEmpty(n.getVirInner()) ? "1" : n.getVirInner()).append("g").append("_").append("100G");
                sb.append("\", \"key_name\": \"oskey\", \"networks\": [{\"port\":{\"get_resource\": \"");
                sb.append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"}}]}},");
                if (n.getFloating()){
                    sb.append("\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIP\",\"properties\":{\"floating_network\":\"public-network\"}}");
                    sb.append(",\"server_floating_ip_assoc-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIPAssociation\",\"properties\":{\"floatingip_id\":{\"get_resource\":\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\"},\"port_id\":{\"get_resource\":\"");
                    sb.append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"}}},");
                }
//                if (floatList.size() >0){
//                    for ( String floatId : floatList ){
//                       for (EnvPlanLines envPlanLines : lineList){
//                           if (floatId.equals(envPlanLines.getFrom()) && n.getId().equals(envPlanLines.getTo())){
//                               sb.append("\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIP\",\"properties\":{\"floating_network\":\"public-network\"}}");
//                               sb.append(",\"server_floating_ip_assoc-").append(n.getName()).append(n.getId()).append(i+1).append("\": {\"type\":\"OS::Neutron::FloatingIPAssociation\",\"properties\":{\"floatingip_id\":{\"get_resource\":\"floating_ip-").append(n.getName()).append(n.getId()).append(i+1).append("\"},\"port_id\":{\"get_resource\":\"");
//                               sb.append("port-").append(n.getName()).append(n.getId()).append(i+1).append("\"}}},");
//                           }
//                       }
//                    }
//                }
            }
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}}");
        return sb.toString();
    }
}
