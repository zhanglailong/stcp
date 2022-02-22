package com.sqa.chn.modules.access.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqa.chn.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import com.sqa.chn.modules.access.entity.CeShiXiangBiao;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 测试项表 服务类
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
public interface ICeShiXiangBiaoService extends IService<CeShiXiangBiao> {
    IPage<CeShiXiang4CeShiYongLiVo> pageCeShiXiang4CeShiYongLiVo4LikeFullName(IPage<?> page, String likeFullName);
}
