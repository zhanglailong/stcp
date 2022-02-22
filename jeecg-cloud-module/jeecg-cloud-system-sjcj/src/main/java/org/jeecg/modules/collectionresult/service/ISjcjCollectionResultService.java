package org.jeecg.modules.collectionresult.service;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.collectionresult.entity.SjcjCollectionResult;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISjcjCollectionResultService extends IService<SjcjCollectionResult> {
	// 根据`sjcj_agent_bind_case`表ID获取文件采集历史列表
	Page<SjcjCollectionResult> queryPageList(Page<SjcjCollectionResult> page, String sjcjAgentBindCaseId);

	// 根据采集时间与`sjcj_agent_bind_case`表ID获取文件采集结果列表
	List<Map<String, String>> getResultDetail(String collectionTime, String sjcjAgentBindCaseId);

	// 根据`sjcj_agent_bind_case`表ID获取采集文件存储路径
	SjcjCollectionResult getFilePath(String sjcjAgentBindCaseId);

	// 根据`sjcj_agent_bind_case`表ID获取截屏历史列表
	Page<SjcjCollectionResult> queryScreenshotPageList(Page<SjcjCollectionResult> page, String sjcjAgentBindCaseId,
			String createTimeBegin, String createTimeEnd, String collectionDescription);
}