package org.jeecg.modules.access.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.access.dto.CeShiXiang4CeShiYongLiVo;
import org.jeecg.modules.access.entity.CeShiXiangBiao;
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
