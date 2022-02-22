package org.jeecg.modules.number.service.impl;

import org.jeecg.modules.number.entity.NumberTypeInfo;
import org.jeecg.modules.number.mapper.NumberTypeInfoMapper;
import org.jeecg.modules.number.service.INumberTypeInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 编号信息表
 * @Author: jeecg-boot
 * @Date:   2021-03-15
 * @Version: V1.0
 */
@Service
public class NumberTypeInfoServiceImpl extends ServiceImpl<NumberTypeInfoMapper, NumberTypeInfo> implements INumberTypeInfoService {

}
