package com.mdt.prodigy;

import com.mdt.prodigy.service.H2SchemaService;
import com.mdt.prodigy.service.ISChemaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.mdt.prodigy.entity"})
public class ProdigyApplication {

	public static void main(String[] args) {
//		SpringApplication.run(ProdigyApplication.class, args);

		ISChemaService schemaService = new H2SchemaService();
		schemaService.initializeData();
	}

}
