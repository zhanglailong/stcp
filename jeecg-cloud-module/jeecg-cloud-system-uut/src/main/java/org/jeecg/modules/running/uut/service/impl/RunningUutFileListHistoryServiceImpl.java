package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import org.jeecg.modules.running.uut.entity.RunningUutFileListHistory;
import org.jeecg.modules.running.uut.mapper.RunningUutFileListHistoryMapper;
import org.jeecg.modules.running.uut.mapper.RunningUutFileListMapper;
import org.jeecg.modules.running.uut.service.IRunningUutFileListHistoryService;
import org.jeecg.modules.running.uut.service.IRunningUutFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 被测件附件更动履历表
 * @Author: jeecg-boot
 * @Date:   2021-08-27
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutFileListHistoryServiceImpl extends ServiceImpl<RunningUutFileListHistoryMapper, RunningUutFileListHistory> implements IRunningUutFileListHistoryService {

    @Autowired
    private RunningUutFileListHistoryMapper runningUutFileListHistoryMapper;

    @Override
    public List<RunningUutFileListHistory> getFileNames(String uutVersionId) {
        return runningUutFileListHistoryMapper.getFileNames(uutVersionId);
    }

    @Override
    public List<RunningUutFileListHistory> getFileByUutListIdAndUutVersionId(String uutListId, String uutVersionId) {
        return runningUutFileListHistoryMapper.getFileByUutListIdAndUutVersionId(uutListId, uutVersionId);
    }
}
