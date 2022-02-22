package org.jeecg.modules.project.job;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.project.entity.RunningProject;
import org.jeecg.modules.project.mapper.RunningProjectMapper;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Description: 定时任务,判断是否有到期且未完成的任务,有的话给任务对应的用户发送消息(相同任务只发送一次)
 * @Author: dq
 * @Created Date: 2021年03月15日
 * @LastModifyDate:
 * @LastModifyBy:
 * @Version:
 */
@Slf4j
public class ProjectJob implements Job
{
    @Autowired
    private RunningProjectMapper projectMapper;
    @Autowired
    private SysAnnouncementMapper announcementMapper;
    @Autowired
    private SysAnnouncementSendMapper announcementSendMapper;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        // 查询当前登录用户未完成的任务
        List<RunningProject> notFinishProjectList = projectMapper.getNotFinishProjectList();
        for(RunningProject project : notFinishProjectList)
        {
            String projectMembers = project.getProjectMemberIds();
            if(projectMembers != null)
            {
                //项目成员列表
                String[] memberArray = projectMembers.split(",");
                // 给项目每个成员发送消息
                if(memberArray != null && memberArray.length > 0)
                {
                    //消息主表
                    SysAnnouncement sysAnnouncement = new SysAnnouncement();
                    sysAnnouncement.setTitile("有项目未完成");
                    sysAnnouncement.setSender("admin");
                    sysAnnouncement.setMsgContent(project.getProjectName() + "项目未完成");
                    sysAnnouncement.setPriority("M");
                    sysAnnouncement.setMsgType("USER");
                    sysAnnouncement.setSendTime(new Date());
                    sysAnnouncement.setUserIds(projectMembers);
                    sysAnnouncement.setSendStatus("1");
                    sysAnnouncement.setDelFlag("0");
                    announcementMapper.insert(sysAnnouncement);
                    for(String memberId : memberArray)
                    {
                        SysAnnouncementSend sysAnnouncementSend = new SysAnnouncementSend();
                        sysAnnouncementSend.setAnntId(sysAnnouncement.getId());
                        sysAnnouncementSend.setUserId(memberId);
                        sysAnnouncementSend.setReadFlag("0");
                        sysAnnouncementSend.setCreateBy("admin");
                        sysAnnouncementSend.setCreateTime(new Date());
                        sysAnnouncementSend.setUpdateTime(new Date());
                        announcementSendMapper.insert(sysAnnouncementSend);
                    }
                }
            }
            //设置已发送,下次不会重复发送
            project.setBNotFinishMsg(1);
            projectMapper.updateById(project);
        }
    }
}