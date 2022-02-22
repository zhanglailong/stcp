package org.jeecg.modules.system.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.mapper.SysDictMapper;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.jeecg.modules.system.service.ISysDuplicateCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@DS("uutDatabase")
public class SysDuplicateCheckServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDuplicateCheckService {

    @Autowired
    private SysDictMapper sysDictMapper;

    @Override
    public Long getNum(DuplicateCheckVo duplicateCheckVo) {
        if (StringUtils.isNotBlank(duplicateCheckVo.getDataId())) {
            return sysDictMapper.duplicateCheckCountSql(duplicateCheckVo);
        } else {
            return sysDictMapper.duplicateCheckCountSqlNoDataId(duplicateCheckVo);
        }

    }
}
