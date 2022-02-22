package org.jeecg.modules.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.test.entity.EnvCustomized;
import org.jeecg.modules.test.entity.EvnRunningVo;

import java.util.List;

/**
 * @author zlf
 * 测试环境定制与管理
 * 2020-12-23
 * V1.0
 */
public interface IEnvCustomizedService extends IService<EnvCustomized> {
    /**
     * 发送创建测试环境
     *
     * @param openstackJson 发送给openstack的json数据
     * @param planName      环境名称
     * @param envCustomized 环境对象
     * @param planJson 环境规划的json
     * @return 布尔值
     */
    boolean sendPlanToStack(String openstackJson, String planName, EnvCustomized envCustomized,String planJson);

    /**
     * 查找创建中的环境
     *
     * @return 正在创建环境集合
     */
    List<EnvCustomized> getCreateStacksByState();

    /**
     * 获取创建成功的环境集合
     *
     * @return 创建成功的环境集合
     */
    List<EnvCustomized> getCompleteStacksByState();

    /**
     * 虚拟机创建中的环境
     *
     * @return 虚拟机创建中的环境
     */
    List<EnvCustomized> getVirProgressStacksByState();

    /**
     * 环境挂起
     * @param id 环境id
     * @param stackId  openstack栈环境id
     * @return
     */
    boolean suspend (String id, String stackId);

    /**
     * 环境挂起恢复
     * @param id  环境id
     * @param stackId  openstack栈环境id
     * @return
     */
    boolean resume (String id, String stackId);

    /**
     * 项目+环境 关联查询
     * @param envCustomized
     * @return list
     */
    List<EvnRunningVo> getEnvList(EvnRunningVo envCustomized);

    /**
     * 手动创建测试环境
     */
    boolean manualEnv(String name,String image,String flavor,String rollback,String size,String envName,String environmentUse,String remarks,
                      String projectName,String projectId);
}
