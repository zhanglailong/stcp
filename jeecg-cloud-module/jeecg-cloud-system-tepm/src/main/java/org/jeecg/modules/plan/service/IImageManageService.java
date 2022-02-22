package org.jeecg.modules.plan.service;

import org.jeecg.modules.openstack.entity.ImageList;
import org.jeecg.modules.plan.entity.ImageManage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.running.tools.entity.RunningToolsList;

import java.util.List;


/**
 * @author yeyl
 */
public interface IImageManageService extends IService<ImageManage> {


    /**
     * 编辑
     * @param imageManage  imageManage
     */
    void edit(ImageManage imageManage);

    /**
     * 根据镜像id查虚拟机其他软件，测试工具信息
     * @param openstackId 镜像id
     * @return ImageManage
     */
    ImageManage getByOpenstackId(String openstackId);

    /**
     * 镜像同步
     */
    void sync();

    /**
     * 处理字符串的逗号 去除第一个逗号和最后一个逗号 去除重复逗号
     * @param otherSoftware  otherSoftware
     * @return 处理后的字符串
     */
   String getDealCommaString(String otherSoftware);

    /**
     * 获取该镜像测试工具列表
     * @param imageManage imageManage
     * @param runningToolsLists runningToolsLists
     * @return runningToolsLists
     */
    List<RunningToolsList> getRunningToolsLists(ImageManage imageManage, List<RunningToolsList> runningToolsLists);

    /**
     * 批量删除
     * @param stringList  stringList
     * @return  boolean
     */
    boolean deleteBatch(List<String> stringList);

    /**
     * 根据版本信息 测试工具 其他软件 获取镜像列表 没有输入查全部
     * @param otherSoftware  其他软件
     * @param testTools 测试工具
     * @param systemVersion 系统版本
     * @param sysType 操作系统
     * @return  List<ImageManage>
     */
    List<ImageManage> getImageManagesByOther(String otherSoftware, String testTools, String systemVersion,String sysType);

    /**
     * 根据openstack镜像id，获取镜像
     */

    ImageManage getImageByOpenstackId(String openstackId);
}
