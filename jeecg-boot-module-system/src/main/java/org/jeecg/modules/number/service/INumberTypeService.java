package org.jeecg.modules.number.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.number.entity.NumberType;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 编号管理表
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface INumberTypeService extends IService<NumberType> {
    /**dq add 新增和编辑提交时，增加code重复校验
     * @param numberType true
     * @return 没有返回值
     * */
    Result checkBeforeAddAndEdit(NumberType numberType);
}
