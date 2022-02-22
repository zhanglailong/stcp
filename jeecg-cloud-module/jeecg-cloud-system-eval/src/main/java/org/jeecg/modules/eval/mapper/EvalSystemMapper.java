package org.jeecg.modules.eval.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public interface EvalSystemMapper extends BaseMapper<EvalSystem> {
    /**
     * @return Page<EvalSystem>
     * @param systemId
     * */
    public List<RunningUutList> getUutList(String systemId);
    /**
     * @param fieldName true
     * @param value true
     * @return RunningProject
     * */
    public RunningProject findUniqueBy(@Param("fieldName") String fieldName, @Param("value") String value);

    /**
     * 获取轮次数
     */
    @Select("SELECT COUNT(*) " +
            "FROM running_project_turn " +
            "WHERE project_id = #{projectId}")
    public int getTurnNum(@Param("projectId") String projectId);

    /**
     * 获取轮次数
     */
    @Select("SELECT turn_num " +
            "FROM running_project_turn " +
            "WHERE id = #{turnId}")
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
    public List<String> getCaseIds(@Param("taskIds") List<String> taskIds,
                                   @Param("turnId") String turnId);

    /**
     * 根据状态获取测试用例List
     */
    public List<String> getAccessCaseIds(@Param("taskIds") List<String> taskIds,
                                         @Param("turnId") String turnId);

    /**
     * 获取问题单List
     */
    public int getQuestionIds(@Param("caseIds") List<String> caseIds,
                              @Param("turnId") String turnId);

    /**
     * 根据状态获取问题单List
     */
    public int getSolvedQuestionIds(@Param("caseIds") List<String> caseIds,
                                    @Param("turnId") String turnId);

    /**
     * 获取已执行的测试用例数
     */
    public int getExecutedCaseCount(@Param("taskIds") List<String> taskIds,
                                    @Param("turnId") String turnId);

    /**
     * 获取已执行的测试用例数
     */
    public List<String> getTurnList(String projectId);

    /**
     * 查询测试项完成数并加入轮次
     */
    @Select("SELECT t.turn_num AS turn, COUNT(r.turn_id) AS num " +
            "FROM running_task r " +
            "LEFT JOIN running_project_turn t ON r.turn_id = t.id " +
            "WHERE r.project_id = #{projectId} AND r.task_status = #{taskStatus} " +
            "AND r.del_flag = #{delFlag} " +
            "GROUP BY r.turn_id")
    public Map<String, Object> getTurnTask(String projectId, String taskStatus, int delFlag);

    /**
     * 查询测试用例通过数并加入轮次
     */
    @Select("SELECT t.turn_num AS turn, COUNT(c.id) AS num " +
            "FROM running_case c " +
            "LEFT JOIN running_task r ON r.id = c.test_task_id " +
            "LEFT JOIN running_project_turn t ON r.turn_id = t.id " +
            "WHERE r.project_id = #{projectId} AND c.test_real_result = #{taskStatus} " +
            "AND r.del_flag = #{delFlag} AND c.del_flag = #{delFlag} " +
            "GROUP BY r.turn_id " +
            "ORDER BY r.turn_id")
    public List<Map<String, Object>> getTurnCase(String projectId, String taskStatus, int delFlag);

    /**
     * 查询测试项执行数并加入轮次
     */
    public List<Map<String, Object>> getTurnCaseExecute(String projectId, String taskStatus, int delFlag);

    /**
     * 查询问题单解决数并加入轮次
     */
    public List<Map<String, Object>> getTurnQuestionSolved(String projectId, String taskStatus, int delFlag);

    /**
     * 查询公式参数
     */
    public List<Map<String, Object>> getParamsBySid(String sid);

}
