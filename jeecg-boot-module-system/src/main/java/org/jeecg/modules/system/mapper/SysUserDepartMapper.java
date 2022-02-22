package org.jeecg.modules.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.system.entity.SysUserDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
/**
 * @author Administrator
 *
 * */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{
	/**
	 *
	 * 获取用户id
	 * @param userId true
	 * @return  List<SysUserDepart>
	 * */
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
