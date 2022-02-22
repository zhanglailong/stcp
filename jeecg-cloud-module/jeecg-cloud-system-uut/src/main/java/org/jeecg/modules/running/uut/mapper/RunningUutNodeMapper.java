package org.jeecg.modules.running.uut.mapper;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.running.uut.entity.RunningUutNode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 被测对象流程节点表
 * @Author: jeecg-boot
 * @Date:   2020-12-24
 * @Version: V1.0
 */
public interface RunningUutNodeMapper extends BaseMapper<RunningUutNode> {
	/**
	 * 删除
	 * @param mainId true
	 * @return 布尔值
	 * */
	public boolean deleteByMainId(@Param("mainId") String mainId);
	/**
	 * 查询
	 * @param mainId true
	 * @return  List<RunningUutNode>
	 * */
	public List<RunningUutNode> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 查询流程节点列表，添加编辑节点弹窗初始化需要，查询时如果id有值不能查询自己的节点,最后得到select下拉框需要的数据源
	 * @param params true
	 * @return List<Map<String,Object>>
	 *
	 * */
	public List<Map<String,Object>> selectNodeListByIdAndMainId(@Param("params") RunningUutNode params);
}
