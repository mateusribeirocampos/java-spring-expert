package com.devsuperior.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("ENCODE: " + passwordEncoder.encode("123456"));
//
//		boolean result = passwordEncoder.matches("123456", "$2a$10$JsmUx4yvS64wv15eBU.7jO7iA4d7COPBv0YLEesh364GtZELN/or6");
//
//		if (result) {
//			System.out.println("The password is matches");
//		} else {
//			System.out.println("The password is not matches");
//		}
//	}
}
