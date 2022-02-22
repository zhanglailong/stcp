package org.jeecg.modules.running.uut.service;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 被测件附件更动表
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
public interface IRunningUutFileListService extends IService<RunningUutFileList> {

    List<RunningUutFileList> getFileListByUutListId(String uutListId);

    Integer deleteByUutListId(String uutListId);

    List<DictModel> getUutVersionOptions(String uutListId);

    List<RunningUutFileList> getByFileNameAndFileType(String fileName, String fileType, String uutListId);

}
