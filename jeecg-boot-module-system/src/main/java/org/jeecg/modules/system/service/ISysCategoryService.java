package org.jeecg.modules.system.service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.model.TreeSelectModel;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
public interface ISysCategoryService extends IService<SysCategory> {

	/**根节点父ID的值*/
	public static final String ROOT_PID_VALUE = "0";
	/**
	 * 添加角色事件
	 * @param sysCategory
	 * */
	void addSysCategory(SysCategory sysCategory);
	/**
	 * 修改角色时间
	 * @param sysCategory
	 * */
	void updateSysCategory(SysCategory sysCategory);
	
	/**
	  * 根据父级编码加载分类字典的数据
	 * @param pcode true
	 * @return 咩有返回值
	 * @throws JeecgBootException
	 * @return 没有返回值
	 */
	public List<TreeSelectModel> queryListByCode(String pcode) throws JeecgBootException;
	
	/**
	  * 根据pid查询子节点集合
	 * @param pid true
	 * @return 没有返回值
	 */
	public List<TreeSelectModel> queryListByPid(String pid);

	/**
	 * 根据pid查询子节点集合,支持查询条件
	 * @param pid true
	 * @param condition true
	 * @return 没有返回值
	 */
	public List<TreeSelectModel> queryListByPid(String pid, Map<String,String> condition);

	/**
	 * 根据code查询id
	 * @param code true
	 * @return 没有返回值
	 */
	public String queryIdByCode(String code);
	
}
