package com.change.management.apichangemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpringBootApplication
@ComponentScan("com.change.management")
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class ApiChangeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiChangeManagementApplication.class, args);
	}

	@Bean
	public Properties propertiesConfig() {
		return new Properties();
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:config/tenderchange-messages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return messageSource;
	}
}
