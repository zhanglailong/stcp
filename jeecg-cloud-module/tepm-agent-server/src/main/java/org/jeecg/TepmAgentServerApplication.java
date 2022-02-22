
package org.jeecg;

import org.jeecg.modules.common.WebToolUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author zlf
 */
@EnableScheduling
@EnableAsync
@EnableFeignClients(basePackages = {"org.jeecg"})
@SpringBootApplication(scanBasePackages = "org.jeecg")
public class TepmAgentServerApplication {


    public static void main(String[] args) throws UnknownHostException, SocketException {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TepmAgentServerApplication.class, args);
        String ip = WebToolUtils.getLocalIP();
        System.out.println("IP is "+ip);
    }
}
