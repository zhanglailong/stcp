package org.jeecg.modules.cloudtools.runex.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.modules.cloudtools.runex.entity.RunParamsSet;
import org.jeecg.modules.cloudtools.runex.mapper.RunParamsSetMapper;
import org.jeecg.modules.cloudtools.runex.service.IRunParamsSetService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 设置工具可用参数
 * @Author: jeecg-boot
 * @Date:   2021-03-12
 * @Version: V1.0
 */
@Service
public class RunParamsSetServiceImpl extends ServiceImpl<RunParamsSetMapper, RunParamsSet> implements IRunParamsSetService {

	@Override
	public List<RunParamsSet> getByControlId(String controlId) {
		QueryWrapper<RunParamsSet> wrapper = new QueryWrapper<>();
		wrapper.eq("control_set_id", controlId).orderByAsc("`order`");
		return this.list(wrapper);
	}

	@Override
	public List<RunParamsSet> getParamsList(QueryWrapper<RunParamsSet> queryWrapper) {

		List<RunParamsSet> setList = this.list(queryWrapper);

		return getParamsJson(setList);
	}

	private List<RunParamsSet> getParamsJson(List<RunParamsSet> setList){

		List<RunParamsSet> setResList = new ArrayList<>();

		Map<String,List<RunParamsSet>> map = new HashMap<>();
		//物以类聚
		for(RunParamsSet item: setList){
			item.setKey(item.getId());
			if(!map.keySet().contains(item.getCharge())){
				map.put(item.getCharge(),new ArrayList<>());
			}
			if(StringUtils.isEmpty(item.getCharge())){
				setResList.add(item);
			}
			map.get(item.getCharge()).add(item);
		}
		//人以群分
		setResList.forEach(item->{
			item.setChildren(setParamSet(map,map.get(item.getId())));
		});

		return setResList;
	}

	private List<RunParamsSet> setParamSet(Map<String,List<RunParamsSet>> map,List<RunParamsSet> res){
		if(res == null){
			return null;
		}
		res.forEach(item->{

			if(map.keySet().contains(item.getId())){
				setParamSet(map,map.get(item.getId()));
				item.setChildren(map.get(item.getId()));
			}

		});

		return res;
	}

}
