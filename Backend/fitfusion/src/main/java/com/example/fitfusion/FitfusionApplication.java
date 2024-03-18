package com.example.fitfusion;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class FitfusionApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitfusionApplication.class, args);
	}

//	@Bean
//	public MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		factory.setMaxFileSize("10MB"); // Set your max file size
//		factory.setMaxRequestSize("10MB"); // Set your max request size
//		return factory.createMultipartConfig();
//	}

}
