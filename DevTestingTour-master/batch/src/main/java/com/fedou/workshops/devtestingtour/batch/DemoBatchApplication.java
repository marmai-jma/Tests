package com.fedou.workshops.devtestingtour.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class  DemoBatchApplication {
	public static void main(final String[] args) throws Exception {
		SpringApplication.run(DemoBatchApplication.class, args);
	}

}