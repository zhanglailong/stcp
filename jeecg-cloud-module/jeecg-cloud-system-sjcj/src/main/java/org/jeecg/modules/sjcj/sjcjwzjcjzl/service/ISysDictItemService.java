package org.jeecg.modules.sjcj.sjcjwzjcjzl.service;

import java.util.List;

import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SysDictItem;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 *  服务类
 */
public interface ISysDictItemService extends IService<SysDictItem> {
    
    /**
     * 查询采集类型列表
     */
    public List<SysDictItem> getTypeOfCollection();
    
}
