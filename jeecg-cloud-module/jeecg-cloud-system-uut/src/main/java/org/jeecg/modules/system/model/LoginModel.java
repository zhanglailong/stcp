package org.jeecg.modules.system.model;

import lombok.Data;

/**
 * 免密登录
 */
@Data
/**
 * @Author: test
 * */
public class LoginModel {

    /**
     * token
     */
    private String token;

    /**
     * 密钥
     */
    private String publicKey;

    /**
     * 用户名
     */
    private String userName;
}
