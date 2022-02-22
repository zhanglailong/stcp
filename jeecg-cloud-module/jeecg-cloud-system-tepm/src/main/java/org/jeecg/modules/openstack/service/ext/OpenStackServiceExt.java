package org.jeecg.modules.openstack.service.ext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.R;
import org.jeecg.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.*;
import org.jeecg.modules.openstack.service.OpenStackService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.jeecg.common.api.vo.Result;

import java.util.*;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/5/10
 */
@Service
@Slf4j
public class OpenStackServiceExt extends OpenStackService {

    /**
     * 创建测试环境
     */
    @Value(value = "${openstack.createStack}")
    private String createStack;

    /**
     * 查询环境信息
     */
    @Value(value = "${openstack.searchStack}")
    private String searchStack;
    /**
     * 获取镜像列表
     */
    @Value(value = "${openstack.imageList}")
    private String imageList;
    /**
     * 获取环境的资源列表
     */
    @Value(value = "${openstack.stackList}")
    private String stackList;
    /**
     * 查询虚拟机信息
     */
    @Value(value = "${openstack.searchServer}")
    private String searchServer;
    /**
     * 从虚拟机创建镜像
     */
    @Value(value = "${openstack.foundImageServer}")
    private String foundImageServer;
    /**
     * 创建单独虚拟机
     */
    @Value(value = "${openstack.serverCompute}")
    private String serverCompute;

    /**
     * 测试虚拟机串口
     */
    @Value(value = "${openstack.testSerial}")
    private String testSerial;

    /**
     * 根据测试id获取测试串口结果
     */
    @Value(value = "${openstack.getTestSerialResult}")
    private String getTestSerialResult;

    /**
     * 创建串口
     */
    @Value(value = "${openstack.createSerial}")
    private String createSerial;
    /**
     * 删除串口
     */
    @Value(value = "${openstack.deleteSerial}")
    private String deleteSerial;

    /**
     *usb集合接口
     */
    @Value(value = "${openstack.ukeyList}")
    private String ukeyList;


    /**
     *串口列表
     */
    @Value(value = "${openstack.serialPortList}")
    private String serialPortList;

    /**
     * 绑定usb
     */
    @Value(value = "${openstack.bindukey}")
    private String bindukey;
    /**
     * 解绑usb
     */
    @Value(value = "${openstack.unbindukey}")
    private String unbindukey;

    /**
     * 获取单个镜像的信息
     */
    @Value(value = "${openstack.image}")
    private String image;

    /**
     * 获取单个镜像的信息
     */
    @Value(value = "${openstack.envIncident}")
    private String envIncident;
    /**
     * 获取网络列表
     */
    @Value(value = "${openstack.networks}")
    private String networks;
	/**
     * 获取网络列表
     */
    @Value(value = "${openstack.updateSubnets}")
    private String updateSubnets;
    /**
     * 获取虚拟机操作记录列表
     */
    @Value(value = "${openstack.vmActions}")
    private String vmActions;
    /**
     * 查询虚拟机信息
     */
    @Value(value = "${openstack.deleteVm}")
    private String deleteVm;
    /**
     * 编辑子网路由表
     */
    @Value(value = "${openstack.subnetIds}")
    private String subnetIds;
    /**
     * 获取环境的资源列表
     * @param stackId 环境id
     * @return 返回资源列表
     */
    public StackList getStackList(String stackId){
        return JSON.parseObject(postHeadersT(stackList.replace(CommonConstant.DATA_STRING_STACK_ID,stackId), getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET), StackList.class);
    }
    public JSONObject getImage(String imageId){
        String resultData = postHeadersT(image.replace(CommonConstant.IMAGE_ID,imageId), getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET);
        if (StringUtils.isNotBlank(resultData)){
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (parseObject!=null&&!parseObject.isEmpty()
                    && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                    &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                return parseObject;
            }
        }
        return null;
    }

    /**
     * 虚拟机操作记录列表
     * @param serverId 虚拟机id
     * @return VmAction
     */
    public VmAction getVmActions(String serverId){
        try {
            MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
            return JSON.parseObject(postHeadersT(vmActions.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID,serverId), getHeaders(CommonConstant.DATA_INT_1), pMap,HttpMethod.GET), VmAction.class);
        }catch (Exception e){
            log.error("获取虚拟机操作记录列表接口异常，原因:"+e.getMessage());
            return null;
        }
    }
    /**
     * 虚拟机信息列表
     * @param serverId 虚拟机id
     * @return VmAction
     */
    public VmInformation getVmInformation(String serverId){
            String resultData = postHeadersT(deleteVm.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID,serverId),getHeaders(CommonConstant.DATA_INT_1),null,HttpMethod.GET);
            if (StringUtils.isNotBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject!=null&&!parseObject.isEmpty()
                        && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    String id = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                            .getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_ID).toString();
                    String name = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                            .getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_SERVER_NAME).toString();
                    String status = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                            .getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString();
                    VmInformation vmInformation = new VmInformation();
                    vmInformation.setId(id);
                    vmInformation.setName(name);
                    vmInformation.setStatus(status);
                    return vmInformation;
                }
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
    public EventsList getStacksEvents(String stackId,String action, String status) {
        try {
            EventsList eventsList = JSON.parseObject(postHeadersT(envIncident.replace(CommonConstant.DATA_STRING_STACK_ID, stackId).replace(CommonConstant.ACTION, action).replace(CommonConstant.DATA_STRING_STATUS,status), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.GET),EventsList.class);
            if (CommonConstant.DATA_INT_IDEL_0.equals(eventsList.getCode())) {
                return eventsList;
            }
        } catch (Exception e) {
            log.error("getStacksEvents 异常：" + e.getMessage());
            return null;
        }

        return null;
    }

    /**
     * 获取虚拟机ip
     * @param serverId 虚拟机id
     * @return ip
     */
    public JSONObject getVirIpAndStatus(String serverId){
        String resultData = postHeadersT(searchServer+serverId,getHeaders(CommonConstant.DATA_INT_1),null,HttpMethod.GET);
        if (StringUtils.isNotBlank(resultData)){
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (parseObject!=null&&!parseObject.isEmpty()
                    && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                    &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                String addresses = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                        .getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_ADDRESS).toString();
                if (StringUtils.isNotEmpty(addresses)){
                    return parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                            .getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER);
                }
            }
        }
        return null;
    }

    /**
     * 创建测试环境
     * @param openstackJson json数据
     * @param planName  环境名称
     * @return 环境id
     */
    public String createPlan(String openstackJson,String planName){
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            pMap.put("name",planName);
            pMap.put("tags",null);
            pMap.put("template_content",openstackJson);
            pMap.put("rollback",true);
            String resultData = postHeadersT(createStack,getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNotBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject!=null&&!parseObject.isEmpty()
                        && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    String stackId = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                            .getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_ID).toString();
                    if (StringUtils.isNotEmpty(stackId)){
                        log.info("stackId:"+stackId);
                        return stackId;
                    }
                }
            }
            return CommonConstant.DATA_STR_1;
        }catch (Exception e){
            log.error("createPlan 异常："+e.getMessage());
            return CommonConstant.DATA_STR_2;
        }
    }

    /**
     * 根据stack id 查询环境信息
     * @param stackId 环境id
     * @return 环境状态&状态原因
     */
    public JSONObject searchStackById(String stackId){
        JSONObject resultJsonObj = new JSONObject();
        String resultData = postHeadersT(searchStack+stackId,super.getHeaders(CommonConstant.DATA_INT_1),null, HttpMethod.GET);
        if (StringUtils.isNotBlank(resultData)){
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (parseObject!=null&&!parseObject.isEmpty()
                    && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                    &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                String stackStatus = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                        .getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_STACK_STATUS).toString();
                String stackStatusReason = parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA)
                        .getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_STACK_STATUS_REASON).toString();
                if (StringUtils.isNotEmpty(stackStatus)){
                    resultJsonObj.put(CommonConstant.DATA_STRING_STACK,stackStatus);
                    resultJsonObj.put(CommonConstant.DATA_STRING_STACK_STATUS_REASON,StringUtils.isEmpty(stackStatusReason)?"":stackStatusReason);
                    resultJsonObj.put(CommonConstant.DATA_STRING_STACK_STATUS,stackStatus);
                    return resultJsonObj;
                }
            }
        }
        return null;
    }

    /**
     *获取usb列表
     * @return UkeyList
     */
    public UkeyList getUkeyList(){
        try {
            MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
            return JSON.parseObject(postHeadersT(ukeyList, getHeaders(CommonConstant.DATA_INT_1), pMap,HttpMethod.GET), UkeyList.class);
        }catch (Exception e){
            log.error("获取usb列表接口异常，原因:"+e.getMessage());
            return null;
        }
    }

    /**
     *获取串口列表
     * @return list
     */
    public SerialPortList getSerialPortList(){
        try {
            MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
            return JSON.parseObject(postHeadersT(serialPortList, getHeaders(CommonConstant.DATA_INT_1), pMap,HttpMethod.GET), SerialPortList.class);
        }catch (Exception e){
            log.error("获取usb列表接口异常，原因:"+e.getMessage());
            return null;
        }
    }
    /**
     *获取镜像列表
     * @return 镜像列表
     */
    public ImageList getImageList(){
        try {
            MultiValueMap<String, Object> pMap = new LinkedMultiValueMap<>();
            return JSON.parseObject(postHeadersT(imageList, getHeaders(CommonConstant.DATA_INT_1), pMap,HttpMethod.GET), ImageList.class);
        }catch (Exception e){
            log.error("获取镜像列表接口异常，原因:"+e.getMessage());
            return null;
        }
    }
    /**
     * 创建单独虚拟机
     * @param name  虚拟机名称
     * @param image 镜像名或者id
     * @param flavor 虚拟机规格id
     * @param securityGroups 安全组名称
     * @param availabilityZone 可用域（默认nova即可）
     * @param network 网络名（单独的孤岛网络，默认alone-server-network即可）
     * @param volumeSize 启动盘大小，单位GB，不能小于image的virtual_size（B）
     * @param autoIp 自动获取ip
     * @param bootFromVolume 自动获取ip
     * @param terminateVolume true（创建时删除多余文件）
     * @return 创建单独虚拟机
     */
    public ServerCompute getServerCompute(String name, String image, String flavor, String securityGroups,
                                          String availabilityZone, String network, String volumeSize, String autoIp, String bootFromVolume,String terminateVolume){
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            pMap.put("name",name);
            pMap.put("image",image);
            pMap.put("flavor",flavor);
            pMap.put("auto_ip",autoIp);
            pMap.put("availability_zone",availabilityZone);
            pMap.put("volume_size",volumeSize);
            pMap.put("boot_from_volume",bootFromVolume);
            pMap.put("terminate_volume",terminateVolume);
            List<String> securityGroupsList = Arrays.asList(securityGroups);
            pMap.put("security_groups",securityGroupsList);
            List<String> networkList = Arrays.asList(network);
            pMap.put("network",networkList);
            String resultData = postHeadersT(serverCompute,getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    ServerCompute serverCompute = new ServerCompute();
                    serverCompute.setCode((Integer)parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE));
                    serverCompute.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    serverCompute.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_ID).toString());
                    serverCompute.setName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_SERVER_NAME).toString());
                    serverCompute.setImage(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_IMAGE).toString());
                    serverCompute.setStatus(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString());
                    serverCompute.setOriginalName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERVERS_SERVER).getJSONObject(CommonConstant.DATA_STRING_FLAVOR).get(CommonConstant.DATA_STRING_ORIGINAL_NAME).toString());
                    return serverCompute;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("getServerCompute 异常："+e.getMessage());
            return null;
        }
    }
    public ServerCompute getServerCompute(String name,String image,String flavor,String rollback,String size){
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            String openstackJson = openstackJson(image,flavor,size);
            pMap.put("name",name);
            pMap.put("template_content",openstackJson);
            pMap.put("rollback",rollback);
            String resultData = postHeadersT(createStack,getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    ServerCompute serverCompute = new ServerCompute();
                    serverCompute.setCode((Integer)parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE));
                    serverCompute.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    serverCompute.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_ID).toString());
                    serverCompute.setName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_SERVER_NAME).toString());
                    serverCompute.setStatus(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_STACK).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString());
                    return serverCompute;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("getServerCompute 异常："+e.getMessage());
            return null;
        }
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
     * 从虚拟机创建镜像
     * @param id
     * @param serverId
     * @param name
     * @return 从虚拟机创建镜像
     */
    public FoundImage getServerAction(String id, String serverId, String name){
        try {
            Map<String, Object> pMap = new HashMap<>(6);
            CreateImage createImage = new CreateImage();
            createImage.setName(name);
            pMap.put("createImage",createImage);
            String resultData = postHeadersT(foundImageServer.replace(CommonConstant.DATA_STRING_STACK_SERVER_ID,serverId),getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        && parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    FoundImage foundImage = new FoundImage();
                    foundImage.setCode((Integer)parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE));
                    foundImage.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    foundImage.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_IMAGE).get(CommonConstant.DATA_STRING_ID).toString());
                    foundImage.setName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_IMAGE).get(CommonConstant.DATA_STRING_SERVER_NAME).toString());
                    foundImage.setStatus(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_IMAGE).get(CommonConstant.DATA_STRING_SERVER_STATUS).toString());
                    return foundImage;
                }
            }
            return null;
        } catch (Exception e) {
            log.error("getServerAction 异常："+e.getMessage());
            return null;
        }
    }


    /**
     * 测试虚拟机串口 若成功获取测试id
     * @param domain  虚拟机Id serverId
     * @param serverMode true为服务端模式，false为客户端模式
     * @param port 串口的通讯端口
     * @param host 目标地址
     * @return  测试id
     */
    public String testSerial(String domain, Boolean serverMode, Integer port,String host){
        try {
            Map<String, Object> pMap = getTestAndCreateSerialMap(domain, serverMode, port, host);
            String resultData = postHeadersT(testSerial,getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    return  parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_TEST_ID).toString();
                }
            }
            log.info("resultData："+resultData);
            return null;
        } catch (Exception e) {
            log.error("得到测试Id 异常："+e.getMessage());
            return null;
        }
    }


    /**
     * 根据测试id 获取测试串口结果
     * @param testId 测试串口id
     * @return 测试串口端口返回结果
     */
    public TestSerialResult getTestSerialResult(String testId){
        try {
            String resultData = postHeadersT(getTestSerialResult.replace(CommonConstant.DATA_STRING_STACK_TEST_ID,testId),getHeaders(CommonConstant.DATA_INT_1),null,HttpMethod.GET);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        && parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    TestSerialResult testSerialResult=new TestSerialResult();
                    testSerialResult.setCode((Integer)parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE));
                    testSerialResult.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    testSerialResult.setHost(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_HOST).toString());
                    testSerialResult.setPort(Integer.parseInt(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_PORT).toString()));
                    testSerialResult.setServerMode(Boolean.getBoolean(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_SERVER_MODE).toString()));
                    testSerialResult.setSuccess(Boolean.parseBoolean(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).get(CommonConstant.DATA_STRING_SUCCESS).toString()));
                    return testSerialResult;
                }
            }
            log.info("resultData："+resultData);
            return null;
        } catch (Exception e) {
            log.error("根据测试id获取测试串口结果 异常："+e.getMessage());
            return null;
        }
    }


    /**
     *
     * @param domain  虚拟机Id serverId
     * @param serverMode true为服务端模式，false为客户端模式
     * @param port 串口的通讯端口
     * @param host 目标地址
     * @return  createSerialPortResult
     */
    public CreateSerialPortResult createSerialPort(String domain, Boolean serverMode, Integer port,String host){
        try {
            Map<String, Object> pMap = getTestAndCreateSerialMap(domain, serverMode, port, host);
            String resultData = postHeadersT(createSerial,getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())
                        &&parseObject.containsKey(CommonConstant.DATA_STRING_DATA)){
                    CreateSerialPortResult createSerialPortResult=new CreateSerialPortResult();
                    createSerialPortResult.setCode((Integer)parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE));
                    createSerialPortResult.setMsg(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_MSG).toString());
                    createSerialPortResult.setHost(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_HOST).toString());
                    createSerialPortResult.setPort(Integer.parseInt(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_PORT).toString()));
                    createSerialPortResult.setServerMode(Boolean.getBoolean(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_SERVER_MODE).toString()));
                    createSerialPortResult.setDomainId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_DOMAIN_ID).toString());
                    createSerialPortResult.setDomainName(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_DOMAIN_NAME).toString());
                    createSerialPortResult.setCreatedAt(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_CREATED_AT).toString());
                    createSerialPortResult.setId(parseObject.getJSONObject(CommonConstant.DATA_STRING_DATA).getJSONObject(CommonConstant.DATA_STRING_SERIAL).get(CommonConstant.DATA_STRING_ID).toString());
                    return createSerialPortResult;
                }
            }
            log.info("resultData："+resultData);
            return null;
        } catch (Exception e) {
            log.error("创建串口 异常："+e.getMessage());
            return null;
        }
    }

    private Map<String, Object> getTestAndCreateSerialMap(String domain, Boolean serverMode, Integer port, String host) {
        Map<String, Object> pMap = new HashMap<>(3);
        pMap.put("domain", domain);
        pMap.put("server_mode", serverMode);
        pMap.put("port", port);
        pMap.put("host", host);
        return pMap;
    }


    /**
     * 删除串口
     * @param openstackDeviceId  串口Id
     * @return Boolean
     */
    public Boolean deleteSerial(String openstackDeviceId) {
        String resultData= postHeadersT(deleteSerial.replace(CommonConstant.DATA_STRING_DEVICE_ID, openstackDeviceId), super.getHeaders(CommonConstant.DATA_INT_1), null, HttpMethod.DELETE);
        if (StringUtils.isNoneBlank(resultData)) {
            JSONObject parseObject = JSONObject.parseObject(resultData);
            if (CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())) {
                //删除成功
                return true;
            }
        }
        log.info("resultData："+resultData);
        return false;
    }


    /**
     * 绑定usb
     * @param destDom  destDom
     * @param deviceId deviceId
     * @return boolean
     */
    public boolean bindukey(String destDom ,String deviceId){
        try {
            Map<String, Object> pMap = new HashMap<>(1);
            pMap.put("dest_dom", destDom);
            String resultData = postHeadersT(bindukey.replace(CommonConstant.DATA_STRING_STACK_DEVICE_ID,deviceId),getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.POST);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                return parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString());
            }
            log.info("resultData："+resultData);
            return false;
        } catch (Exception e) {
            log.error("绑定usb 异常："+e.getMessage());
            return false;
        }
    }


    /**
     * 解绑usb
     * @param deviceId 设备id
     * @return  boolean
     */
    public boolean unbindukey(String deviceId){
        try {
            String resultData = postHeadersT(unbindukey.replace(CommonConstant.DATA_STRING_STACK_DEVICE_ID,deviceId),getHeaders(CommonConstant.DATA_INT_1),null,HttpMethod.DELETE);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject != null && !parseObject.isEmpty() && CommonConstant.DATA_STR_0.equals(parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString())){
                    return true;
                }
            }
            log.info("resultData："+resultData);
            return false;
        } catch (Exception e) {
            log.error("解绑usb 异常："+e.getMessage());
            return false;
        }
    }
	
	/**
     * 获取网络列表
     * @return  Result
     */
    public JSONObject getNetworks(){
        try {
            String resultData = postHeadersT(networks,getHeaders(CommonConstant.DATA_INT_1),null,HttpMethod.GET);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject.containsKey(CommonConstant.DATA_STRING_DATA)
                        && parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString().equals(CommonConstant.DATA_STR_0)){
                    Object data = parseObject.get(CommonConstant.DATA_STRING_DATA);
                    return parseObject;
                }
            }

        } catch (Exception e) {
            log.error("获取网络列表接口异常："+e.getMessage());
        }
        return null;
    }

    /**
     * 编辑子网路由表
     * @return  Result
     */
    public Boolean updateSubnets(String subnetId,List<HostRoute> hostRoutes){
        try {
            Map<String, Object> pMap = new HashMap<>(1);
            pMap.put("host_routes", hostRoutes);
            String resultData = postHeadersT(subnetIds.replace(CommonConstant.SUBNET_ID,subnetId),getHeaders(CommonConstant.DATA_INT_1),pMap,HttpMethod.PUT);
            if (StringUtils.isNoneBlank(resultData)){
                JSONObject parseObject = JSONObject.parseObject(resultData);
                if (parseObject.containsKey(CommonConstant.DATA_STRING_DATA)
                        && parseObject.get(CommonConstant.REST_TEMPLATE_RESULT_CODE).toString().equals(CommonConstant.DATA_STR_0)){
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("编辑子网路由表接口异常："+e.getMessage());
        }
        return false;
    }


}
