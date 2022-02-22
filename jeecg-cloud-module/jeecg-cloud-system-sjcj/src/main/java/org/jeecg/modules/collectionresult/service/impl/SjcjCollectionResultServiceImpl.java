package org.jeecg.modules.collectionresult.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.collectionresult.entity.SjcjCollectionResult;
import org.jeecg.modules.collectionresult.mapper.SjcjCollectionResultMapper;
import org.jeecg.modules.collectionresult.service.ISjcjCollectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SjcjCollectionResultServiceImpl extends ServiceImpl<SjcjCollectionResultMapper, SjcjCollectionResult>
		implements ISjcjCollectionResultService {
	@Autowired
	SjcjCollectionResultMapper sjcjCollectionResultMapper;

	@Override
	public Page<SjcjCollectionResult> queryPageList(Page<SjcjCollectionResult> page, String sjcjAgentBindCaseId) {
		List<SjcjCollectionResult> list = sjcjCollectionResultMapper.getHistory(sjcjAgentBindCaseId);
		return page.setRecords(list);
	}

	@Override
	public List<Map<String, String>> getResultDetail(String collectionTime, String sjcjAgentBindCaseId) {
		return sjcjCollectionResultMapper.getResultDetail(collectionTime, sjcjAgentBindCaseId);
	}

	@Override
	public SjcjCollectionResult getFilePath(String sjcjAgentBindCaseId) {
		return sjcjCollectionResultMapper.getFilePath(sjcjAgentBindCaseId);
	}

	@Override
	public Page<SjcjCollectionResult> queryScreenshotPageList(Page<SjcjCollectionResult> page,
			String sjcjAgentBindCaseId, String createTimeBegin, String createTimeEnd, String collectionDescription) {
		List<SjcjCollectionResult> list = sjcjCollectionResultMapper.getScreenshotHistory(sjcjAgentBindCaseId,
				createTimeBegin, createTimeEnd, collectionDescription);
		return page.setRecords(list);
	}
}
