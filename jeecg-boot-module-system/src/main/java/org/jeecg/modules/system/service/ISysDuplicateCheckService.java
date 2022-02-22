package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.model.DuplicateCheckVo;

public interface ISysDuplicateCheckService extends IService<SysDict> {

    Long getNum(DuplicateCheckVo duplicateCheckVo);

}
