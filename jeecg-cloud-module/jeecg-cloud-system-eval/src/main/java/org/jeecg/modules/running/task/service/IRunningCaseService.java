package org.jeecg.modules.running.task.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.running.task.entity.RunningCase;
import org.jeecg.modules.running.task.entity.RunningTask;
import org.jeecg.modules.running.task.vo.RunningCaseVO;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
public interface IRunningCaseService extends IService<RunningCase> {
	
	
	

	/**
	 * 分页查询测试用例列表
	 * @param page true
	 * @param testTaskId true
	 * @param testName true
	 * @param testCode true
	 * @param testPerson true
	 * @param testDate true
	 * @return 没有返回值
	 */
	Page<RunningCaseVO> queryPageList(Page<RunningCaseVO> page, String testTaskId, String testName, String testCode, String testPerson, String testDate);
	
	
	
	
	
	/**
	 * 查询复制所需数据
	 * @param id true
	 * @return 没有返回值
	 */
	List<RunningCase> getCopyDataById(String id);
	
	/**
	 * 根据任务id,查询被测对象分析体系id
	 * @param id true
	 * @return 分析体系id
	 */
	String getTestTemplateById(String id);
	
	/**
	 * 获取测试用例树形结构数据
	 * @param templateId true
	 * @return 树形结构数据
	 */
    List<CaseTreeIdModel> queryTreeList(String templateId);
    
    /**
     * 根据任务ID查询测试用例名称
     * @param taskId  true
	 * @return 测试用例名称
	 * 
     */
	List<RunningCase> getCaseNameByTaskId(String taskId);
	
	/**
	 * 根据任务ID查询出所有测试用例信息
	 * @param testTaskId true
	 * @return 没有返回值
	 */
	List<RunningCaseVO> getRunningCaseData(String testTaskId);


	/**
	 * 根据任务ID查询出所有测试用例信息
	 * @param testTaskId true
	 * @return List<RunningCaseVO>
	 */
	List<RunningCaseVO> getRunningCaseData1(String testTaskId);

	/**
	 * 根据任务ID查询项目ID
	 * @param taskId true
	 * @return projectId
	 */
	RunningTask getProjectIdByTaskId(String taskId);

	/**
	 * 根据测试用例ID查询被测对象实体
	 * @param caseId true
	 * @return projectId
	 */
	RunningUutList getUutListByCaseId(String caseId);

	/**
	 * 根据任务ID查询被测对象实体
	 * @param taskId true
	 * @return projectId
	 */
	RunningUutList getUutListByTaskId(String taskId);
}
