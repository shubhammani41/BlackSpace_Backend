package com.dev.blackspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.dev.blackspace.entities"})
@EnableJpaRepositories(basePackages = {"com.dev.blackspace.repositories"})
@ComponentScan(basePackages = {"com.dev.blackspace"})
public class BlackSpaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlackSpaceApplication.class, args);
	}

}
