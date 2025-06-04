package com.alkaid.emailservice_re;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.alkaid.emailservice_re",
    "hipstershop"
})
public class EmailserviceReApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailserviceReApplication.class, args);
	}

}
