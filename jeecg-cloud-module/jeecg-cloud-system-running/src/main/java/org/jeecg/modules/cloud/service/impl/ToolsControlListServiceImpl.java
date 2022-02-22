package org.jeecg.modules.cloud.service.impl;

import org.jeecg.modules.cloud.service.IToolsControlListService;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;
import org.jeecg.modules.cloud.entity.ToolsControlList;
import org.jeecg.modules.cloud.mapper.ToolsControlListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * @Description: 测试工具控制表
 * @Author: jeecg-boot
 * @Date:   2021-05-25
 * @Version: V1.0
 */
@Service
public class ToolsControlListServiceImpl extends ServiceImpl<ToolsControlListMapper, ToolsControlList> implements IToolsControlListService {
	
	@Autowired
	private IRunParamsSetService paramService;
	
	@Override
	public boolean save(ToolsControlList entity) {
        return SqlHelper.retBool(getBaseMapper().insert(entity));
    }
}
