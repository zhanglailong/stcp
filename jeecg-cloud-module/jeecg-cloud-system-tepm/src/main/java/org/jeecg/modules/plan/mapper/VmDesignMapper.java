package org.jeecg.modules.plan.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.plan.entity.VmDesign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.plan.entity.VmProject;
import org.jeecg.modules.plan.entity.VmProjectVo;
import org.jeecg.modules.test.entity.EvnRunningVo;
import org.springframework.stereotype.Component;

/**
 * @author zlf
 * @description 虚拟机设计
 * @date 2021-01-06
 * @version V1.0
 */
@Component(value = "VmDesignMapper")
public interface VmDesignMapper extends BaseMapper<VmDesign> {

    /**
     *根据id获取虚拟机集合
     * @param ids id集合
     * @return 根据id获取虚拟机集合
     */
    List<VmDesign> getVmListByIds(@Param("ids") List<String> ids);

    /**
     * 虚拟机+环境 关联查询
     * @param vmProjectVo
     * @return list
     */
    List<VmProject> findByPage(VmProjectVo vmProjectVo);
}
