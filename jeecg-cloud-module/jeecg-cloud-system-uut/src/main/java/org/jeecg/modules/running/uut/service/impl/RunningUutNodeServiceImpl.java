package org.jeecg.modules.running.uut.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.running.uut.entity.RunningUutNode;
import org.jeecg.modules.running.uut.mapper.RunningUutNodeMapper;
import org.jeecg.modules.running.uut.service.IRunningUutNodeService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

/**
 * @Description: 被测对象流程节点表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
@Service
@DS("uutDatabase")
public class RunningUutNodeServiceImpl extends ServiceImpl<RunningUutNodeMapper, RunningUutNode> implements IRunningUutNodeService {
	
	@Autowired
	private RunningUutNodeMapper runningUutNodeMapper;
	
	@Override
	public List<RunningUutNode> selectByMainId(String mainId) {
		return runningUutNodeMapper.selectByMainId(mainId);
	}

	/**查询流程节点列表，添加编辑节点弹窗初始化需要，查询时如果id有值不能查询自己的节点,最后得到select下拉框需要的数据源*/
	@Override
	public List<Map<String,Object>> selectNodeListByIdAndMainId(RunningUutNode params) {
		return runningUutNodeMapper.selectNodeListByIdAndMainId(params);
	}

	/**更新节点数据*/
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Result<?> updateNodeData(RunningUutNode params) {
		//节点标识
		String code = params.getCode();
		//节点名称
		String name = params.getName();
		//校验节点标识与节点名称是否重复
		QueryWrapper<RunningUutNode> queryWrapperOfCode = new QueryWrapper<RunningUutNode>();
		queryWrapperOfCode.eq("code",code);
		queryWrapperOfCode.eq("catagory_id",params.getCatagoryId());
		queryWrapperOfCode.ne("id",params.getId());
		RunningUutNode runningUutNodeOfCode = runningUutNodeMapper.selectOne(queryWrapperOfCode);
		if(runningUutNodeOfCode != null) {
			return Result.error("节点标识与已有标识重复");
		}
		QueryWrapper<RunningUutNode> queryWrapperOfName = new QueryWrapper<RunningUutNode>();
		queryWrapperOfName.eq("name",name);
		queryWrapperOfName.eq("catagory_id",params.getCatagoryId());
		queryWrapperOfName.ne("id",params.getId());
		RunningUutNode runningUutNodeOfName = runningUutNodeMapper.selectOne(queryWrapperOfName);
		if(runningUutNodeOfName != null) {
			return Result.error("节点名称与已有名称重复");
		}
		return Result.ok("节点数据更新成功",runningUutNodeMapper.updateById(params));
	}

	/**新增节点数据*/
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Result<?> insertNodeData(RunningUutNode params) {
		//节点标识
		String code = params.getCode();
		//节点名称
		String name = params.getName();
		//校验节点标识与节点名称是否重复
		QueryWrapper<RunningUutNode> queryWrapperOfCode = new QueryWrapper<RunningUutNode>();
		queryWrapperOfCode.eq("code",code);
		queryWrapperOfCode.eq("catagory_id",params.getCatagoryId());
		RunningUutNode runningUutNodeOfCode = runningUutNodeMapper.selectOne(queryWrapperOfCode);
		if(runningUutNodeOfCode != null) {
			return Result.error("节点标识与已有标识重复");
		}
		QueryWrapper<RunningUutNode> queryWrapperOfName = new QueryWrapper<RunningUutNode>();
		queryWrapperOfName.eq("name",name);
		queryWrapperOfName.eq("catagory_id",params.getCatagoryId());
		RunningUutNode runningUutNodeOfName = runningUutNodeMapper.selectOne(queryWrapperOfName);
		if(runningUutNodeOfName != null) {
			return Result.error("节点名称与已有名称重复");
		}
		//更新当前节点数据
		runningUutNodeMapper.insert(params);
		//当前插入的id
		String insertId = params.getId();
		//更新上一节点数据
		if(params.getPreNode() != null) {
			RunningUutNode preNode = runningUutNodeMapper.selectById(params.getPreNode());
			preNode.setNextNode(insertId);
			runningUutNodeMapper.updateById(preNode);
		}
		//更新下一节点数据
		if(params.getNextNode() != null) {
			RunningUutNode nextNode = runningUutNodeMapper.selectById(params.getNextNode());
			nextNode.setPreNode(insertId);
			runningUutNodeMapper.updateById(nextNode);
		}
		return Result.ok("节点数据新增成功",null);
	}

	/**删除节点数据*/
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Result<?> deleteNodeData(RunningUutNode params) {
		String preNode = params.getPreNode();
		String nextNode = params.getNextNode();
		String currentId = params.getId();
		//删除当前节点数据
		runningUutNodeMapper.deleteById(currentId);
		//更新上一节点数据,清空它的下一节点数据
		if(preNode != null) {
			RunningUutNode preData = runningUutNodeMapper.selectById(preNode);
			preData.setNextNode(null);
			runningUutNodeMapper.updateById(preData);
		}
		//更新下一节点数据,清空它的上一节点数据
		if(nextNode != null) {
			RunningUutNode nextData = runningUutNodeMapper.selectById(nextNode);
			nextData.setPreNode(null);
			runningUutNodeMapper.updateById(nextData);
		}
		return Result.ok("节点数据删除成功",null);
	}

	/**删除临时节点数据,当新增流程时,如果创建了节点,但是页面中点击了取消,这时就需要删除主表id为null的节点数据*/
	@Override
	@Transactional(rollbackOn = Exception.class)
	public Result<?> deleteTempNodeData(RunningUutNode params) {
		QueryWrapper<RunningUutNode> queryWrapper = new QueryWrapper<>();
		queryWrapper.isNull("catagory_id");
		runningUutNodeMapper.delete(queryWrapper);
		return Result.ok("临时节点数据删除成功",null);
	}
}
