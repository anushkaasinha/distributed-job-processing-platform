package com.example.jobprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobprocessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobprocessorApplication.class, args);
	}

}
