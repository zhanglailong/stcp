package org.jeecg.modules.base.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.dto.LogDTO;

public interface BaseCommonMapper {

    /**
     * 保存日志
     * @param dto
     */
    //@SqlParser(filter=true)
    @Insert("insert into sys_log (id, log_type, log_content, method, operate_type, request_param, ip, userid, username, cost_time, create_time)\n" +
            "        values(" +
            "            #{dto.id,jdbcType=VARCHAR}," +
            "            #{dto.logType,jdbcType=INTEGER}," +
            "            #{dto.logContent,jdbcType=VARCHAR}," +
            "            #{dto.method,jdbcType=VARCHAR}," +
            "            #{dto.operateType,jdbcType=INTEGER}," +
            "            #{dto.requestParam,jdbcType=VARCHAR}," +
            "            #{dto.ip,jdbcType=VARCHAR}," +
            "            #{dto.userid,jdbcType=VARCHAR}," +
            "            #{dto.username,jdbcType=VARCHAR}," +
            "            #{dto.costTime,jdbcType=BIGINT}," +
            "            #{dto.createTime,jdbcType=TIMESTAMP}" +
            "        )")
    void saveLog(@Param("dto")LogDTO dto);

}
