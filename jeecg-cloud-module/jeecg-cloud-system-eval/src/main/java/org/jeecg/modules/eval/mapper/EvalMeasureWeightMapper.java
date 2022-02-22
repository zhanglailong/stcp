package org.jeecg.modules.eval.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureInfoVo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;

import java.util.List;

/**
 * @Description: eval_measure_weight
 * @Author: jeecg-boot
 * @Date: 2021-02-25
 * @Version: V1.0
 */
@Mapper
public interface EvalMeasureWeightMapper extends BaseMapper<EvalMeasureWeight> {
    /**
     * 获取树状数据
     * @param systemId true
     * @return 没有返回值
     * */
    List<EvalMeasureInfoVo> queryTreeList(@Param("systemId") String systemId);
    /**
     * 获取数组
     * @param id true
     * @return 没有返回值
     * */
    EvalMeasureStructureVo queryCount(@Param("id") String id);
    /**
     * 获取树状数据
     * @param grandId true
     * @param systemId true
     * @param parentId true
     * @return 没有返回值
     * */
    EvalMeasureStructureVo queryGrand(@Param("grandId") String grandId,@Param("systemId") String systemId,@Param("parentId") String parentId);
    /**
     * 获取数据
     * @param systemId true
     * @param grandId true
     * @return 没有返回值
     * */
    EvalMeasureStructureVo queryGrandOne(@Param("grandId") String grandId,@Param("systemId") String systemId);

    /**
     * 根据systemId 查询所有实体
     * @param systemId
     * @return
     */
    public List<EvalMeasureWeight> getListBySystemId(@Param("systemId") String systemId);
}
