package com.ms.ms_licencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsLicenciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsLicenciaApplication.class, args);
	}

}
