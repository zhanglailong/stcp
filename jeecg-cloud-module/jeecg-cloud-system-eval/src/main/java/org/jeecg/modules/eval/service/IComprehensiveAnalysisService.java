package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.eval.vo.CompAnalysisMainInfoVO;

/**
 * @Description: 评价体系公式信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface IComprehensiveAnalysisService extends IService<CompAnalysisMainInfoVO> {
    /**
     * analysisProcess
     * @param resultDataId true
     * @throws Exception
     * @return 无返回值
     * */
    Result<?> analysisProcess(String resultDataId) throws Exception;
}
