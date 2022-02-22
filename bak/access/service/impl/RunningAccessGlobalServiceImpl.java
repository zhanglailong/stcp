package org.jeecg.modules.access.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.modules.access.entity.RunningAccessGlobal;
import org.jeecg.modules.access.mapper.RunningAccessGlobalMapper;
import org.jeecg.modules.access.service.IRunningAccessGlobalService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Access相关全局变量 服务实现类
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-14
 */
@Service
public class RunningAccessGlobalServiceImpl extends ServiceImpl<RunningAccessGlobalMapper, RunningAccessGlobal> implements IRunningAccessGlobalService {

    public static final String KEY_DATA_SOURCE_FILE_PATH = "dataSourceFilePath";


    public String getValue4DataSourceFilePath(){
        if (0 == this.count(Wrappers.<RunningAccessGlobal>query().lambda().eq(RunningAccessGlobal::getKey, KEY_DATA_SOURCE_FILE_PATH))){
            throw new RuntimeException("数据源文件路径未配置");
        }
        return this.getById(KEY_DATA_SOURCE_FILE_PATH).getValue();
    }

    @Override
    public void setValue4DataSourceFilePath(String value) {
        if (0 == this.count(Wrappers.<RunningAccessGlobal>query().lambda().eq(RunningAccessGlobal::getKey, KEY_DATA_SOURCE_FILE_PATH))){
            this.save(RunningAccessGlobal.builder().key(KEY_DATA_SOURCE_FILE_PATH).value(value).build());
        }
        else {
            this.update(Wrappers.<RunningAccessGlobal>update().lambda().eq(RunningAccessGlobal::getKey, KEY_DATA_SOURCE_FILE_PATH).set(RunningAccessGlobal::getValue, value));
        }
    }
}
