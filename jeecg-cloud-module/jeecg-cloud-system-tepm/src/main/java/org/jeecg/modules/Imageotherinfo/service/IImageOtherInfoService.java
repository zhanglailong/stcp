package org.jeecg.modules.Imageotherinfo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.Imageotherinfo.entity.ImageOtherInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.plan.entity.ImageManage;
import org.jeecg.modules.running.tools.entity.RunningToolsList;

import java.util.List;


/**
 * @author yeyl
 */
public interface IImageOtherInfoService extends IService<ImageOtherInfo> {

    /**
     * 批量新增该镜像与系统版本 其他软件 测试工具的关联记录
     * @param imageManage  镜像信息
     * @param runningToolsLists 测试工具信息
     */
    void saveInfos(ImageManage imageManage, List<RunningToolsList> runningToolsLists);

    /**
     * 初始化 imageOtherInfo
     * @param imageManage imageManage
     * @return
     */
     ImageOtherInfo getImageOtherInfo(ImageManage imageManage);
}
