package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.TreeSelectModel;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
public interface ISysDictService extends IService<SysDict> {
	/**
	 * 查询集合id
	 * @param code true
	 * @return list集合
	 * */
    public List<DictModel> queryDictItemsByCode(String code);
	/**
	 * 查询集合
	 * @return list集合
	 * */
    public Map<String,List<DictModel>> queryAllDictItems();

	/**
	 * 查询集合id
	 * @param table true
	 * @param text true
	 * @param code true
	 * @return list集合
	 * */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

	/**
	 * 查询集合id
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param filterSql true
	 * @return list集合
	 * */
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);
	/**
	 * 查询集合id
	 * @param code true
	 * @param key true
	 * @return 没有返回值
	 * */
    public String queryDictTextByKey(String code, String key);

	/**
	 * 查询
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param key true
	 * @return list集合
	 * */
	String queryTableDictTextByKey(String table, String text, String code, String key);

	/**
	 * 查询
	 * @param table true
	 * @param text true
	 * @param code true
	 * @param keys true
	 * @return list集合
	 * */
	List<String> queryTableDictByKeys(String table, String text, String code, String keys);

    /**
     * 根据字典类型删除关联表中其对应的数据
     *
     * @param sysDict true
     * @return 布尔类型
     */
    boolean deleteByDictId(SysDict sysDict);

    /**
     * 添加一对多
     * @param sysDict true
     * @param sysDictItemList true
     * @return 没有返回值
     */
    public Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);
    
    /**
	 * 查询所有部门 作为字典信息 id -->value,departName -->text
	 * @return 没有返回值
	 */
	public List<DictModel> queryAllDepartBackDictModel();
	
	/**
	 * 查询所有用户  作为字典信息 username -->value,realname -->text
	 * @return 没有返回值
	 */
	public List<DictModel> queryAllUserBackDictModel();
	
	/**
	 * 通过关键字查询字典表
	 * @param table true
	 * @param text  true
	 * @param code true
	 * @param keyword true
	 * @return 没有返回值
	 */
	 
	public List<DictModel> queryTableDictItems(String table, String text, String code,String keyword);
	
	/**
	  * 根据表名、显示字段名、存储字段名 查询树
	 * @param table true
	 * @param query true
	 * @param text true
	 * @param code true
	 * @param pidField true
	 * @param pid true
	 * @param hasChildField true
	 * @return 没有返回值
	 */
	 
	List<TreeSelectModel> queryTreeList(Map<String, String> query,String table, String text, String code, String pidField,String pid,String hasChildField);

	/**
	 * 真实删除
	 * @param id true
	 */
	public void deleteOneDictPhysically(String id);

	/**
	 * 修改delFlag
	 * @param delFlag true
	 * @param id true
	 */
	public void updateDictDelFlag(int delFlag,String id);

	/**
	 * 查询被逻辑删除的数据
	 * @return 返回List集合
	 */
	public List<SysDict> queryDeleteList();

	/**
	 * 分页查询
	 * @param query true
	 * @param pageSize true
	 * @param pageNo true
	 * @return list结合
	 */
	public List<DictModel> queryDictTablePageList(DictQuery query,int pageSize, int pageNo);

}
