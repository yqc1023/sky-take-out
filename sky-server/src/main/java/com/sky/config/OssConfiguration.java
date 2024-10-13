package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类,用于创建AliOssUtil的对象
 */
@Slf4j
@Configuration

public class OssConfiguration {

    @Bean//使用@Bean注解可以调用此方法,而如果在类上加@Component则可以调用整个类,
    // 没必要,我们只需aliOssUtil方法,所以用@Bean
    @ConditionalOnMissingBean//当没有这个bean时再创建 增加复用
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getBucketName());
    }
}
