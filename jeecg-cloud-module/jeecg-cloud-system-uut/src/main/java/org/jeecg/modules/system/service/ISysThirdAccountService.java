package org.jeecg.modules.system.service;

import org.jeecg.modules.system.entity.SysThirdAccount;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysUser;

/**
 * @Description: 第三方登录账号表
 * @Author: jeecg-boot
 * @Date:   2020-11-17
 * @Version: V1.0
 */
public interface ISysThirdAccountService extends IService<SysThirdAccount> {
    /**
     * 更新第三方账户信息
     * @param sysUser true
     * @param thirdUserUuid true
     * */
    void updateThirdUserId(SysUser sysUser,String thirdUserUuid);
    /**
     * 创建第三方用户
     * @param phone true
     * @param thirdUserUuid true
     * @return 无返回值
     * */
    SysUser createUser(String phone, String thirdUserUuid);
    
}
