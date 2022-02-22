package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.model.DuplicateCheckVo;
import org.jeecg.modules.system.model.TreeSelectModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 字典表 Mapper 接口
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Mapper
public interface SysDictMapper extends BaseMapper<SysDict> {
	
	/**
	  *  重复检查SQL
	 * @param duplicateCheckVo true
	 * @return 没有返回值
	 */
	public Long duplicateCheckCountSql(DuplicateCheckVo duplicateCheckVo);
	/**
	 *  重复检查SQL
	 * @param duplicateCheckVo true
	 * @return 没有返回值
	 */
	public Long duplicateCheckCountSqlNoDataId(DuplicateCheckVo duplicateCheckVo);
	/**
	 *  重复检查SQL
	 * @param code true
	 * @return List<DictModel>
	 */
	public List<DictModel> queryDictItemsByCode(@Param("code") String code);

	 /** @Deprecated */
	/**
	 *  重复检查SQL
	 * @param table true
	 * @param code true
	 * @param text true
	 * @return List<DictModel>
	 */
	public List<DictModel> queryTableDictItemsByCode(@Param("table") String table,@Param("text") String text,@Param("code") String code);

	 /** @Deprecated */
	/**
	 *  重复检查SQL
	 * @param table true
	 * @param code true
	 * @param text true
	 * @param filterSql true
	 * @return List<DictModel>
	 */
	public List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("filterSql") String filterSql);

	 /** @Deprecated */
	@Select("select ${key} as \"label\",${value} as \"value\" from ${table}")
	/**
	 *  重复检查SQL
	 * @param table true
	 * @param key true
	 * @param value true
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,String>> getDictByTableNgAlain(@Param("table") String table, @Param("key") String key, @Param("value") String value);
	/**
	 *  通过key值遍历
	 * @param code true
	 * @param key true
	 * @return 没有返回值
	 */
	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

	 /** @Deprecated */
	/**
	 *  通过key值遍历
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param key true
	 * @return 没有返回值
	 */
	public String queryTableDictTextByKey(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("key") String key);

	 /** @Deprecated */
	/**
	 *  通过key值遍历
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param keyArray true
	 * @return List<DictModel>
	 */
	public List<DictModel> queryTableDictByKeys(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyArray") String[] keyArray);

	/**
	 * 查询所有部门 作为字典信息 id -->value,departName -->text
	 * @return List<DictModel>
	 */
	public List<DictModel> queryAllDepartBackDictModel();
	
	/**
	 * 查询所有用户  作为字典信息 username -->value,realname -->text
	 * @return List<DictModel>
	 */
	public List<DictModel> queryAllUserBackDictModel();

	/** @Deprecated */
	/**
	 * 通过关键字查询出字典表
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param keyword true
	 * @return List<DictModel>
	 */
	public List<DictModel> queryTableDictItems(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("keyword") String keyword); 

	 /** @Deprecated */
	/**
	 * 根据表名、显示字段名、存储字段名 查询树
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param pid true
	 * @param hasChildField true
	 * @param query true
	 * @param pidField true
	 * @return List<TreeSelectModel>
	 */
	List<TreeSelectModel> queryTreeList(@Param("query") Map<String, String> query,@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("pidField") String pidField,@Param("pid") String pid,@Param("hasChildField") String hasChildField);


	@Select("delete from sys_dict where id = #{id}")
	/**
	 * 删除
	 * @param id true
	 */
	public void deleteOneById(@Param("id") String id);

	@Select("select * from sys_dict where del_flag = 1")
	/**
	 * 查询被逻辑删除的数据
	 * @return  List<SysDict>
	 */
	public List<SysDict> queryDeleteList();

	@Update("update sys_dict set del_flag = #{flag,jdbcType=INTEGER} where id = #{id,jdbcType=VARCHAR}")
	/**
	 * 修改状态值
	 * @param delFlag true
	 * @param id true
	 */
	public void updateDictDelFlag(@Param("flag") int delFlag, @Param("id") String id);

	 /** @Deprecated */
	/**
	 * 分页查询字典表数据
	 * @param page true
	 * @param query true
	 * @return Page<DictModel>
	 */
	public Page<DictModel> queryDictTablePageList(Page page, @Param("query") DictQuery query);
}
