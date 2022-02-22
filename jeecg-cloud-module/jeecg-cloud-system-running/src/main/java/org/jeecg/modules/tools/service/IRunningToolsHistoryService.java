package org.jeecg.modules.tools.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import org.jeecg.modules.tools.entity.RunningToolsHistory;

import java.util.Map;

/**
 * @Description: 测试工具操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
public interface IRunningToolsHistoryService extends IService<RunningToolsHistory> {
    /**
     * 获取运新工具列表
     * @param page true
     * @param params true
     * @return 没有返回值
     * */
    IPage<Map<String,Object>> getRunningToolsOperationList(Page page, RunningToolsHistory params);
}
