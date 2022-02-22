package org.jeecg.config.oss;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.MinioUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio文件上传配置文件
 */
@Slf4j
@Configuration
public class MinioConfig {
    @Value(value = "${jeecg.minio.minio-url}")
    private String minioUrl;
    @Value(value = "${jeecg.minio.minio-name}")
    private String minioName;
    @Value(value = "${jeecg.minio.minio-pwd}")
    private String minioPass;

    @Bean
    public void initMinio(){
        if(!minioUrl.startsWith("http")){
            minioUrl = "http://" + minioUrl;
        }
        if(!minioUrl.endsWith("/")){
            minioUrl = minioUrl.concat("/");
        }
        MinioUtil.setMinioUrl(minioUrl);
        MinioUtil.setMinioName(minioName);
        MinioUtil.setMinioPass(minioPass);
    }

}
