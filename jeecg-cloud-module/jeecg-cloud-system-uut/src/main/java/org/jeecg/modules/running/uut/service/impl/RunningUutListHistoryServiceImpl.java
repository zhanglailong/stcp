package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.running.uut.entity.RunningUutListHistory;
import org.jeecg.modules.running.uut.mapper.RunningUutListHistoryMapper;
import org.jeecg.modules.running.uut.service.IRunningUutListHistoryService;
import org.jeecg.modules.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Map;

/**
 * @Description: 被测对象操作历史表
 * @Author: jeecg-boot
 * @Date:   2021-03-22
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutListHistoryServiceImpl extends ServiceImpl<RunningUutListHistoryMapper, RunningUutListHistory> implements IRunningUutListHistoryService {

    @Autowired
    private RunningUutListHistoryMapper runningUutListHistoryMapper;
    @Autowired
    private ISysDictService dictService;

    @Override
    /**查询被测对象操作历史记录*/
    public IPage<Map<String,Object>> getRunningUutOperationList(Page page, RunningUutListHistory params)
    {
        IPage<Map<String,Object>> runningUutOperationList = runningUutListHistoryMapper.getRunningUutOperationList(page, params);
        /*资产库text需要从字典表中单独查一下*/
        for(Map<String, Object> mapObj : runningUutOperationList.getRecords())
        {
            String modifyField = (String) mapObj.get("modifyField");
            if(modifyField != null && "uutAssetsId".equals(modifyField))
            {
                String oldId = (String) mapObj.get("modifyFieldOldVale");
                String newId = (String) mapObj.get("modifyFieldVale");
                if(oldId != null)
                {
                    String oldText = dictService.queryDictTextByKey("getAssetsList", oldId);
                    mapObj.put("modifyFieldOldVale",oldText);
                }
                if(newId != null)
                {
                    String newText = dictService.queryDictTextByKey("getAssetsList", newId);
                    mapObj.put("modifyFieldVale",newText);
                }
            }
        }
        return runningUutOperationList;
    }
}
