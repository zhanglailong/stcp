package org.jeecg.modules.openstack.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.common.enums.ServerStatusEnum;
import org.jeecg.modules.openstack.entity.*;
import org.jeecg.modules.openstack.service.IStackQueueService;
import org.jeecg.modules.openstack.service.ext.BasedInterfaceServiceExt;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.plan.service.IVmDesignService;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.tepm.feign.AgentClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author zlf
 */
@Api(tags="openstack接口")
@RestController
@RequestMapping("/openstack")
@Slf4j
public class OpenStackController {

	@Resource
	OpenStackServiceExt openStackServiceExt;
	@Resource
	BasedInterfaceServiceExt basedInterfaceServiceExt;
	@Resource
	IStackQueueService iStackQueueService;
	@Resource
	AgentClient agentClient;

	@Resource
	IVmDesignService iVmDesignService;

	/**
	 * 分页列表查询
	 *@return 环境列表
	 */
	@AutoLog(value = "获取环境的资源列表")
	@ApiOperation(value="获取环境的资源列表", notes="获取环境的资源列表")
	@GetMapping(value = "/stack/list")
	public Result<?> getStackList() {
		try {
			StackList stackList = openStackServiceExt.getStackList("ba8104b5-0990-47b7-8b10-a46eadcf8c93");
			if (stackList != null){
				return Result.OK(stackList);
			}
		}catch (Exception e){
			log.error("获取环境的资源列表，原因:"+e.getMessage());
			return Result.error("获取环境的资源列表，原因:"+e.getMessage());
		}
		return Result.error("未获取环境资源列表");
	}

	/**
	 *获取镜像列表
	 * @return 获取镜像列表
	 */
	@AutoLog(value = "获取镜像列表")
	@ApiOperation(value="获取镜像列表", notes="获取镜像列表")
	@GetMapping(value = "/image/list")
	public Result<?> getImageList() {
		try {
			ImageList imageList = openStackServiceExt.getImageList();
			if (imageList != null && imageList.getCode() == CommonConstant.DATA_INT_0 && imageList.getDatas() != null){
				List<ImageList.Images> images = imageList.getDatas().getImages();
				if (images != null && images.size() >0){
					return Result.OK(images);
				}
			}
		}catch (Exception e){
			log.error("获取镜像列表controller异常,原因:"+e.getMessage());
			return Result.error("获取失败异常,请联系管理员！");
		}
		return Result.error("获取失败，请重新获取！");
	}

	/**
	 *虚拟机使用率
	 * @param serverId 虚拟机id
	 * @return 虚拟机使用率
	 */
	@AutoLog(value = "获取虚拟机资源使用率")
	@ApiOperation(value="获取虚拟机资源使用率", notes="获取虚拟机资源使用率")
	@GetMapping(value = "/server/states")
	public Result<?> getServerStats(@RequestParam(name="serverId") String serverId) {
		try {
			ResourceUtilization resourceUtilization = basedInterfaceServiceExt.getServersStats(serverId);
			if (resourceUtilization != null && CommonConstant.DATA_INT_0 == resourceUtilization.getCode()){
				return Result.OK(resourceUtilization);
			}
		} catch (Exception e) {
			log.error("虚拟机使用率controller异常,原因:" + e.getMessage());
			return Result.error("获取虚拟机资源使用率异常,请联系管理员！");
		}
		return Result.error("获取虚拟机使用率失败，请重新获取！");
	}

	/**
	 *获取虚拟机vnc界面
	 * @param serverId 虚拟机id
	 * @return 获取虚拟机vnc界面
	 */
	@AutoLog(value = "获取虚拟机vnc界面")
	@ApiOperation(value="获取虚拟机vnc界面", notes="获取虚拟机vnc界面")
	@GetMapping(value = "/server/vnc")
	public Result<?> getServerVnc(@RequestParam(name="serverId") String serverId) {
		try {
			String vncUrl = basedInterfaceServiceExt.getVnc(serverId);
			if (StringUtils.isNotEmpty(vncUrl)){
				return Result.OK(vncUrl);
			}
		} catch (Exception e) {
			log.error("获取虚拟机vnc界面异常:"+e.getMessage());
			return Result.error("获取虚拟机vnc界面异常:"+e.getMessage());
		}
		return Result.error("获取虚拟机vnc界面失败!");

	}


	/**
	 *从虚拟机创建镜像
	 * @param id 平台虚拟机id
	 * @param serverId openstack虚拟机id
	 * @param stackName 虚拟机名称
	 * @param stackStatus 虚拟机状态
	 * @param remark 镜像备注
	 * @param name 镜像名称
	 * @param sysType 操作系统
	 * @param otherSoftware 其他软件
	 * @param testTools 测试工具
	 * @param systemVersion 系统版本
	 * @return 状态
	 */
	@AutoLog(value = "从虚拟机创建镜像")
	@ApiOperation(value="从虚拟机创建镜像", notes="从虚拟机创建镜像")
	@PostMapping(value = "/server/action")
	public Result<?> getServerAction( @RequestParam(name="id") String id,
									  @RequestParam(name="serverId") String serverId,
									  @RequestParam(name = "stackName") String stackName,
									  @RequestParam(name = "stackStatus") String stackStatus,
									  @RequestParam(name = "remark") String remark,
									  @RequestParam(name = "name") String name,
									  @RequestParam(name = "sysType") String sysType,
									  @RequestParam(name = "otherSoftware") String otherSoftware,
									  @RequestParam(name = "testTools") String testTools,
									  @RequestParam(name = "systemVersion") String systemVersion

	) {
		try {
			Boolean getServerAction = iStackQueueService.getServerAction(id,serverId,stackName,stackStatus,remark,name,sysType,otherSoftware,testTools,systemVersion);
			if (getServerAction){
				return Result.OK("从虚拟机创建镜像发送数据成功，等待创建中！");
			}
		} catch (Exception e) {
			log.error("从虚拟机创建镜像异常,原因:"+e.getMessage());
			return Result.error("从虚拟机创建镜像异常,请联系管理员！");
		}
		return Result.error("从虚拟机创建镜像失败！");
	}

	/**
	 *镜像删除
	 * @param id 平台镜像id
	 * @param imageId 镜像id
	 * @return 状态
	 */
	@AutoLog(value = "镜像删除")
	@ApiOperation(value="镜像删除", notes="镜像删除")
	@DeleteMapping(value = "/images/del")
	public Result<?> imagesDelById( @RequestParam(name="id") String id,
									@RequestParam(name="imageId") String imageId) {
		try {
			Boolean code = iStackQueueService.deleteImages(id,imageId);
			if (code){
				return Result.OK("从虚拟机创建镜像发送数据成功，等待创建中！");
			}
		} catch (Exception e) {
			log.error("镜像删除异常,原因:"+e.getMessage());
			return Result.error("镜像删除异常,请联系管理员！");
		}
		return Result.error("镜像删除失败!");
	}

	/**
	 * 获取环境的事件
	 * @param stackId openstack环境id
	 * @param status 可以用来过滤，status可以是COMPLETE, FAILED, IN_PROGRESS
	 * @return 获取环境的事件
	 */
	@AutoLog(value = "获取环境的事件")
	@ApiOperation(value="获取环境的事件", notes="获取环境的事件")
	@GetMapping(value = "/stacks/events")
	public Result<?> getStacksEvents(@RequestParam(name="stackId") String stackId,
									  @RequestParam(name="status") String status) {
        try {
        	if (StringUtils.isBlank(stackId)){
        		return Result.OK("此环境暂无事件！");
			}
            EventsList eventsList = basedInterfaceServiceExt.getStacksEvents(stackId,status);
            if (eventsList != null && eventsList.getCode() == CommonConstant.DATA_INT_0 && eventsList.getDatas() != null){
                List<EventsList.Events> events = eventsList.getDatas().getEvents();
                if (events != null && events.size() >0){
                    return Result.OK(events);
                }
            }
        } catch (Exception e) {
            log.error("获取环境的事件controller异常,原因:"+e.getMessage());
            return Result.error("获取失败异常,请联系管理员！");
        }
		return Result.error("获取失败，请重新获取！");
	}

	/**
	 * 九宫格监控
	 * @param serverIds 虚拟机id列表（长度<=9）
	 * @return 九宫格监控
	 */
	@AutoLog(value = "九宫格监控")
	@ApiOperation(value="九宫格监控", notes="九宫格监控")
	@PostMapping(value = "/server/multipleServerVnc")
	public Result<?> getServerMultipleServerVnc(@RequestParam(name="serverIds") String serverIds) {
		try {
			MultipleVnc multipleVnc = basedInterfaceServiceExt.getMultipleVnc(serverIds);
			if (multipleVnc != null && multipleVnc.getCode() == CommonConstant.DATA_INT_0 && multipleVnc.getDatas() != null){
				List<MultipleVnc.RemoteCons> remoteConsList = multipleVnc.getDatas().getRemoteConsoles();
				if (remoteConsList != null && remoteConsList.size() >0){
					return Result.OK(remoteConsList);
				}
			}
		} catch (Exception e) {
			log.error("九宫格监控controller异常,原因:"+e.getMessage());
			return Result.error("获取失败异常,请联系管理员！");
		}
		return Result.error("九宫格监控失败，请重新获取！");
	}

	/**
	 * 虚拟机删除
	 * @param id 虚拟机主键id
	 * @param serverId 虚拟机id
	 * @return 虚拟机删除
	 */
	@AutoLog(value = "删除虚拟机")
	@ApiOperation(value = "虚拟机删除",notes = "虚拟机删除")
	@DeleteMapping(value = "/server/deleteVm")
	public Result<?> deleteVn (@RequestParam(name = "id") String id,
							   @RequestParam(name = "serverId") String serverId){
		try {
			Boolean code = iStackQueueService.deleteVm(id,serverId);
			if (code){
				return Result.OK("虚拟机删除成功！");
			}
		} catch (Exception e) {
			log.error("虚拟机删除异常,原因:"+e.getMessage());
			return Result.error("虚拟机删除异常,请联系管理员！");
		}
		return Result.error("虚拟机删除失败!");
	}

	/**
	 * 虚拟机挂起
	 * @param id 虚拟机表主键
	 * @param serverId 虚拟机id
	 * @return true
	 */
	@AutoLog(value = "虚拟机挂起")
	@ApiOperation(value = "虚拟机挂起",notes = "虚拟机挂起")
	@PostMapping(value = "/server/suspendVm")
	public Result<?> getsSuspendVm(@RequestParam(name = "id") String id,
							   @RequestParam(name = "serverId") String serverId){
		try {
			boolean suspendVm = iStackQueueService.suspendVm(id,serverId);
			if (suspendVm){
				try {
					VmDesign vm = iVmDesignService.getById(id);
					List<String> agentIps=new ArrayList<>();
					agentIps.add(vm.getNetworkAddress());
					List<String> plans=new ArrayList<>();
					plans.add(vm.getPlanId());
					agentClient.changeAgentStatus(agentIps,plans, ServerStatusEnum.SUSPENDED.getStatus());
				}catch (Exception e){
					log.error("发送大连状态异常:" + e.getMessage());
				}
				return Result.OK("虚拟机挂起成功");
			}else {
				return Result.error("挂起失败");
			}
		} catch (Exception e) {
			log.error("虚拟机-挂起异常:" + e.getMessage());
			return Result.error("虚拟机-挂起异常:" + e.getMessage());
		}
	}

	/**
	 * 虚拟机挂起恢复
	 * @param id 虚拟机表主键
	 * @param serverId 虚拟机id
	 * @return true
	 */
	@AutoLog(value = "虚拟机挂起恢复")
	@ApiOperation(value = "虚拟机挂起恢复",notes = "虚拟机挂起恢复")
	@PostMapping(value = "/server/resumeVm")
	public Result<?> getResumeVm(@RequestParam(name = "id") String id,
							   @RequestParam(name = "serverId") String serverId){
		try {
			boolean resumeVm = iStackQueueService.resumeVm(id,serverId);
			if (resumeVm){
				try {
					VmDesign vmDesign = iVmDesignService.getById(id);
					List<String> plans=new ArrayList<>();
					plans.add(vmDesign.getPlanId());
					List<String> agentIps=new ArrayList<>();
					agentIps.add(vmDesign.getNetworkAddress());
					agentClient.changeAgentStatus(agentIps,plans, ServerStatusEnum.ACTIVE.getStatus());
				}catch (Exception e){
					log.error("发送大连状态异常:" + e.getMessage());
				}
				return Result.OK("虚拟机挂起恢复成功");
			}else {
				return Result.error("恢复失败");
			}
		} catch (Exception e) {
			log.error("虚拟机-恢复异常:" + e.getMessage());
			return Result.error("虚拟机-恢复异常:" + e.getMessage());
		}
	}

	/**
	 * 调整虚拟机配置
	 * @param id 虚拟机表主键id
	 * @param flavor 修改参数
	 * @param size  磁盘大小
	 * @param rollback  是否回滚
	 * @return true
	 */
	@AutoLog(value = "调整虚拟机配置")
	@ApiOperation(value = "调整虚拟机配置",notes = "调整虚拟机配置")
	@PostMapping(value = "/server/getVmAlter")
	public Result<?> getVmAlter(@RequestParam(name = "id") String id,
								@RequestParam(name = "flavor") String flavor,
								@RequestParam(name = "size") String size,
								@RequestParam(name = "rollback") String rollback){
		try {
			boolean getVmAlter = iStackQueueService.getVmAlter(id,flavor,size,rollback);
			if (getVmAlter){
				return Result.OK("调整虚拟机配置成功");
			}
			return Result.error("调整虚拟机失败");
		} catch (Exception e) {
			log.error("调整虚拟机配置-异常:" + e.getMessage());
			return Result.error("调整虚拟机配置-异常:" + e.getMessage());
		}
	}


	/**
	 * @param domain  虚拟机serverId
	 * @param serverMode true为服务端模式，false为客户端模式
	 * @param port  串口通讯端口
	 * @param host 目标地址
	 * @return testId 测试id
	 */
	@AutoLog(value = "测试串口返回测试Id")
	@ApiOperation(value="测试串口返回测试Id", notes="测试串口返回测试Id")
	@PostMapping(value = "/device/testSerial")
	public Result<?> testSerial( @RequestParam(name="domain") String domain,
									@RequestParam(name="serverMode") boolean serverMode,
									@RequestParam(name="port") Integer port,
									@RequestParam(name="host") String host

	) {
		try {
			String testId = openStackServiceExt.testSerial(domain, serverMode, port, host);
			if (StringUtils.isNotBlank(testId)){

				return Result.OK(testId);
			}
		} catch (Exception e) {
			log.error("测试串口返回测试Id异常,原因:"+e.getMessage());
			return Result.error("测试串口返回测试Id异常,请联系管理员！");
		}
		return Result.error("测试串口返回测试Id失败!");
	}


	/**
	 * 根据测试id查询串口测试结果
	 * @param testId 测试id
	 * @return 测试结果
	 */
	@AutoLog(value = "根据测试id查询串口测试结果")
	@ApiOperation(value="根据测试id查询串口测试结果", notes="根据测试id查询串口测试结果")
	@PostMapping(value = "/device/getTestSerialResult")
	public Result<?> getTestSerialResult( @RequestParam(name="testId") String testId) {
		try {
			TestSerialResult testSerialResult = openStackServiceExt.getTestSerialResult(testId);
			if (testSerialResult!=null){
				return Result.OK(testSerialResult);
			}
		} catch (Exception e) {
			log.error("根据测试id查询串口测试结果异常,原因:"+e.getMessage());
			return Result.error("根据测试id查询串口测试结果异常,请联系管理员！");
		}
		return Result.error("根据测试id查询串口测试结果失败!");
	}


	/**
	 * @param domain  虚拟机serverId
	 * @param serverMode true为服务端模式，false为客户端模式
	 * @param port  串口通讯端口
	 * @param host 目标地址
	 * @return 创建的串口
	 */
	@AutoLog(value = "创建串口")
	@ApiOperation(value="创建串口", notes="创建串口")
	@PostMapping(value = "/device/createSerialPort")
	public Result<?> createSerialPort( @RequestParam(name="domain") String domain,
										  @RequestParam(name="serverMode") boolean serverMode,
										  @RequestParam(name="port") Integer port,
										  @RequestParam(name="host") String host) {
		try {
			CreateSerialPortResult serialPort = openStackServiceExt.createSerialPort(domain, serverMode, port, host);
			if (serialPort!=null){
				return Result.OK(serialPort);
			}
		} catch (Exception e) {
			log.error("创建串口异常,原因:"+e.getMessage());
			return Result.error("创建串口异常,请联系管理员！");
		}
		return Result.error("创建串口测试结果失败!");
	}


	/**
	 * 成功或者失败
	 * @param openstackDeviceId 设备id
	 * @return 成功或者失败
	 */
	@AutoLog(value = "删除串口")
	@ApiOperation(value="删除串口", notes="删除串口")
	@DeleteMapping(value = "/device/deleteSerial")
	public Result<?> deleteSerial( @RequestParam(name="openstackDeviceId") String openstackDeviceId) {
		try {
			Boolean code = openStackServiceExt.deleteSerial(openstackDeviceId);
			if (code){
				return Result.OK("删除串口成功");
			}
		} catch (Exception e) {
			log.error("删除串口异常,原因:"+e.getMessage());
			return Result.error("删除串口异常,请联系管理员！");
		}
		return Result.error("删除串口测试结果失败!");
	}

	/**
	 * @param destDom serverId
	 * @param openstackDeviceId 设备id
	 * @return 成功或者失败
	 */
	@AutoLog(value = "绑定USB")
	@ApiOperation(value="绑定USB", notes="绑定USB")
	@PostMapping(value = "/device/bindukey")
	public Result<?> bindukey( @RequestParam(name="destDom") String destDom,
								   @RequestParam(name="openstackDeviceId") String openstackDeviceId) {
		try {
			boolean code = openStackServiceExt.bindukey(destDom, openstackDeviceId);
			if (code){
				return Result.OK("绑定USB成功");
			}
		} catch (Exception e) {
			log.error("绑定USB异常,原因:"+e.getMessage());
			return Result.error("绑定USB异常,请联系管理员！");
		}
		return Result.error("绑定USB测试结果失败!");
	}


	/**
	 * 解绑usb失败
	 * @param openstackDeviceId 设备id
	 * @return 成功或者失败
	 */
	@AutoLog(value = "解绑USB")
	@ApiOperation(value="解绑USB", notes="解绑USB")
	@PostMapping(value = "/device/unbindukey")
	public Result<?> unbindukey( @RequestParam(name="openstackDeviceId") String openstackDeviceId) {
		try {
			boolean code = openStackServiceExt.unbindukey(openstackDeviceId);
			if (code){
				return Result.OK("解绑USB成功");
			}
		} catch (Exception e) {
			log.error("解绑USB异常,原因:"+e.getMessage());
			return Result.error("解绑USB异常,请联系管理员！");
		}
		return Result.error("解绑USB测试结果失败!");
	}
	
	/**
	 * 获取网络列表
	 * @return 获取网络列表
	 */
	@AutoLog(value = "获取网络列表")
	@ApiOperation(value="获取网络列表", notes="获取网络列表")
	@PostMapping(value = "/network/networks")
	public Result<?> getNetworks() {
		try {
			return Result.OK(openStackServiceExt.getNetworks());
		} catch (Exception e) {
			log.error("获取网络列表异常,原因:"+e.getMessage());
			return Result.error("获取网络列表异常,请联系管理员！");
		}
	}


}
