package org.jeecg.modules.openstack.service;

import org.jeecg.modules.openstack.entity.StackQueue;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.IOException;
import java.util.List;

/**
 * 堆栈序列-处理openstack相关接口
 *
 * @author zlf
 * 2021-05-14
 * V1.0
 */
public interface IStackQueueService extends IService<StackQueue> {

    /**
     * 布尔值
     *
     * @param name        环境名称
     * @param type        处理类型-0环境1虚拟机
     * @param handleId    处理类型表的id
     * @param openStackId openStackId
     * @param status      处理状态
     * @return 布尔值
     */
    boolean saveOne(String name, int type, String handleId, String openStackId, String status);

    /**
     * 获取队列数据
     *
     * @return 集合
     */
    List<StackQueue> getHandleQueue();

    /**
     * 镜像删除
     * @param imageId 镜像id
     * @param id
     * @return boolean
     */
    Boolean deleteImages(String id ,String imageId);

    /**
     * 删除环境快照 单个
     * @param stackId openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @param id 平台环境快照
     * @return boolean
     */
    Boolean deleteDestroy(String id,String stackId,String snapshotId);

    /**
     * 环境销毁
     * @param ids 平台环境id
     * @return boolean
     */
    List<String> deleteEnv(String ids);


    /**
     * 删除虚拟机
     * @param serverId  虚拟机id
     * @param id 平台虚拟机id
     * @return boolean
     */
    Boolean deleteVm(String id,String serverId);

    /**
     * 虚拟机挂起
     * @param id 虚拟机表主键id
     * @param serverId 虚拟机id
     * @return boolean
     */
    Boolean suspendVm(String id, String serverId);

    /**
     *虚拟机挂起恢复
     * @param id 虚拟机表主键id
     * @param serverId 虚拟机id
     * @return boolean
     */
    Boolean resumeVm(String id, String serverId);

    /**
     * jmeter使用
     * @param planName 测试名称
     * @param numThreads 线程数
     * @param loops 循环次数
     * @param url 域名或者ip
     * @param port 端口号
     * @param httpRequest 请求方式
     * @param duration 并发时间
     * @param path 测试用例路径
     * @param request 请求参数
     * @param jmeterPath 测试用例地址
     * @param id 测试工具表主键id
     * @return boolean
     * @throws IOException
     */
    Boolean getJmeter(String planName,int numThreads,String loops,String url,String port,String httpRequest,int duration,String path,String request,String jmeterPath,String id)throws IOException;

    /**
     * 调整虚拟机配置
     * @param id 虚拟机表主键
     * @param flavor 调整参数
     * @return
     */
    Boolean getVmAlter(String id,String flavor,String size,String rollback);

    /**
     * 创建单独虚拟机
     * @param name 虚拟机名称
     * @param image 镜像名或者id
     * @param flavor 虚拟机规格
     * @param rollback 是否回滚
     * @param size 磁盘
     * @return 创建单独虚拟机
     */
    Boolean getServerCompute(String name,String image,String flavor,String rollback,String size ,String envName, String envId,String projectId);

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
    Boolean getServerAction(String id,String serverId,String stackName,String stackStatus,String remark,String name,String sysType,String otherSoftware,String testTools,String systemVersion);

    /**
     * 环境的恢复-快照/备份恢复
     * @param envId 定制环境id
     * @param stackId openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @return 状态
     */
    Boolean restore(String envId,String stackId,String snapshotId);
    /**
     * 创建环境快照
     * @param stackId openstack栈环境id
     * @param envId 定制环境id
     * @param name 快照/备份名称
     * @param type 0快照/1备份
     * @param typeString 快照/备份
     * @return 状态
     */
    Boolean snapshot(String stackId,String envId,String name,Integer type , String typeString);

    /**
     * appScan 测试工具
     * @param path 测试地址
     * @param id monitorTools主键
     * @return true
     */
    Boolean getAppScan(String id,String path);

    /**
     * UnderStand 测试工具
     * @param id
     * @param path
     * @return true
     */
    Boolean getUnderStand(String id,String path);
}
