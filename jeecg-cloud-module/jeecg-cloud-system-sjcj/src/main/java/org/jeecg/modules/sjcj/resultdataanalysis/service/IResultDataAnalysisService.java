package org.jeecg.modules.sjcj.resultdataanalysis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.sjcj.resultdataanalysis.entity.ResultDataAnalysis;

/**
 * @Description: 结果数据分析
 * @Author: jeecg-boot
 * @Date:   2021-01-18
 * @Version: V1.0
 */
public interface IResultDataAnalysisService extends IService<ResultDataAnalysis> {
    /**
     * 添加
     * @param resultDataAnalysis true
     * @throws Exception
     * @return 没有返回值
     * */
    void add(ResultDataAnalysis resultDataAnalysis) throws Exception;
}
