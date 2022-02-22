package org.jeecg.modules.eval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.eval.entity.EvalFormula;
import org.jeecg.modules.eval.entity.EvalMeasureInfo;
import org.jeecg.modules.eval.entity.EvalMeasureInfoVo;
import org.jeecg.modules.eval.entity.EvalMeasureStructureVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 评价体系度量信息表
 * @Author: jeecg-boot
 * @Date: 2021-02-26
 * @Version: V1.0
 */
public interface EvalMeasureInfoMapper extends BaseMapper<EvalMeasureInfo> {
	/**
	 * 查询列表
	 * @param pId true
	 * @return 无返回值
	 * */
	String selectParentNameByPid(String pId);
	/**
	 * 查询列表
	 * @param gId true
	 * @return 无返回值
	 * */
	String selectGrandNameByGid(String gId);
	/**
	 * 查询列表
	 * @param pName true
	 * @return 无返回值
	 * */
	String selectParentIdByPname(String pName);

	/**
	 * 通过Gname查询GrandId
	 * @param gName /
	 * @return /
	 */
	String selectGrandIdByGname(String gName);

	/**
	 * 通过Id查询MethodName
	 * @param id /
	 * @return /
	 */
	String selectMethodNameById(String id);
	/**
	 * 通过Name查询MethodId
	 * @param name /
	 * @return /
	 */
	String selectMethodIdByName(String name);
	/**
	 * 通过Id查询Name
	 * @param id /
	 * @return /
	 */
	String selectNameById(String id);
	/**
	 * 通过Id查询Formula
	 * @param id /
	 * @param systemId
	 * @return /
	 */
	String selectFormulaById(String id,String systemId);

	/**
	 * 根据systemId 查询所有实体
	 * @param systemId
	 * @return
	 */
	public List<EvalMeasureInfo> getListBySystemId(@Param("systemId") String systemId);

	/**
	 * @return List<Map<String, Object>>
	 */
	@Select("select MAX(grand_id) as id, MAX(grand_name) as name " +
			"from eval_measure_structure " +
			"where del_flag = 0 " +
			"group by grand_id, grand_name " +
			"order by grand_id")
	List<Map<String, Object>> getTreeOne();

	/**
	 * @return List<Map<String, Object>>
	 */
	@Select("select MAX(parent_id) as id, MAX(parent_name) as name " +
			"from eval_measure_structure " +
			"where del_flag = 0 and grand_id = #{grandId} " +
			"group by parent_id, parent_name " +
			"order by parent_id")
	List<Map<String, Object>> getTreeTwo(String grandId);

	/**
	 * @return List<Map<String, Object>>
	 */
	@Select("select e.id, e.name, f.formula " +
			"from eval_measure_structure e " +
			"left join eval_formula f on e.formula = f.id " +
			"where del_flag = 0 and parent_id = #{parentId}")
	List<Map<String, Object>> getTreeThree(String parentId);

	/**
	 * @return List<Map<String, Object>>
	 */
	@Select("select id, name, parent_id, parent_name, grand_id, grand_name, formula " +
			"from eval_measure_structure " +
			"where del_flag = 0 and id = #{id}")
	EvalMeasureStructureVo getEvalMeasuerStructureVo(String id);
}
