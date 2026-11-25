package ru.kafpin.school;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
class SecurityConfiguration {
    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(requests -> requests
                        .requestMatchers("/login", "/app.css", "/classes.js", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/schoolboys/new").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/schoolboys").hasRole("ADMIN")
                        .anyRequest().hasAnyRole("USER", "ADMIN"))
                .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/schoolboys", false))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"))
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(AccountRepository accounts) {
        return username -> accounts.findById(username)
                .map(account -> User.withUsername(account.username()).password(account.passwordHash())
                        .roles(account.role().name()).build())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден."));
    }

    @Bean
    ApplicationRunner demoAccounts(AccountRepository accounts, PasswordEncoder encoder) {
        return args -> accounts.saveAll(List.of(
                new Account("user", encoder.encode("UserLab6!"), Account.Role.USER),
                new Account("admin", encoder.encode("AdminLab6!"), Account.Role.ADMIN)));
    }
}
