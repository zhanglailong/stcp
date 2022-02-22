package org.jeecg.modules.test.entity;

import lombok.Data;

/**
 * @author zlf
 * @version V1.0
 * @date 2021/4/12
 * @Description: 用一句话描述该文件做什么)
 */
@Data
public class UnifyResultData {
    private String body;
    private String statusCode;
    private int statusCodeValue;
}
