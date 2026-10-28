package br.com.petshop.config;

import org.springframework.context.annotation.*; import org.springframework.security.config.annotation.web.builders.HttpSecurity; import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.security.web.SecurityFilterChain;

@Configuration public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
 @Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {http.csrf(csrf->csrf.disable()).authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/error").permitAll().anyRequest().authenticated()).formLogin(f->f.disable()).httpBasic(h->{}).logout(l->l.logoutUrl("/api/auth/logout").permitAll());return http.build();}
}
