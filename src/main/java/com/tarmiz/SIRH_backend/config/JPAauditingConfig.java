package com.tarmiz.SIRH_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JPAauditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                // TODO: Integrate with JWT authentication here
                // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                // if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserDetails) {
                //     return Optional.of(((UserDetails) auth.getPrincipal()).getUsername());
                // }
                return Optional.of("SYSTEM_ADMIN");
            }
        };
    }
}
