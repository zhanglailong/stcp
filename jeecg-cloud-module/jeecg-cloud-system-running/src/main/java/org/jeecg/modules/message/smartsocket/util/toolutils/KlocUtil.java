package org.jeecg.modules.message.smartsocket.util.toolutils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;
import org.jeecg.modules.cloud.service.IRunningCloudKlocCaseService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocQueService;
import org.jeecg.modules.cloudtools.CaseType;
import org.jeecg.modules.message.smartsocket.util.toolutils.smartsocket.SocketHandler;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("klocUtil")
@Slf4j
/**
 * @Author: test
 * */
public class KlocUtil {
	
	@Autowired
	private IRunningCloudKlocCaseService kloccaseService;
	@Autowired
	private IRunningCloudKlocQueService klocqueService;
	@Autowired
	private TaskDispatcher taskDispather;
	
	public void process(String msg,String ip) {
		
		//客户端反馈指令：next/stop
		String info="$commond-";
		String msgInfo="goNext";
		String msgInfo1="ERRR";
		String msgInfo2="$process-";
		String msgInfo3="$process-Klocstep.";
		String msgInfo4="log:";
		String msgInfo5="$lock-";
		String msgInfo6="$unlock-";

		if(StringUtils.startsWith(msg, info)) {
			//移除队列
			String caseId = null;
			caseId = RegExUtils.removePattern(msg, "[^0-9]+");
			klocqueService.removeByCaseId(caseId);
			RunningCloudKlocCase kloccase = kloccaseService.getById(caseId);
			if(msg.contains(msgInfo)) {
				kloccase.setStatus(CommonConstant.KLOCCOMPLETE);
			}else if(msg.contains(msgInfo1)) {
				kloccase.setStatus(CommonConstant.KLOCERRR);
			}else {
				kloccase.setStatus(CommonConstant.KLOCSTOP);
			}
			kloccaseService.updateById(kloccase);
			//开始下一个
			taskDispather.goNext(ip, CaseType.Kloc);
		}else if(StringUtils.startsWith(msg, msgInfo2)){
			String str = StringUtils.remove(msg, msgInfo3);
			changeKlocStep(str.split(":")[1], str.split(":")[0],CaseType.Kloc,ip);
		}else if(StringUtils.startsWith(msg, msgInfo4)) {
			log.info(StringUtils.substring(msg, 4));
		}else if(StringUtils.startsWith(msg, msgInfo5)) {
			CaseType type = CaseType.valueOf(StringUtils.remove(msg, "$lock-"));
			SocketHandler.getSocketLock().get(ip).put(type, 1);
		}else if(StringUtils.startsWith(msg, msgInfo6)) {
			CaseType type = CaseType.valueOf(StringUtils.remove(msg, "$unlock-"));
			SocketHandler.getSocketLock().get(ip).put(type, 0);
			//开始下一个
			taskDispather.goNext(ip, type);
		}
	}
	
	private void changeKlocStep(String caseId,String step,CaseType type,String ip) {
		
		//更改状态
		kloccaseService.updateStep(caseId, step);
		int stepInfo=400;
		try {
			if(Integer.valueOf(step)==-1) {
				kloccaseService.updateStatus(caseId, "1");
			}
			if(Integer.valueOf(step)>=stepInfo) {
				klocqueService.removeByCaseId(caseId);
				kloccaseService.updateStatus(caseId, step);
			}else {
				klocqueService.updateStep(caseId, step);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
