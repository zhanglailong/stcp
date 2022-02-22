package org.jeecg.modules.eval.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.eval.entity.EvalMeasureWeight;
import org.jeecg.modules.eval.entity.EvalMethodInfo;
import org.jeecg.modules.eval.vo.CompAnalysisMainInfoVO;
import org.jeecg.modules.eval.vo.TemporaryAnalysisDataDto;

import java.util.List;
import java.util.Map;

/**
 * @Description: 评价体系公式信息表
 * @Author: jeecg-boot
 * @Date:   2021-04-14
 * @Version: V1.0
 */
public interface EvalComprehensiveAnalysisMapper extends BaseMapper<CompAnalysisMainInfoVO> {
    /**
     * 获取sustemId
     * @param systemId true
     * @return 没有返回值
     * */
    List<CompAnalysisMainInfoVO> selectMainInfo(@Param("systemId") String systemId);
    /**
     * 获取列表
     * @return 没有返回值
    */
    @DS("uutDatabase")
    List<TemporaryAnalysisDataDto> queryList();
    /**
     * 获取列表
     **/
    @DS("uutDatabase")
    void sqaAnalysisData(String uutId);
    /**
     * 获取systemId
     * @param systemId true
     * @return 没有返回值
     */
    List<EvalMeasureWeight> measureWeight(@Param("systemId") String systemId);
    /**
     * 获取methodID
     * @param methodId true
     * @return 没有返回值
     * */
    List<EvalMethodInfo> selectEvalMethodInfo(@Param("methodId") String methodId);
    /**
     * 获取ID
     * @param measureId true
     * @return 没有返回值
     * */
    List<String> selectCalcParam(@Param("measureId") String measureId);
    /**
     * 获得参数
     * @param methodId true
     * @param val true
     * @since  获取methodId和val值
     * @return 没有返回值
     */
    String calcRange(@Param("methodId") String methodId, @Param("val") double val);

}
