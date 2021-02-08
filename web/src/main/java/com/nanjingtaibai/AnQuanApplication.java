package com.nanjingtaibai;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.nanjingtaibai.system.mapper")
@EnableSwagger2
@EnableAsync
@EnableScheduling
public class AnQuanApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnQuanApplication.class,args);
    }



}
