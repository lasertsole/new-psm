package com.psm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DaemonEndApplication {

	private static final Logger logger = LoggerFactory.getLogger(DaemonEndApplication.class);
	public static void main(String[] args) {
		logger.info("DaemonEndApplication start");
		SpringApplication.run(DaemonEndApplication.class, args);
	}

}
