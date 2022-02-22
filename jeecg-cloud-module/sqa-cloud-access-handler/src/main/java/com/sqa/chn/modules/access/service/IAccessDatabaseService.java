package com.sqa.chn.modules.access.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.javatuples.Triplet;
import com.sqa.chn.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import com.sqa.chn.modules.task.entity.RunningCase;
import com.sqa.chn.modules.task.entity.RunningCaseStep;
import com.sqa.chn.modules.task.entity.RunningTask;

import javax.sql.DataSource;
import java.util.List;


public interface IAccessDatabaseService {

    /**
     * 获取Access数据源实例
     *
     * @param dbFilePath
     * @return
     */
    public DataSource getAccessDataSourceInstance(String dbFilePath);

    /**
     * 更新dynamic-druid多数据源
     *
     * @param dataSource
     */
    public void updateAccessDataSource(DataSource dataSource);

//    public void SyncAccess2Master();


    public void test();

    public IPage<CeShiXiang4CeShiYongLiVo> pageCeShiXiang4CeShiYongLiVo4LikeCeShiXiangFullName(String likeFullName, IPage<?> page);

    public Triplet<List<RunningCase>, List<RunningTask>, List<RunningCaseStep>> getNeedSyncData2MySQL(List<String> ids, String projectId, String turnId, String turnVerId);
}