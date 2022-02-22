package org.jeecg.modules.tools.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.tools.entity.RunningToolsLicense;
import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;
import org.jeecg.modules.tools.entity.RunningToolsLicensemonitor;
import org.jeecg.modules.tools.mapper.RunningToolsLicenseMapper;
import org.jeecg.modules.tools.service.IRunningToolsLicenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: License云平台适配
 * @Author: jeecg-boot
 * @Date: 2021-01-07
 * @Version: V1.0
 */
@Service
public class RunningToolsLicenseServiceImpl extends ServiceImpl<RunningToolsLicenseMapper, RunningToolsLicense> implements IRunningToolsLicenseService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(RunningToolsLicense runningToolsLicense, List<RunningToolsLicensemonitor> runningToolsLicensemonitorList, List<RunningToolsLicenselimit> runningToolsLicenselimitList) {
//		runningToolsLicenseMapper.insert(runningToolsLicense);
//		if(runningToolsLicensemonitorList!=null && runningToolsLicensemonitorList.size()>0) {
//			for(RunningToolsLicensemonitor entity:runningToolsLicensemonitorList) {
//				//外键设置
//				entity.setLicenseId(runningToolsLicense.getId());
//				runningToolsLicensemonitorMapper.insert(entity);
//			}
//		}
//		if(runningToolsLicenselimitList!=null && runningToolsLicenselimitList.size()>0) {
//			for(RunningToolsLicenselimit entity:runningToolsLicenselimitList) {
//				//外键设置
//				entity.setLicenseId(runningToolsLicense.getId());
//				runningToolsLicenselimitMapper.insert(entity);
//			}
//		}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(RunningToolsLicense runningToolsLicense, List<RunningToolsLicensemonitor> runningToolsLicensemonitorList, List<RunningToolsLicenselimit> runningToolsLicenselimitList) {
//		runningToolsLicenseMapper.updateById(runningToolsLicense);
//
//		//1.先删除子表数据
//		runningToolsLicensemonitorMapper.deleteByMainId(runningToolsLicense.getId());
//		runningToolsLicenselimitMapper.deleteByMainId(runningToolsLicense.getId());
//
//		//2.子表数据重新插入
//		if(runningToolsLicensemonitorList!=null && runningToolsLicensemonitorList.size()>0) {
//			for(RunningToolsLicensemonitor entity:runningToolsLicensemonitorList) {
//				//外键设置
//				entity.setLicenseId(runningToolsLicense.getId());
//				runningToolsLicensemonitorMapper.insert(entity);
//			}
//		}
//		if(runningToolsLicenselimitList!=null && runningToolsLicenselimitList.size()>0) {
//			for(RunningToolsLicenselimit entity:runningToolsLicenselimitList) {
//				//外键设置
//				entity.setLicenseId(runningToolsLicense.getId());
//				runningToolsLicenselimitMapper.insert(entity);
//			}
//		}
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
//		runningToolsLicensemonitorMapper.deleteByMainId(id);
//		runningToolsLicenselimitMapper.deleteByMainId(id);
//		RunningToolsLicense runningToolsLicense = runningToolsLicenseMapper.selectById(id);
//		runningToolsLicense.setDelFlag(CommonConstant.DEL_FLAG_1);
//		runningToolsLicenseMapper.updateById(runningToolsLicense);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
//		for(Serializable id:idList) {
//			runningToolsLicensemonitorMapper.deleteByMainId(id.toString());
//			runningToolsLicenselimitMapper.deleteByMainId(id.toString());
//			runningToolsLicenseMapper.deleteById(id);
//		}
    }

}
