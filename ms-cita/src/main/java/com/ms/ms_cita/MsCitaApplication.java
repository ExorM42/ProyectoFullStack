package com.ms.ms_cita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsCitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCitaApplication.class, args);
	}

}
