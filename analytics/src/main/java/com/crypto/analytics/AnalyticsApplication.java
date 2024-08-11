package com.crypto.analytics;

import com.crypto.analytics.service.CoinGeckoApiResponseToJsonSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(
		scanBasePackages = {"com.crypto.**"},
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@RequiredArgsConstructor
@EnableAsync
public class AnalyticsApplication implements ApplicationRunner {

	private final CoinGeckoApiResponseToJsonSaver coinGeckoApiResponseToJsonSaver;

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
//		coinGeckoApiResponseToJsonSaver.saveAllCoinById();
	}
}
