package org.jeecg.modules.running.uut.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.running.uut.entity.RunningUutListHistory;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @Description: 被测对象操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
public interface IRunningUutListHistoryService extends IService<RunningUutListHistory> {
    /**
     * 查询被测对象操作历史记录
     * @param page true
     * @param params true
     * @return 没有返回值
     * */
    IPage<Map<String,Object>> getRunningUutOperationList(Page page, RunningUutListHistory params);
}
