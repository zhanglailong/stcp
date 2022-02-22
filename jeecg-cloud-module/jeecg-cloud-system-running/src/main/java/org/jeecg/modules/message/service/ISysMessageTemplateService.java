package org.jeecg.modules.message.service;

import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.modules.message.entity.SysMessageTemplate;

import java.util.List;

/**
 * @Description: 消息模板
 * @Author: jeecg-boot
 * @Date:  2019-04-09
 * @Version: V1.0
 */
public interface ISysMessageTemplateService extends JeecgService<SysMessageTemplate> {
    /**
     * 通过标识查询
     * @param code true
     * @return list集合
     * */
    List<SysMessageTemplate> selectByCode(String code);
}
