package org.jeecg.modules.tools.service.impl;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.tools.entity.RunningToolsHistory;
import org.jeecg.modules.tools.mapper.RunningToolsHistoryMapper;
import org.jeecg.modules.tools.service.IRunningToolsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 测试工具操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
@Service
public class RunningToolsHistoryServiceImpl extends ServiceImpl<RunningToolsHistoryMapper, RunningToolsHistory> implements IRunningToolsHistoryService {

    @Autowired
    private RunningToolsHistoryMapper runningToolsHistoryMapper;

    @Override
    public IPage<Map<String, Object>> getRunningToolsOperationList(Page page, RunningToolsHistory params) {
        IPage<Map<String,Object>> runningToolsHistory = runningToolsHistoryMapper.getRunningToolsOperationList(page,params);
        return  runningToolsHistory;
    }
}
