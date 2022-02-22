package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysAnnouncement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 系统通告表
 * @Author: jeecg-boot
 * @Date:  2019-01-02
 * @Version: V1.0
 */
public interface ISysAnnouncementService extends IService<SysAnnouncement> {
	/**
	 * 保存
	 * @param sysAnnouncement true
	 * */
	public void saveAnnouncement(SysAnnouncement sysAnnouncement);
	/**
	 * 更新
	 * @param sysAnnouncement true
	 * @return 没有返回值
	 * */
	public boolean upDateAnnouncement(SysAnnouncement sysAnnouncement);
	/**
	 * 保存
	 * @param title true
	 * @param msgContent true
	 * */
	public void saveSysAnnouncement(String title, String msgContent);
	/**
	 * 获取ID
	 * @param page true
	 * @param userId true
	 * @param msgCategory true
	 * @return 没有返回值
	 * */
	public Page<SysAnnouncement> querySysCementPageByUserId(Page<SysAnnouncement> page,String userId,String msgCategory);


}
