package org.jeecg.modules.util;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.jeecg.modules.running.uut.service.IRunningUutListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
/**
 * @Author: test
 * */
public class UuListFileUtil {
	
	 private static UuListFileUtil uuListFileUtil;
	
	
	@Autowired
	private IRunningUutListService runningUutListService;

	@PostConstruct
    public void init(){
		uuListFileUtil = this;
		uuListFileUtil.runningUutListService = this.runningUutListService;
    }
	
	/**
	 * 通过被测对象id查询被测对象实体
	 *
	 * @param id
	 * @return
	 */
	public Result<?> searchById(String id) {
		RunningUutList runningUutList = runningUutListService.getById(id);
		if(runningUutList==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(runningUutList);
	}

}
