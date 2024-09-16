package com.sparta.logistics.client.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@ComponentScan(basePackages = {"com.sparta.logistics.client.user", "com.sparta.logistics.common"})
public class UserApplication {
//멀티모듈인데 aopcomponent를 인식해야하는데 implementation user-server가 뜰 때 aop component를 갖고가는게 아니니깐
	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
