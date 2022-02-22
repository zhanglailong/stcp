package com.sqa.chn.modules.access.service.impl;

import cn.icesun.util.entityutil.EntityUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.creator.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.javatuples.Triplet;
import com.sqa.chn.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import com.sqa.chn.modules.access.dto.CeShiYongLi4ExistVo;
import com.sqa.chn.modules.access.entity.*;
import com.sqa.chn.modules.access.service.*;
import com.sqa.chn.modules.task.entity.RunningCase;
import com.sqa.chn.modules.task.entity.RunningCaseStep;
import com.sqa.chn.modules.task.entity.RunningTask;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS(AccessDatabaseServiceImpl.DB_NAME)
public class AccessDatabaseServiceImpl implements IAccessDatabaseService {

    public static final String DB_NAME = "access-db";

    @Autowired
    private DruidDataSourceCreator druidDataSourceCreator;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IWenTiBaoGaoDanService wenTiBaoGaoDanService;

    @Autowired
    private ICeShiGuoChengService ceShiGuoChengService;

    @Autowired
    private ICeShiXiangBiaoService ceShiXiangBiaoService;

    @Autowired
    private ICeShiYongLiService ceShiYongLiService;

    @Autowired
    private IBeiCeDuiXiangBiaoService beiCeDuiXiangBiaoService;

    @Autowired
    private ICeShiLeiXingBiaoService ceShiLeiXingBiaoService;

    @Autowired
    private ICeShiYongLiYuCeShiXiangGuanXiBiaoService ceShiYongLiYuCeShiXiangGuanXiBiaoService;

    private DynamicRoutingDataSource getDynamicRoutingDataSource(){
        return applicationContext.getBean(DynamicRoutingDataSource.class);
    }

    @Override
    public DataSource getAccessDataSourceInstance(String dbFilePath) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setJndiName(DB_NAME);
        dataSourceProperty.setPoolName(DB_NAME);
        dataSourceProperty.setDruid(druidDataSourceCreator.getDruidConfig());
        dataSourceProperty.setUrl("jdbc:ucanaccess://{dbFilePath};openExclusive=false;ignoreCase".replace("{dbFilePath}", dbFilePath));
        dataSourceProperty.setDriverClassName("net.ucanaccess.jdbc.UcanaccessDriver");
        DataSource dataSource = druidDataSourceCreator.createDataSource(dataSourceProperty);
        return dataSource;
    }

    @Override
    public void updateAccessDataSource(DataSource dataSource) {
        if (isAccessDataSourceExist()){
            try {
                getDynamicRoutingDataSource().getDataSource(DB_NAME).getConnection().close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            getDynamicRoutingDataSource().removeDataSource(DB_NAME);
        }
        getDynamicRoutingDataSource().addDataSource(DB_NAME, dataSource);

    }

    @Override
    public void test() {
        IPage<CeShiXiang4CeShiYongLiVo> pageR = pageCeShiXiang4CeShiYongLiVo4LikeCeShiXiangFullName("维护", new Page<>(0, 100));

        //        List<WenTiBaoGaoDan> wenTiBaoGaoDanList = wenTiBaoGaoDanService.list();
//        List<CeShiGuoCheng> ceShiGuoChengList = ceShiGuoChengService.list();
//        List<CeShiXiangBiao> ceShiXiangBiaoList = ceShiXiangBiaoService.list();
//        List<CeShiYongLi> ceShiYongLiList = ceShiYongLiService.list();
//        List<RunningQuestion> runningQuestionList = wenTiBaoGaoDanList
//                .stream()
//                .map(i->EntityUtil.syncEntity(i, RunningQuestion.class)
//                        .toBuilder()
//                        .caseId(ceShiGuoChengList
//                                .stream()
//                                .filter(j->i.getWenTiID().equals(j.getWenTiBaoGaoDanID()))
//                                .map(j->j.getCeShiYongLiID())
//                                .findFirst()
//                                .orElseThrow(RuntimeException::new)
//                        )
////                        .status(i.getWenTiChuZhiQingKuang())
//                        .build()
//                )
//                .collect(Collectors.toList());
//        List<RunningCaseStep> runningCaseStepList = ceShiGuoChengList
//                .stream()
//                .map(i->EntityUtil.syncEntity(i, RunningCaseStep.class)
//                )
//                .collect(Collectors.toList());
//        List<RunningTask> runningTaskList = ceShiXiangBiaoList
//                .stream()
//                .map(i->{
//                    RunningTask runningTask = EntityUtil.syncEntity(i, RunningTask.class);
//                    String typeId = i.getSuoShuCeShiLeiXingID();
//                    if (typeId.equals("0")){
//                        return runningTask;
//                    }
//                    CeShiLeiXingBiao ceShiLeiXingBiao = ceShiLeiXingBiaoService.list(Wrappers.<CeShiLeiXingBiao>query().lambda().eq(CeShiLeiXingBiao::getCeShiLeiXingID, typeId)).stream().findFirst().orElse(null);
//                    if (ceShiLeiXingBiao == null){
//                        return runningTask;
//                    }
//                    BeiCeDuiXiangBiao beiCeDuiXiangBiao = beiCeDuiXiangBiaoService.list(Wrappers.<BeiCeDuiXiangBiao>query().lambda().eq(BeiCeDuiXiangBiao::getBeiCeDuiXiangID, ceShiLeiXingBiao.getSuoShuBeiCeDuiXiangID())).stream().findFirst().orElse(null);
//                    if (beiCeDuiXiangBiao == null){
//                        return runningTask;
//                    }
//
//                    if (beiCeDuiXiangBiao.getBeiCeDuiXiangMingCheng() != null){
//                        runningTask.setTaskSoftName(beiCeDuiXiangBiao.getBeiCeDuiXiangMingCheng());
//                    }
//                    if (beiCeDuiXiangBiao.getBanBen() != null){
//                        runningTask.setTaskSoftVersion(beiCeDuiXiangBiao.getBanBen());
//                    }
//                    return runningTask;
//                })
//                .collect(Collectors.toList());
//        List<RunningCase> runningCaseList = ceShiYongLiList
//                .stream()
//                .map(i->{
//                    RunningCase runningCase = EntityUtil.syncEntity(i, RunningCase.class);
//
//                    runningCase.setDelFlag(0);
//
//                    return runningCase;
//                })
//                .collect(Collectors.toList());
        return;
    }

    private boolean isAccessDataSourceExist(){
        if (!getDynamicRoutingDataSource().getCurrentDataSources().containsKey(DB_NAME)){
            return false;
        }
        return true;
    }

    private List<CeShiXiangBiao> listCeShiXiangBiao4CeShiLeiXingId(String ceShiLeiXingId){
        List<CeShiXiangBiao> ceShiXiangBiaoResultList = new ArrayList<>();
        List<CeShiXiangBiao> ceShiXiangBiaoList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().eq(CeShiXiangBiao::getSuoShuCeShiLeiXingID, ceShiLeiXingId));
        ceShiXiangBiaoResultList.addAll(ceShiXiangBiaoList);
        while (ceShiXiangBiaoList.size() > 0){
            Set<String> parentIdList = ceShiXiangBiaoList.stream().map(CeShiXiangBiao::getCeShiXiangID).collect(Collectors.toSet());
            ceShiXiangBiaoList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().in(CeShiXiangBiao::getFuJieDianID, parentIdList));
            ceShiXiangBiaoResultList.addAll(ceShiXiangBiaoList);
        }
        return ceShiXiangBiaoResultList;
    }

    private List<CeShiXiangBiao> listCeShiXiangBiao4CeShiLeiXingIds(List<String> ceShiLeiXingIds){
        List<CeShiXiangBiao> ceShiXiangBiaoList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().in(CeShiXiangBiao::getSuoShuCeShiLeiXingID, ceShiLeiXingIds));
        List<CeShiXiangBiao> ceShiXiangBiaoResultList = new ArrayList<>(ceShiXiangBiaoList);
        while (ceShiXiangBiaoList.size() > 0){
            Set<String> parentIdList = ceShiXiangBiaoList.stream().map(CeShiXiangBiao::getCeShiXiangID).collect(Collectors.toSet());
            ceShiXiangBiaoList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().in(CeShiXiangBiao::getFuJieDianID, parentIdList));
            ceShiXiangBiaoResultList.addAll(ceShiXiangBiaoList);
        }
        return ceShiXiangBiaoResultList;
    }

    private IPage<CeShiXiangBiao> pageCeShiXiangBiao4LikeCeShiXiangMingCheng(String likeCeShiXiangMingCheng, IPage<CeShiXiangBiao> page){
        if (likeCeShiXiangMingCheng == null || likeCeShiXiangMingCheng.trim().length() <= 0){
            return page;
        }
        return ceShiXiangBiaoService.page(page, Wrappers.<CeShiXiangBiao>query().lambda().like(CeShiXiangBiao::getCeShiXiangMingCheng, likeCeShiXiangMingCheng));
    }

    private List<CeShiYongLi> listCeShiYongLi4CeShiYongLiIds(List<String> ceShiYongLiIds){
        if (ceShiYongLiIds == null || ceShiYongLiIds.size() <= 0){
            return new ArrayList<>();
        }
        return ceShiYongLiService.list(Wrappers.<CeShiYongLi>query().lambda().in(CeShiYongLi::getCeShiYongLiID, ceShiYongLiIds));
    }

    private Map<String, String> mapCeShiYongLiId2CeShiXiangId4CeShiXiangIds(List<String> ceShiXiangIds){
        if (ceShiXiangIds == null || ceShiXiangIds.size() <= 0){
            return new HashMap<>();
        }
        List<CeShiYongLiYuCeShiXiangGuanXiBiao> ceShiYongLiYuCeShiXiangGuanXiBiaoList = ceShiYongLiYuCeShiXiangGuanXiBiaoService.list(Wrappers.<CeShiYongLiYuCeShiXiangGuanXiBiao>query().lambda().in(CeShiYongLiYuCeShiXiangGuanXiBiao::getCeShiXiangID, ceShiXiangIds));
        if (ceShiYongLiYuCeShiXiangGuanXiBiaoList == null){
            return new HashMap<>();
        }
        return ceShiYongLiYuCeShiXiangGuanXiBiaoList.stream().collect(Collectors.toMap(i->i.getCeShiYongLiID(), i->i.getCeShiXiangID()));
    }

    private Map<String, String> mapCeShiYongLiId2CeShiXiangId4CeShiYongLiIds(List<String> ceShiYongLiIds){
        if (ceShiYongLiIds == null || ceShiYongLiIds.size() <= 0){
            return new HashMap<>();
        }
        List<CeShiYongLiYuCeShiXiangGuanXiBiao> ceShiYongLiYuCeShiXiangGuanXiBiaoList = ceShiYongLiYuCeShiXiangGuanXiBiaoService.list(Wrappers.<CeShiYongLiYuCeShiXiangGuanXiBiao>query().lambda().in(CeShiYongLiYuCeShiXiangGuanXiBiao::getCeShiYongLiID, ceShiYongLiIds));
        if (ceShiYongLiYuCeShiXiangGuanXiBiaoList == null){
            return new HashMap<>();
        }
        return ceShiYongLiYuCeShiXiangGuanXiBiaoList.stream().collect(Collectors.toMap(i->i.getCeShiYongLiID(), i->i.getCeShiXiangID()));
    }

    /**
     * 通过测试项名称模糊查询所有符合条件的测试项并获得他的测试用例列表
     * @param likeFullName 模糊查询测试项全名称
     * @param page
     * @return
     */
    public IPage<CeShiXiang4CeShiYongLiVo> pageCeShiXiang4CeShiYongLiVo4LikeCeShiXiangFullName(String likeFullName, IPage<?> page){
        //IPage<CeShiXiangBiao> pageR = pageCeShiXiangBiao4LikeCeShiXiangMingCheng(likeCeShiXiangMingCheng, page);
        IPage<CeShiXiang4CeShiYongLiVo> pageR = ceShiXiangBiaoService.pageCeShiXiang4CeShiYongLiVo4LikeFullName(page, likeFullName);
        List<String> ceShiXiangIds = pageR.getRecords().stream().map(i->i.getCeShiXiangID()).collect(Collectors.toList());
        Map<String, String> ceShiYongLiId4CeShiXiangIdMap = mapCeShiYongLiId2CeShiXiangId4CeShiXiangIds(ceShiXiangIds);
        List<CeShiYongLi> ceShiYongLiList = listCeShiYongLi4CeShiYongLiIds(ceShiYongLiId4CeShiXiangIdMap.keySet().stream().collect(Collectors.toList()));
        if (ceShiYongLiList == null){
            ceShiYongLiList = new ArrayList<>();
        }
        List<String> existCeShiYongLiIds = listExistCeShiYongLiIds(ceShiYongLiList.stream().map(i->i.getCeShiYongLiID()).collect(Collectors.toList()));
        Map<String, List<CeShiYongLi>> ceShiYongLi4CeShiXiangIdMap = ceShiYongLiList.stream().collect(Collectors.groupingBy(i->ceShiYongLiId4CeShiXiangIdMap.get(i.getCeShiYongLiID())));
        pageR.getRecords().forEach(i->{
            i.setCeShiYongLiList(ceShiYongLi4CeShiXiangIdMap.getOrDefault(i.getCeShiXiangID(), new ArrayList<>()).stream().map(j->{
                CeShiYongLi4ExistVo ceShiYongLi4ExistVo = new CeShiYongLi4ExistVo();
                BeanUtils.copyProperties(j, ceShiYongLi4ExistVo);
                ceShiYongLi4ExistVo.setExist(existCeShiYongLiIds.contains(j.getCeShiYongLiID()));
                return ceShiYongLi4ExistVo;
            }).collect(Collectors.toList()));
        });
        return pageR;
//        return new Page<CeShiXiang4CeShiYongLiVo>(
//                pageR.getCurrent(),
//                pageR.getSize(),
//                pageR.getTotal(),
//                pageR.isSearchCount())
//                .setRecords(pageR.getRecords().stream().map(i->{
//                    CeShiXiang4CeShiYongLiVo ceShiXiang4CeShiYongLiVo = new CeShiXiang4CeShiYongLiVo();
//                    BeanUtils.copyProperties(i, ceShiXiang4CeShiYongLiVo);
//                    ceShiXiang4CeShiYongLiVo.setCeShiYongLiList(ceShiYongLi4CeShiXiangIdMap.getOrDefault(i.getCeShiXiangID(), new ArrayList<>()));
//                    return ceShiXiang4CeShiYongLiVo;
//                }).collect(Collectors.toList()));
    }

    private List<String> listExistCeShiYongLiIds(List<String> ids){
        if (ids == null || ids.size() <= 0){
            return new ArrayList<>();
        }
        List<CeShiYongLi> ceShiYongLiList = ceShiYongLiService.list(Wrappers.<CeShiYongLi>query().lambda().in(CeShiYongLi::getCeShiYongLiID, ids));
        if (ceShiYongLiList == null || ceShiYongLiList.size() <= 0){
            return new ArrayList<>();
        }
        List<String> listR = ceShiYongLiList.stream().map(i->i.getCeShiYongLiID()).collect(Collectors.toList());
        if (listR == null){
            listR = new ArrayList<>();
        }
        return listR;
    }

    private List<CeShiYongLi> listCeShiYongLi4Ids(List<String> ids){
        if (ids == null || ids.size() <= 0){
            return new ArrayList<>();
        }
        return ceShiYongLiService.list(Wrappers.<CeShiYongLi>query().lambda().in(CeShiYongLi::getCeShiYongLiID, ids));
    }

    private List<CeShiXiangBiao> listCeShiXiang4CeShiYongLiIds(List<String> ceShiYongLiIds){
        if (ceShiYongLiIds == null || ceShiYongLiIds.size() <= 0){
            return new ArrayList<>();
        }
        List<CeShiYongLiYuCeShiXiangGuanXiBiao> ceShiYongLiYuCeShiXiangGuanXiBiaoList = ceShiYongLiYuCeShiXiangGuanXiBiaoService.list(Wrappers.<CeShiYongLiYuCeShiXiangGuanXiBiao>query().lambda().in(CeShiYongLiYuCeShiXiangGuanXiBiao::getCeShiYongLiID, ceShiYongLiIds));
        if (ceShiYongLiYuCeShiXiangGuanXiBiaoList == null || ceShiYongLiYuCeShiXiangGuanXiBiaoList.size() <= 0){
            return new ArrayList<>();
        }
        Set<String> ceShiXiangIds = ceShiYongLiYuCeShiXiangGuanXiBiaoList.stream().map(i->i.getCeShiXiangID()).collect(Collectors.toSet());
        List<CeShiXiangBiao> ceShiXiangBiaoList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().in(CeShiXiangBiao::getCeShiXiangID, ceShiXiangIds));
        if (ceShiXiangBiaoList == null || ceShiXiangBiaoList.size() <= 0){
            return new ArrayList<>();
        }
        List<String> parentIds = ceShiXiangBiaoList.stream().map(i->i.getFuJieDianID()).filter(i->i != null && !i.equals("0")).collect(Collectors.toList());
        if (parentIds.size() <= 0){
            return ceShiXiangBiaoList;
        }
        List<CeShiXiangBiao> ceShiXiangBiaoParentList = ceShiXiangBiaoService.list(Wrappers.<CeShiXiangBiao>query().lambda().in(CeShiXiangBiao::getCeShiXiangID, parentIds));
        Map<String, CeShiXiangBiao> ceShiXiangBiaoParent4IdMap = ceShiXiangBiaoParentList.stream().collect(Collectors.toMap(i->i.getCeShiXiangID(), i->i));
        ceShiXiangBiaoList.forEach(i->{
            if (ceShiXiangBiaoParent4IdMap.containsKey(i.getFuJieDianID())){
                i.setSuoShuCeShiLeiXingID(ceShiXiangBiaoParent4IdMap.get(i.getFuJieDianID()).getSuoShuCeShiLeiXingID());
            }
        });
        return ceShiXiangBiaoList;
    }

    private List<CeShiGuoCheng> listCeShiGuoCheng4CeShiYongLiIds(List<String> ceShiYongLiIds){
        if (ceShiYongLiIds == null || ceShiYongLiIds.size() <= 0){
            return new ArrayList<>();
        }
        return ceShiGuoChengService.list(Wrappers.<CeShiGuoCheng>query().lambda().in(CeShiGuoCheng::getCeShiYongLiID, ceShiYongLiIds));
    }

    private Map<String, BeiCeDuiXiangBiao> mapCeShiLeiXingId2BeiCeDuiXiang4CeShiLeiXingIds(Set<String> ceShiLeiXingIds){
        if (ceShiLeiXingIds == null || ceShiLeiXingIds.size() <= 0){
            return new HashMap<>();
        }
        List<CeShiLeiXingBiao> ceShiLeiXingBiaoList = ceShiLeiXingBiaoService.list(Wrappers.<CeShiLeiXingBiao>query().lambda().in(CeShiLeiXingBiao::getCeShiLeiXingID, ceShiLeiXingIds));
        if (ceShiLeiXingBiaoList == null || ceShiLeiXingBiaoList.size() <= 0){
            return new HashMap<>();
        }
        Map<String, String> beiCeDuiXiangId4CeShiLeiXingIdMap = ceShiLeiXingBiaoList.stream().collect(Collectors.toMap(i->i.getCeShiLeiXingID(), i->i.getSuoShuBeiCeDuiXiangID()));
        List<BeiCeDuiXiangBiao> beiCeDuiXiangBiaoList = beiCeDuiXiangBiaoService.list(Wrappers.<BeiCeDuiXiangBiao>query().lambda().in(BeiCeDuiXiangBiao::getBeiCeDuiXiangID, beiCeDuiXiangId4CeShiLeiXingIdMap.values()));
        if (beiCeDuiXiangBiaoList == null || beiCeDuiXiangBiaoList.size() <= 0){
            return new HashMap<>();
        }
        Map<String, BeiCeDuiXiangBiao> beiCeDuiXiang4IdMap = beiCeDuiXiangBiaoList.stream().collect(Collectors.toMap(i->i.getBeiCeDuiXiangID(), i->i));

        return beiCeDuiXiangId4CeShiLeiXingIdMap.entrySet().stream().collect(Collectors.toMap(i->i.getKey(),i->beiCeDuiXiang4IdMap.getOrDefault(i.getValue(), new BeiCeDuiXiangBiao())));
    }


    @Override
    public Triplet<List<RunningCase>, List<RunningTask>, List<RunningCaseStep>> getNeedSyncData2MySQL(List<String> ids, String projectId, String turnId, String turnVerId) {
        List<CeShiYongLi> ceShiYongLiList = listCeShiYongLi4CeShiYongLiIds(ids);
        List<CeShiGuoCheng> ceShiGuoChengList = listCeShiGuoCheng4CeShiYongLiIds(ids);
        List<CeShiXiangBiao> ceShiXiangBiaoList = listCeShiXiang4CeShiYongLiIds(ids);

        Set<String> ceShiLeiXingIds = ceShiXiangBiaoList.stream().map(i->i.getSuoShuCeShiLeiXingID()).filter(Objects::nonNull).filter(i->!i.equals("0")).collect(Collectors.toSet());
        Map<String, BeiCeDuiXiangBiao> beiCeDuiXiangBiao4CeShiLeiXingIdMap = mapCeShiLeiXingId2BeiCeDuiXiang4CeShiLeiXingIds(ceShiLeiXingIds);

        Map<String, String> ceShiYongLiId4CeShiXiangIdMap = mapCeShiYongLiId2CeShiXiangId4CeShiYongLiIds(ids);

        List<RunningCase> runningCaseList = ceShiYongLiList
                .stream()
                .map(i->{
                    RunningCase runningCase = EntityUtil.syncEntity(i, RunningCase.class);
                    runningCase.setDelFlag(0);
                    if (ceShiYongLiId4CeShiXiangIdMap.containsKey(i.getCeShiYongLiID())){
                        runningCase.setTestTaskId(ceShiYongLiId4CeShiXiangIdMap.get(i.getCeShiYongLiID()));
                    }
                    return runningCase;
                })
                .collect(Collectors.toList());
        List<RunningTask> runningTaskList = ceShiXiangBiaoList
                .stream()
                .map(i->{
                    RunningTask runningTask = EntityUtil.syncEntity(i, RunningTask.class);
                    runningTask.setProjectId(projectId);
                    runningTask.setTurnId(turnId);
                    runningTask.setTurnVersion(turnVerId);
                    if (beiCeDuiXiangBiao4CeShiLeiXingIdMap.containsKey(i.getSuoShuCeShiLeiXingID())){
                        BeiCeDuiXiangBiao beiCeDuiXiangBiao = beiCeDuiXiangBiao4CeShiLeiXingIdMap.get(i.getSuoShuCeShiLeiXingID());
                        if (beiCeDuiXiangBiao.getBeiCeDuiXiangMingCheng() != null){
                            runningTask.setTaskSoftName(beiCeDuiXiangBiao.getBeiCeDuiXiangMingCheng());
                        }
                        if (beiCeDuiXiangBiao.getBanBen() != null){
                            runningTask.setTaskSoftVersion(beiCeDuiXiangBiao.getBanBen());
                        }
                    }
                    return runningTask;
                })
                .collect(Collectors.toList());
        List<RunningCaseStep> runningCaseStepList = ceShiGuoChengList
                .stream()
                .map(i->EntityUtil.syncEntity(i, RunningCaseStep.class)
                )
                .collect(Collectors.toList());

        return Triplet.with(runningCaseList, runningTaskList, runningCaseStepList);
    }
}
