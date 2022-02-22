package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * <p>
 * 
 * @Author:Steve
 * @Since：   2019-01-22
 */
public interface ISysDepartService extends IService<SysDepart>{

    /**
     * 查询我的部门信息,并分节点进行显示
     * @param departIds true
     * @return  没有返回值
     */
    List<SysDepartTreeModel> queryMyDeptTreeList(String departIds);

    /**
     * 查询所有部门信息,并分节点进行显示
     * @return List
     */
    List<SysDepartTreeModel> queryTreeList();

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     * @return List
     */
    public List<DepartIdModel> queryDepartIdTreeList();

    /**
     * 保存部门数据
     * @param sysDepart true
     * @param username true
     */
    void saveDepartData(SysDepart sysDepart,String username);

    /**
     * 更新depart数据
     * @param sysDepart true
     * @param username true
     * @return 没有返回值
     */
    Boolean updateDepartDataById(SysDepart sysDepart,String username);
    
    /**
     * 根据关键字搜索相关的部门数据
     * @param keyWord true
     * @param myDeptSearch true
     * @param departIds true
     * @return 没有返回值
     */
    List<SysDepartTreeModel> searhBy(String keyWord,String myDeptSearch,String departIds);
    
    /**
     * 根据部门id删除并删除其可能存在的子级部门
     * @param id true
     * @return 没有返回值
     */
    boolean delete(String id);
    
    /**
     * 查询SysDepart集合
     * @param userId true
     * @return 没有返回值
     */
	public List<SysDepart> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     *
     * @param username true
     * @return 没有返回值
     */
    List<SysDepart> queryDepartsByUsername(String username);

	 /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     * @param ids true
     */
	void deleteBatchWithChildren(List<String> ids);

    /**
     *  根据部门Id查询,当前和下级所有部门IDS
     * @param departId true
     * @return 没有返回值
     */
    List<String> getSubDepIdsByDepId(String departId);

    /**
     * 获取我的部门下级所有部门IDS
     * @param departIds true
     * @return 没有返回值
     */
    List<String> getMySubDepIdsByDepId(String departIds);
    /**
     * 根据关键字获取部门信息（通讯录）
     * @param keyWord true
     * @return 没有返回值
     */
    List<SysDepartTreeModel> queryTreeByKeyWord(String keyWord);
}
