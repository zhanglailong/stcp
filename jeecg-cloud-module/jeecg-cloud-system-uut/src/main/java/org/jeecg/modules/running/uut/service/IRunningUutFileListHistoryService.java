package org.jeecg.modules.running.uut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.running.uut.entity.RunningUutFileListHistory;

import java.util.List;

public interface IRunningUutFileListHistoryService extends IService<RunningUutFileListHistory> {

    public List<RunningUutFileListHistory> getFileNames(String uutVersionId);

    public List<RunningUutFileListHistory> getFileByUutListIdAndUutVersionId(String uutListId, String uutVersionId);

}
