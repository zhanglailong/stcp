package org.jeecg.modules.number.service;

import org.jeecg.modules.number.entity.NumberRuleInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
public interface INumberRuleInfoService extends IService<NumberRuleInfo> {

    /**
     * dq add,根据标识编码生成编号字符串
     * @param numberCode: number_type表中的code
     * @return 没有返回值
     */
    String generateNumberStrByNumberCode(String numberCode);
}
