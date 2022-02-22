package org.jeecg.modules.system.service.impl;

import java.util.List;

import org.jeecg.modules.system.entity.SysPermission;
import org.jeecg.modules.system.entity.SysPermissionProject;
import org.jeecg.modules.system.mapper.SysPermissionProjectMapper;
import org.jeecg.modules.system.service.ISysPermissionProjectService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目权限表
 * @Author: jeecg-boot
 * @Date:   2021-07-04
 * @Version: V1.0
 */
@Service
public class SysPermissionProjectServiceImpl extends ServiceImpl<SysPermissionProjectMapper, SysPermissionProject> implements ISysPermissionProjectService {

	@Override
	public List<SysPermission> getProjectPermission(List<String> parentIds, String projectId, String userId) {
		return this.getBaseMapper().getProjectPermission(parentIds, projectId, userId);
	}

}
