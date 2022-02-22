package com.sqa.chn.modules.access.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import com.sqa.chn.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import com.sqa.chn.modules.access.entity.CeShiXiangBiao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 测试项表 Mapper 接口
 * </p>
 *
 * @author GodMeowIceSun
 * @since 2021-08-06
 */
public interface CeShiXiangBiaoMapper extends BaseMapper<CeShiXiangBiao> {

    IPage<CeShiXiang4CeShiYongLiVo> pageCeShiXiang4CeShiYongLiVo4LikeFullName(IPage<?> page, @Param("likeFullName") String likeFullName);
}
