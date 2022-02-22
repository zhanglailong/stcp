package org.jeecg.modules.running.uut.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.running.uut.entity.RunningUutNode;
import org.jeecg.modules.running.uut.entity.RunningUutCatagory;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 被测对象流程分类表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface IRunningUutCatagoryService extends IService<RunningUutCatagory> {

	/**
	 * 添加一对多
	 * @param runningUutCatagory true
	 * @param runningUutNodeList true
	 * @return result集合
	 *
	 */
	public Result<?> saveMain(RunningUutCatagory runningUutCatagory, List<RunningUutNode> runningUutNodeList) ;
	
	/**
	 * 修改一对多
	 * @param runningUutCatagory true
	 * @param runningUutNodeList true
	 * @return result集合
	 */
	public Result<?> updateMain(RunningUutCatagory runningUutCatagory,List<RunningUutNode> runningUutNodeList);
	
	/**
	 * 删除一对多
	 * @param id true
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 * @param idList true
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
	/**
	 * 查询下一个节点信息
	 * @param uutType true
	 * @param nodeId true
	 * @return 没有返回值
	 */
	public RunningUutNode findNextStep (String uutType, String nodeId);
	
}
