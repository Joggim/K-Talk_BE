package com.joggim.ktalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class KTalkApplication {

	public static void main(String[] args) {
		SpringApplication.run(KTalkApplication.class, args);
	}

}
