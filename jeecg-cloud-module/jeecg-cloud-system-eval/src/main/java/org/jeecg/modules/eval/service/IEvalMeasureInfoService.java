package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;

/**
 * @Description: 评价体系度量信息表
 * @Author: jeecg-boot
 * @Date: 2021-02-26
 * @Version: V1.0
 */
public interface IEvalMeasureInfoService extends IService<EvalMeasureInfo> {

	/**
	 * 查询关联姓名
	 * @param pId true
	 * @return 没有返回值
	 * */
	String selectParentNameByPid(String pId);
	/**
	 * 查询关联姓名
	 * @param gId true
	 * @return 没有返回值
	 * */
	String selectGrandNameByGid(String gId);
	/**
	 * 查询关联id
	 * @param pName true
	 * @return 没有返回值
	 * */
	String selectParentIdByPname(String pName);
	/**
	 * 查询关联id
	 * @param gName true
	 * @return 没有返回值
	 * */
	String selectGrandIdByGname(String gName);
	/**
	 * 查询方法名
	 * @param id true
	 * @return 没有返回值
	 * */
	String selectMethodNameById(String id);
	/**
	 * 查询方法id
	 * @param name true
	 * @return 没有返回值
	 * */
	String selectMethodIdByName(String name);
	/**
	 * 查询关联姓名
	 * @param id true
	 * @return 没有返回值
	 * */
	String selectNameById(String id);
	/**
	 * 查询关联姓名
	 * @param systemId true
	 * @param id true
	 * @return 没有返回值
	 * */
	String selectFormulaById(String id,String systemId);

	/**
	 * 根据systemId 查询所有实体
	 * @param systemId
	 * @return
	 */
	public List<EvalMeasureInfo> getListBySystemId(String systemId);

	/**
	 * 查询度量树状数据
	 */
	public List<Map<String, Object>> getTreeData();

	/**
	 * 查询度量树状数据
	 */
	public List<Map<String, Object>> getTreeDataMix();

	/**
	 * 查询度量树状数据one
	 */
	public List<Map<String, Object>> getTreeOne();

	/**
	 * 查询度量树状数据two
	 */
	public List<Map<String, Object>> getTreeTwo(String grandId);

	/**
	 * 查询度量树状数据three
	 */
	public List<Map<String, Object>> getTreeThree(String parentId);

	/**
	 * 查询度量结构数据
	 */
	public EvalMeasureStructureVo getEvalMeasuerStructureVo(String id);
}
