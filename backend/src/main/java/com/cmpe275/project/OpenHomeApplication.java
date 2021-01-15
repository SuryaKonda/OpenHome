package com.cmpe275.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.cmpe275.project.Controller.UserController;

@SpringBootApplication
@EnableScheduling
//@ComponentScan(basePackages = "com.cmpe275.project.Controller")
public class OpenHomeApplication {

	public static void main(String[] args) {
		System.out.println("Open Home Application Started");
		SpringApplication.run(OpenHomeApplication.class, args);
	}
}