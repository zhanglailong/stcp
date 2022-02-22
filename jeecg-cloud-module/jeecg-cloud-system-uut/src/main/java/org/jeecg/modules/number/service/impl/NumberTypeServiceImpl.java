package org.jeecg.modules.number.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.number.entity.NumberType;
import org.jeecg.modules.number.mapper.NumberTypeMapper;
import org.jeecg.modules.number.service.INumberTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;

/**
 * @Description: 编号管理表
 * @Author: dq
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Service
public class NumberTypeServiceImpl extends ServiceImpl<NumberTypeMapper, NumberType> implements INumberTypeService {
    @Autowired
    private NumberTypeMapper numberTypeMapper;

    @Override
    /**dq add 新增和编辑提交时，增加code重复校验*/
    public Result checkBeforeAddAndEdit(NumberType numberType)
    {
        QueryWrapper<NumberType> queryWrapper = new QueryWrapper<NumberType>();
        queryWrapper.eq("code",numberType.getCode());
        // 不为空就是编辑操作，查询条件需要加上id
        if(numberType.getId() != null)
        {
            queryWrapper.ne("id",numberType.getId());
        }
        List<NumberType> numberTypeList = numberTypeMapper.selectList(queryWrapper);
        if(numberTypeList != null && numberTypeList.size() > 0)
        {
            return Result.error("编码已存在");
        }
        return Result.ok();
    }
}
