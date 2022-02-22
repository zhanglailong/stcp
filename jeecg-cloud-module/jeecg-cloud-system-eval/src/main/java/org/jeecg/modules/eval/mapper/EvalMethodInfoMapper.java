package org.jeecg.modules.eval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.eval.entity.EvalMethodInfo;

import java.util.List;

/**
 * @Description: eval_method_info
 * @Author: jeecg-boot
 * @Date: 2021-03-01
 * @Version: V1.0
 */
public interface EvalMethodInfoMapper extends BaseMapper<EvalMethodInfo> {
    /**
     * 通过Id删除
     * @param mainId /
     * @return /
     */
    boolean deleteByMainId(@Param("mainId") String mainId);
    /**
     * 通过Id查询
     * @param mainId /
     * @return /
     */
    List<EvalMethodInfo> selectByMainId(@Param("mainId") String mainId);
    /**
     * 通过Id查询
     * @param methodId /
     * @param score /
     * @return String /
     */
    String getEvaluate(@Param("methodId") String methodId, @Param("score") Double score);
}
