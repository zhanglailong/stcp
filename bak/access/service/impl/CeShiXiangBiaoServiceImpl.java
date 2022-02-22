package org.jeecg.modules.access.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import org.jeecg.modules.access.entity.CeShiXiangBiao;
import org.jeecg.modules.access.mapper.CeShiXiangBiaoMapper;
import org.jeecg.modules.access.service.ICeShiXiangBiaoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 测试项表 服务实现类
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
@Service
public class CeShiXiangBiaoServiceImpl extends ServiceImpl<CeShiXiangBiaoMapper, CeShiXiangBiao> implements ICeShiXiangBiaoService {


    @Override
    public IPage<CeShiXiang4CeShiYongLiVo> pageCeShiXiang4CeShiYongLiVo4LikeFullName(IPage<?> page, String likeFullName) {
        return getBaseMapper().pageCeShiXiang4CeShiYongLiVo4LikeFullName(page, likeFullName);
    }
}
