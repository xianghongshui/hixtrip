package com.hixtrip.sample;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = {"com.hixtrip"})
@MapperScan(basePackages = {"com.hixtrip.sample.infra.db.mapper"})
@Component("com.hixtrip.sample.*")
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

}
