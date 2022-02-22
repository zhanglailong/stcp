package org.jeecg.modules.running.uut.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.running.uut.entity.RunningUutNode;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import java.util.Map;

/**
 * @Description: 被测对象流程节点表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface IRunningUutNodeService extends IService<RunningUutNode> {
	/**
	 * 查询主id
	 * @param mainId true
	 * @return List
	 * */
	public List<RunningUutNode> selectByMainId(String mainId);

	/**
	 * 查询流程节点列表，添加编辑节点弹窗初始化需要，查询时如果id有值不能查询自己的节点,最后得到select下拉框需要的数据源
	 * @param params true
	 * @return 没有返回值
	 * */
	public List<Map<String,Object>> selectNodeListByIdAndMainId(RunningUutNode params);

	/**
	 * 更新节点数据
	 * @param params true
	 * @return 没有返回值
	 * */
	public Result<?> updateNodeData(RunningUutNode params);

	/**
	 * 新增节点数据
	 * @param params true
	 * @return result集合
	 * */
	public Result<?> insertNodeData(RunningUutNode params);

	/**
	 * 删除节点数据
	 * @param params true
	 * @return result集合
	 * */
	public Result<?> deleteNodeData(RunningUutNode params);

	/**
	 * 删除临时节点数据,当新增流程时,如果创建了节点,但是页面中点击了取消,这时就需要删除主表id为null的节点数据
	 * @param params true
	 * @return result集合
	 * */
	public Result<?> deleteTempNodeData(RunningUutNode params);
}
