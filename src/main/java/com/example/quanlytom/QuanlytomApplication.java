package com.example.quanlytom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuanlytomApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanlytomApplication.class, args);
	}

}
