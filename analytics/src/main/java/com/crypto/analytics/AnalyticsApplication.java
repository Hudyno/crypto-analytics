package com.crypto.analytics;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
		scanBasePackages = {"com.crypto.**"},
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@EnableFeignClients(basePackages = "com.crypto.**")
@EnableJpaRepositories("com.crypto.**")
@EntityScan("com.crypto.**")
@RequiredArgsConstructor
public class AnalyticsApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {

	}
}
