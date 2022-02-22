package org.jeecg.modules.oss.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.oss.entity.RunningUploadForm;
import org.jeecg.modules.oss.mapper.RunningUploadFormMapper;
import org.jeecg.modules.oss.service.IRunningUploadFormService;
import org.springframework.stereotype.Service;

@Service
public class RunningUploadFormService extends ServiceImpl<RunningUploadFormMapper, RunningUploadForm> implements IRunningUploadFormService {
}
