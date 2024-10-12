package com.crypto.analytics;

import com.crypto.analytics.populate.JsonToDatabasePopulate;
import com.crypto.analytics.saver.CryptoCompareApiResponseToJsonSaver;
import com.crypto.analytics.service.IndexService;
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

	private final JsonToDatabasePopulate populate;
	private final CryptoCompareApiResponseToJsonSaver saver;
    private final IndexService indexService;

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		indexService.createIndex();
	}
}
