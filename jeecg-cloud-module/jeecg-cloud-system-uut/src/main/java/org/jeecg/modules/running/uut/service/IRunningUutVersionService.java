package org.jeecg.modules.running.uut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.running.uut.entity.RunningUutVersion;

public interface IRunningUutVersionService extends IService<RunningUutVersion> {

    String getUutVersionId(String uutListId, String version);

}
