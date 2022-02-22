package org.jeecg.modules.running.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.running.entity.RunningProject;
import org.jeecg.modules.running.entity.RunningProjectTurn;
import org.jeecg.modules.running.vo.RunningProjectInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 项目管理
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Mapper
public interface IRunningProjectService extends IService<RunningProject> {

	/**
	 * 根据字段查唯一实体
	 * @param fieldname true
	 * @param value true
	 * @return 没有返回值
	 */
	public RunningProject findUniqueBy (String fieldname, String value);

}
