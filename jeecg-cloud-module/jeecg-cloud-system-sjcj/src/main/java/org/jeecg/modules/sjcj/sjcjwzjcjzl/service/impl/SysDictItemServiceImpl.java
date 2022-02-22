package org.jeecg.modules.sjcj.sjcjwzjcjzl.service.impl;

import java.util.List;

import org.jeecg.modules.sjcj.sjcjwzjcjzl.entity.SysDictItem;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.mapper.SysDictItemMapper;
import org.jeecg.modules.sjcj.sjcjwzjcjzl.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

	@Override
	public List<SysDictItem> getTypeOfCollection() {
		return sysDictItemMapper.getTypeOfCollection();
	}
	
}
