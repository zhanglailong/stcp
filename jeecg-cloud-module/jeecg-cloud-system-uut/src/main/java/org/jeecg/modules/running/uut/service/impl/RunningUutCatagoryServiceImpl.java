package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.running.uut.entity.RunningUutCatagory;
import org.jeecg.modules.running.uut.entity.RunningUutNode;
import org.jeecg.modules.running.uut.mapper.RunningUutNodeMapper;
import org.jeecg.modules.running.uut.mapper.RunningUutCatagoryMapper;
import org.jeecg.modules.running.uut.service.IRunningUutCatagoryService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 被测对象流程分类表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutCatagoryServiceImpl extends ServiceImpl<RunningUutCatagoryMapper, RunningUutCatagory> implements IRunningUutCatagoryService {

	@Autowired
	private RunningUutCatagoryMapper runningUutCatagoryMapper;
	@Autowired
	private RunningUutNodeMapper runningUutNodeMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<?> saveMain(RunningUutCatagory runningUutCatagory, List<RunningUutNode> runningUutNodeList) {
		//校验流程标识是否重复
		QueryWrapper<RunningUutCatagory> codeWrapper = new QueryWrapper<>();
		codeWrapper.eq("code",runningUutCatagory.getCode());
		if(runningUutCatagoryMapper.selectOne(codeWrapper) != null) {
			return Result.error("流程标识与现有标识重复");
		}
		//校验流程名称是否重复
		QueryWrapper<RunningUutCatagory> nameWrapper = new QueryWrapper<>();
		nameWrapper.eq("name",runningUutCatagory.getName());
		if(runningUutCatagoryMapper.selectOne(nameWrapper) != null) {
			return Result.error("流程名称与现有名称重复");
		}
		//校验结束，开始正常逻辑
		runningUutCatagoryMapper.insert(runningUutCatagory);
		if(runningUutNodeList!=null && runningUutNodeList.size()>0) {
			for(RunningUutNode entity:runningUutNodeList) {
				//外键设置
				if(entity.getId() == null)
				{
					// entity.getId() == null代表节点数据之前没有插入,因为从表数据有可能是先插入的
					entity.setCatagoryId(runningUutCatagory.getId());
					runningUutNodeMapper.insert(entity);
				}
				else
				{
					// 从表数据如果之前插入了就是更新操作
					entity.setCatagoryId(runningUutCatagory.getId());
					runningUutNodeMapper.updateById(entity);
				}
			}
		}
		return Result.ok("添加成功！");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result<?> updateMain(RunningUutCatagory runningUutCatagory,List<RunningUutNode> runningUutNodeList) {
		//校验流程标识是否重复
		QueryWrapper<RunningUutCatagory> codeWrapper = new QueryWrapper<>();
		codeWrapper.eq("code",runningUutCatagory.getCode());
		codeWrapper.ne("id",runningUutCatagory.getId());
		if(runningUutCatagoryMapper.selectOne(codeWrapper) != null) {
			return Result.error("流程标识与现有标识重复");
		}
		//校验流程名称是否重复
		QueryWrapper<RunningUutCatagory> nameWrapper = new QueryWrapper<>();
		nameWrapper.eq("name",runningUutCatagory.getName());
		nameWrapper.ne("id",runningUutCatagory.getId());
		if(runningUutCatagoryMapper.selectOne(nameWrapper) != null) {
			return Result.error("流程名称与现有名称重复");
		}
		//校验结束，开始正常逻辑
		runningUutCatagoryMapper.updateById(runningUutCatagory);
		//1.先删除子表数据
		runningUutNodeMapper.deleteByMainId(runningUutCatagory.getId());
		//2.子表数据重新插入
		if(runningUutNodeList!=null && runningUutNodeList.size()>0) {
			for(RunningUutNode entity:runningUutNodeList) {
				//外键设置
				entity.setCatagoryId(runningUutCatagory.getId());
				runningUutNodeMapper.insert(entity);
			}
		}
		return Result.ok("编辑成功!");
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		runningUutNodeMapper.deleteByMainId(id);
		runningUutCatagoryMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			runningUutNodeMapper.deleteByMainId(id.toString());
			runningUutCatagoryMapper.deleteById(id);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RunningUutNode findNextStep(String uutType, String nodeId) {
		return runningUutCatagoryMapper.findNextStep(uutType, nodeId);
	}
	
}
