package com.cvt.backend_demo;

import com.cvt.backend_demo.service.CsvUserImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendDemoApplication  implements CommandLineRunner {
	@Autowired
	private CsvUserImporter csvUserImporter;
	public static void main(String[] args) {
		SpringApplication.run(BackendDemoApplication.class, args);System.out.println("*****************************APPLICATION STARTED*******************************");


	}
	@Override
	public void run(String... args) {
		String filePath = "src/main/resources/users.csv"; // Update this path
		csvUserImporter.importUsersFromCsv(filePath);
	}
}
