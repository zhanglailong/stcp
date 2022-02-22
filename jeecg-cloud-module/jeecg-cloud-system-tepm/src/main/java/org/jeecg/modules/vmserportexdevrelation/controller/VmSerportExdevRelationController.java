package org.jeecg.modules.vmserportexdevrelation.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.common.CommonConstant;
import org.jeecg.modules.openstack.entity.SerialPortList;
import org.jeecg.modules.openstack.entity.UkeyList;
import org.jeecg.modules.openstack.service.ext.OpenStackServiceExt;
import org.jeecg.modules.vmserportexdevrelation.entity.VmSerportExdevRelation;
import org.jeecg.modules.vmserportexdevrelation.service.IVmSerportExdevRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import java.util.List;


/**
 * @author yeyl
 */
@Api(tags="虚拟机串口外部工装关系表")
@RestController
@RequestMapping("/vmserportexdevrelation/vmSerportExdevRelation")
@Slf4j
public class VmSerportExdevRelationController extends JeecgController<VmSerportExdevRelation, IVmSerportExdevRelationService> {
	@Resource
	private IVmSerportExdevRelationService vmSerportExdevRelationService;

	@Resource
	private  OpenStackServiceExt openStackServiceExt;
	
	/**
	 * 分页列表查询
	 *
	 * @param vmSerportExdevRelation vmSerportExdevRelation
	 * @param pageNo 第几页
	 * @param pageSize 页数
	 * @param req  请求
	 * @return list
	 */
	@AutoLog(value = "虚拟机串口外部工装关系表-分页列表查询")
	@ApiOperation(value="虚拟机串口外部工装关系表-分页列表查询", notes="虚拟机串口外部工装关系表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(VmSerportExdevRelation vmSerportExdevRelation,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<VmSerportExdevRelation> queryWrapper = QueryGenerator.initQueryWrapper(vmSerportExdevRelation, req.getParameterMap());
		Page<VmSerportExdevRelation> page = new Page<>(pageNo, pageSize);
		IPage<VmSerportExdevRelation> pageList = vmSerportExdevRelationService.page(page, queryWrapper);
		return Result.OK(pageList);
	}


	 /**
	  * 测试串口
	  * @param portNumber 端口
	  * @param desAddress 目标地址
	  * @param id 串口id
	  * @return 成功或者失败
	  */
	@AutoLog(value = "虚拟机串口-测试")
	@ApiOperation(value="虚拟机串口-测试", notes="虚拟机串口-测试")
	@PostMapping(value = "/testSerIalPort")
	public Result<?> testSerIalPort(@RequestParam Integer portNumber ,@RequestParam String desAddress ,@RequestParam String id ) {
		try {
			if (StringUtils.isNotBlank(id) && portNumber!=null&&StringUtils.isNotBlank(desAddress)) {
				if (vmSerportExdevRelationService.testSerIalPort(portNumber, desAddress, id)) {
					return Result.OK("测试串口成功！");
				}
			}
		} catch (Exception e) {
			log.error("测试串口异常，原因:" + e.getMessage());
			return Result.error("测试串口异常，原因:" + e.getMessage());
		}
		return Result.error("测试串口失败！");
	}

	 /**
	  * 绑定串口
	  * @param portNumber  端口
	  * @param desAddress 目标地址
	  * @param id id
	  * @return 成功或者失败
	  */
	@AutoLog(value = "绑定串口-编辑")
	@ApiOperation(value="绑定串口-编辑", notes="绑定串口-编辑")
	@PostMapping(value = "/bindSerial")
	public Result<?> bindSerial(@RequestParam Integer portNumber ,@RequestParam String desAddress ,@RequestParam String id) {
		try {
			if (StringUtils.isNotBlank(id) && portNumber!=null&&StringUtils.isNotBlank(desAddress)) {
				if (vmSerportExdevRelationService.bindSerial(portNumber, desAddress, id)) {
					return Result.OK("绑定串口成功！");
				}
			}
		} catch (Exception e) {
			log.error("绑定串口异常，原因:" + e.getMessage());
			return Result.error("绑定串口异常，原因:" + e.getMessage());
		}
		return Result.error("绑定串口失败！");
	}
	

	@AutoLog(value = "解绑串口")
	@ApiOperation(value="解绑串口", notes="解绑串口")
	@PostMapping(value = "/unbindSerial")
	public Result<?> unbindSerial(@RequestParam(name="id") String id) {
		try{
		if (StringUtils.isNotBlank(id)) {
			if (vmSerportExdevRelationService.unbindSerial(id)) {
				return Result.OK("解绑串口成功！");
			}
		}
	} catch (Exception e) {
		 log.error("解绑串口异常，原因:" + e.getMessage());
		 return Result.error("解绑串口异常，原因:" + e.getMessage());
	 }
		return Result.error("解绑串口失败！");
	}
	

	
	/**
	 * 通过id查询
	 * @param id id
	 * @return VmSerportExdevRelation
	 */
	@AutoLog(value = "虚拟机串口外部工装关系表-通过id查询")
	@ApiOperation(value="虚拟机串口外部工装关系表-通过id查询", notes="虚拟机串口外部工装关系表-通过id查询")
	@PostMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id") String id) {
		VmSerportExdevRelation vmSerportExdevRelation = vmSerportExdevRelationService.getById(id);
		if(vmSerportExdevRelation==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(vmSerportExdevRelation);
	}

    /**
    * 导出excel
    *
    * @param request request
    * @param vmSerportExdevRelation vmSerportExdevRelation
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VmSerportExdevRelation vmSerportExdevRelation) {
        return super.exportXls(request, vmSerportExdevRelation, VmSerportExdevRelation.class, "虚拟机串口外部工装关系表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request request
    * @param response response
    * @return 成功或者失败
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, VmSerportExdevRelation.class);
    }

	/**
	 *获取usb列表
	 * @return 获取usb列表
	 */
	@AutoLog(value = "获取usb列表")
	@ApiOperation(value="获取usb列表", notes="获取usb列表")
	@GetMapping(value = "/ukey/list")
	public Result<?> getUkeyList() {
		try {
			UkeyList ukeyList = openStackServiceExt.getUkeyList();
			if (ukeyList != null && ukeyList.getCode() == CommonConstant.DATA_INT_0 && ukeyList.getDatas() != null){
				List<UkeyList.Ukeys> ukeys = ukeyList.getDatas().getUkeys();
				if (ukeys != null && ukeys.size() >0){
					return Result.OK(ukeys);
				}
			}
		}catch (Exception e){
			log.error("获取usb列表controller异常,原因:"+e.getMessage());
			return Result.error("获取失败异常,请联系管理员！");
		}
		return Result.error("获取失败，请重新获取！");
	}
	/**
	 *获取串口列表
	 * @return 获取串口列表
	 */
	@AutoLog(value = "获取串口列表")
	@ApiOperation(value="获取串口列表", notes="获取串口表")
	@GetMapping(value = "/serialPorts/list")
	public Result<?> getSerialPortList() {
		try {
			SerialPortList serialPortList = openStackServiceExt.getSerialPortList();
			if (serialPortList != null && serialPortList.getCode() == CommonConstant.DATA_INT_0 && serialPortList.getDatas() != null){
				List<SerialPortList.serial> serialPorts = serialPortList.getDatas().getSerial();
				if (serialPorts != null && serialPorts.size() >0){
					return Result.OK(serialPorts);
				}
			}
		}catch (Exception e){
			log.error("获取串口列表controller异常,原因:"+e.getMessage());
			return Result.error("获取失败异常,请联系管理员！");
		}
		return Result.error("获取失败，请重新获取！");
	}

	/**
	 * @param openstackHost  主机名
	 * @param openstackHostId 主机id
	 * @param openstackDeviceId 设备id
	 * @param openstackBoundUsb 被绑定usb名称
	 * @param id 表id
	 * @return 成功或者失败
	 */
	@AutoLog(value = "绑定usb-编辑")
	@ApiOperation(value="绑定usb-编辑", notes="绑定usb-编辑")
	@PostMapping(value = "/bindUsb")
	public Result<?> bindUsb(@RequestParam(name="openstackHost") String openstackHost,@RequestParam(name="openstackHostId") String openstackHostId,@RequestParam(name="openstackDeviceId") String openstackDeviceId, @RequestParam(name="openstackBoundUsb")  String openstackBoundUsb, @RequestParam(name="id") String id) {
		try {
			if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(openstackHostId)
					&&StringUtils.isNotBlank(openstackHost)
					&&StringUtils.isNotBlank(openstackDeviceId)
					&&StringUtils.isNotBlank(openstackBoundUsb)
			) {
				if (vmSerportExdevRelationService.bindUsb(openstackHost,openstackHostId,openstackDeviceId,openstackBoundUsb, id)) {
					return Result.OK("绑定usb成功！");
				}
			}
		} catch (Exception e) {
			log.error("绑定usb异常，原因:" + e.getMessage());
			return Result.error("绑定usb异常，原因:" + e.getMessage());
		}
		return Result.error("绑定usb失败，请解绑其他虚拟机上usb");
	}

	@AutoLog(value = "解绑usb")
	@ApiOperation(value="解绑usb", notes="解绑usb")
	@PostMapping(value = "/unbindUsb")
	public Result<?> unbindUsb(@RequestParam(name="id") String id) {
		try{
			if (StringUtils.isNotBlank(id)) {
				if (vmSerportExdevRelationService.unbindUsb(id)) {
					return Result.OK("解绑USB成功！");
				}
			}
		} catch (Exception e) {
			log.error("解绑USB异常，原因:" + e.getMessage());
			return Result.error("解绑USB异常，原因:" + e.getMessage());
		}
		return Result.error("解绑USB失败！");
	}


}
