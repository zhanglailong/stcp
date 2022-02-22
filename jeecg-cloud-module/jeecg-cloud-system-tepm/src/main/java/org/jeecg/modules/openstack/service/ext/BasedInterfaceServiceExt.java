package org.jeecg.modules.openstack.service.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.*;
import org.jeecg.modules.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hxsi
 * @description 统一接口
 * @date 2021年05月10日 17:36
 */

@Service
@Slf4j
public class BasedInterfaceServiceExt extends OpenStackService {
    /**
     * 创建环境快照
     */
    @Value(value = "${openstack.createEnvStack}")
    public String createEnvStack;

    /**
     * 获取vnc界面
     */
    @Value(value = "${openstack.vnc}")
    public String vnc;

    /**
     * 获取虚拟机资源使用率
     */
    @Value(value = "${openstack.serversStats}")
    public String serversStats;

    /**
     * 获取镜像列表
     */
    @Value(value = "${openstack.imageList}")
    public String imageList;
    /**
     * 九宫格监控
     */
    @Value(value = "${openstack.multipleVnc}")
    public String multipleVnc;
    /**
     * 删除环境快照
     */
    @Value(value = "${openstack.deleteEnvSnapshots}")
    public String deleteEnvSnapshots;
    /**
     * 获取环境的事件
     */
    @Value(value = "${openstack.vncIncident}")
    public String vncIncident;
    /**
     * 删除环境
     */
    @Value(value = "${openstack.deleteEnv}")
    public String deleteEnv;
    /**
     * 环境从快照恢复
     */
    @Value(value = "${openstack.envRecover}")
    private String envRecover;
    /**
     * 环境挂起
     */
    @Value(value = "${openstack.suspend}")
    private String suspend;
    /**
     * 环境恢复
     */
    @Value(value = "${openstack.resume}")
    private String resume;
    /**
     * 删除虚拟机
     */
    @Value(value = "${openstack.deleteVm}")
    private String deleteVm;
    /**
     * 虚拟机挂起/恢复
     */
    @Value(value = "${openstack.suspendVm}")
    private String suspendVm;
    /**
     * 虚拟机迁移
     */
    @Value(value = "${openstack.vmRemoval}")
    private String vmRemoval;
    /**
     * 计算机节点列表
     */
    @Value(value = "${openstack.host}")
    private String host;

    /**
     * 创建环境快照
     *
     * @param stackId      环境id
     * @param snapshotName 环境快照的名称
     * @param stackId   type 0快照/1备份
     * @return 创建环境快照
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public Snapshots getSnapshots(String stackId, String snapshotName ,boolean incremental) {
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            pMap.put("name", snapshotName);
            pMap.put("incremental", incremental);
            String resultData = postHeadersT(createEnvStack.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
            Snapshots snapshots = new Snapshots();
            if (StringUtils.isNotBlank(resultData)) {
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty()
                        && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        && parseObject.containsKey(CommonConstant.DATA_STRING_DATA)) {
                    snapshots.setCode(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString());
                    if(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG)!=null) {
                        snapshots.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_ID)!=null) {
                        snapshots.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_ID).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_NAME)!=null) {
                        snapshots.setName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_NAME).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS)!=null) {
                        snapshots.setStatus(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS_REASON)!=null) {
                        snapshots.setStatusReason(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS_REASON).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.CREATION_TIME) !=null) {
                        snapshots.setTime(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.CREATION_TIME).toString());
                    }
                    }
                return snapshots;
            }
            return null;
        } catch (Exception e) {
            log.error("创建环境快照/备份超时：" + e.getMessage());
            return null;
        }
    }

    /**
     * 查询环境快照列表
     *
     * @param stackId 环境id
     * @return 快照列表
     */
    public JSONObject getSnapshotsList(String stackId) {
        String resultData = postHeadersT(createEnvStack.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET);
        if (StringUtils.isNotBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_INT_0 == parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE)) {
                return parseObject;
            }
        }
        return null;
    }

    /**
     * 环境从快照恢复
     *
     * @param stackId    环境id
     * @param snapshotId 环境快照id
     * @return 快照恢复
     */
    public String getRestore(String stackId, String snapshotId) {
        //请求获取数据
        String resultData = postHeadersT(envRecover.replace(CommonConstant.DATA_STRING_STACK_ID, stackId).replace(CommonConstant.DATA_STRING_SNAPSHOT_ID, snapshotId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.POST);
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            //有返回值 并且 code 为 0 代表成功
            if (parseObject != null && !parseObject.isEmpty() &&
                    CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                return parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString();
            }
        }
        return null;
    }

    /**
     * 获取虚拟机vnc界面
     *
     * @param serverId 虚拟机id
     * @return vnc数据
     */
    public String getVnc(String serverId) {
        //请求获取数据
        String resultData = postHeadersT(vnc.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID, serverId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.POST);
        if (StringUtils.isNotBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (parseObject != null && !parseObject.isEmpty()
                    && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                    && parseObject.containsKey(CommonConstant.DATA_STRING_DATA)) {
                String vncUrl = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_URL).toString();
                if (StringUtils.isNotEmpty(vncUrl)) {
                    return vncUrl;
                }

            }
        }
        return null;
    }

    /**
     * 获取虚拟机资源使用率
     *
     * @param serverId 虚拟机id
     * @return 资源使用率
     */
    public ResourceUtilization getServersStats(String serverId) {
        try {
            //判空
            ResourceUtilization resourceUtilization = JSON.parseObject(postHeadersT(serversStats.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID, serverId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET), ResourceUtilization.class);
            if (resourceUtilization != null) {
                return resourceUtilization;
            }
        } catch (Exception e) {
            log.error("getServersStats 异常：" + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * 镜像删除 + 删除环境 + 删除环境快照 + 虚拟机删除
     *
     * @param imageId    镜像id
     * @param stackId    环境id
     * @param snapshotId 环境快照id
     * @param serverId   虚拟机id
     * @param type       0:镜像删除 1:删除环境 2:删除环境快照 3:删除虚拟机
     * @return boolean
     */
    public Boolean deleteImages(String imageId, String stackId, String snapshotId, String serverId, String type) {
        String resultData = null;
        if (CommonConstant.DATA_STR_0.equals(type)) {
            //镜像删除
            resultData = postHeadersT(imageList + imageId, super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        } else if (CommonConstant.DATA_STR_1.equals(type)) {
            //环境删除
            resultData = postHeadersT(deleteEnv.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        } else if (CommonConstant.DATA_STR_2.equals(type)) {
            //删出环境快照
            resultData = postHeadersT(deleteEnvSnapshots.replace(CommonConstant.DATA_STRING_STACK_ID, stackId).replace(CommonConstant.DATA_STRING_SNAPSHOT_ID, snapshotId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        } else if (CommonConstant.DATA_STR_3.equals(type)) {
            //虚拟机删除
            resultData = postHeadersT(deleteVm.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID, serverId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        }
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                //删除成功
                return true;
            }
        }
        return null;
    }

    /**
     * 九宫格监控
     *
     * @param serverIds 虚拟机id列表
     * @return 九宫格监控
     */
    public MultipleVnc getMultipleVnc(String serverIds) {
        try {
            Map<String, Object> pMap = new HashMap<>(3);
            List<String> serverIdsList = Arrays.asList(serverIds.split(","));
            System.out.println(serverIdsList);
            pMap.put("server_ids", serverIdsList);
            MultipleVnc multipleVncs = JSON.parseObject(postHeadersT(multipleVnc, getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST), MultipleVnc.class);
            if (multipleVncs != null) {
                return multipleVncs;
            }
        } catch (Exception e) {
            log.error("九宫格监控 异常：" + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * 获取环境的事件
     *
     * @param stackId 环境id
     * @param status  用来过滤
     * @return 环境事件
     */
    public EventsList getStacksEvents(String stackId, String status) {
        try {
            EventsList eventsList = JSON.parseObject(postHeadersT(vncIncident.replace(CommonConstant.DATA_STRING_STACK_ID, stackId).replace(CommonConstant.DATA_STRING_STATUS, status), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET),EventsList.class);
            if (eventsList != null) {
                return eventsList;
            }
        } catch (Exception e) {
            log.error("getStacksEvents 异常：" + e.getMessage());
            return null;
        }

        return null;
    }


    /**
     *删除环境快照
     * @param stackId 环境Id
     * @param snapshotId 环境快照id
     * @return boolean
     */
    public Boolean deleteEnvSnapshots(String stackId, String snapshotId) {
       String resultData = postHeadersT(deleteEnvSnapshots.replace(CommonConstant.DATA_STRING_STACK_ID, stackId).replace(CommonConstant.DATA_STRING_SNAPSHOT_ID, snapshotId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                return true;
            }
        }
        return null;
    }
    /**
     * 环境挂起 + 环境挂起恢复
     *
     * @param stackId openstack栈环境id
     * @param type    0:环境挂起  1:环境挂起恢复
     * @return 挂起||恢复
     */
    public Boolean getSuspend(String stackId, String type) {
        String resultData = null;
        if (CommonConstant.DATA_STR_0.equals(type)) {
            //环境挂起 type : 0
            resultData = postHeadersT(suspend.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.POST);
        } else if (CommonConstant.DATA_STR_1.equals(type)) {
            //环境挂起恢复 type : 1
            resultData = postHeadersT(resume.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.POST);
        }
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                return true;
            }
        }
        return null;
    }

    /**
     * 虚拟机扩容
     * @param stackId 环境id
     * @param flavor 虚拟接规格
     * @param name 虚拟机名字
     * @param rollback 是否回滚 默认true
     * @param image 镜像id
     * @param size 磁盘大小
     * @return true
     */
    public Boolean getVmAlter(String stackId, String flavor ,String name, String rollback,String image,String size) {
        Map<String, Object> pMap = new HashMap<>(6);
        String openstackJson = openstackJson(image,flavor,size);
        pMap.put("name",name);
        pMap.put("template_content",openstackJson);
        pMap.put("rollback",rollback);
        String resultData = postHeadersT(deleteEnv.replace(CommonConstant.DATA_STRING_STACK_ID, stackId), super.getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.PUT);
        if (StringUtils.isNoneBlank(resultData)){
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                    &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                return true;
            }else {
                return false;
            }
        }
        return null;
    }

    public String openstackJson(String image,String flavor,String size){
        StringBuffer sb = new StringBuffer();
        sb.append("{").append("\"heat_template_version\":\"2013-05-23\",\"resources\":{\"port-server1\":");
        sb.append("{\"type\":\"OS::Neutron::Port\",\"properties\":{\"network_id\":\"alone-server-network\",\"security_groups\":[\"default\"]}},");
        sb.append("\"volume-server1\":{\"type\":\"OS::Cinder::Volume\",\"properties\":{\"volume_type\":\"__DEFAULT__\",");
        sb.append("\"size\":\"").append(size).append("\",\"image\":\"").append(image).append("\"}},");
        sb.append("\"Server1\":{\"type\":\"OS::Nova::Server\",\"properties\":{\"block_device_mapping_v2\":[{\"volume_id\":{\"Ref\":\"volume-server1\"},");
        sb.append("\"device_name\":\"vda\",\"boot_index\":0,\"delete_on_termination\":\"True\"}],\"flavor\":\"").append(flavor).append("\",\"networks\":[{\"port\":{\"get_resource\":\"port-server1\"}}]}}}}");
        return sb.toString();
    }

    /**
     * 虚拟机挂起 、恢复
     * @param serverId 虚拟机id
     * @param type 0 挂起  1 恢复
     * @return true
     */
    public Boolean getSuspendVm(String serverId,String type){
        String resultData = null;
        Map<String, Object> pMap = new HashMap<>(6);
        if (CommonConstant.DATA_STR_0.equals(type)) {
            //虚拟机挂起 type : 0 suspend:挂起 默认1
            pMap.put("suspend", CommonConstant.DATA_INT_1);
            resultData = postHeadersT(suspendVm.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID, serverId), super.getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        } else if (CommonConstant.DATA_STR_1.equals(type)) {
            //虚拟机恢复 type : 1  resume 恢复 默认1
            pMap.put("resume", CommonConstant.DATA_INT_1);
            resultData = postHeadersT(suspendVm.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID, serverId), super.getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        }
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                return true;
            }
        }
        return null;
    }
    /**
     * 虚拟机迁移
     * @param vmId 虚拟机id
     * @param force 是否强制迁移 默认 false
     * @param targetHostName 目标物理机名称
     * @return true
     */
    public Boolean getVmRemoval(String vmId,Boolean force,String targetHostName,Integer type){
        Map<String, Object> pMap = new HashMap<>(6);
        //冷迁移
        if (type.equals(CommonConstant.DATA_INT_1)){
            ColdMigration coldMigration = new ColdMigration();
            coldMigration.setHost(targetHostName);
            pMap.put("migrate", coldMigration);
        }else {
            //热迁移
            VmRemovalList vmRemovalList = new VmRemovalList();
            vmRemovalList.setHost(targetHostName);
            vmRemovalList.setForce(force);
            pMap.put("os_migrateLive", vmRemovalList);
        }
        String resultData = postHeadersT(vmRemoval.replace(CommonConstant.SERVICE_ID, vmId), super.getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                return true;
            }
        }
        return null;
    }
    /**
     *获取物理机列表
     * @return 镜像列表
     */
    public Hypervisors getHomeList(){
        try {
            MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
            Hypervisors hypervisors = JSON.parseObject(postHeadersT(host, getHeaders(CommonConstant.DATA_INT_1), pMap,HttpMethod.GET), Hypervisors.class);
            return hypervisors;
        }catch (Exception e){
            log.error("获取镜像列表接口异常，原因:"+e.getMessage());
            return null;
        }
    }

    /**
     * 虚拟机 快照、备份
     * @param vmId 虚拟机id
     * @param snapshotName 快照名称
     * @param incremental 0 快照 | 1 备份
     * @return true
     */
    public Snapshots vmSnapshot(String vmId, String snapshotName ,boolean incremental){
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            pMap.put("name", snapshotName);
            pMap.put("incremental", incremental);
            String resultData = postHeadersT(createEnvStack.replace(CommonConstant.DATA_STRING_STACK_ID, vmId), getHeaders(CommonConstant.DATA_INT_1), pMap, HttpMethod.POST);
            Snapshots snapshots = new Snapshots();
            if (StringUtils.isNotBlank(resultData)) {
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty()
                        && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        && parseObject.containsKey(CommonConstant.DATA_STRING_DATA)) {
                    snapshots.setCode(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString());
                    if(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG)!=null) {
                        snapshots.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_ID)!=null) {
                        snapshots.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_ID).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_NAME)!=null) {
                        snapshots.setName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_NAME).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS)!=null) {
                        snapshots.setStatus(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString());
                    }
                    if(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS_REASON)!=null) {
                        snapshots.setStatusReason(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVER_SNAPSHOT).get(CommonConstant.DATA_STRING_SERVER_STATUS_REASON).toString());
                    }
                }
                return snapshots;
            }
            return null;
        } catch (Exception e) {
            log.error("虚拟机快照/备份超时：" + e.getMessage());
            return null;
        }
    }
}
