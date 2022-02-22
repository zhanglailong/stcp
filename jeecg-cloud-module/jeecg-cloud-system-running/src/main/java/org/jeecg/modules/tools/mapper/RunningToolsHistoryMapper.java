package org.jeecg.modules.tools.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.tools.entity.RunningToolsHistory;

import java.util.Map;

/**
 * @Description: 测试工具操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-24
 * @Version: V1.0
 */
public interface RunningToolsHistoryMapper extends BaseMapper<RunningToolsHistory> {
    /**
     * 查询测试工具操作历史记录
     * @param page true
     * @param params true
     * @return  IPage<Map<String,Object>>
     * */
    IPage<Map<String,Object>> getRunningToolsOperationList(Page page, @Param("params") RunningToolsHistory params);
}
