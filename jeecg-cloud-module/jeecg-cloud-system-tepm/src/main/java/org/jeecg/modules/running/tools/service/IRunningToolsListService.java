package org.jeecg.modules.running.tools.service;

import org.jeecg.modules.plan.entity.VmDesign;
import org.jeecg.modules.running.tools.entity.RunningToolsList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 测试工具列表
 * @Author: jeecg-boot
 * @Date:   2020-12-18
 * @Version: V1.0
 */
public interface IRunningToolsListService extends IService<RunningToolsList> {

    /**
     * 通过镜像中的测试工具编号，找到测试工具列表并添加
     */


    boolean setRunningToolListBytools(List<String> list, VmDesign vmDesign);

    boolean setRunningToolByTool();
}
