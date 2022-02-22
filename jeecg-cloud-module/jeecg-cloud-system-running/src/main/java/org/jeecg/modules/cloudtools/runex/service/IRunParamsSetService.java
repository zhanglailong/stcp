package org.jeecg.modules.cloudtools.runex.service;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 设置工具可用参数
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
public interface IRunParamsSetService extends IService<RunParamsSet> {
	
	public List<RunParamsSet> getByControlId(String controlId);

	public List<RunParamsSet> getParamsList(QueryWrapper<RunParamsSet> queryWrapper);
	
}
