package com.loyalty.auth;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
		System.out.println("JVM Timezone: " + TimeZone.getDefault());
		System.out.println(new BCryptPasswordEncoder().encode("admin123"));
	}

}
