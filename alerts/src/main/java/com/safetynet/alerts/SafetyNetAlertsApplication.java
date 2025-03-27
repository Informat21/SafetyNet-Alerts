package com.safetynet.alerts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.safety.alerts.service")
@ComponentScan(basePackages = "com.safetyNet.alerts")

public class SafetyNetAlertsApplication {
	public static final Logger logger = LoggerFactory.getLogger(SafetyNetAlertsApplication.class);
	public static void main(String[] args) {

		SpringApplication.run(SafetyNetAlertsApplication.class, args);


		logger.info("=== Ceci est un test de log INFO ===");
		logger.debug("=== Ceci est un test de log DEBUG ===");
		logger.error("=== Ceci est un test de log ERROR ===");
	}
}
