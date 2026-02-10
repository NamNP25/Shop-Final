package com.example.Shop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		info = @Info(
				title = "HỆ THỐNG QUẢN LÝ SHOP 2026",
				version = "1.0",
				description = "Tài liệu API cá nhân hóa cho dự án Shop",
				contact = @Contact(name = "Admin Shop", email = "support@shop.com")
		),
		servers = @Server(url = "http://localhost:8080", description = "Local Server")
)
@SpringBootApplication
public class 	ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
