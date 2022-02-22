package com.sqa.chn.modules.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqa.chn.modules.task.entity.RunningCase;
import org.apache.ibatis.annotations.Mapper;


/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Mapper
public interface RunningCaseMapper extends BaseMapper<RunningCase> {
}
