package com.tarmiz.SIRH_backend.config;

import com.tarmiz.SIRH_backend.model.entity.Employee;
import com.tarmiz.SIRH_backend.model.repository.EmployeeRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(EmployeeRepo repository) {
        return args -> {
            if (repository.count() == 0) {
                Employee e = new Employee();
                e.setMatricule("EMP001");
                e.setFirstName("Tasniim");
                e.setLastName("Chaouii");
                e.setCin("AA123456");
                e.setBirthDate(LocalDate.of(2001, 5, 12));
                e.setGender("F");
                e.setPhone("0600000000");
                e.setEmail("tasniim@example.com");

                repository.save(e);
            }
        };
    }
}
