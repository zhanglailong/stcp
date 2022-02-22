package org.jeecg.modules.collectionresult.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.collectionresult.entity.SjcjCollectionResult;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import feign.Param;

public interface SjcjCollectionResultMapper extends BaseMapper<SjcjCollectionResult> {
	// 根据`sjcj_agent_bind_case`表ID获取文件采集历史列表
	@Select("SELECT * FROM sjcj_collection_result WHERE id IN " + "( SELECT MAX(id) FROM `sjcj_collection_result` "
			+ "WHERE sjcj_agent_bind_case_id = #{sjcjAgentBindCaseId} GROUP BY collection_time) AND type_of_collection != 'screenshot'"
			+ "ORDER BY collection_time DESC")
	List<SjcjCollectionResult> getHistory(String sjcjAgentBindCaseId);

	// 根据采集时间与`sjcj_agent_bind_case`表ID获取文件采集结果列表
	@Select("SELECT\r\n" + "	scr.id AS id,\r\n" + "	scr.collection_description AS collectionDescription,\r\n"
			+ "	scr.collection_result AS collectionResult,\r\n" + "	scr.file_storage_path AS fileStoragePath,\r\n"
			+ "	sdi.item_text AS typeOfCollectionDict \r\n" + "FROM\r\n" + "	sjcj_collection_result scr,\r\n"
			+ "	sys_dict_item sdi,\r\n" + "	sys_dict sd \r\n" + "WHERE\r\n" + "	sdi.dict_id = sd.id \r\n"
			+ "	AND scr.type_of_collection = sdi.item_value \r\n" + "	AND sd.dict_code = 'typeOfCollection' \r\n"
			+ "	AND scr.collection_time = #{collectionTime} \r\n"
			+ "	AND scr.sjcj_agent_bind_case_id = #{sjcjAgentBindCaseId}")
	List<Map<String, String>> getResultDetail(String collectionTime, String sjcjAgentBindCaseId);

	// 根据`sjcj_agent_bind_case`表ID获取采集文件存储路径
	@Select("SELECT * FROM sjcj_collection_result WHERE sjcj_agent_bind_case_id = #{sjcjAgentBindCaseId} "
			+ "ORDER BY collection_time DESC LIMIT 1")
	SjcjCollectionResult getFilePath(String sjcjAgentBindCaseId);

	// 根据`sjcj_agent_bind_case`表ID获取截屏历史列表
	List<SjcjCollectionResult> getScreenshotHistory(@Param(value = "sjcjAgentBindCaseId") String sjcjAgentBindCaseId,
			@Param(value = "createTimeBegin") String createTimeBegin,
			@Param(value = "createTimeEnd") String createTimeEnd,
			@Param(value = "collectionDescription") String collectionDescription);
}
