package com.xwtec.tools;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@MapperScan("com.xwtec.tools.core.repository")
public class WebToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebToolsApplication.class, args);
    }

}
