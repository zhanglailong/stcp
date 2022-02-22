package org.jeecg.modules.running.uut.mapper;

import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.running.uut.entity.RunningUutListHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 被测对象操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Mapper
public interface RunningUutListHistoryMapper extends BaseMapper<RunningUutListHistory> {
    /**
     * 查询被测对象操作历史记录
     * @param params true
     * @param page  true
     * @return  IPage<Map<String,Object>>
     * */
    IPage<Map<String,Object>> getRunningUutOperationList(Page page, @Param("params") RunningUutListHistory params);
}
