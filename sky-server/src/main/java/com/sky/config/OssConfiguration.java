package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类,用于创建AliOssUtil的对象
 */
@Slf4j
@Configuration
public class OssConfiguration {
    @Autowired
    //第三方bean,需要的时候使用@Autowired注入进来
    @Bean//使用@Bean注解可以将此方法执行的返回值对象(new AliOssUtil())交给IOC容器管理，成为IOC容器当中的bean对象(省去创建对象)
    @ConditionalOnMissingBean//当没有这个bean时再创建 增加复用
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象:{}",aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getBucketName());
    }
}
