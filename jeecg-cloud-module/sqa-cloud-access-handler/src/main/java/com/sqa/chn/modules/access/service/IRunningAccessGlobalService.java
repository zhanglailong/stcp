package com.sqa.chn.modules.access.service;

import com.sqa.chn.modules.access.entity.RunningAccessGlobal;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Access相关全局变量 服务类
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-14
 */
public interface IRunningAccessGlobalService extends IService<RunningAccessGlobal> {
    public String getValue4DataSourceFilePath();

    public void setValue4DataSourceFilePath(String value);

}
