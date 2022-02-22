package org.jeecg.modules.plan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.openstack.entity.StackList;
import org.jeecg.modules.plan.entity.AgentInfo;
import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.plan.entity.VmProject;
import org.jeecg.modules.plan.entity.VmProjectVo;
import org.jeecg.modules.running.monitortools.entity.RunningToolsAndVm;
import java.util.List;

/**
 * 虚拟机设计
 * 2021-01-06
 * V1.0
 *
 * @author zlf
 */
public interface IVmDesignService extends IService<VmDesign> {

    /**
     * 根据规划id查询虚拟机集合
     *
     * @param envId 规划id
     * @return 根据规划id查询虚拟机集合
     */
    List<VmDesign> getVmListByEnvId(String envId);

    /**
     * 根据id获取虚拟机信息
     *
     * @param vmId 虚拟机id
     * @return 根据id获取虚拟机信息
     */
    VmDesign getVmByVmId(String vmId);

    /**
     * 根据id集合获取虚拟机集合
     *
     * @param ids id集合
     * @return 根据id集合获取虚拟机集合
     */
    List<VmDesign> getVmListByIds(List<String> ids);

    /**
     * 虚拟机启动还是停止
     *
     * @param ids  id集合
     * @param type 启动还是停止
     * @throws Exception void
     */
    void sendVmOrder(String ids, Integer type) throws Exception;

    /**
     * 添加
     *
     * @param planJson  规划json数据
     * @param envId     环境id
     * @param resources 栈资源列表 虚拟机id集合
     * @return 布尔值
     */
    boolean getSaveOrUpByJsonAndPid(String createBy, String planJson, String envId, String envName, List<StackList.Resources> resources);

    /**
     * 获取状态为build的虚拟机集合
     *
     * @return 集合
     */
    List<VmDesign> getStateBuildServer();

    /**
     * 获取虚拟机下所有的测试工具
     */
    List<RunningToolsAndVm> toolList(String vmId);
    /**
     * 获取虚拟机数据
     *
     * @return 集合
     */
    List<VmDesign> getHandleVm();

    /**
     * 创建虚拟机快照
     * @param vmId 虚拟机表主键id
     * @param name 快照/备份名称
     * @param type 0快照/1备份
     * @param typeString 快照/备份
     * @return 状态
     */
    Boolean vmSnapshot(String vmId,String name,Integer type , String typeString);

    /**
     * 虚拟机+项目 联合查询
     * @param vmProjectVo
     * @return
     */
    List<VmProject> getEnvList(VmProjectVo vmProjectVo);
    /**
     * 虚拟机-快照/备份恢复
     * @param vmId 虚拟机表主键id
     * @param stackId openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @return 状态
     */
    Boolean restore(String envId,String vmId,String stackId,String snapshotId);
    /**
     * 删除环境快照 单个
     * @param stackId openstack栈环境id
     * @param snapshotId 环境快照/备份id
     * @param id 平台环境快照
     * @return boolean
     */
    Boolean deleteDestroy(String id,String stackId,String snapshotId);

    /**
     * 通过客户端ip 查询 项目id,工具id,环境id,客户端名称，客户端状态。
     * @param agentIp 客户端ip
     * @return agentInfo
     */
    AgentInfo queryByAgentIp(String agentIp);

}
