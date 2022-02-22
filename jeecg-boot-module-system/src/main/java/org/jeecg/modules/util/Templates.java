package org.jeecg.modules.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @program: jeecg-boot-parent
 * @description:
 *   将配置文件中配置的每一个属性的值，映射到这个组件中
 *   @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定；
 *   prefix = "person"：配置文件中哪个下面的所有属性进行一一映射 *
 *   只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 * @author: Wangly
 * @create: 2021-01-21 11:10
 */
@Component
@ConfigurationProperties(prefix = "templates")
public class Templates {
    @Value("${templates.templates-name}")
    private String templateName;

    public Templates() {
    }

    public Templates(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}