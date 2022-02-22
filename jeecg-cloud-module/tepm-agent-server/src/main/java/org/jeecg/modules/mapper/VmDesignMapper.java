package org.jeecg.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.entity.VmDesign;

import java.util.List;

/**
 * @Description: 虚拟机设计
 * @Author: jeecg-boot
 * @Date: 2021-01-06
 * @Version: V1.0
 */
public interface VmDesignMapper extends BaseMapper<VmDesign> {

    List<VmDesign> getVmListByIds(@Param("ids") List<String> ids);
}
