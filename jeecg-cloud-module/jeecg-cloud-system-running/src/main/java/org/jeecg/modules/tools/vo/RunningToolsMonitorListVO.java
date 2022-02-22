package org.jeecg.modules.tools.vo;

import lombok.Data;

/**
 * 监控列表
 */
@Data
/**
 * @Author: test
 * */
public class RunningToolsMonitorListVO {
    /**
     * 测试工具id
     */
    private String toolsId;
    /**
     * 测试工具名称
     */
    private String toolName;
    /**
     * License总数量
     */
    private Integer totalLicenseNum;
    /**
     * License使用数量
     */
    private Integer usedLicenseNum;
    /**
     * 是否支持监控
     */
    private Integer needMonitorOrNot;
}
