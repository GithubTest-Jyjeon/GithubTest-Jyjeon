package com.ex.springboot;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class Sb16ThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sb16ThymeleafApplication.class, args);
	}
	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
