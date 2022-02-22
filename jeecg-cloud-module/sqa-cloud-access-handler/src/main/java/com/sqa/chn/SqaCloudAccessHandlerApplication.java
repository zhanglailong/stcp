package com.sqa.chn;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

/**
 * @author zlf
 * @EnableAsync  线程池注解
 * @EnableScheduling 定时器注解
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"org.jeecg", "com.sqa.chn"})
@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = {"org.jeecg", "com.sqa.chn"})
@MapperScan(value={"com.sqa.chn.modules.**.mapper"})
public class SqaCloudAccessHandlerApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SqaCloudAccessHandlerApplication.class);
    }

    @Bean("timeoutRestTempate")
    public RestTemplate initRestTemplate() {
        SimpleClientHttpRequestFactory requestFactroty = new SimpleClientHttpRequestFactory();
        requestFactroty.setConnectTimeout(30000);
        requestFactroty.setReadTimeout(30000);
        return new RestTemplate(requestFactroty);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDbType(DbType.MYSQL);
        return page;

    }


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(SqaCloudAccessHandlerApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = oConvertUtils.getString(env.getProperty("server.servlet.context-path"));
        log.info("\n----------------------------------------------------------\n\t" +
                "SqaCloudAccessHandlerApplication is running! Access URLs:\n\t" +
                "Local: \t\thttp://localhost:" + port + path + "/doc.html\n" +
                "External: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "版本号: " + env.getProperty("running.version") + "\n" +
                "版本备注: " + env.getProperty("running.remarks") + "\n" +
                "----------------------------------------------------------");

    }

}