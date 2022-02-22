package org.jeecg.modules.tools.vo;

import lombok.Data;
import org.jeecg.modules.tools.entity.RunningToolsLicenselimit;

import java.util.List;

/**
 * 黑名单
 */
@Data
/**
 * @Author: test
 * */
public class RunningToolsLicenselimitVO {
    /**
     * License黑名单对象
     */
    private List<RunningToolsLicenselimit> runningToolsLicenselimitList;
    /**
     * LicenseId
     */
    private String licenseId;
}
