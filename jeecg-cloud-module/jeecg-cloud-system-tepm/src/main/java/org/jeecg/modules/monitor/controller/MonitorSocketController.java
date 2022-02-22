package org.jeecg.modules.monitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.monitor.entity.MonitorSocket;
import org.jeecg.modules.monitor.fegin.SocketServerFegin;
import org.jeecg.modules.monitor.service.IMonitorSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: 监控服务管理
 * @Author: jeecg-boot
 * @Date: 2021-01-19
 * @Version: V1.0
 */
@Api(tags = "监控服务管理")
@RestController
@RequestMapping("/socket/monitor")
@Slf4j
public class MonitorSocketController extends JeecgController<MonitorSocket, IMonitorSocketService> {
    @Autowired
    private IMonitorSocketService monitorSocketService;

    @Autowired
    private SocketServerFegin socketServerFegin;

    /**
     * 分页列表查询
     *
     * @param monitorSocket
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "监控服务管理-分页列表查询")
    @ApiOperation(value = "监控服务管理-分页列表查询", notes = "监控服务管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(MonitorSocket monitorSocket,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<MonitorSocket> queryWrapper = QueryGenerator.initQueryWrapper(monitorSocket, req.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        queryWrapper.eq("create_by", sysUser.getUsername()).eq("idel", "1");
        queryWrapper.isNotNull("agent_ip");
        Page<MonitorSocket> page = new Page<MonitorSocket>(pageNo, pageSize);
        IPage<MonitorSocket> pageList = monitorSocketService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param monitorSocket
     * @return
     */
    @AutoLog(value = "监控服务管理-添加")
    @ApiOperation(value = "监控服务管理-添加", notes = "监控服务管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody MonitorSocket monitorSocket) {
        monitorSocketService.save(monitorSocket);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param monitorSocket
     * @return
     */
    @AutoLog(value = "监控服务管理-编辑")
    @ApiOperation(value = "监控服务管理-编辑", notes = "监控服务管理-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody MonitorSocket monitorSocket) {
        monitorSocketService.updateById(monitorSocket);
        return Result.OK("编辑成功!");
    }

    /**
     * 修改主服务表的收集数据时间
     *
     * @param
     * @return
     */
    @AutoLog(value = "修改主服务表收集时间")
    @ApiOperation(value = "修改主服务表收集时间", notes = "修改主服务表收集时间")
    @GetMapping(value = "/updateCoolecTime")
    public Result<?> queryByIp(String coolectTime) {
        QueryWrapper<MonitorSocket> virCollectQueryWrapper = new QueryWrapper<>();
        virCollectQueryWrapper.eq("coolectTime", coolectTime);
        monitorSocketService.update(virCollectQueryWrapper);
        return Result.OK("修改成功");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监控服务管理-通过id删除")
    @ApiOperation(value = "监控服务管理-通过id删除", notes = "监控服务管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        monitorSocketService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "监控服务管理-批量删除")
    @ApiOperation(value = "监控服务管理-批量删除", notes = "监控服务管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.monitorSocketService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "监控服务管理-通过id查询")
    @ApiOperation(value = "监控服务管理-通过id查询", notes = "监控服务管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        MonitorSocket monitorSocket = monitorSocketService.getById(id);
        if (monitorSocket == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(monitorSocket);
    }

    /**
     * 更新定时采集数据时间
     *
     * @param
     * @return
     */
    @AutoLog(value = "监控服务管理-更新定时采集数据时间")
    @ApiOperation(value = "监控服务管理-更新定时采集数据时间", notes = "监控服务管理-更新定时采集数据时间")
    @GetMapping(value = "/updateCollectTime")
    public Result<?> updateCollectTime(@RequestParam String time) {
        MonitorSocket monitorSocket = null;
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        System.out.println("updateCollectTime:" + time);
        try {
            monitorSocket = monitorSocketService.getOne(new QueryWrapper<MonitorSocket>(
            ).eq("create_by", sysUser.getUsername()).isNull("agent_ip"
            ).eq("idel", "1").eq("state", "1"));
            if (monitorSocket != null && StringUtils.isNotEmpty(monitorSocket.getId())) {
                if (StringUtils.isNotEmpty(time)) {
                    monitorSocket.setCollectTime(time);
                    monitorSocketService.updateById(monitorSocket);
                }
            } else {
                monitorSocket = new MonitorSocket();
                monitorSocket.setIdel("1");
                monitorSocket.setCollectTime("5");
                monitorSocket.setState("1");
                monitorSocketService.save(monitorSocket);
            }
            // 通知主监控服务更新时间
            socketServerFegin.updateCoolecTime(sysUser.getUsername(), monitorSocket.getCollectTime());
        } catch (Exception e) {
            log.info("更新定时采集数据时间异常:{}", e.getMessage());
            return Result.error("更新定时采集数据时间异常:" + e.getMessage());
        }
        return Result.OK(monitorSocket);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param monitorSocket
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MonitorSocket monitorSocket) {
        return super.exportXls(request, monitorSocket, MonitorSocket.class, "监控服务管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MonitorSocket.class);
    }

}
