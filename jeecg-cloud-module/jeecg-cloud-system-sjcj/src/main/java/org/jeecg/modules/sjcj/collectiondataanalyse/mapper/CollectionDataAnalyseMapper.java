package org.jeecg.modules.sjcj.collectiondataanalyse.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.sjcj.collectiondataanalyse.entity.CollectionDataAnalyse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 采集数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-08
 * @Version: V1.0
 */
@Mapper
public interface CollectionDataAnalyseMapper extends BaseMapper<CollectionDataAnalyse> {

    /**
     * 插入
     * @param param true
     * @return 无返回值
     * */
    void batchInster(List<CollectionDataAnalyse> param);

}
