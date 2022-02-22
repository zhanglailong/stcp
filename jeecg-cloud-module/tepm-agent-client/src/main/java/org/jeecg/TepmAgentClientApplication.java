
package org.jeecg;

import org.jeecg.modules.common.WebToolUtils;
import org.jeecg.modules.socket.SocketServers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zlf
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = "org.jeecg")
public class TepmAgentClientApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TepmAgentClientApplication.class, args);
        String ip = WebToolUtils.getLocalIP();
        System.out.println("IP is " + ip);
        applicationContext.getBean(SocketServers.class).socketServer();
    }
}
