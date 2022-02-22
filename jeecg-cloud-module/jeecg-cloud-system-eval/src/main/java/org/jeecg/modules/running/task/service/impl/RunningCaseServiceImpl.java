package org.jeecg.modules.running.task.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.jeecg.modules.eval.entity.CaseTreeIdModel;
import org.jeecg.modules.running.task.entity.RunningCase;
import org.jeecg.modules.running.task.entity.RunningTask;
import org.jeecg.modules.running.task.mapper.RunningCaseMapper;
import org.jeecg.modules.running.task.service.IRunningCaseService;
import org.jeecg.modules.running.task.vo.CaseTreeVO;
import org.jeecg.modules.running.task.vo.RunningCaseVO;
import org.jeecg.modules.running.uut.entity.RunningUutList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 测试用例表
 * @Author: jeecg-boot
 * @Date:   2021-01-14
 * @Version: V1.0
 */
@Service
public class RunningCaseServiceImpl extends ServiceImpl<RunningCaseMapper, RunningCase> implements IRunningCaseService {

	@Autowired
	private RunningCaseMapper runningCaseMapper;

	@Override
	public Page<RunningCaseVO> queryPageList(Page<RunningCaseVO> page, String testTaskId, String testName, String testCode, String testPerson, String testDate) {
		// TODO Auto-generated method stub
		return page.setRecords(runningCaseMapper.getListData(testTaskId, testName, testCode,testPerson,testDate));
	}
	
	@Override
	public List<RunningCase> getCopyDataById(String id) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getCopyDataById(id);
	}
	@Override
	public String getTestTemplateById(String id) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getTestTemplateById(id);
	}
	

	@Override
	public List<CaseTreeIdModel> queryTreeList(String templateId) {
		// 获取树形下拉列表数据
		List<CaseTreeVO> list = runningCaseMapper.getTreeData(templateId);
		List<CaseTreeIdModel> treeList = new ArrayList<CaseTreeIdModel>();
		// 处理中的子特性
		String prePid = null;
		// 处理中的质量特性
		String preRootId = null;
		// 质量特性节点
		CaseTreeIdModel rootModel = null;
		// 子特性节点
		CaseTreeIdModel parentModel = null;
		
		for (CaseTreeVO caseTreeVO : list) {
			String pid = caseTreeVO.getParentId();
			String rootId = caseTreeVO.getRootId();
			// 初期设定：将处理中的质量特性ID和子特性ID记录下来
			if (preRootId == null && prePid == null) {
				prePid = pid;
				preRootId = rootId;
				rootModel = new CaseTreeIdModel();
				rootModel.setTitle(caseTreeVO.getRootName());
				rootModel.setKey(rootId);
				rootModel.setValue(rootId);
				parentModel = new CaseTreeIdModel();
				parentModel.setTitle(caseTreeVO.getParentName());
				parentModel.setKey(pid);
				parentModel.setValue(pid);
			}
			// 比较处理中的质量特性ID和当前质量特性ID
			if (preRootId.equals(rootId)) {
				// 比较处理中的子特性ID和当前子特性ID
				if (prePid.equals(pid)) {
					// 相同的情况下，添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel();
					childModel.setTitle(caseTreeVO.getName());
					childModel.setKey(caseTreeVO.getId());
					childModel.setValue(caseTreeVO.getId());
					parentModel.getChildren().add(childModel);
				} else {
					// 不相同的情况下，将处理中的子特性信息添加到处理中的质量特性的节点下方
					rootModel.getChildren().add(parentModel);
					// 重新初始化子特性ID
					prePid = pid;
					// 创建子特性节点
					parentModel = new CaseTreeIdModel();
					parentModel.setTitle(caseTreeVO.getParentName());
					parentModel.setKey(pid);
					parentModel.setValue(pid);
					// 添加度量节点
					CaseTreeIdModel childModel = new CaseTreeIdModel();
					childModel.setTitle(caseTreeVO.getName());
					childModel.setKey(caseTreeVO.getId());
					childModel.setValue(caseTreeVO.getId());
					parentModel.getChildren().add(childModel);
				}
			} else {
				// 更换了质量特性ID的时候，将处理中的子特性信息添加到处理中的质量特性的节点下方
				rootModel.getChildren().add(parentModel);
				treeList.add(rootModel);
				// 重新初始化处理中的质量特性ID和当前质量特性ID
				preRootId = rootId;
				prePid = pid;
				// 创建质量特性节点
				rootModel = new CaseTreeIdModel();
				rootModel.setTitle(caseTreeVO.getRootName());
				rootModel.setKey(rootId);
				rootModel.setValue(rootId);
				// 创建子特性节点
				parentModel = new CaseTreeIdModel();
				parentModel.setTitle(caseTreeVO.getParentName());
				parentModel.setKey(pid);
				parentModel.setValue(pid);
				// 添加度量节点
				CaseTreeIdModel childModel = new CaseTreeIdModel();
				childModel.setTitle(caseTreeVO.getName());
				childModel.setKey(caseTreeVO.getId());
				childModel.setValue(caseTreeVO.getId());
				parentModel.getChildren().add(childModel);
			}
		}
		// 将最后的处理的子特性信息添加到处理中的质量特性的节点下方
		rootModel.getChildren().add(parentModel);
		treeList.add(rootModel);
		
		return treeList;		
	}
	@Override
	public List<RunningCase> getCaseNameByTaskId(String taskId) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getCaseNameByTaskId(taskId);
	}

	@Override
	public List<RunningCaseVO> getRunningCaseData(String testTaskId) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getRunningCaseData(testTaskId);
	}

	@Override
	public List<RunningCaseVO> getRunningCaseData1(String testTaskId) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getRunningCaseData1(testTaskId);
	}

	@Override
	public RunningTask getProjectIdByTaskId(String taskId) {
		// TODO Auto-generated method stub
		return runningCaseMapper.getProjectIdByTaskId(taskId);
	}

	@Override
	public RunningUutList getUutListByCaseId(String caseId) {
		return runningCaseMapper.getUutListByCaseId(caseId);
	}

	/**
	 * 关闭输入流
	 * @param is
	 */
	private static void close(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导入word （doc格式）
	 * @param file
	 * @return
	 */
	private Map<String,String> importWordToDoc (MultipartFile file){
		InputStream inputStream = null;
		Map<String, String> result = new HashMap<>(30);
		try {
			//载入文档
			inputStream = file.getInputStream();
			HWPFDocument hwpf = new HWPFDocument(inputStream);
			//得到文档的读取范围
			Range range = hwpf.getRange();
			TableIterator it = new TableIterator(range);
			int index = 1;
			String value = "";
			String key = "";
			// 迭代文档中的表格
			while (it.hasNext()) {
				Table tb = (Table) it.next();
				for (int i = 0; i < tb.numRows(); i++) {
					TableRow tr = tb.getRow(i);
					StringBuffer stringBuffer = new StringBuffer();
					for (int j = 0; j < tr.numCells(); j++) {
						//取得单元格
						TableCell td = tr.getCell(j);
						//取得单元格的内容
						for (int k = 0; k < td.numParagraphs(); k++) {
							Paragraph para = td.getParagraph(k);
							Optional<String> text1 = Optional.ofNullable(para.text());
							String text = text1.orElse("");
							text = text.replaceAll("\r", "");
							text = text.replaceAll("\r\n", "");
							text = text.replaceAll("\\u0007", "");
							if (index % 2 != 0) {
								key = text;
							} else {
								value = text;
								stringBuffer.append(key).append(":").append(value);
								result.put(key, value);
							}
							index++;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(inputStream);
		}
		return result;
	}

	/**
	 * 新增
	 * @param result
	 */
	private void insert(Map<String, String> result){
		RunningCase runningCase = new RunningCase();
		runningCase.setId(result.get("id"));
		runningCase.setTestTaskId(result.get("test_task_id"));
		runningCase.setTestName(result.get("test_name"));
		runningCase.setTestType(result.get("test_type"));
		runningCase.setTestInstructions(result.get("test_instructions"));
		runningCase.setDelFlag(0);
		runningCaseMapper.insert(runningCase);
	}

	/**
	 * 根据任务ID查询被测对象实体
	 */
	public RunningUutList getUutListByTaskId(String taskId){
		return this.getBaseMapper().getUutListByTaskId(taskId);
	}
}
