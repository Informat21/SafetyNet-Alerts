package com.safetynet.alerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.safety.alerts.service")
@ComponentScan(basePackages = "com.safetyNet.alerts")

public class SafetyNetAlertsApplication {

	public static void main(String[] args) {

		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

}
