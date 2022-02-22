package org.jeecg.modules.running.uut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.running.uut.entity.RunningUutListUser;

import java.util.List;

/**
 * @Description: 被测对象出库
 * @Author: jeecg-boot
 * @Date:   2021-8-4
 * @Version: V1.0
 */
public interface IRunningUutListUserService extends IService<RunningUutListUser> {

    List<RunningUutListUser> getUutUserById(String id);

}
