package com.yokonev.fintrack.configuration;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.yokonev.fintrack.entity.AppUser;
import com.yokonev.fintrack.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Useful for public pages, deactivate auth for the specified paths
    @Bean 
    WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
            .requestMatchers("/public/**");
    }

    //Security filter chain
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .formLogin(Customizer.withDefaults()) //Can be replaced later with our own logging page route
            .logout(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Note, only keep this during dev, never in production
    @Bean
    CommandLineRunner createDefaultUser(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Logger.getLogger(this.getClass().getName()).warning("CREATING DEFAULT USER");
                AppUser user = new AppUser("admin", "random@random.me", encoder.encode("admin123"));
                repo.save(user);
            }
        };
    }

}
