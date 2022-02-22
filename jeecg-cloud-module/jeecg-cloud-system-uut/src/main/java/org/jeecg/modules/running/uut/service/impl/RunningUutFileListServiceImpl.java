package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.running.uut.entity.RunningUutFileList;
import org.jeecg.modules.running.uut.mapper.RunningUutFileListMapper;
import org.jeecg.modules.running.uut.service.IRunningUutFileListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 被测件附件更动表
 * @Author: jeecg-boot
 * @Date:   2021-08-20
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutFileListServiceImpl extends ServiceImpl<RunningUutFileListMapper, RunningUutFileList> implements IRunningUutFileListService {

    @Autowired
    private RunningUutFileListMapper runningUutFileListMapper;

    @Override
    public List<RunningUutFileList> getFileListByUutListId(String uutListId) {
        return runningUutFileListMapper.getFileListByUutListId(uutListId);
    }

    @Override
    public Integer deleteByUutListId(String uutListId) {
        return runningUutFileListMapper.deleteByUutListId(uutListId);
    }

    @Override
    public List<DictModel> getUutVersionOptions(String uutListId) {
        return runningUutFileListMapper.getUutVersionOptions(uutListId);
    }

    @Override
    public List<RunningUutFileList> getByFileNameAndFileType(String fileName, String fileType, String uutListId) {
        return runningUutFileListMapper.getByFileNameAndFileType(fileName,fileType,uutListId);
    }
}
