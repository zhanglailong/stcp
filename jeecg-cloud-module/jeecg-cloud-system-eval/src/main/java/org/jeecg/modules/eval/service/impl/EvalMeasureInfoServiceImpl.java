package org.jeecg.modules.eval.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;
import org.jeecg.modules.eval.mapper.EvalMeasureInfoMapper;
import org.jeecg.modules.eval.service.IEvalMeasureInfoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 评价体系度量信息表
 * @Author: jeecg-boot
 * @Date: 2021-02-26
 * @Version: V1.0
 */
@Service
public class EvalMeasureInfoServiceImpl extends ServiceImpl<EvalMeasureInfoMapper, EvalMeasureInfo> implements IEvalMeasureInfoService {
	
	@Override
    public String selectParentNameByPid(String pId) {
		return this.getBaseMapper().selectParentNameByPid(pId);
    }
	
	@Override
	public String selectGrandNameByGid(String gId) {
		return this.getBaseMapper().selectGrandNameByGid(gId);
	}
	
	@Override
	public String selectParentIdByPname(String pName) {
		return this.getBaseMapper().selectParentIdByPname(pName);
	}
	
	@Override
	public String selectGrandIdByGname(String gName) {
		return this.getBaseMapper().selectGrandIdByGname(gName);
	}
	
	@Override
	public String selectMethodNameById(String id) {
		return this.getBaseMapper().selectMethodNameById(id);
	}
	
	@Override
	public String selectMethodIdByName(String name) {
		return this.getBaseMapper().selectMethodIdByName(name);
	}
	
	@Override
	public String selectFormulaById(String id,String systemId) {
		return this.getBaseMapper().selectFormulaById(id,systemId);
	}

	@Override
    public String selectNameById(String id) {
	    return this.getBaseMapper().selectNameById(id);
    }

	@Override
	public List<EvalMeasureInfo> getListBySystemId(String systemId) {
		return this.getBaseMapper().getListBySystemId(systemId);
	}

	@Override
	public List<Map<String, Object>> getTreeDataMix() {
		List<Map<String, Object>> list = this.getTreeOne();
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> map : list) {
			Map<String, Object> resultMap1 = new HashMap<>();
			resultMap1.put("id", map.get("id"));
			resultMap1.put("name", map.get("name"));
			List<Map<String, Object>> result1 = new ArrayList<>();
			List<Map<String, Object>> list1 =
					this.getTreeTwo((String) map.get("id"));
			for (Map<String, Object> map1 : list1) {
				Map<String, Object> resultMap2 = new HashMap<>();
				resultMap2.put("id", map1.get("id"));
				resultMap2.put("name", map1.get("name"));
				List<Map<String, Object>> list2 =
						this.getTreeThree((String) map1.get("id"));
				resultMap2.put("children", list2);
				result1.add(resultMap2);
			}
			resultMap1.put("children", result1);
			result.add(resultMap1);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getTreeData() {
		List<Map<String, Object>> list = this.getTreeOne();
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> map : list) {
			Map<String, Object> resultMap1 = new HashMap<>();
			resultMap1.put("id", map.get("id"));
			resultMap1.put("name", map.get("name"));
			List<Map<String, Object>> result1 = new ArrayList<>();
			List<Map<String, Object>> list1 =
					this.getTreeTwo((String) map.get("id"));
			for (Map<String, Object> map1 : list1) {
				Map<String, Object> resultMap2 = new HashMap<>();
				resultMap2.put("id", map1.get("id"));
				resultMap2.put("name", map1.get("name"));
				List<Map<String, Object>> list2 =
						this.getTreeThree((String) map1.get("id"));
				resultMap2.put("children", list2);
				result1.add(resultMap2);
			}
			resultMap1.put("children", result1);
			result.add(resultMap1);
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getTreeOne(){
		return this.getBaseMapper().getTreeOne();
	}

	@Override
	public List<Map<String, Object>> getTreeTwo(String grandId){
		return this.getBaseMapper().getTreeTwo(grandId);
	}

	@Override
	public List<Map<String, Object>> getTreeThree(String parentId){
		return this.getBaseMapper().getTreeThree(parentId);
	}

	@Override
	public EvalMeasureStructureVo getEvalMeasuerStructureVo(String id){
		return this.getBaseMapper().getEvalMeasuerStructureVo(id);
	}
}
