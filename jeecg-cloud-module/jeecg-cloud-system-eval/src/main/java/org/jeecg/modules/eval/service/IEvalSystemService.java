package org.jeecg.modules.eval.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.eval.entity.EvalSystem;
import org.jeecg.modules.running.project.entity.RunningProject;
import org.jeecg.modules.running.uut.entity.RunningUutList;

import java.util.List;
import java.util.Map;

/**
 * @Description: eval_system
 * @Author: jeecg-boot
 * @Date: 2021-02-24
 * @Version: V1.0
 */
public interface IEvalSystemService extends IService<EvalSystem> {
    /**
     * 根据评价体系id查询被测对象列表
     * @param systemId true
     * @return List<RunningUutList>
     */
    List<RunningUutList> getUutList(String systemId);

    /**
     * 删除权重信息
     * @param systemId true
     * @return 无返回值
     */
    void deleteMeasureAndWeightInfo(String systemId);
    /**
     * @param fieldName true
     * @param value true
     * @return RunningProject
     * */
    public RunningProject findUniqueBy(@Param("fieldName") String fieldName, @Param("value") String value);

    /**
     * 获取轮次数
     */
    public int getTurnNum(@Param("projectId") String projectId);

    /**
     * 获取轮次名称
     */
    public String getTurnName(@Param("turnId") String turnId);

    /**
     * 获取测试项List
     */
    public List<String> getTaskIds(@Param("projectId") String projectId,
                                   @Param("turnId") String turnId,
                                   @Param("delStatus") int delStatus);

    /**
     * 获取测试用例List
     */
    public List<String> getCaseIds(List<String> taskIds,
                                   @Param("turnId") String turnId);

    /**
     * 根据状态获取测试用例List
     */
    public List<String> getAccessCaseIds(List<String> taskIds,
                                         @Param("turnId") String turnId);

    /**
     * 获取问题单List
     */
    public int getQuestionIds(List<String> caseIds,
                              @Param("turnId") String turnId);

    /**
     * 根据状态获取问题单List
     */
    public int getSolvedQuestionIds(List<String> caseIds,
                                    @Param("turnId") String turnId);

    /**
     * 获取已执行的测试用例数
     */
    public int getExecutedCaseCount(List<String> taskIds,
                                    @Param("turnId") String turnId);

    /**
     * 获取已执行的测试用例数
     */
    public List<String> getTurnList(String projectId);

    /**
     * 查询测试项完成数并加入轮次
     */
    public Map<String, Object> getTurnTask(String projectId, String taskStatus);

    /**
     * 查询测试用例完成数并加入轮次
     */
    public List<Map<String, Object>> getTurnCase(String projectId, String taskStatus);

    /**
     * 查询测试用例执行数并加入轮次
     */
    public List<Map<String, Object>> getTurnCaseExecute(String projectId, String taskStatus);

    /**
     * 查询问题单解决数并加入轮次
     */
    public List<Map<String, Object>> getTurnQuestionSolved(String projectId, String taskStatus);

    /**
     * 查询公式参数
     */
    public List<Map<String, Object>> getParamsBySid(String sid);
}
